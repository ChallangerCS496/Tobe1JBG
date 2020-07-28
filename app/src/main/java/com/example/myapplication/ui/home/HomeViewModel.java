package com.example.myapplication.ui.home;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.TextView;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.myapplication.Constants;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class HomeViewModel extends ViewModel {

    private MutableLiveData<ArrayList<MyGroupInfo>> groupList;

    public HomeViewModel() {
        groupList = new MutableLiveData<>();
    }


    public LiveData<ArrayList<MyGroupInfo>> getList() {return groupList;}

    public void init(String id){

        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormBody.Builder()
                .add("body", "ToGetInfo")
                .build();
        Request request = new Request.Builder()
                .url(String.format("%s/api/recorder/%s", Constants.SERVER_IP, id))
                .post(body)
                .build();
        Log.d("LogAdapter_main액티비티", request.toString());

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                call.cancel();
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                final String jsonString = response.body().string();

                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    public void run() {
                        try {
                            ArrayList<MyGroupInfo> tmp_list = new ArrayList<MyGroupInfo>();

                            JSONArray total = new JSONArray(jsonString);
                            for(int j=0; j<total.length(); j++){
                                JSONObject data = total.getJSONObject(j);

                                String name = data.getString("groupID");
                                String type_ = data.getString("template");
                                int send_type = 1;
                                if(type_.equals("time")) send_type = 0;
                                String str_date = data.getString("start");
                                String todayLog = data.getString("history");
                                int today = Integer.parseInt(todayLog);
                                String period_unit = data.getString("period_unit");

                                String unit = data.getString("unit");

                                MyGroupInfo add_one = new MyGroupInfo(name, send_type, today, Integer.parseInt(period_unit) , unit);
                                add_one.setStart_time(str_date);

                                String goal = data.getString("goal");
                                int num_goal = Integer.parseInt(goal);
                                add_one.calculate_DailyGoal(num_goal);

                                if(data.has("WiFi")){
                                    JSONArray wifi_arr = data.getJSONArray("WiFi");
                                    for(int i =0; i < wifi_arr.length(); i++){
                                        JSONObject obj = wifi_arr.getJSONObject(i);
                                        String str = obj.toString();
                                        add_one.addWiFi_list(str);
                                    }
                                }

                                tmp_list.add(add_one);
                                groupList.setValue(tmp_list);
                            }

                        } catch (JSONException|NullPointerException e) {

                            ArrayList<MyGroupInfo> tmp_list = new ArrayList<MyGroupInfo>();
                            MyGroupInfo add_one = new MyGroupInfo("그룹을 생성해주세요", 0, 0, 1 , " ");
                            add_one.setStart_time("2020-01-01 12:00:00");
                            add_one.calculate_DailyGoal(1);
                            tmp_list.add(add_one);
                            Log.e("ImageGalleryAdapter", Log.getStackTraceString(e));
                            groupList.setValue(tmp_list);
                        }
                    }
                });
            }
        });
    }
}