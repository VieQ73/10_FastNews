package com.example.news.fragments;

import android.graphics.Color;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
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
import com.example.news.MainActivity;
import com.example.news.NewsApi;
import com.example.news.NewsModel;
import com.example.news.R;
import com.example.news.adapters.NavbarAdapter;
import com.example.news.adapters.NewsAdapter;
import com.example.news.data.SharedPreferencesHelper;
import com.example.news.utils.NewsDetailBottomSheet;
import com.facebook.shimmer.ShimmerFrameLayout;

import java.text.ParseException;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

// HomeFragment hiển thị danh sách danh mục và tin tức
public class HomeFragment extends Fragment implements NavbarAdapter.OnCategoryClickListener, NewsAdapter.OverlayVisibilityListener {

    private RecyclerView navbarRecyclerView, newsRecyclerView;
    private NavbarAdapter navbarAdapter;
    private NewsAdapter newsAdapter;
    private ArrayList<String> navItems = new ArrayList<>();
    private ArrayList<NewsModel.Articles> newsItems = new ArrayList<>();

    private CardView imgNews1;
    private ImageView imgOfNews1;
    private TextView titleOfNews1, nameOfNews1, timeAgoOfNews1, newsStatus;

    private String defaultLanguage, defaultCountry;

    private int defaultMaxNews;

    private LinearLayout mainLL, noNewsLL;

    private View dimOverlay;
    private ShimmerFrameLayout shimmerFrameLayout, shimmerNews1;

    SharedPreferencesHelper helper;

    public HomeFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // Khởi tạo giao diện bài viết nổi bật
        imgNews1 = view.findViewById(R.id.imgNews1);
        imgOfNews1 = view.findViewById(R.id.imgOfNews1);
        titleOfNews1 = view.findViewById(R.id.titleOfNews1);
        nameOfNews1 = view.findViewById(R.id.nameOfNews1);
        timeAgoOfNews1 = view.findViewById(R.id.timeAgoOfNews1);
        newsStatus = view.findViewById(R.id.newsStatus);
        mainLL = view.findViewById(R.id.mainLL);
        noNewsLL = view.findViewById(R.id.noNewsLL);
        dimOverlay = view.findViewById(R.id.dimOverlay);

        helper = new SharedPreferencesHelper(getContext());
        checkForDefault();

        // Khởi tạo ShimmerFrameLayout
        shimmerFrameLayout = view.findViewById(R.id.shimmerFrameLayout);
        shimmerFrameLayout.startShimmer();
        shimmerNews1 = view.findViewById(R.id.shimmerNews1);
        shimmerNews1.startShimmer();

        // Khởi tạo RecyclerView cho danh mục
        navbarRecyclerView = view.findViewById(R.id.navRV);
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
        newsRecyclerView = view.findViewById(R.id.newsRV);
        newsAdapter = new NewsAdapter(newsItems, getContext(), "Home", this);
        newsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        newsRecyclerView.setAdapter(newsAdapter);

        // Tải tin tức mặc định
        getNews("Sports");

