package com.example.news.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class ViewPagerAdapter extends FragmentPagerAdapter {
    private final List<Fragment> fragmentList = new ArrayList<>();

    // Constructor khởi tạo adapter với FragmentManager và behavior
    public ViewPagerAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    // Trả về fragment tại vị trí position
    @NonNull
    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    // Trả về số lượng fragment trong adapter
    @Override
    public int getCount() {
        return fragmentList.size();
    }

    // Thêm fragment vào danh sách
    public void addFragment(Fragment fragment) {
        fragmentList.add(fragment);
    }
}