package com.example.myapplication.ui.grouplist;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DefaultItemAnimator;

import com.example.myapplication.Constants;
import com.example.myapplication.R;
import com.example.myapplication.register.NewGroupActivity;
import com.example.myapplication.ui.home.MyGroupInfo;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpCookie;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cn.iwgang.familiarrecyclerview.FamiliarRecyclerView;
import cn.iwgang.familiarrecyclerview.FamiliarRefreshRecyclerView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class GroupListFragment extends Fragment{
    private final static int LOAD_MORE_TAG = 1;
    View view;
    /**
     * 어댑터, 데이터
     **/
    GroupListAdapter groupListAdapter;
    GroupData groupData;
    ArrayList<GroupData> arraydata = new ArrayList<>();
    /**
     * List Flag
     **/
    String id; //기본 0//
    String NICKNAME;

    private FamiliarRefreshRecyclerView Group_list;
    private FamiliarRecyclerView recyclerview;


    private Callback requestuserlistcallback = new Callback() {
        @Override
        public void onFailure(Call call, IOException e) {
            //네트워크 자체에서의 에러상황.//
            Log.d("ERROR Message : ", e.getMessage());

            if (getActivity() != null) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
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


                            String name = data.getString("groupID");
                            String template = data.getString("template");
                            int goal = data.getInt("goal");
                            int period_unit = data.getInt("period_unit");
                            String period_start = data.getString("period_start");
                            String period_end = data.getString("period_end");
                            JSONArray jsonArray = data.getJSONArray("members");
                            int members = jsonArray.length();
                            String unit = data.getString("unit");
                            if(unit.equals("1000")){
                                unit = "초";
                            } else if(unit.equals("60000")){
                                unit = "분";
                            } else if(unit.equals("3600000")){
                                unit = "시간";
                            }
                            String goal_unit = data.getString("goal_unit");

                            GroupData add_one = new GroupData();
                            add_one.setGroup_id(name);
                            add_one.setGroup_goal(goal);
                            add_one.setGroup_period_unit(period_unit);
                            add_one.setGroup_period_start(period_start);
                            add_one.setGroup_period_end(period_end);
                            add_one.setGroup_members(members);
                            add_one.setGroup_template(template);
                            add_one.setGroup_unit(unit);
                            add_one.setGroup_goal_unit(goal_unit);


//                            if(data.has("WiFi")){
//                                JSONArray wifi_arr = data.getJSONArray("WiFi");
//                                for(int i =0; i < wifi_arr.length(); i++){
//                                    JSONObject obj = wifi_arr.getJSONObject(i);
//                                    String str = obj.toString();
//                                    add_one.addWiFi_list(str);
//                                }
//                            }

                            groupData.GroupDataList.add(add_one);
                            arraydata.add(add_one);

                        }
                        groupListAdapter.set_GroupData(groupData);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    };


    public void GroupListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        id = getActivity().getIntent().getStringExtra("USER_ID");
        NICKNAME = getActivity().getIntent().getStringExtra("NICKNAME");
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_grouplist, container, false);


        FloatingActionButton add = (FloatingActionButton) view.findViewById(R.id.add_group);
        add.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick (View v){
                create_new_group();
            }
        });



        Group_list = (FamiliarRefreshRecyclerView) view.findViewById(R.id.group_info_list);

        /** RecyclerView 정의 **/
        //RefreshRecyclerView 기능설정.//
        Group_list.setId(android.R.id.list);
        Group_list.setColorSchemeColors(0xFFFF5000, Color.RED, Color.YELLOW, Color.GREEN);
        Group_list.setLoadMoreEnabled(true);

        //RecyclerView의 특징을 RefreshRecyclerView에 연결//
        recyclerview = Group_list.getFamiliarRecyclerView();
        recyclerview.setItemAnimator(new DefaultItemAnimator());
        recyclerview.setHasFixedSize(true);


