package com.example.news.fragments;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.graphics.Color;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.news.MainActivity;
import com.example.news.NewsApi;
import com.example.news.NewsModel;
import com.example.news.R;
import com.example.news.adapters.NewsAdapter;
import com.facebook.shimmer.ShimmerFrameLayout;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SearchFragment extends Fragment implements NewsAdapter.OverlayVisibilityListener {
    RecyclerView watchLaterRV;
    NewsAdapter newsAdapter;
    ArrayList<NewsModel.Articles> newsArrayList = new ArrayList<>();
    EditText searchNews;
    TextView showResults;
    View dimOverlay;
    ShimmerFrameLayout shimmerSearch;

    private LinearLayout filterLayout, searchNewsLL, noNewsLL;
    private Spinner languageSpinner, countrySpinner, numberSpinner;
    private Button fromDateButton, toDateButton;
    private String selectedFromDate, selectedToDate;

    // Biến lưu trữ giá trị được chọn từ spinner
    private String selectedLanguage = null;
    private String selectedCountry = null;
    private String selectedNumber = "20";

    // Constructor rỗng bắt buộc cho Fragment
    public SearchFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Nạp layout cho fragment từ file fragment_search.xml
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        // Khởi tạo các thành phần giao diện
        searchNews = view.findViewById(R.id.searchNews);
        watchLaterRV = view.findViewById(R.id.watchLaterRV);
        showResults = view.findViewById(R.id.showResults);
        dimOverlay = view.findViewById(R.id.dimOverlay);

        filterLayout = view.findViewById(R.id.filterLayout);
        languageSpinner = view.findViewById(R.id.languageSpinner);
        countrySpinner = view.findViewById(R.id.countrySpinner);
        numberSpinner = view.findViewById(R.id.numberSpinner);
        shimmerSearch = view.findViewById(R.id.shimmerSearch);
        searchNewsLL = view.findViewById(R.id.searchNewsLL);
        noNewsLL = view.findViewById(R.id.noNewsLL);

        // Thiết lập RecyclerView cho danh sách kết quả tìm kiếm
        newsAdapter = new NewsAdapter(newsArrayList, getContext(), "Tìm kiếm", this);
        watchLaterRV.setLayoutManager(new LinearLayoutManager(getContext()));
        watchLaterRV.setAdapter(newsAdapter);

        // Hiển thị giao diện tìm kiếm nếu danh sách tin tức rỗng
        if (newsArrayList.isEmpty()) {
            searchNewsLL.setVisibility(View.VISIBLE);
        } else {
            searchNewsLL.setVisibility(View.GONE);
        }

        // Xử lý sự kiện nhấn phím Enter hoặc Done trên bàn phím
        searchNews.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE || (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN)) {
                    if (!searchNews.getText().toString().trim().isEmpty()) searchNewsByWords();
                    return true;
                }
                return false;
            }
        });

        // Xử lý sự kiện nhấn vào biểu tượng lọc trong ô tìm kiếm
        searchNews.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (searchNews.getCompoundDrawables()[2] != null) {
                        if (event.getRawX() >= (searchNews.getRight() - searchNews.getCompoundDrawables()[2].getBounds().width())) {
                            // Hiển thị hoặc ẩn bộ lọc
                            if (filterLayout.getVisibility() == View.GONE) {
                                filterLayout.setVisibility(View.VISIBLE);
                            } else {
                                filterLayout.setVisibility(View.GONE);
                            }
                            return true;
                        }
                    }
                }
                return false;
            }
        });

        // Xử lý sự kiện chọn ngôn ngữ từ spinner
        languageSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = parent.getItemAtPosition(position).toString();
                if (selectedItem.contains("(") && selectedItem.contains(")")) {
                    selectedLanguage = selectedItem.substring(selectedItem.indexOf('(') + 1, selectedItem.indexOf(')'));
                } else {
                    selectedLanguage = null;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selectedLanguage = null;
            }
        });

        // Xử lý sự kiện chọn quốc gia từ spinner
        countrySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = parent.getItemAtPosition(position).toString();
                if (selectedItem.contains("(") && selectedItem.contains(")")) {
                    selectedCountry = selectedItem.substring(selectedItem.indexOf('(') + 1, selectedItem.indexOf(')'));
                } else {
                    selectedCountry = null;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selectedCountry = null;
            }
        });

        // Xử lý sự kiện chọn số lượng bài viết từ spinner
        numberSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedNumber = parent.getItemAtPosition(position).toString();
                if (selectedNumber.equals("Số bài tối đa")) {
                    selectedNumber = null;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        return view;
    }

    // Hàm tìm kiếm tin tức theo từ khóa sử dụng GNews API
    private void searchNewsByWords() {
        // Hiển thị hiệu ứng tải và ẩn giao diện tìm kiếm
        shimmerSearch.setVisibility(View.VISIBLE);
        shimmerSearch.startShimmer();
        searchNewsLL.setVisibility(View.GONE);

        String keywords = searchNews.getText().toString();
        String API_KEY = "ca9fed4acd8ed6f43b8b793edfde087b";
        String BASE_URL = "https://gnews.io/api/v4/";

        // Sử dụng giá trị mặc định nếu không có lựa chọn
        String language = selectedLanguage != null ? selectedLanguage : "vi";
        String country = selectedCountry != null ? selectedCountry : null;
        int number = selectedNumber != null ? Integer.parseInt(selectedNumber) : 20;

        // Ghi log các tham số tìm kiếm để debug
        Log.d("SearchFragment", "Tham số tìm kiếm: từ khóa=" + keywords + ", ngôn ngữ=" + language + ", quốc gia=" + country + ", số bài=" + number);

        // Khởi tạo Retrofit
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        // Xóa danh sách tin tức hiện tại
        newsArrayList.clear();
        NewsApi newsApi = retrofit.create(NewsApi.class);

        // Gọi API tìm kiếm của GNews
        Call<NewsModel> call = newsApi.getNewsByKeywords(API_KEY, keywords, language, country, number);
        Log.d("SearchFragment", "URL yêu cầu API: " + call.request().url().toString());

        // Thực hiện yêu cầu API bất đồng bộ
        call.enqueue(new Callback<NewsModel>() {
            @Override
            public void onResponse(Call<NewsModel> call, Response<NewsModel> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<NewsModel.Articles> allArticles = response.body().getArticles();
                    if (!allArticles.isEmpty()) {
                        // Cập nhật danh sách tin tức và hiển thị kết quả
                        newsArrayList.clear();
                        newsArrayList.addAll(allArticles);
                        noNewsLL.setVisibility(View.GONE);
                        newsAdapter.notifyDataSetChanged();
                    } else {
                        // Hiển thị thông báo khi không tìm thấy bài viết
                        Toast.makeText(getContext(), "Không tìm thấy bài viết cho từ khóa này.", Toast.LENGTH_SHORT).show();
                        noNewsLL.setVisibility(View.VISIBLE);
                    }
                    // Hiển thị số lượng kết quả tìm kiếm
                    showResults.setVisibility(View.VISIBLE);
                    showResults.setText("Hiển thị " + newsArrayList.size() + " bài viết liên quan đến: \"" + keywords + "\"");
                } else {
                    // Xử lý trường hợp API trả về lỗi
                    Toast.makeText(getContext(), "Không thể tải tin tức. Vui lòng thử lại.", Toast.LENGTH_SHORT).show();
                    noNewsLL.setVisibility(View.VISIBLE);
                }
                // Dừng hiệu ứng tải
                shimmerSearch.stopShimmer();
                shimmerSearch.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<NewsModel> call, Throwable t) {
                // Xử lý lỗi khi gọi API thất bại
                noNewsLL.setVisibility(View.VISIBLE);
                Toast.makeText(getContext(), "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                shimmerSearch.stopShimmer();
                shimmerSearch.setVisibility(View.GONE);
            }
        });
    }

    // Hàm hiển thị dialog chọn ngày
    private void showDatePickerDialog(final Button dateButton, boolean isFromDate) {
        final Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);

        // Tạo và hiển thị DatePickerDialog để chọn ngày
        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDay) {
                String selectedDate = selectedDay + "/" + (selectedMonth + 1) + "/" + selectedYear;
                dateButton.setText(selectedDate);
                if (isFromDate) {
                    selectedFromDate = selectedDate;
                } else {
                    selectedToDate = selectedDate;
                }
            }
        }, year, month, day);

        datePickerDialog.show();
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