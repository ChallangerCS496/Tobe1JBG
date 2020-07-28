package com.example.myapplication.ui.MyPage;

import android.content.Intent;
import android.net.Uri;
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

import java.util.ArrayList;

public class MyPageFragment extends Fragment {

    private MyPageViewModel myPageViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        myPageViewModel =
                ViewModelProviders.of(this).get(MyPageViewModel.class);
        View root = inflater.inflate(R.layout.fragment_mypage, container, false);
        final TextView textView = root.findViewById(R.id.text_notifications);
        textView.setText("Funny page");

        ArrayList<String> random_url = new ArrayList<>();

        random_url.add("https://www.facebook.com/%ED%95%B4%EC%B0%AC%EC%82%AC%EC%A7%84%EB%AA%A8%EC%9D%8C-1762325144081232/");
        random_url.add("https://battlegroundsmobile.kr/");
        random_url.add("https://www.youtube.com/watch?v=wN1FHQGj4n0");
        random_url.add("https://www.youtube.com/watch?v=xCVqH32p4MA");

        int i = (int) (Math.random()*(random_url.size()));
        String url = random_url.get( i );

        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));

        startActivity(intent);

        return root;
    }
}