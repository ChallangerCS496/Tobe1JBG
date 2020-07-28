package com.example.myapplication.register;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Constants;
import com.example.myapplication.MainActivity;
import com.example.myapplication.R;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.facebook.FacebookSdk.getApplicationContext;

public class NewGroupActivity extends AppCompatActivity implements View.OnClickListener {

    private Button type_0, type_1;
    private EditText start_date, end_date, goalTimes, goalUnit, group_name, goal, unit;
    private Spinner period_unit;
    private int day_period;
    private DatePickerDialog.OnDateSetListener callbackMethod;
    private String id, Period_Unit, nickname_from_intent;
    private memberAdapter adapter;
    private WifiAdapter list_adapter;
    private RecyclerView recyclerView;
    private ListView listview;
    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private WifiManager wifiManager;
    private ArrayList<WifiData> wifidata;
    private List<ScanResult> scanDatas;
    private static String[] requiredPermissions = new String[]{
            Manifest.permission.ACCESS_WIFI_STATE,
            Manifest.permission.ACCESS_NETWORK_STATE,
            Manifest.permission.CHANGE_WIFI_STATE
    };
    private static int PERMISSIONS_REQUEST_ALL = 8;

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            if(action.equals(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION)) {
                scanDatas = wifiManager.getScanResults();
                //Toast.makeText(getApplicationContext(), scanDatas.get(0).SSID, Toast.LENGTH_SHORT).show();

                wifidata = new ArrayList<>();
                for(ScanResult select : scanDatas){
                    String BBSID = select.BSSID;
                    String SSID = select.SSID;
                    WifiData wifiData = new WifiData(BBSID, SSID, false);
                    wifidata.add(wifiData);
                }

                // 어댑터
                list_adapter = new WifiAdapter(getApplicationContext(), R.layout.wifi_row_layout, wifidata);
                listview.setAdapter(list_adapter);

                listview.setOnItemClickListener(new AdapterView.OnItemClickListener(){
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        //Toast.makeText(getApplicationContext(), wifidata.get(i).getSSID(), Toast.LENGTH_SHORT).show();
                    }
                });
                // listview 갱신
                adapter.notifyDataSetChanged();

            }else if(action.equals(WifiManager.NETWORK_STATE_CHANGED_ACTION)) {
                sendBroadcast(new Intent("wifi.ON_NETWORK_STATE_CHANGED"));
            }
        }
    };

    @Override
    public void onResume() {
        super.onResume();
        IntentFilter intentFilter = new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
        intentFilter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
        registerReceiver(receiver, intentFilter);
        wifiManager.startScan();
    }

    @Override
    public void onPause() {
        super.onPause();
        unregisterReceiver(receiver);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.newgroup_application);
        requestRequiredPermissions();

        wifiManager = (WifiManager) getSystemService(this.WIFI_SERVICE);
        if(!wifiManager.isWifiEnabled()){
            wifiManager.setWifiEnabled(true);
        }

        id = this.getIntent().getStringExtra("USER_ID");
        nickname_from_intent = this.getIntent().getStringExtra("NICKNAME");
        Log.d("닉네임", nickname_from_intent);

        recyclerView = (RecyclerView) findViewById(R.id.party_detail_member_list);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL, false);
        adapter = new memberAdapter(id, new ArrayList<User>(), nickname_from_intent);
        recyclerView.setLayoutManager(layoutManager);

        listview = (ListView) findViewById(R.id.wifi_list_act);

        new GetUser().execute(new String() ); // user 셋팅한다.

        ActionBar actionBar = getSupportActionBar();

        if(actionBar != null){
            actionBar.setDisplayShowCustomEnabled(true);
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        goalUnit = (EditText) findViewById(R.id.new_index);
        type_0 = (Button) findViewById(R.id.new_type0);
        type_0.setOnClickListener(this);
        type_1 = (Button) findViewById(R.id.new_type1);
        type_1.setOnClickListener(this);
        start_date = (EditText) findViewById(R.id.new_start_date);
        end_date = (EditText) findViewById(R.id.new_end_date);
        group_name = (EditText) findViewById(R.id.new_group_name);
        goal = (EditText) findViewById(R.id.new_goal);
        unit = (EditText) findViewById(R.id.new_unit);

        period_unit = (Spinner) findViewById(R.id.new_period_unit);
        period_unit.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    String get = adapterView.getItemAtPosition(i).toString();
                    if(get.equals("매일")) {
                        day_period = 1;
                        Period_Unit = "일";
                    }
                    else if(get.equals("매주")) {
                        day_period = 7;
                        Period_Unit = "주";
                    }
                    else if(get.equals("매월")) {
                        day_period = 30;
                        Period_Unit = "월";
                    }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }


    @Override
    public void onClick(View view) {
        int id = view.getId();

        if(id == R.id.new_type0){
            type_1.setBackgroundTintList(getResources().getColorStateList(R.color.transparent));
            type_0.setBackgroundTintList(getResources().getColorStateList(R.color.material_blue));
            unit.setText("시간");
        }
        else if(id == R.id.new_type1){
            type_0.setBackgroundTintList(getResources().getColorStateList(R.color.transparent));
            type_1.setBackgroundTintList(getResources().getColorStateList(R.color.material_blue));
            unit.setText(null);
        }



    }

    @Override //뒤로가기
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home: //toolbar의 back키 눌렀을 때 동작
                finish();
                return true;
            case R.id.im_done: //Todo 서버에 내용 전송, Wifi 설정 인텐트로 연결. edittext.getText()사용
                if(nullcheck())
                    {
                    Toast toast = Toast.makeText(getApplicationContext(), "서버에 정보를 전송합니다.", Toast.LENGTH_SHORT);
                    toast.show();
                    finish();
                    }
                else{
                    Toast toast = Toast.makeText(getApplicationContext(), "모든 정보를 입력해주세요.", Toast.LENGTH_SHORT);
                    toast.show();
                }
                return true;
            }

        return super.onOptionsItemSelected(item);
    }

    public boolean nullcheck(){
        boolean res = true;
        try{

            String group_Name = group_name.getText().toString();

            String period_start = start_date.getText().toString();
            period_start.replace('-', '/');
            String period_end = end_date.getText().toString();
            period_end.replace('-', '/');

            String goal_num = goal.getText().toString();

            String tmp = unit.getText().toString();
            String unit_num;
            String templ = "time";
            if(tmp.equals("시간")) unit_num = "3600000";
            else if(tmp.equals("분")) unit_num = "60000";
            else if(tmp.equals("초")) unit_num = "1000";
            else {
                unit_num = tmp;
                templ = "count";
            }

            String goal_unit = goalUnit.getText().toString();

            String mem = adapter.generatePartyString();

            String PERIOD = Integer.toString(day_period);

            String wifiname = list_adapter.generatePartyString();
            send_NewGroup(group_Name, templ, mem, period_start, period_end, goal_num, unit_num, PERIOD, wifiname , goal_unit);
            res = true;

        }catch (NullPointerException | JSONException e){
            res = false;
            Log.d("추가 액티비티", Log.getStackTraceString(e));
        }

        return res;
    }

    public void send_NewGroup(String GN, String temp, String mem, String PS, String PE, String Goal, String unit, String p_unit, String wifi, String GU) throws JSONException {

        OkHttpClient client = new OkHttpClient();
        JSONObject postBody = new JSONObject();
        //Todo adapter에서 nickname 찾기
        postBody.put("members", mem);
        //period_start 2020/00/00 형식으로
        postBody.put("period_start", PS);
        //period_end 2020/00/00형식으로
        postBody.put("period_end", PE);
        //goal
        postBody.put("goal", Goal);
        //unit 1초 1000
        postBody.put("unit", unit);
        //period_unit 1730
        postBody.put("period_unit", p_unit);
         //goal_unit JBG
        postBody.put("goal_unit", GU);
        postBody.put("Wifi", wifi);
        RequestBody body = RequestBody.create(postBody.toString(), JSON);

        final Request request = new Request.Builder()
                .url(String.format("%s/api/create/%s/%s", Constants.SERVER_IP,GN, temp))//group name, time or number
                .post(body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                call.cancel();
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                Log.d("그룹추가 액티비티", request.toString());
                finish();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.newgroup_done, menu);
        return true;
    }


    private class GetUser extends AsyncTask {

        @Override
        protected void onPostExecute(Object obj){
        }

        @Override
        protected Object doInBackground(Object[] objects) {
            OkHttpClient client = new OkHttpClient();
            RequestBody body = new FormBody.Builder()
                    .add("body", "ToGetMember")
                    .build();
            Request request = new Request.Builder()
                    .url(String.format("%s/api/create/users/%s", Constants.SERVER_IP, id))
                    .post(body)
                    .build();
            Log.d("그룹추가 액티비티" , request.toString());

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                    call.cancel();
                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    final String jsonString = response.body().string();
                    final ArrayList<User> tmp = new ArrayList<User>();

                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        public void run() {
                            try {
                                JSONArray total = new JSONArray(jsonString);
                                Log.d("그룹추가 액티비티" , jsonString);

                                for(int j=0; j<total.length(); j++){
                                    JSONObject data = total.getJSONObject(j);

                                    String name = data.getString("name");
                                    String nickname = data.getString("nickname");

                                    User add_one = new User(name, nickname);
                                    tmp.add(add_one);
                                }
                                adapter.setUserList(tmp);
                                recyclerView.setAdapter(adapter);

                                Log.d("그룹추가 액티비티 UserList size", Integer.toString(adapter.getItemCount()));

                            } catch (JSONException e) {
                                Log.e("ImageGalleryAdapter", Log.getStackTraceString(e));
                                User add_one = new User("초대 가능한 유저가 없습니다.", " ");
                                tmp.add(add_one);
                                adapter.setUserList(tmp);
                                recyclerView.setAdapter(adapter);

                                Log.d("그룹추가 액티비티 UserList size", Integer.toString(adapter.getItemCount()));
                            }
                        }
                    });
                }
            });



            return objects;
        }
    }

    private void requestRequiredPermissions() {
        boolean allGranted = true;
        for (String permission : this.requiredPermissions) {
            boolean granted = ActivityCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED;
            allGranted = allGranted & granted;
        }

        if (!allGranted)
            ActivityCompat.requestPermissions(NewGroupActivity.this, requiredPermissions, PERMISSIONS_REQUEST_ALL);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
    }

}
