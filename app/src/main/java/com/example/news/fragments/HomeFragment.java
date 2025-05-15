package com.example.news.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.news.R;
import com.example.news.adapters.NavbarAdapter;

import java.util.ArrayList;

// HomeFragment hiển thị danh sách các danh mục tin tức và tin tức cho mỗi danh mục.
public class HomeFragment extends Fragment implements NavbarAdapter.OnCategoryClickListener {

    private RecyclerView navbarRecyclerView; // RecyclerView để hiển thị danh sách danh mục (navbar)
    private NavbarAdapter navbarAdapter; // Adapter cho RecyclerView
    private ArrayList<String> navItems = new ArrayList<>(); // Danh sách các mục điều hướng (tên các danh mục tin tức)

    // Phương thức này được gọi khi Fragment được tạo.
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate layout cho Fragment (fragment_home.xml)
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // Tìm RecyclerView (navbarRecyclerView) trong layout
        navbarRecyclerView = view.findViewById(R.id.navbarRecyclerView);
        // Thiết lập LayoutManager cho RecyclerView (hiển thị danh sách ngang)
        navbarRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        // Thêm các danh mục tin tức vào danh sách
        navItems.add("Sports");
        navItems.add("Entertainment");
        navItems.add("Technology");
        navItems.add("Business");
        navItems.add("Health");

        // Tạo NavbarAdapter và gán danh sách các mục điều hướng và context
        navbarAdapter = new NavbarAdapter(navItems, getContext());
        // Gán listener cho sự kiện click danh mục (HomeFragment implement OnCategoryClickListener)
        navbarAdapter.setOnCategoryClickListener(this);
        // Gán adapter cho RecyclerView
        navbarRecyclerView.setAdapter(navbarAdapter);

        return view; // Trả về View đã được inflate
    }

    // Phương thức này được gọi khi một danh mục được click trong NavbarAdapter.
    @Override
    public void onCategoryClick(String category) {
        // Xử lý khi người dùng nhấn vào một danh mục
        // Hiển thị một Toast message để thông báo danh mục đã được click
        Toast.makeText(getContext(), "Category clicked: " + category, Toast.LENGTH_SHORT).show();
        // TODO: Load tin tức tương ứng với danh mục này

        //Ví dụ: có thể tạo một hàm để load tin tức:
        // loadNewsByCategory(category);
    }
}
