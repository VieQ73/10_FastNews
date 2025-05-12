package com.example.fastnews;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    private ViewPager2 viewPager;
    private ViewPagerAdapter viewPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        viewPager = findViewById(R.id.viewPager);

        // Tạo danh sách các fragment
        List<Fragment> fragmentList = new ArrayList<>();
        fragmentList.add(new HomeFragment());
        fragmentList.add(new SearchFragment());
        fragmentList.add(new WatchLaterFragment());
        fragmentList.add(new SettingsFragment());

        // Tạo adapter cho ViewPager2
        viewPagerAdapter = new ViewPagerAdapter(this, fragmentList);
        viewPager.setAdapter(viewPagerAdapter);

        // Hiển thị HomeFragment mặc định
        viewPager.setCurrentItem(0, false); // 0 là vị trí của HomeFragment, false để tắt animation

        // Liên kết BottomNavigationView với ViewPager2
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.home) {
                viewPager.setCurrentItem(0, false);
                return true;
            } else if (itemId == R.id.search) {
                viewPager.setCurrentItem(1, false);
                return true;
            } else if (itemId == R.id.watch_later) {
                viewPager.setCurrentItem(2, false);
                return true;
            } else if (itemId == R.id.settings) {
                viewPager.setCurrentItem(3, false);
                return true;
            }
            return false;
        });

        // Đồng bộ ViewPager2 với BottomNavigationView
        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        bottomNavigationView.setSelectedItemId(R.id.home);
                        break;
                    case 1:
                        bottomNavigationView.setSelectedItemId(R.id.search);
                        break;
                    case 2:
                        bottomNavigationView.setSelectedItemId(R.id.watch_later);
                        break;
                    case 3:
                        bottomNavigationView.setSelectedItemId(R.id.settings);
                        break;
                }
            }
        });
    }
}