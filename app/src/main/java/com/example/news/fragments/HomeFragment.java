package com.example.news.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.news.NewsModel;
import com.example.news.R;
import com.example.news.adapters.NavbarAdapter;
import com.example.news.adapters.NewsAdapter;
import com.facebook.shimmer.ShimmerFrameLayout;

import java.util.ArrayList;
import java.util.Arrays;

// HomeFragment hiển thị danh sách các danh mục tin tức và tin tức cho mỗi danh mục.
public class HomeFragment extends Fragment implements NavbarAdapter.OnCategoryClickListener {

    RecyclerView navRV, newsRV;
    NavbarAdapter navAdapter;
    NewsAdapter newsAdapter;
    ArrayList<String> navArrayList = new ArrayList<>();
    ArrayList<NewsModel.Articles> newsArrayList = new ArrayList<>();

    ImageView imgOfNews1;
    CardView imgNews1;
    TextView titleOfNews1, nameOfNews1, timeAgoOfNews1, newsStatus;

    String defaultLanguage, defaultCountry;
    int defaultMaxNews;

    ShimmerFrameLayout shimmerFrameLayout, shimmerNews1;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate layout cho Fragment (fragment_home.xml)
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        imgNews1 = view.findViewById(R.id.imgNews1);
        imgOfNews1 = view.findViewById(R.id.imgOfNews1);
        titleOfNews1 = view.findViewById(R.id.titleOfNews1);
        nameOfNews1 = view.findViewById(R.id.nameOfNews1);
        timeAgoOfNews1 = view.findViewById(R.id.timeAgoOfNews1);
        newsStatus = view.findViewById(R.id.newsStatus);
        shimmerFrameLayout = view.findViewById(R.id.shimmerFrameLayout);
        shimmerNews1 = view.findViewById(R.id.shimmerNews1);
        navRV = view.findViewById(R.id.navRV);
        newsRV = view.findViewById(R.id.newsRV);

        navArrayList.addAll(Arrays.asList("Tổng quát", "Giải trí", "Kinh doanh", "Thể thao", "Sức khỏe", "Công nghệ"));
        navAdapter = new NavbarAdapter(navArrayList, getContext());
        navRV.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        navRV.setAdapter(navAdapter);
        navAdapter.setOnCategoryClickListener(this);

        newsAdapter = new NewsAdapter(newsArrayList, getContext(), "Trang chủ");
        newsRV.setLayoutManager(new LinearLayoutManager(getContext()));
        newsRV.setAdapter(newsAdapter);

        shimmerFrameLayout.startShimmer();
        shimmerNews1.startShimmer();

        return view;
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