        return view;
    }

    // Xử lý khi nhấn danh mục
    @Override
    public void onCategoryClick(String category) {
        shimmerFrameLayout.setVisibility(View.VISIBLE);
        shimmerFrameLayout.startShimmer();
        shimmerNews1.setVisibility(View.VISIBLE);
        shimmerNews1.startShimmer();
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
                        updateUIWithFirstArticle(firstArticle);

                        // Thêm sự kiện nhấn cho bài viết nổi bật
                        imgOfNews1.setOnClickListener(v -> {
                            Toast.makeText(getContext(), "Clicked: " + firstArticle.getTitle(), Toast.LENGTH_SHORT).show();
                        });

                        mainLL.setVisibility(View.VISIBLE);
                        noNewsLL.setVisibility(View.GONE);

                        // Cập nhật danh sách tin tức
                        newsItems.clear();
                        newsItems.addAll(articles.subList(1, articles.size()));
                        newsAdapter.notifyDataSetChanged();
                    } else {
                        shimmerFrameLayout.stopShimmer();
                        shimmerFrameLayout.setVisibility(View.GONE);
                        shimmerNews1.stopShimmer();
                        shimmerNews1.setVisibility(View.GONE);
                        newsRecyclerView.setVisibility(View.VISIBLE);
                        imgNews1.setVisibility(View.VISIBLE);
                        mainLL.setVisibility(View.GONE);
                        noNewsLL.setVisibility(View.VISIBLE);
                        Toast.makeText(getContext(), "No news available", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<NewsModel> call, Throwable t) {
                shimmerFrameLayout.stopShimmer();
                shimmerFrameLayout.setVisibility(View.GONE);
                shimmerNews1.stopShimmer();
                shimmerNews1.setVisibility(View.GONE);
                newsRecyclerView.setVisibility(View.VISIBLE);
                imgNews1.setVisibility(View.VISIBLE);
                mainLL.setVisibility(View.GONE);
                noNewsLL.setVisibility(View.VISIBLE);
                Toast.makeText(getContext(), "Failed to load news", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateUIWithFirstArticle(NewsModel.Articles article) {
        String title = article.getTitle();
        String name = article.getSource().getName();
        String content = article.getContent();
        String time = timeDifference(article.getPublishedAt());
        String urlImage = article.getUrlToImage();
        String urlToWeb = article.getUrl();

        // Ghi log thông tin bài viết để debug
        Log.d("HomeFragment", "Cập nhật giao diện: Tiêu đề=" + title + ", Nguồn=" + name + ", Hình ảnh=" + urlImage);

        // Cập nhật các thành phần giao diện
        titleOfNews1.setText(title);
        nameOfNews1.setText(name);
        timeAgoOfNews1.setText(time);
        Glide.with(getContext()).load(urlImage).placeholder(R.drawable.news_placeholder_img).into(imgOfNews1);

        // Thiết lập sự kiện click để mở chi tiết bài viết
        imgOfNews1.setOnClickListener(v -> {
            NewsDetailBottomSheet bottomSheet = new NewsDetailBottomSheet();
            if (urlImage != null) {
                bottomSheet.setNewsData(urlImage, name, title, time, urlToWeb, content);
            } else {
                bottomSheet.setNewsData("https://apdl.lu/wp-content/uploads/2017/09/news-636978_1280.jpg", name, title, time, urlToWeb, content);
            }
            bottomSheet.setOverlayVisibilityListener(this);
            bottomSheet.show(getActivity().getSupportFragmentManager(), "newsDetailBottomSheet");
            showOverlay();
        });
    }

    private void checkForDefault() {
        defaultLanguage = helper.getLanguage();
        defaultCountry = helper.getCountry();
        defaultMaxNews = helper.getMaxNumbers();

    }

    // Hàm tính thời gian chênh lệch từ thời điểm xuất bản
    public static String timeDifference(String dateTimeString) {
        if (dateTimeString.contains("giờ") || dateTimeString.contains("phút")) return dateTimeString;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            Instant inputTime = Instant.parse(dateTimeString);
            Instant currentTime = Instant.now();
            Duration duration = Duration.between(inputTime, currentTime);

            long minutesPassed = duration.toMinutes();
            long hoursPassed = duration.toHours();

            if (minutesPassed < 60) {
                return minutesPassed + " phút trước";
            } else {
                return hoursPassed + " giờ trước";
            }
        }
        return dateTimeString;
    }

    @Override
    public void showOverlay() {
        dimOverlay.setVisibility(View.VISIBLE);
        ((MainActivity) getActivity()).setStatusBarColor(Color.argb(128, 0, 0, 0));
    }

    @Override
    public void hideOverlay() {
        dimOverlay.setVisibility(View.GONE);
        ((MainActivity) getActivity()).setStatusBarColor(Color.argb(128, 0, 0, 0));
    }
}