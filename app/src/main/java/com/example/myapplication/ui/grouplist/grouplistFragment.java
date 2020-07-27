package com.example.myapplication.ui.grouplist;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.myapplication.R;
import com.example.myapplication.register.NewGroupActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class grouplistFragment extends Fragment implements View.OnClickListener {

    private grouplistViewModel grouplistViewModel;
    private String id;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        id = getActivity().getIntent().getStringExtra("USER_ID");

        grouplistViewModel =
                ViewModelProviders.of(this).get(grouplistViewModel.class);
        View root = inflater.inflate(R.layout.fragment_grouplist, container, false);

        FloatingActionButton add = (FloatingActionButton) root.findViewById(R.id.add_group);
        add.setOnClickListener(this);

        //뷰모델 응용
//        final TextView textView = root.findViewById(R.id.text_dashboard);
//
//        grouplistViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.setText(s);
//            }
//        });

        return root;
    }

    @Override
    public void onClick(View v){
        int id = v.getId();
        if(id == R.id.add_group){
            create_new_group();
        }
    }

    private void create_new_group(){
        Intent intent = new Intent(getContext(), NewGroupActivity.class);
        intent.putExtra("USER_ID", id);
        startActivity(intent);

    }
}