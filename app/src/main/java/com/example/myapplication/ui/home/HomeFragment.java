package com.example.myapplication.ui.home;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.MainActivity;
import com.example.myapplication.R;
import com.example.myapplication.register.NewGroupActivity;

import java.util.ArrayList;
import java.util.Objects;

import okhttp3.OkHttpClient;

public class HomeFragment extends Fragment {


    private HomeViewModel homeViewModel;
    private final OkHttpClient client = new OkHttpClient();
    private LogAdapter adapter;
    private static String[] requiredPermissions = new String[]{
            Manifest.permission.ACCESS_NETWORK_STATE,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION
    };
    private static int PERMISSIONS_REQUEST_ALL = 8;
    private RecyclerView recview;
    private ArrayList<MyGroupInfo> backupList;
    String id, NICKNAME;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        id = getActivity().getIntent().getStringExtra("USER_ID");
        NICKNAME = getActivity().getIntent().getStringExtra("NICKNAME");

        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        final Observer<ArrayList<MyGroupInfo>> contactObserver = new Observer<ArrayList<MyGroupInfo>>() {
            @Override
            public void onChanged(@Nullable final ArrayList<MyGroupInfo> newContacts) {
                adapter.updateItems(newContacts);
            }
        };
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recview = root.findViewById(R.id.home_group_list);
        adapter = new LogAdapter(this.getContext(), getActivity(), new ArrayList<MyGroupInfo>(), id);

        backupList = new ArrayList<>();

        recview.setLayoutManager(layoutManager);
        recview.setAdapter(adapter);

        initializeContacts();
        homeViewModel.getList().observe(getViewLifecycleOwner(), contactObserver);

        requestRequiredPermissions();

        return root;
    }

    private void initializeContacts() {
        ArrayList<MyGroupInfo> data = homeViewModel.getList().getValue();

        if (data == null)
            homeViewModel.init(id);
        else
            adapter.updateItems(homeViewModel.getList().getValue());
        //backupList.addAll(homeViewModel.getList().getValue());
    }


    private void requestRequiredPermissions() {
        boolean allGranted = true;
        for (String permission : HomeFragment.requiredPermissions) {
            boolean granted = ActivityCompat.checkSelfPermission(getContext(), permission) == PackageManager.PERMISSION_GRANTED;
            allGranted = allGranted & granted;
        }

        if (!allGranted)
            requestPermissions(requiredPermissions, PERMISSIONS_REQUEST_ALL);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
    }


}