//        /** RecyclerView의 각종 뷰 정의(HeaderView, EmptyView, FooterView) **/
        View Grouplist_emptyview = LayoutInflater.from(getActivity()).inflate(R.layout.grouplist_emptyview, null);


        //리사이클뷰에 연결//
        recyclerview.setEmptyView(Grouplist_emptyview, true);

        /** RecyclerView에 Adapter, 데이터 설정 **/
        groupData = new GroupData();
        groupListAdapter = new GroupListAdapter(getActivity());

        recyclerview.setAdapter(groupListAdapter);


        /** RecyclerView Item Click Listener **/
        Group_list.setOnItemClickListener(new FamiliarRecyclerView.OnItemClickListener() {
            @Override
            public void onItemClick(FamiliarRecyclerView familiarRecyclerView, View view, int position) {
                String select_name = groupData.getGroupDataList().get(position).getGroup_id();
                //이 부분 짜기!
                Toast.makeText(getActivity(), "[" + select_name + "](" + position + ") 선택", Toast.LENGTH_SHORT).show();
            }
        });

        /** RecyclerView Refresh이벤트 처리(일반적으로 위에서 당기기기는 현 정보에서 갱신, 아래에서 로딩은 '더보기'기능) **/
        Group_list.setOnPullRefreshListener(new FamiliarRefreshRecyclerView.OnPullRefreshListener() {
            @Override
            public void onPullRefresh() {
                new android.os.Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Log.i("EVENT :", "새로고침 완료");

                        Group_list.pullRefreshComplete();
                        if (groupData.getGroupDataList().size() > 0) {
                            groupData.getGroupDataList().clear(); //리스트 정보 초기화//

                            groupListAdapter.set_GroupData(groupData); //초기화된 정보를 갱신//
                        }
                        get_GroupData(id);


                    }
                }, 1000);
            }
        });


        Group_list.setOnLoadMoreListener(new FamiliarRefreshRecyclerView.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                new android.os.Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Log.i("EVENT :", "새로고침 완료");

                        Group_list.loadMoreComplete();

                        //맨 아래일 시 '더보기' 작업(더보기 작업 생략 - 더미데이터로는 구현하지 않음)//
                        Toast.makeText(getActivity(), "더 이상 정보가 없습니다", Toast.LENGTH_SHORT).show();
                    }
                }, 1000);

            }
        });

        return view;
    }

    public void get_GroupData(String id) {

        OkHttpClient client = new OkHttpClient();
//        RequestBody body = new FormBody.Builder()
//                .add("body", "ToGetInfo")
//                .build();
        Request request = new Request.Builder()
                .url(String.format("%s/api/group/%s", Constants.SERVER_IP, id))
                .build();

        /** 비동기 방식(enqueue)으로 Callback 구현 **/
        client.newCall(request).enqueue(requestuserlistcallback);
    }

    public void set_Groupdata(final UserListRequestResultsGroupList[] GroupList, final int length) {
        if (getActivity() != null) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    List<UserListRequestResultsGroupList> userlist = new ArrayList<>();

                    userlist.addAll(Arrays.asList(GroupList));

                    for (int i = 0; i < length; i++) {
                        GroupData new_Group = new GroupData();

                        new_Group.setGroup_id(userlist.get(i).getGroup_id());
                        new_Group.setGroup_goal(userlist.get(i).getGroup_goal());
                        new_Group.setGroup_goal_unit(userlist.get(i).getGroup_goal_unit());
                        new_Group.setGroup_period_unit(userlist.get(i).getGroup_period_unit());
                        new_Group.setGroup_period_start(userlist.get(i).getGroup_period_start());
                        new_Group.setGroup_period_end(userlist.get(i).getGroup_period_end());
                        new_Group.setGroup_template(userlist.get(i).getGroup_template());
                        new_Group.setGroup_unit(userlist.get(i).getGroup_unit());
                        new_Group.setGroup_members(userlist.get(i).getGroup_members());

                        groupData.GroupDataList.add(new_Group);
                    }

                    groupListAdapter.set_GroupData(groupData);
                }
            });
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        Toast.makeText(getActivity(), "이미지가 보이지 않을 시 새로고침(당기기) 해주세요", Toast.LENGTH_SHORT).show();
        //수정이나 삭제가 되었을 시 다시 프래그먼트로 돌아오면 초기화//
        if (groupData.getGroupDataList().size() > 0) {
            groupData.getGroupDataList().clear(); //리스트 정보 초기화//

            groupListAdapter.set_GroupData(groupData); //초기화된 정보를 갱신//
        }

        get_GroupData(id); //데이터 Reload//


    }

        private void create_new_group(){
            Intent intent = new Intent(getContext(), NewGroupActivity.class);
            intent.putExtra("USER_ID", id);
            intent.putExtra("NICKNAME", NICKNAME);

            startActivity(intent);
        }


}
