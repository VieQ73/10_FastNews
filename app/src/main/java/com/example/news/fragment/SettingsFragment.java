package com.example.news.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.news.R;
import com.example.news.data.SharedPreferencesHelper;

public class SettingsFragment extends Fragment {

    ImageView toggleNotifications;
    SharedPreferencesHelper helper;
    private boolean isNotificationOn = false;

    public SettingsFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        toggleNotifications = view.findViewById(R.id.toggleNotifications);
        helper = new SharedPreferencesHelper(getContext());

        // Load saved notification status
        int notificationStatus = helper.getNotificationStatus();
        if (notificationStatus == 1) {
            isNotificationOn = true;
            toggleNotifications.setImageResource(R.drawable.switch_on_icon);
        } else {
            isNotificationOn = false;
            toggleNotifications.setImageResource(R.drawable.switch_off_icon);
        }

        // Handle toggle notification click
        toggleNotifications.setOnClickListener(v -> {
            if (!isNotificationOn) {
                isNotificationOn = true;
                toggleNotifications.setImageResource(R.drawable.switch_on_icon);
                helper.setNotificationStatus(1);
                Toast.makeText(getContext(), "Đã bật thông báo", Toast.LENGTH_SHORT).show();
            } else {
                isNotificationOn = false;
                toggleNotifications.setImageResource(R.drawable.switch_off_icon);
                helper.setNotificationStatus(0);
                Toast.makeText(getContext(), "Đã tắt thông báo", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }
}