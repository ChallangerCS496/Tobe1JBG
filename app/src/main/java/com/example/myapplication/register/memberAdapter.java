package com.example.myapplication.register;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Constants;
import com.example.myapplication.R;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.facebook.FacebookSdk.getApplicationContext;


public class memberAdapter extends RecyclerView.Adapter<memberAdapter.memberViewHolder>{
    private ArrayList<User> UserList;
    private String fb_id, my_nickname;
    private HashSet<Integer> selectedUsers;

    public memberAdapter(String id, ArrayList<User> users, String my_nickname){
        this.fb_id = id;
        //fetch_member();
        this.UserList = users;
        this.selectedUsers = new HashSet<>();
        this.my_nickname = my_nickname;
    }

    public void setUserList(ArrayList<User> userList) {
        UserList.addAll(userList);
    }

    public void add_member(int index){
        this.selectedUsers.add(index);
    }


    @NonNull
    @Override
    public memberAdapter.memberViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.newgroup_member_obj, parent, false);
        return new memberViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull memberAdapter.memberViewHolder holder, int position) {
        User item = UserList.get(position);
        holder.bind(item);
    }

    @Override
    public int getItemCount() {
        if(UserList != null){
            return UserList.size();
        }
        else return 0;
    }

    public ArrayList<String> generatePartyString() {
//        String members = "[ \"" + my_nickname + "\",";//Todo 닉네임을 찾아라
        ArrayList members = new ArrayList();
        members.add(my_nickname);


        for (Integer index : selectedUsers) {
            members.add(UserList.get(index).getNickname());
        }

        Log.d("액티비티 memberAdapter", members.toString());
        return members;
    }

    public class memberViewHolder extends RecyclerView.ViewHolder{
        public TextView name, nickname;
        public ImageButton selectButton;
        public boolean selected = false;

        public memberViewHolder(View itemView){
            super(itemView);

            //click event 처리

            name = itemView.findViewById(R.id.member_name);
            nickname = itemView.findViewById(R.id.member_nickname);
            selectButton = itemView.findViewById(R.id.checkbox);

            selectButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = getAdapterPosition();
                    if(pos != RecyclerView.NO_POSITION){
                        if(selectedUsers.contains(pos)){
                            selectButton.setBackgroundResource(R.drawable.ic_baseline_radio_button_unchecked_24);
                            selectedUsers.remove(pos);
                        }
                        else{
                            selectButton.setBackgroundResource(R.drawable.ic_baseline_radio_button_checked_24);
                            add_member(pos);
                        }
                    }
                }
            });
        }

        public void bind(User user_info){
            name.setText(user_info.getName());
            nickname.setText(user_info.getNickname());
        }
    }
}
