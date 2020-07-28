package com.example.myapplication.ui.grouplist;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;

public class GroupListViewHolder extends RecyclerView.ViewHolder {
    public TextView group_ID_text, group_goal_text, group_unit_text, group_period_unit_text, group_period_start_text, group_period_end_text;
    public TextView group_goal_unit, group_members;
    public ImageView group_template;

    //데이터 클래스 정의//
    public GroupData groupData;

    public GroupListViewHolder(View itemView) {
        super(itemView);

        group_ID_text = (TextView) itemView.findViewById(R.id.grouplist_card_groupID);
        group_goal_text = (TextView) itemView.findViewById(R.id.grouplist_card_goal);
        group_goal_unit = (TextView) itemView.findViewById(R.id.grouplist_card_goal_unit);
        group_unit_text = (TextView) itemView.findViewById(R.id.grouplist_card_unit);
        group_period_unit_text = (TextView) itemView.findViewById(R.id.grouplist_card_period_unit);
        group_template = (ImageView) itemView.findViewById(R.id.grouplist_card_template);
        group_period_start_text = (TextView) itemView.findViewById(R.id.grouplist_card_period_start);
        group_period_end_text = (TextView) itemView.findViewById(R.id.grouplist_card_period_end);
        group_members = (TextView) itemView.findViewById(R.id.grouplist_card_members);
    }

    public void set_Group_info(GroupData group_data, Context context) {
        this.groupData = group_data; //데이터 정보 등록//

        this.groupData.setGroup_id(group_data.getGroup_id());

        this.group_ID_text.setText(group_data.getGroup_id());
        this.group_goal_text.setText("" + group_data.getGroup_goal());
        this.group_goal_unit.setText(group_data.getGroup_goal_unit());
        this.group_unit_text.setText(group_data.getGroup_unit());
        int period_unit = group_data.getGroup_period_unit();
        if(period_unit == 1){
            this.group_period_unit_text.setText("매일");
        } else if (period_unit == 7) {
            this.group_period_unit_text.setText("매주");
        } else {
            this.group_period_unit_text.setText("매달");
        }
        this.group_members.setText(Integer.toString(group_data.getGroup_members()));
        this.group_period_start_text.setText(group_data.getGroup_period_start().substring(0,10));
        this.group_period_end_text.setText(group_data.getGroup_period_end().substring(0,10));
        //이미지 설정(Drawable방식)//
        String imageName = group_data.getGroup_template();
        int resID = context.getResources().getIdentifier(imageName, "drawable","myapplication");
        this.group_template.setImageResource(resID);

    }
}