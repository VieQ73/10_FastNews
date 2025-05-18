package com.example.news.fragments;

import android.graphics.Color;
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

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HomeFragment extends Fragment implements NavbarAdapter.OnCategoryClickListener, NewsAdapter.OverlayVisibilityListener {
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

    LinearLayout mainLL, noNewsLL;

    View dimOverlay;

    SharedPreferencesHelper helper;

    // Ánh xạ danh mục tiếng Việt sang tiếng Anh cho GNews API
    private final Map<String, String> categoryMap = new HashMap<>();

    public HomeFragment() {
    }

    @Nullable
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

        mainLL = view.findViewById(R.id.mainLL);
        noNewsLL = view.findViewById(R.id.noNewsLL);

        dimOverlay = view.findViewById(R.id.dimOverlay);

        helper = new SharedPreferencesHelper(getContext());

        checkForDefault();

        // Khởi tạo ánh xạ danh mục
        categoryMap.put("tổng quát", "general");
        categoryMap.put("giải trí", "entertainment");
        categoryMap.put("kinh doanh", "business");
        categoryMap.put("thể thao", "sports");
        categoryMap.put("sức khỏe", "health");
        categoryMap.put("công nghệ", "technology");

        navArrayList.clear();
        Collections.addAll(navArrayList, "Tổng quát", "Giải trí", "Kinh doanh", "Thể thao", "Sức khỏe", "Công nghệ");

        navRV = view.findViewById(R.id.navRV);
        navAdapter = new NavbarAdapter(navArrayList, getContext());
        navRV.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        navRV.setAdapter(navAdapter);
        navAdapter.setOnCategoryClickListener(this);

        newsRV = view.findViewById(R.id.newsRV);
        newsAdapter = new NewsAdapter(newsArrayList, getContext(), "Trang chủ", this);
        newsRV.setLayoutManager(new LinearLayoutManager(getContext()));
        newsRV.setAdapter(newsAdapter);

        shimmerFrameLayout.startShimmer();
        shimmerNews1.startShimmer();

        getNews("Tổng quát");

        return view;
    }

    public void getNews(String category) {
        String API_KEY = "ca9fed4acd8ed6f43b8b793edfde087b";
        String BASE_URL = "https://gnews.io/api/v4/";
        String country = defaultCountry;
        String language = defaultLanguage != null ? defaultLanguage : "vi";
        int maxNews = defaultMaxNews;

        // Chuyển danh mục tiếng Việt sang tiếng Anh
        String apiCategory = categoryMap.getOrDefault(category.toLowerCase(), "general");
        Log.d("HomeFragment", "Calling API with category: " + apiCategory);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        newsArrayList.clear();

        NewsApi newsApi = retrofit.create(NewsApi.class);
        Call<NewsModel> call = newsApi.getNewsByCategory(API_KEY, apiCategory, language, country, maxNews);

        Log.d("HomeFragment", "API Request URL: " + call.request().url().toString());

        call.enqueue(new Callback<NewsModel>() {
            @Override
            public void onResponse(Call<NewsModel> call, Response<NewsModel> response) {
                shimmerFrameLayout.stopShimmer();
                shimmerFrameLayout.setVisibility(View.GONE);
                shimmerNews1.stopShimmer();
                shimmerNews1.setVisibility(View.GONE);
                newsRV.setVisibility(View.VISIBLE);
                imgNews1.setVisibility(View.VISIBLE);

                if (response.isSuccessful() && response.body() != null) {
                    List<NewsModel.Articles> allArticles = response.body().getArticles();
                    Log.d("HomeFragment", "Received articles: " + (allArticles != null ? allArticles.size() : 0));
                    if (allArticles != null && !allArticles.isEmpty()) {
                        NewsModel.Articles firstArticle = allArticles.get(0);
                        updateUIWithFirstArticle(firstArticle);

                        mainLL.setVisibility(View.VISIBLE);
                        noNewsLL.setVisibility(View.GONE);

                        newsArrayList.clear(); // Xóa lại để đảm bảo không giữ dữ liệu cũ
                        newsArrayList.addAll(allArticles.subList(1, allArticles.size()));
                        newsAdapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(getContext(), "Không tìm thấy bài viết cho danh mục này.", Toast.LENGTH_SHORT).show();
                        mainLL.setVisibility(View.GONE);
                        noNewsLL.setVisibility(View.VISIBLE);
                    }
                } else {
                    Toast.makeText(getContext(), "Không thể tải tin tức. Vui lòng thử lại.", Toast.LENGTH_SHORT).show();
                    mainLL.setVisibility(View.GONE);
                    noNewsLL.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(@NonNull Call<NewsModel> call, Throwable t) {
                shimmerFrameLayout.stopShimmer();
                shimmerFrameLayout.setVisibility(View.GONE);
                shimmerNews1.stopShimmer();
                shimmerNews1.setVisibility(View.GONE);
                newsRV.setVisibility(View.VISIBLE);
                imgNews1.setVisibility(View.VISIBLE);

                mainLL.setVisibility(View.GONE);
                noNewsLL.setVisibility(View.VISIBLE);

                Toast.makeText(getContext(), "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("HomeFragment", "API call failed: " + t.getMessage());
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

        Log.d("HomeFragment", "Updating UI: Title=" + title + ", Source=" + name + ", Image=" + urlImage);

        titleOfNews1.setText(title);
        nameOfNews1.setText(name);
        timeAgoOfNews1.setText(time);
        Glide.with(getContext()).load(urlImage).placeholder(R.drawable.news_placeholder_img).into(imgOfNews1);

        imgOfNews1.setOnClickListener(v -> {
            NewsDetailBottomSheet bottomSheet = new NewsDetailBottomSheet();
            if (urlImage != null) {
                bottomSheet.setNewsData(urlImage, name, title, time, urlToWeb, content);
            } else {
                bottomSheet.setNewsData("https://cdn.pixabay.com/photo/2015/02/15/09/33/news-636978_1280.jpg", name, title, time, urlToWeb, content);
            }
            bottomSheet.setOverlayVisibilityListener(this);
            bottomSheet.show(getActivity().getSupportFragmentManager(), "newsDetailBottomSheet");
            showOverlay();
        });
    }

    @Override
    public void onCategoryClicked(String category) {
        shimmerFrameLayout.setVisibility(View.VISIBLE);
        shimmerFrameLayout.startShimmer();
        shimmerNews1.setVisibility(View.VISIBLE);
        shimmerNews1.startShimmer();
        imgNews1.setVisibility(View.GONE);
        newsRV.setVisibility(View.GONE);
        getNews(category);
    }

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
        } else {
            return dateTimeString;
        }
    }

    private void checkForDefault() {
        defaultLanguage = helper.getLanguage();
        defaultCountry = helper.getCountry();
        defaultMaxNews = helper.getMaxNumbers();

        Log.d("HomeFragment", "Language: " + defaultLanguage + ", Country: " + defaultCountry + ", Max news: " + defaultMaxNews);
    }

    @Override
    public void showOverlay() {
        dimOverlay.setVisibility(View.VISIBLE);
        ((MainActivity) getActivity()).setStatusBarColor(Color.argb(128, 0, 0, 0));
    }

    @Override
    public void hideOverlay() {
        dimOverlay.setVisibility(View.GONE);
        ((MainActivity) getActivity()).setStatusBarColor(Color.argb(255, 255, 255, 255));
    }
}