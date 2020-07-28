package com.example.myapplication.ui.grouplist;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.ui.grouplist.PersonListViewHolder;

public class PersonListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    //데이터 클래스 정의//
    PersonData PersonData;

    //자원 클래스 정의//
    Context context;

    //생성자를 이용하여서 자원과 데이터 클래스의 초기화//
    public PersonListAdapter(Context context) {
        this.context = context;

        PersonData = new PersonData();
    }

    public void set_PersonData(PersonData PersonData) {
        if (this.PersonData != PersonData) {
            this.PersonData = PersonData;

            notifyDataSetChanged(); //UI데이터 갱신//
        }

        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //리스트에 나타낼 뷰를 생성//
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.personlist, parent, false);

        PersonListViewHolder holder = new PersonListViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (PersonData.getPersonDataList().size() > 0) {
            if (position < PersonData.getPersonDataList().size()) {
                //뷰홀더의 자원을 초기화//
                final PersonListViewHolder PersonListViewHolder = (PersonListViewHolder) holder;

                //데이터 클래스, 자원을 할당//
                PersonListViewHolder.set_Person_info(PersonData.getPersonDataList().get(position), context);
                return;

            }

            position -= PersonData.getPersonDataList().size();
        }

        throw new IllegalArgumentException("invalid position");
    }

    @Override
    public int getItemCount() {
        if (PersonData == null) {
            return 0;
        }

        //현재 리스트에 등록된 개수만큼 반환//
        return PersonData.getPersonDataList().size();
    }
}