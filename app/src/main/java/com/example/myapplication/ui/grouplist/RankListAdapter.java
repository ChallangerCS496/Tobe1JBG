package com.example.myapplication.ui.grouplist;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;

public class RankListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    //데이터 클래스 정의//
    RankData RankData;

    //자원 클래스 정의//
    Context context;

    //생성자를 이용하여서 자원과 데이터 클래스의 초기화//
    public RankListAdapter(Context context) {
        this.context = context;

        RankData = new RankData();
    }

    public void set_RankData(RankData RankData) {
        if (this.RankData != RankData) {
            this.RankData = RankData;

            notifyDataSetChanged(); //UI데이터 갱신//
        }

        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //리스트에 나타낼 뷰를 생성//
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ranklist, parent, false);

        RankListViewHolder holder = new RankListViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (RankData.getRankDataList().size() > 0) {
            if (position < RankData.getRankDataList().size()) {
                //뷰홀더의 자원을 초기화//
                final RankListViewHolder RankListViewHolder = (RankListViewHolder) holder;

                //데이터 클래스, 자원을 할당//
                RankListViewHolder.set_Rank_info(RankData.getRankDataList().get(position),context);
                return;

            }

            position -= RankData.getRankDataList().size();
        }

        throw new IllegalArgumentException("invalid position");
    }

    @Override
    public int getItemCount() {
        if (RankData == null) {
            return 0;
        }

        //현재 리스트에 등록된 개수만큼 반환//
        return RankData.getRankDataList().size();
    }
}