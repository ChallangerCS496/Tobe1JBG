package com.example.myapplication.ui.grouplist;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;

public class RankListViewHolder extends RecyclerView.ViewHolder {
    public TextView rank_index, rank_total, rank_unit, rank_name, rank_nickname;

    //데이터 클래스 정의//
    public RankData rankData;

    public RankListViewHolder(View itemView) {
        super(itemView);

        rank_total = (TextView) itemView.findViewById(R.id.ranklist_total);
        rank_unit = (TextView) itemView.findViewById(R.id.ranklist_unit);
        rank_name = (TextView) itemView.findViewById(R.id.ranklist_name);
        rank_nickname = (TextView) itemView.findViewById(R.id.ranklist_nickname);
        rank_index = (TextView) itemView.findViewById(R.id.ranklist_index);
    }

    public void set_Rank_info(RankData rank_data, Context context) {
        this.rankData = rank_data; //데이터 정보 등록//

        this.rankData.setRank_id(rank_data.getRank_id());

        this.rank_name.setText(rank_data.getRank_name());
        this.rank_nickname.setText(rank_data.getRank_nickname());
        this.rank_index.setText(Integer.toString(rank_data.getRank_index())); //for문 index로 넣어주면 될듯
        this.rank_total.setText(Integer.toString(rank_data.getRank_total()));
        this.rank_unit.setText(rank_data.getRank_unit());// set_Rank_info에 unit 넣어줘야 함.


    }
}