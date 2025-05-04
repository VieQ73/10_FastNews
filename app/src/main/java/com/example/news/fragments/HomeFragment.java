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
import java.util.List;

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

    // Constructor rỗng bắt buộc cho Fragment
    public HomeFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Nạp layout cho fragment từ file fragment_home.xml
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // Khởi tạo các thành phần giao diện
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

        // Khởi tạo SharedPreferencesHelper để lưu trữ và truy xuất cài đặt người dùng
        helper = new SharedPreferencesHelper(getContext());

        // Kiểm tra và lấy các giá trị cài đặt mặc định
        checkForDefault();

        // Xóa danh sách danh mục hiện tại và thêm các danh mục tin tức bằng tiếng Việt
        navArrayList.clear();
        Collections.addAll(navArrayList, "Tổng quát", "Giải trí", "Kinh doanh", "Thể thao", "Sức khỏe", "Công nghệ");

        // Thiết lập RecyclerView cho thanh điều hướng danh mục
        navRV = view.findViewById(R.id.navRV);
        navAdapter = new NavbarAdapter(navArrayList, getContext());
        navRV.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        navRV.setAdapter(navAdapter);
        navAdapter.setOnCategoryClickListener(this);

        // Bắt đầu hiệu ứng tải cho shimmer layout
        shimmerFrameLayout.startShimmer();
        shimmerNews1.startShimmer();

        // Gọi hàm lấy tin tức cho danh mục mặc định "Tổng quát"
        getNews("tổng quát");

        // Thiết lập RecyclerView cho danh sách tin tức
        newsRV = view.findViewById(R.id.newsRV);
        newsAdapter = new NewsAdapter(newsArrayList, getContext(), "Trang chủ", this);
        newsRV.setLayoutManager(new LinearLayoutManager(getContext()));
        newsRV.setAdapter(newsAdapter);

        return view;
    }

    // Hàm lấy tin tức từ GNews API dựa trên danh mục được chọn
    public void getNews(String category) {
        // Định nghĩa API key và URL cơ bản của GNews API
        String API_KEY = "ca9fed4acd8ed6f43b8b793edfde087b"; //API key thực tế của GNews
        String BASE_URL = "https://gnews.io/api/v4/";
        String country = defaultCountry;
        String language = defaultLanguage != null ? defaultLanguage : "vi"; // Ngôn ngữ mặc định là tiếng Việt
        int maxNews = defaultMaxNews;

        // Khởi tạo Retrofit để thực hiện gọi API
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        // Xóa danh sách tin tức hiện tại để chuẩn bị cho dữ liệu mới
        newsArrayList.clear();
        NewsApi newsApi = retrofit.create(NewsApi.class);
        Call<NewsModel> call = newsApi.getNewsByCategory(API_KEY, category.toLowerCase(), language, country, maxNews);

        // Ghi log URL yêu cầu để debug
        Log.d("HomeFragment", "URL yêu cầu API: " + call.request().url().toString());

        // Thực hiện yêu cầu API bất đồng bộ
        call.enqueue(new Callback<NewsModel>() {
            @Override
            public void onResponse(Call<NewsModel> call, Response<NewsModel> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<NewsModel.Articles> allArticles = response.body().getArticles();
                    if (!allArticles.isEmpty()) {
                        // Dừng hiệu ứng tải và ẩn shimmer layout
                        shimmerFrameLayout.stopShimmer();
                        shimmerFrameLayout.setVisibility(View.GONE);
                        shimmerNews1.stopShimmer();
                        shimmerNews1.setVisibility(View.GONE);
                        newsRV.setVisibility(View.VISIBLE);
                        imgNews1.setVisibility(View.VISIBLE);

                        // Cập nhật giao diện với bài viết đầu tiên
                        NewsModel.Articles firstArticle = allArticles.get(0);
                        updateUIWithFirstArticle(firstArticle);

                        // Hiển thị giao diện chính và ẩn thông báo không có tin tức
                        mainLL.setVisibility(View.VISIBLE);
                        noNewsLL.setVisibility(View.GONE);

                        // Thêm các bài viết còn lại vào danh sách
                        newsArrayList.addAll(allArticles.subList(1, allArticles.size()));
                        newsAdapter.notifyDataSetChanged();
                    } else {
                        // Hiển thị thông báo khi không tìm thấy bài viết
                        shimmerFrameLayout.stopShimmer();
                        shimmerFrameLayout.setVisibility(View.GONE);
                        shimmerNews1.stopShimmer();
                        shimmerNews1.setVisibility(View.GONE);
                        newsRV.setVisibility(View.VISIBLE);
                        imgNews1.setVisibility(View.VISIBLE);
                        Toast.makeText(getContext(), "Không tìm thấy bài viết cho danh mục này.", Toast.LENGTH_SHORT).show();

                        mainLL.setVisibility(View.GONE);
                        noNewsLL.setVisibility(View.VISIBLE);
                    }
                } else {
                    // Xử lý trường hợp API trả về lỗi
                    shimmerFrameLayout.stopShimmer();
                    shimmerFrameLayout.setVisibility(View.GONE);
                    shimmerNews1.stopShimmer();
                    shimmerNews1.setVisibility(View.GONE);
                    newsRV.setVisibility(View.VISIBLE);
                    imgNews1.setVisibility(View.VISIBLE);
                    Toast.makeText(getContext(), "Không thể tải tin tức. Vui lòng thử lại.", Toast.LENGTH_SHORT).show();

                    mainLL.setVisibility(View.GONE);
                    noNewsLL.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(@NonNull Call<NewsModel> call, Throwable t) {
                // Xử lý lỗi khi gọi API thất bại
                shimmerFrameLayout.stopShimmer();
                shimmerFrameLayout.setVisibility(View.GONE);
                shimmerNews1.stopShimmer();
                shimmerNews1.setVisibility(View.GONE);
                newsRV.setVisibility(View.VISIBLE);
                imgNews1.setVisibility(View.VISIBLE);

                mainLL.setVisibility(View.GONE);
                noNewsLL.setVisibility(View.VISIBLE);

                Toast.makeText(getContext(), "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Hàm cập nhật giao diện với bài viết đầu tiên
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
                bottomSheet.setNewsData("https://cdn.pixabay.com/photo/2015/02/15/09/33/news-636978_1280.jpg", name, title, time, urlToWeb, content);
            }
            bottomSheet.setOverlayVisibilityListener(this);
            bottomSheet.show(getActivity().getSupportFragmentManager(), "newsDetailBottomSheet");
            showOverlay();
        });
    }

    // Xử lý sự kiện khi người dùng chọn một danh mục
    @Override
    public void onCategoryClicked(String category) {
        // Hiển thị hiệu ứng tải và ẩn nội dung hiện tại
        shimmerFrameLayout.setVisibility(View.VISIBLE);
        shimmerFrameLayout.startShimmer();
        shimmerNews1.setVisibility(View.VISIBLE);
        shimmerNews1.startShimmer();
        imgNews1.setVisibility(View.GONE);
        newsRV.setVisibility(View.GONE);
        getNews(category.toLowerCase());
    }

    // Hàm tính thời gian chênh lệch từ thời điểm xuất bản bài viết
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

    // Kiểm tra và lấy các giá trị cài đặt mặc định từ SharedPreferences
    private void checkForDefault() {
        defaultLanguage = helper.getLanguage();
        defaultCountry = helper.getCountry();
        defaultMaxNews = helper.getMaxNumbers();

        // Ghi log các giá trị cài đặt để debug
        Log.d("HomeFragment", "Ngôn ngữ: " + defaultLanguage + ", Quốc gia: " + defaultCountry + ", Số tin tối đa: " + defaultMaxNews);
    }

    // Hiển thị lớp phủ khi mở bottom sheet
    @Override
    public void showOverlay() {
        dimOverlay.setVisibility(View.VISIBLE);
        ((MainActivity) getActivity()).setStatusBarColor(Color.argb(128, 0, 0, 0));
    }

    // Ẩn lớp phủ khi đóng bottom sheet
    @Override
    public void hideOverlay() {
        dimOverlay.setVisibility(View.GONE);
        ((MainActivity) getActivity()).setStatusBarColor(Color.argb(255, 255, 255, 255));
    }
}