package com.example.myapplication.ui.home;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
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

import com.example.myapplication.R;

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


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recview = root.findViewById(R.id.home_group_list);
        adapter = new LogAdapter(this.getContext(), this.getActivity());
        recview.setLayoutManager(layoutManager);
        recview.setAdapter(adapter);



        requestRequiredPermissions();
        return root;
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