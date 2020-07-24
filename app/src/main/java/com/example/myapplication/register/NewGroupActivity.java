package com.example.myapplication.register;

import android.app.DatePickerDialog;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;

public class NewGroupActivity extends AppCompatActivity {

    private Button start_date, end_date;
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
        LinearLayout top_layout = (LinearLayout) findViewById(R.id.new_application_top);
        Display display = getWindowManager().getDefaultDisplay();  // in Activity
        /* getActivity().getWindowManager().getDefaultDisplay() */ // in Fragment
        Point size = new Point();
        display.getRealSize(size); // or getSize(size)
        int width = size.x;
        int height = size.y;
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(width, height);
        top_layout.setLayoutParams(params);



    }

    @Override //뒤로가기
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home: //toolbar의 back키 눌렀을 때 동작
                finish();
                return true;
            case R.id.im_done: //Todo Wifi 설정 인텐트 추가
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
