package com.example.news.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.news.MainActivity;
import com.example.news.NewsModel;
import com.example.news.R;
import com.example.news.adapters.NewsAdapter;
import com.example.news.data.DbHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Fragment hiển thị danh sách tin tức đã được lưu vào mục "Xem Sau".
 * Người dùng có thể sắp xếp danh sách theo các tiêu chí như "Mới nhất trước" hoặc "Cũ nhất trước".
 */
public class WatchLaterFragment extends Fragment implements NewsAdapter.OverlayVisibilityListener {

    // Các thành phần giao diện
    private RecyclerView watchLaterRV;
    private NewsAdapter newsAdapter;
    private ArrayList<NewsModel.Articles> newsArrayList = new ArrayList<>();
    private DbHelper db;
    private View dimOverlay;
    private LinearLayout noWatchLaterNews;
    private Spinner sortBySpinner;
    private String orderOfNews = "Cũ nhất trước";

    // Constructor rỗng bắt buộc cho Fragment
    public WatchLaterFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Nạp layout cho fragment từ file fragment_watch_later.xml
        View view = inflater.inflate(R.layout.fragment_watch_later, container, false);

        // Ánh xạ các thành phần giao diện từ layout
        dimOverlay = view.findViewById(R.id.dimOverlay);
        noWatchLaterNews = view.findViewById(R.id.noWatchLaterNews);
        sortBySpinner = view.findViewById(R.id.sortBySpinner);
        watchLaterRV = view.findViewById(R.id.watchLater);

        // Khởi tạo DbHelper để quản lý cơ sở dữ liệu tin tức xem sau
        db = new DbHelper(getContext());

        // Thiết lập RecyclerView với adapter và layout manager
        newsAdapter = new NewsAdapter(newsArrayList, getContext(), "Xem sau", this);
        watchLaterRV.setLayoutManager(new LinearLayoutManager(getContext()));
        watchLaterRV.setAdapter(newsAdapter);

        // Thiết lập sự kiện khi người dùng chọn tiêu chí sắp xếp từ Spinner
        sortBySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Lấy tiêu chí sắp xếp được chọn
                String selectedOption = parent.getItemAtPosition(position).toString();
                if (!selectedOption.equals(orderOfNews)) {
                    // Áp dụng sắp xếp khi có thay đổi tiêu chí
                    applySorting(selectedOption);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Không làm gì khi không có lựa chọn
            }
        });

        // Lấy danh sách tin tức đã lưu từ cơ sở dữ liệu
        getSavedNews();

        return view;
    }

    /**
     * Lấy danh sách tin tức đã lưu từ cơ sở dữ liệu và cập nhật giao diện.
     * Nếu danh sách trống, hiển thị thông báo "Không có tin tức để hiển thị".
     */
    public void getSavedNews() {
        // Lấy danh sách tin tức đã lưu
        List<NewsModel.Articles> savedNews = db.getSavedNews();

        // Kiểm tra và hiển thị layout thông báo nếu không có tin tức
        if (savedNews.isEmpty()) {
            noWatchLaterNews.setVisibility(View.VISIBLE);
            watchLaterRV.setVisibility(View.GONE);
        } else {
            noWatchLaterNews.setVisibility(View.GONE);
            watchLaterRV.setVisibility(View.VISIBLE);
        }

        // Cập nhật danh sách tin tức
        newsArrayList.clear();
        newsArrayList.addAll(savedNews);

        // Áp dụng sắp xếp dựa trên tiêu chí hiện tại
        applySorting(orderOfNews);

        // Cập nhật giao diện
        newsAdapter.notifyDataSetChanged();
    }

    /**
     * Áp dụng sắp xếp danh sách tin tức theo tiêu chí được chọn.
     * @param sortOption Tiêu chí sắp xếp: "Mới nhất trước" hoặc "Cũ nhất trước"
     */
    private void applySorting(String sortOption) {
        if (newsArrayList.isEmpty()) return; // Thoát nếu danh sách trống

        // Lưu tiêu chí sắp xếp hiện tại
        orderOfNews = sortOption;

        // Sắp xếp danh sách
        if (sortOption.equals("Mới nhất trước")) {
            // Sắp xếp theo thời gian giảm dần (mới nhất trước)
            Collections.sort(newsArrayList, (o1, o2) -> o2.getPublishedAt().compareTo(o1.getPublishedAt()));
        } else if (sortOption.equals("Cũ nhất trước")) {
            // Sắp xếp theo thời gian tăng dần (cũ nhất trước)
            Collections.sort(newsArrayList, (o1, o2) -> o1.getPublishedAt().compareTo(o2.getPublishedAt()));
        }

        // Cập nhật giao diện sau khi sắp xếp
        newsAdapter.notifyDataSetChanged();
    }

    /**
     * Hiển thị lớp phủ mờ khi mở bottom sheet chi tiết tin tức.
     */
    @Override
    public void showOverlay() {
        dimOverlay.setVisibility(View.VISIBLE);
        ((MainActivity) getActivity()).setStatusBarColor(Color.argb(128, 0, 0, 0));
    }

    /**
     * Ẩn lớp phủ mờ khi đóng bottom sheet chi tiết tin tức.
     */
    @Override
    public void hideOverlay() {
        dimOverlay.setVisibility(View.GONE);
        ((MainActivity) getActivity()).setStatusBarColor(Color.argb(255, 255, 255, 255));
    }
}