package com.example.myapplication.ui.home;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MyGroupInfo {
    String group_name;
    int type;
    Date start_time;
    int today_log; //달성률, 누적시간이나 갯수 보여주는데 사용함
    int daily_goal;//계산해내야함
    String unit;
    ArrayList<String> WiFi_list;
    int period_unit;

    public MyGroupInfo(String name, int send_type, int today, int period_unit, String unit) {
        this.group_name = name;
        this.type = send_type;
        this.today_log = today;
        this.period_unit = period_unit;
        this.unit = unit;
    }

    public int calculate_DailyGoal( int goal){

        return goal/period_unit;
    }

    public int getDaily_goal() {
        return daily_goal;
    }

    public ArrayList<String> getWiFi_list() {
        return WiFi_list;
    }

    public void addWiFi_list(String new_wifi){
        WiFi_list.add(new_wifi);
    }

    public void clear_wifi_list(){
        WiFi_list.clear();
    }

    public String getGroup_name() {
        return group_name;
    }

    public void setGroup_name(String group_name){
        this.group_name = group_name;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getToday_log() {
        return Integer.toString(today_log);
    }

    public void setToday_log(int today_log) {
        this.today_log = today_log;
    }

    public double getWorkDone(){
        double work = (double)today_log/daily_goal;
        return work*100;
    }

    public Date getStart_time() {
        return start_time;
    }

    public void setStart_time(String form_string){
        SimpleDateFormat transFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try{
            this.start_time = transFormat.parse(form_string);
        }catch (ParseException e){
            Log.d("MyGroupInfo_wrong time", Log.getStackTraceString(e));
            //this.start_time = transFormat.parse("2018-01-01 08:25:09");
        }

    }
}
