package com.example.myapplication.ui.home;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
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
import com.example.myapplication.register.NewGroupActivity;
import com.example.myapplication.register.User;

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
    ArrayList<MyGroupInfo> items = new ArrayList<MyGroupInfo>();
    String id;

    public LogAdapter(Context context, Activity activity, ArrayList<MyGroupInfo> n_items, String id) {
        this.context = context;
//        fetchGroup(activity.getIntent().getStringExtra("USER_ID"));
        this.items = n_items;
        this.id = id;
    }

    public void updateItems(ArrayList<MyGroupInfo> n_items){
        items.clear();
        if(items != null) items.addAll(n_items);
        notifyDataSetChanged();
    }
    public int getItemCont(){return items.size();}


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

        if (model.getType()==0) {
            TimeViewHolder holder2 = (TimeViewHolder) holder;
            holder2.groupname.setText(model.getGroup_name());
            try{
                //holder2.percentage.setText(Double.toString(model.getWorkDone())+"%"); //for test
                holder2.percentage.setText(Integer.toString(model.getDaily_goal())+model.getUnit());
            }catch(NumberFormatException e){
                holder2.percentage.setText("0%");
            }

            Date date = model.getStart_time();
            SimpleDateFormat transformat = new SimpleDateFormat("MM-dd");
            if(date == null){
                holder2.time_start.setText("-");
            } else {
                holder2.time_start.setText(transformat.format(date));
            }


        } else {
            NumberViewHolder holder1 = (NumberViewHolder) holder;

            try{
                holder1.percentage.setText(Integer.toString(model.getDaily_goal()) + model.getUnit());
//                holder1.percentage.setText(Double.toString(model.getWorkDone())+"%"); //for test
            }catch(NumberFormatException e){
                holder1.percentage.setText("0%");
            }
            holder1.groupname.setText(model.getGroup_name());
            holder1.num_cumulate.setText(model.getToday_log()+model.getUnit());
            holder1.input_text.setHint(model.getToday_log());

        }
    }

    @Override
    public int getItemViewType(int i) {
        final int thistype = items.get(i).getType();
        return thistype;
    }

    @Override
    public int getItemCount() {
        if(items != null){return items.size();}
        else {return 0;}
    }


    //뷰들을 바인딩 해줍니다.
    public class TimeViewHolder extends RecyclerView.ViewHolder {
        TextView percentage;
        TextView groupname;
        TextView time_start;


        public TimeViewHolder(@NonNull final View itemView) {
            super(itemView);
            percentage = itemView.findViewById(R.id.work_done_percentage);
            groupname = itemView.findViewById(R.id.group_name);
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
        Button save;

        public NumberViewHolder (@NonNull final View itemView) {
            super(itemView);
            percentage = itemView.findViewById(R.id.work_done_percentage2);
            groupname = itemView.findViewById(R.id.group_name2);
            num_cumulate = itemView.findViewById(R.id.num_cumulate);
            input_text = itemView.findViewById(R.id.input_number);
            save = itemView.findViewById(R.id.save_button);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d("Logadpater 액티비티", "클릭은 되니");
                    int position = getLayoutPosition();
                    MyGroupInfo item = items.get(position);

                    int recent = Integer.parseInt(input_text.getText().toString());
                    int today = Integer.parseInt(item.getToday_log());
                    item.setToday_log(today+recent);

                    String group = item.getGroup_name();
                    GetUser getUser = new GetUser();
                    Object[] objects = {recent, group, today};
                    getUser.execute(objects);
                }
            });

        }
    }


    private class GetUser extends AsyncTask {

        @Override
        protected void onPostExecute(Object obj){
        }

        @Override
        protected Object doInBackground(Object[] objects) {

            int num = (int) objects[0];
            String group = (String) objects[1];
            int daycount = (int) objects[2];
            OkHttpClient client = new OkHttpClient();
            RequestBody body = new FormBody.Builder()
                    .add("count", Integer.toString(num))
                    .build();
            final Request request = new Request.Builder()
                    .addHeader("Connection", "close")
                    .url(String.format("%s/api/recorder/count/%s/%s/%s", Constants.SERVER_IP, id, group, Integer.toString(daycount)))
                    .post(body)
                    .build();
            Log.d("logadapter add 액티비티" , Integer.toString(num)+request.toString());

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                    Log.d("Log Adapter 액티비티 fail", Log.getStackTraceString(e));
                    call.cancel();
                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull final Response response) throws IOException {
                    final String jsonString = response.body().string();

                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        public void run() {
                            Log.d("Log Adapter 액티비티", request.toString());
                            Log.d("Log Adapter 액티비티", response.toString());
                        }
                    });
                }
            });

            return objects;
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