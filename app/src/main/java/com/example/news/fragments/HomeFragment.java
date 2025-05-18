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

import com.bumptech.glide.Glide;
import com.example.news.NewsApi;
import com.example.news.NewsModel;
import com.example.news.R;
import com.example.news.adapters.NavbarAdapter;
import com.example.news.adapters.NewsAdapter;
import com.facebook.shimmer.ShimmerFrameLayout;

import java.util.ArrayList;
import java.util.Arrays;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

// HomeFragment hiển thị danh sách danh mục và tin tức
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
    private RecyclerView navbarRecyclerView, newsRecyclerView;
    private NavbarAdapter navbarAdapter;

    private ArrayList<String> navItems = new ArrayList<>();
    private ArrayList<NewsModel.Articles> newsItems = new ArrayList<>();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
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
        newsRV = view.findViewById(R.id.newsRecyclerView);

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

        // Khởi tạo ShimmerFrameLayout
        shimmerFrameLayout = view.findViewById(R.id.shimmerFrameLayout);
        shimmerFrameLayout.startShimmer();

        // Khởi tạo RecyclerView cho danh mục
        navbarRecyclerView = view.findViewById(R.id.navbarRecyclerView);
        navbarRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        // Thêm danh mục tin tức
        navItems.add("Sports");
        navItems.add("Entertainment");
        navItems.add("Technology");
        navItems.add("Business");
        navItems.add("Health");

        navbarAdapter = new NavbarAdapter(navItems, getContext());
        navbarAdapter.setOnCategoryClickListener(this);
        navbarRecyclerView.setAdapter(navbarAdapter);

        // Khởi tạo RecyclerView cho tin tức
        newsRecyclerView = view.findViewById(R.id.newsRecyclerView);
        newsAdapter = new NewsAdapter(newsItems, getContext(), "Home");
        newsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        newsRecyclerView.setAdapter(newsAdapter);

        // Tải tin tức mặc định
        getNews("Sports");

        return view;
    }


    // Phương thức này được gọi khi một danh mục được click trong NavbarAdapter.
    @Override
    public void onCategoryClick(String category) {
        shimmerFrameLayout.setVisibility(View.VISIBLE);
        shimmerFrameLayout.startShimmer();
        newsRecyclerView.setVisibility(View.GONE);
        imgNews1.setVisibility(View.GONE);
        getNews(category);
    }

    // Tải tin tức từ GNews API
    private void getNews(String category) {
        String API_KEY = "ca9fed4acd8ed6f43b8b793edfde087b";
        String BASE_URL = "https://gnews.io/api/v4/";

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        NewsApi newsApi = retrofit.create(NewsApi.class);
        Call<NewsModel> call = newsApi.getNewsByCategory(API_KEY, category.toLowerCase(), "en", "us", 10);

        call.enqueue(new Callback<NewsModel>() {
            @Override
            public void onResponse(Call<NewsModel> call, Response<NewsModel> response) {
                shimmerFrameLayout.stopShimmer();
                shimmerFrameLayout.setVisibility(View.GONE);
                newsRecyclerView.setVisibility(View.VISIBLE);
                imgNews1.setVisibility(View.VISIBLE);

                if (response.isSuccessful() && response.body() != null) {
                    List<NewsModel.Articles> articles = response.body().getArticles();
                    if (articles != null && !articles.isEmpty()) {
                        // Hiển thị bài viết nổi bật
                        NewsModel.Articles firstArticle = articles.get(0);
                        titleOfNews1.setText(firstArticle.getTitle());
                        nameOfNews1.setText(firstArticle.getSource().getName());
                        Glide.with(getContext()).load(firstArticle.getUrlToImage())
                                .placeholder(R.drawable.news_placeholder_img)
                                .into(imgOfNews1);

                        // Thêm sự kiện nhấn cho bài viết nổi bật
                        imgOfNews1.setOnClickListener(v -> {
                            Toast.makeText(getContext(), "Clicked: " + firstArticle.getTitle(), Toast.LENGTH_SHORT).show();
                        });

                        // Cập nhật danh sách tin tức
                        newsItems.clear();
                        newsItems.addAll(articles.subList(1, articles.size()));
                        newsAdapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(getContext(), "No news available", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<NewsModel> call, Throwable t) {
                shimmerFrameLayout.stopShimmer();
                shimmerFrameLayout.setVisibility(View.GONE);
                Toast.makeText(getContext(), "Failed to load news", Toast.LENGTH_SHORT).show();
            }
        });
    }
}