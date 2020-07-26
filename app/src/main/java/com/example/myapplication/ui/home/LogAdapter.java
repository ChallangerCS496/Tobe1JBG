package com.example.myapplication.ui.home;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Constants;
import com.example.myapplication.MainActivity;
import com.example.myapplication.R;
import com.example.myapplication.register.FacebookActivity;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class LogAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context context;
    final ArrayList<MyGroupInfo> items = new ArrayList<MyGroupInfo>();
    //private String mProfileUid = FirebaseAuth.getInstance().getCurrentUser().getUid(); //내 uid

    public LogAdapter(Context context, Activity activity) {
        this.context = context;
        fetchGroup(activity.getIntent().getStringExtra("USER_ID"));
    }

    protected void fetchGroup(final String id) {
        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormBody.Builder()
                .add("body", "ToGetInfo")
                .build();
        Request request = new Request.Builder()
                .url(String.format("%s/api/recorder/%s", Constants.SERVER_IP, id))
                .post(body)
                .build();
        Log.d("LogAdapter", request.toString());

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

                                MyGroupInfo add_one = new MyGroupInfo(name, send_type, today, period_unit , unit);
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

                                items.add(add_one);
                            }

                            notifyDataSetChanged();
                        } catch (JSONException e) {
                            Log.e("ImageGalleryAdapter", Log.getStackTraceString(e));
                        }
                    }
                });
            }
        });
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view;
        switch (viewType) {
            case 0:
                view = LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.home_type_time, viewGroup, false);
                return new TimeViewHolder(view);

            case 1:
                view = LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.home_type_number, viewGroup, false);
                return new NumberViewHolder(view);
        }
        view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.home_type_number, viewGroup, false);
        return new TimeViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int i) {
        final MyGroupInfo model = items.get(i);

        if (model.getType()==0) { //내 메세지인 경우
            TimeViewHolder holder2 = (TimeViewHolder) holder;
            holder2.groupname.setText(model.getGroup_name());
            holder2.time_cumulate.setText(model.getToday_log()+model.getUnit());
            holder2.percentage.setText(Double.toString(model.getWorkDone())+"%");

            Date date = model.getStart_time();
            SimpleDateFormat transformat = new SimpleDateFormat("MM-dd");
            holder2.time_start.setText(transformat.format(date));

        } else {
            NumberViewHolder holder1 = (NumberViewHolder) holder;

            holder1.percentage.setText(Double.toString(model.getWorkDone())+"%");
            holder1.groupname.setText(model.getGroup_name());
            holder1.num_cumulate.setText(model.getToday_log()+model.getUnit());
            holder1.input_text.setHint(model.getToday_log());
            //d_day 계산하기

            Calendar calendar = new GregorianCalendar(Locale.KOREA);

            long diff = calendar.getTimeInMillis()*1000 - model.getStart_time().getTime();
            long dday = diff/(24*60*60*1000);
            holder1.d_day.setText("D+"+Long.toString(dday));

        }
    }

    @Override
    public int getItemViewType(int i) {
        final int thistype = items.get(i).getType();
        return thistype;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }


    //뷰들을 바인딩 해줍니다.
    public class TimeViewHolder extends RecyclerView.ViewHolder {
        TextView percentage;
        TextView groupname;
        TextView time_cumulate;
        TextView time_start;


        public TimeViewHolder(@NonNull final View itemView) {
            super(itemView);
            percentage = itemView.findViewById(R.id.work_done_percentage);
            groupname = itemView.findViewById(R.id.group_name);
            time_cumulate = itemView.findViewById(R.id.time_cumulate);
            time_start = itemView.findViewById(R.id.time_start);

        }
    }

    //뷰들을 바인딩 해줍니다.
    public class NumberViewHolder extends RecyclerView.ViewHolder {
        TextView percentage;
        TextView groupname;
        TextView d_day;
        TextView num_cumulate;
        EditText input_text;

        public NumberViewHolder (@NonNull final View itemView) {
            super(itemView);
            percentage = itemView.findViewById(R.id.work_done_percentage2);
            groupname = itemView.findViewById(R.id.group_name2);
            d_day = itemView.findViewById(R.id.dday);
            num_cumulate = itemView.findViewById(R.id.num_cumulate);
            input_text = itemView.findViewById(R.id.input_number);

        }
    }

    //아이템을 추가해주고싶을때 이거쓰면됨
    public void addItem(MyGroupInfo item) {
        items.add(item);
    }

    //한꺼번에 추가해주고싶을떄
    public void update(ArrayList<MyGroupInfo> items) {
        this.items.clear();
        this.items.addAll(items);
    }

    //아이템 전부 삭제
    public void clear() {
        items.clear();
    }
}