package com.example.myapplication.ui.grouplist;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Constants;
import com.example.myapplication.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class GroupClickedActivity extends AppCompatActivity {
    private String userid;
    private String nickname;
    private String group_id;
    private int goal;
    private String unit;
    private String period_start;
    private String period_end;
    private int period_unit;//1 7 30
    private String template;
    private int members;
    private  String goal_unit;

    PersonListAdapter personListAdapter;
    PersonData personData;
    ArrayList<PersonData> arraydata = new ArrayList<>();
    private RecyclerView recyclerView;

    RankListAdapter rankListAdapter;
    RankData rankData;
    ArrayList<RankData> rank_arraydata = new ArrayList<>();
    private RecyclerView rank_recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.grouplist_clicked);

        Intent intent = getIntent();
        userid = intent.getStringExtra("userid");
        nickname = intent.getStringExtra("nickname");
        group_id = intent.getStringExtra("group_id");
        goal = intent.getIntExtra("goal",0);
        unit = intent.getStringExtra("unit");
        period_start = intent.getStringExtra("period_start");
        period_end = intent.getStringExtra("period_end");
        period_unit = intent.getIntExtra("period_unit",0);
        template = intent.getStringExtra("template");
        members = intent.getIntExtra("members",0);
        goal_unit = intent.getStringExtra("goal_unit");

        TextView vgroup_id= (TextView) findViewById(R.id.click_group_id);
        TextView vgoal = (TextView) findViewById(R.id.click_goal);
        TextView vunit = (TextView) findViewById(R.id.click_unit);
        TextView vperiod_start = (TextView) findViewById(R.id.click_period_start);
        TextView vperiod_end = (TextView) findViewById(R.id.click_period_end);
        TextView vperiod_unit = (TextView) findViewById(R.id.click_period_unit);
        TextView vmembers = (TextView) findViewById(R.id.click_members);
        TextView vgoal_unit = (TextView) findViewById(R.id.click_goal_unit);
        final EditText search_index = (EditText) findViewById(R.id.serach_index);
        final Spinner search_unit = (Spinner) findViewById(R.id.rank_period_unit);
        final ImageView vtemplate = (ImageView) findViewById(R.id.click_template);
        final Button search = (Button) findViewById(R.id.rank_search);

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (rankData.getRankDataList().size() > 0) {
                    rankData.getRankDataList().clear(); //리스트 정보 초기화//

                    rankListAdapter.set_RankData(rankData); //초기화된 정보를 갱신//
                }

                String standard = search_unit.getSelectedItem().toString();
                if(standard.equals("일차")){
                    standard = "1";
                } else if(standard.equals("주차")){
                    standard = "7";
                } else {
                    standard = "30";
                }
                get_RankData(group_id,standard,(Integer.parseInt(search_index.getText().toString()))+""); //데이터 Reload//
            }
        });


        vgroup_id.setText(group_id);
        vgoal.setText(Integer.toString(goal));
        vunit.setText(unit);
        vperiod_start.setText(period_start.substring(0,10));
        vperiod_end.setText(period_end.substring(0,10));
        if(period_unit == 1){
            vperiod_unit.setText("매일");
        } else if (period_unit == 7) {
            vperiod_unit.setText("매주");
        } else {
            vperiod_unit.setText("매달");
        }
        vmembers.setText(Integer.toString(members));
        vgoal_unit.setText(goal_unit);
        int resID = getResources().getIdentifier(template,"drawable","myapplication");
        vtemplate.setImageResource(resID);

        recyclerView = (RecyclerView) findViewById(R.id.grouplist_clicked_recyclerview);
        rank_recyclerView = (RecyclerView) findViewById(R.id.grouplist_clicked_rank_recyclerview);


        personData = new PersonData();
        personListAdapter = new PersonListAdapter(this);
        rankData = new RankData();
        rankListAdapter = new RankListAdapter(this);


        if (personData.getPersonDataList().size() > 0) {
            personData.getPersonDataList().clear(); //리스트 정보 초기화//

            personListAdapter.set_PersonData(personData); //초기화된 정보를 갱신//
        }
        recyclerView.setAdapter(personListAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        rank_recyclerView.setAdapter(rankListAdapter);
        rank_recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        get_PersonData(userid,group_id); //데이터 Reload//




    }

    public void get_PersonData(String id, String group_id) {

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(String.format("%s/api/group/%s/%s", Constants.SERVER_IP, id, group_id))
                .build();

        /** 비동기 방식(enqueue)으로 Callback 구현 **/
        client.newCall(request).enqueue(requestuserlistcallback);
    }
    public void get_RankData(String group_id, String standard, String index) {

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(String.format("%s/api/group/ranking/%s/%s/%s", Constants.SERVER_IP, group_id, standard, index ))
                .build();

        /** 비동기 방식(enqueue)으로 Callback 구현 **/
        client.newCall(request).enqueue(requestranklistcallback);
    }

    private Callback requestuserlistcallback = new Callback() {
        @Override
        public void onFailure(Call call, IOException e) {
            //네트워크 자체에서의 에러상황.//
            Log.d("ERROR Message : ", e.getMessage());

            if (this != null) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getApplicationContext());
                        alertDialog.setTitle("People Search")
                                .setMessage("요청에러 (네트워크 상태를 점검해주세요.)")
                                .setCancelable(false)
                                .setPositiveButton("확인",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {

                                            }
                                        });
                        AlertDialog alert = alertDialog.create();
                        alert.show();
                    }
                });
            }
        }

        @Override
        public void onResponse(Call call, Response response) throws IOException {
            final String jsonString = response.body().string();
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                public void run() {
                    try {
                        JSONArray total = new JSONArray(jsonString);
                        for(int j=0; j<total.length(); j++){
                            JSONObject data = total.getJSONObject(j);

                            int goal = data.getInt("goal");
                            int history = data.getInt("history");
                            int period_unit = data.getInt("period_unit");
                            int index = data.getInt("_id");
                            String unit = data.getString("unit");

                            if(unit.equals("1000")){
                                unit = "초";
                            } else if(unit.equals("60000")){
                                unit = "분";
                            } else if(unit.equals("3600000")){
                                unit = "시간";
                            }

                            PersonData add_one = new PersonData();
                            add_one.setPerson_goal(goal);
                            add_one.setPerson_period_unit(period_unit);
                            add_one.setPerson_goal_unit(goal_unit);
                            add_one.setPerson_index(index);
                            add_one.setPerson_unit(unit);
                            add_one.setPerson_history(history);


                            personData.PersonDataList.add(add_one);
                            arraydata.add(add_one);

                        }
                        personListAdapter.set_PersonData(personData);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    };
    private Callback requestranklistcallback = new Callback() {
        @Override
        public void onFailure(Call call, IOException e) {
            //네트워크 자체에서의 에러상황.//
            Log.d("ERROR Message : ", e.getMessage());

            if (this != null) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getApplicationContext());
                        alertDialog.setTitle("People Search")
                                .setMessage("요청에러 (네트워크 상태를 점검해주세요.)")
                                .setCancelable(false)
                                .setPositiveButton("확인",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {

                                            }
                                        });
                        AlertDialog alert = alertDialog.create();
                        alert.show();
                    }
                });
            }
        }

        @Override
        public void onResponse(Call call, Response response) throws IOException {
            final String jsonString = response.body().string();
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                public void run() {
                    try {
                        JSONArray total = new JSONArray(jsonString);
                        for(int j=0; j<total.length(); j++){
                            JSONObject data = total.getJSONObject(j);

                            String name = data.getString("name");
                            String nickname = data.getString("nickname");
                            int _total = data.getInt("total");


                            RankData add_one = new RankData();
                            add_one.setRank_index(j+1);
                            add_one.setRank_name(name);
                            add_one.setRank_nickname(nickname);
                            add_one.setRank_total(_total);
                            add_one.setRank_unit(unit);

                            rankData.RankDataList.add(add_one);
                            rank_arraydata.add(add_one);

                        }
                        rankListAdapter.set_RankData(rankData);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    };
}
