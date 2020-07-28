package com.example.myapplication.ui.grouplist;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.ui.grouplist.PersonData;

public class PersonListViewHolder extends RecyclerView.ViewHolder {
    public TextView person_index, person_goal_text, person_period_unit_text, person_goal_unit;

    //데이터 클래스 정의//
    public PersonData personData;

    public PersonListViewHolder(View itemView) {
        super(itemView);

        person_goal_text = (TextView) itemView.findViewById(R.id.personlist_goal);
        person_goal_unit = (TextView) itemView.findViewById(R.id.personlist_goal_unit);
        person_period_unit_text = (TextView) itemView.findViewById(R.id.personlist_period_unit);
        person_index = (TextView) itemView.findViewById(R.id.personlist_index);
    }

    public void set_Person_info(PersonData person_data, Context context) {
        this.personData = person_data; //데이터 정보 등록//

        this.personData.setPerson_id(person_data.getPerson_id());

        this.person_goal_unit.setText(person_data.getPerson_goal_unit());
        this.person_goal_text.setText(Float.toString((float)person_data.getPerson_history()/person_data.getPerson_goal()));
        this.person_index.setText(Integer.toString(person_data.getPerson_index()));
        int period_unit = person_data.getPerson_period_unit();
        if(period_unit == 1){
            this.person_period_unit_text.setText("일");
        } else if (period_unit == 7) {
            this.person_period_unit_text.setText("주");
        } else {
            this.person_period_unit_text.setText("개월");
        }


    }
}