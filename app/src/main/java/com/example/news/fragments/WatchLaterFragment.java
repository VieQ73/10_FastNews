package com.example.news.fragments;

import android.graphics.Color;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.example.news.MainActivity;
import com.example.news.R;
import com.example.news.NewsModel;
import com.example.news.adapters.NewsAdapter;
import com.example.news.data.DbHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class WatchLaterFragment extends Fragment {

    private RecyclerView watchLaterRV;
    private ArrayList<NewsModel.Articles> newArrayList = new ArrayList<>();
    private View dimOverlay;
    private LinearLayout noWatchLaterNews;
    private Spinner sortBySpinner;
    private String orderOfNews = "Cũ nhất trước";
    private DbHelper db;
    private NewsAdapter newsAdapter;
    private List<NewsModel.Articles> newsArrayList = new ArrayList<>();



    public WatchLaterFragment() {
    }


    @Override
    public void onCreate (Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_watch_later, container, false);

        dimOverlay = view.findViewById(R.id.dimOverlay);
        noWatchLaterNews = view.findViewById(R.id.noWatchLaterNews);
        sortBySpinner = view.findViewById(R.id.sortBySpinner);
        watchLaterRV = view.findViewById(R.id.watchLater);

        db = new DbHelper(getContext());

        // thiết lập RecycleView với newsAdapter
        newsAdapter = new NewsAdapter(getContext(),newsArrayList);
        watchLaterRV.setLayoutManager(new LinearLayoutManager(getContext()));
        watchLaterRV.setAdapter(newsAdapter);

        sortBySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String selectOption = adapterView.getItemAtPosition(i).toString();
                if(!selectOption.equals(orderOfNews)){
                    applySorting(selectOption);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        getSavedNews();

        return view;
    }

    private void getSavedNews() {
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


    private void applySorting(String option) {
        if(newArrayList.isEmpty())
            return;

        orderOfNews = option;

        if(option.equals("Mới nhất trước")){
            Collections.sort(newArrayList, (o1, o2) -> o2.getPublishedAt().compareTo(o1.getPublishedAt()));
        }
        else if(option.equals("Mới nhất trước")){
            Collections.sort(newArrayList, (o1, o2) -> o1.getPublishedAt().compareTo(o2.getPublishedAt()));
        }

    }

    // Hiển thị lớp phủ mờ khi xem chi tiết tin tức

    public void showOverlay(){
        dimOverlay.setVisibility(View.VISIBLE);
        ((MainActivity) getActivity()).setStatusBarColor(Color.argb(128, 0, 0, 0));
    }

    public void hideOverlay() {
        dimOverlay.setVisibility(View.GONE);
        ((MainActivity) getActivity()).setStatusBarColor(Color.argb(255, 255, 255, 255));
    }
}

