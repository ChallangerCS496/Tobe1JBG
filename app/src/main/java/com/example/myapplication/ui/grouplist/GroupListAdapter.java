package com.example.myapplication.ui.grouplist;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;
import com.example.myapplication.R;

public class GroupListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    //데이터 클래스 정의//
    GroupData GroupData;

    //자원 클래스 정의//
    Context context;

    //생성자를 이용하여서 자원과 데이터 클래스의 초기화//
    public GroupListAdapter(Context context) {
        this.context = context;

        GroupData = new GroupData();
    }

    public void set_GroupData(GroupData GroupData) {
        if (this.GroupData != GroupData) {
            this.GroupData = GroupData;

            notifyDataSetChanged(); //UI데이터 갱신//
        }

        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //리스트에 나타낼 뷰를 생성//
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.grouplist_card, parent, false);

        GroupListViewHolder holder = new GroupListViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (GroupData.getGroupDataList().size() > 0) {
            if (position < GroupData.getGroupDataList().size()) {
                //뷰홀더의 자원을 초기화//
                final GroupListViewHolder GroupListViewHolder = (GroupListViewHolder) holder;

                //데이터 클래스, 자원을 할당//
                GroupListViewHolder.set_Group_info(GroupData.getGroupDataList().get(position), context);
                return;

            }

            position -= GroupData.getGroupDataList().size();
        }

        throw new IllegalArgumentException("invalid position");
    }

    @Override
    public int getItemCount() {
        if (GroupData == null) {
            return 0;
        }

        //현재 리스트에 등록된 개수만큼 반환//
        return GroupData.getGroupDataList().size();
    }
}