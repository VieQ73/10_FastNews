package com.example.fastnews;

import android.app.AlarmManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.view.WindowInsetsController;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.example.news.adapters.ViewPagerAdapter;
import com.example.news.fragments.HomeFragment;
import com.example.news.fragments.SearchFragment;
import com.example.news.fragments.WatchLaterFragment;
import com.example.news.fragments.SettingsFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    private BottomNavigationView bottomNavigationView;
    private ViewPager viewPager;

    private HomeFragment homeFragment;
    private SearchFragment searchFragment;
    private WatchLaterFragment watchLaterFragment;
    private SettingsFragment settingsFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Nạp layout cho activity từ file activity_main.xml
        setContentView(R.layout.activity_main);

        // Thiết lập màu thanh trạng thái thành màu trắng
        getWindow().setStatusBarColor(getResources().getColor(R.color.white, this.getTheme()));

        // Đảm bảo biểu tượng thanh trạng thái là màu đen trên nền sáng
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            getWindow().getDecorView().getWindowInsetsController().setSystemBarsAppearance(
                    WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS,
                    WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS);
        } else {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }

        // Khởi tạo các fragment
        homeFragment = new HomeFragment();
        searchFragment = new SearchFragment();
        watchLaterFragment = new WatchLaterFragment();
        settingsFragment = new SettingsFragment();

        // Khởi tạo ViewPager và BottomNavigationView
        viewPager = findViewById(R.id.viewPager);
        bottomNavigationView = findViewById(R.id.bottom_navigation);

        // Thiết lập ViewPager với các fragment
        setupViewPager(viewPager);

        // Xử lý sự kiện chọn mục trên thanh điều hướng dưới
        bottomNavigationView.setOnNavigationItemSelectedListener(
                item -> {
                    int itemId = item.getItemId();
                    if (itemId == R.id.menu_home) {
                        viewPager.setCurrentItem(0);
                        return true;
                    } else if (itemId == R.id.menu_search) {
                        viewPager.setCurrentItem(1);
                        return true;
                    } else if (itemId == R.id.menu_subscription) {
                        viewPager.setCurrentItem(2);
                        return true;
                    } else if (itemId == R.id.menu_personal) {
                        viewPager.setCurrentItem(3);
                        return true;
                    } else return false;
                });

        // Đồng bộ ViewPager với BottomNavigationView
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                // Không làm gì
            }

            @Override
            public void onPageSelected(int position) {
                // Cập nhật mục được chọn trên thanh điều hướng
                bottomNavigationView.getMenu().getItem(position).setChecked(true);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                // Không làm gì
            }
        });
    }



    // Hàm thiết lập màu thanh trạng thái
    public void setStatusBarColor(int color) {
        getWindow().setStatusBarColor(color);
    }
}