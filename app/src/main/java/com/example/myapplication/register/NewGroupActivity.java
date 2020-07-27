package com.example.myapplication.register;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;
import com.example.myapplication.SplashActivity;

public class NewGroupActivity extends AppCompatActivity implements View.OnClickListener {

    private Button type_0, type_1;
    private EditText start_date, end_date, groupName, goalTimes, goalUnit;
    private Spinner period_unit;
    private int day_period;
    private DatePickerDialog.OnDateSetListener callbackMethod;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();

        if(actionBar != null){
            actionBar.setDisplayShowCustomEnabled(true);
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        setContentView(R.layout.newgroup_application);

        //전체 크기 맞춰준다
//        LinearLayout top_layout = (LinearLayout) findViewById(R.id.new_application_top);
//        Display display = getWindowManager().getDefaultDisplay();  // in Activity
//        /* getActivity().getWindowManager().getDefaultDisplay() */ // in Fragment
//        Point size = new Point();
//        display.getRealSize(size); // or getSize(size)
//        int width = size.x;
//        int height = size.y-"?android:attr/actionBarSize";
//        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(width, height);
//        top_layout.setLayoutParams(params);

        goalUnit = (EditText) findViewById(R.id.new_unit);
        type_0 = (Button) findViewById(R.id.new_type0);
        type_0.setOnClickListener(this);
        type_1 = (Button) findViewById(R.id.new_type1);
        type_1.setOnClickListener(this);
        start_date = (EditText) findViewById(R.id.new_start_date);
        end_date = (EditText) findViewById(R.id.new_end_date);

        period_unit = (Spinner) findViewById(R.id.new_period_unit);
        period_unit.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    String get = adapterView.getItemAtPosition(i).toString();
                    if(get.equals("매일")) day_period = 1;
                    else if(get.equals("매주")) day_period = 7;
                    else if(get.equals("매월")) day_period = 30;
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
            goalUnit.setText("시간");
        }
        else if(id == R.id.new_type1){
            type_0.setBackgroundTintList(getResources().getColorStateList(R.color.transparent));
            type_1.setBackgroundTintList(getResources().getColorStateList(R.color.material_blue));
            goalUnit.setText(null);
        }



    }

    @Override //뒤로가기
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home: //toolbar의 back키 눌렀을 때 동작
                finish();
                return true;
            case R.id.im_done: //Todo 서버에 내용 전송, Wifi 설정 인텐트로 연결. edittext.getText()사용
                Toast toast = Toast.makeText(getApplicationContext(), "서버에 정보를 전송합니다.", Toast.LENGTH_SHORT);
                toast.show();
                return true;
            }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.newgroup_done, menu);
        return true;
    }
}
