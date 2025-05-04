package com.example.news.utils;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;

import com.bumptech.glide.Glide;
import com.example.news.R;
import com.example.news.adapters.NewsAdapter;
import com.example.news.data.DbHelper;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class NewsDetailBottomSheet extends BottomSheetDialogFragment {
    private String imgUrl;
    private String newsSource;
    private String newsTitle;
    private String newsTimeAgo;
    private String newsContent;
    private String urlToWeb;

    TextView newsTitleTextView;
    TextView newsSourceTextView;
    TextView newsTimeAgoTextView;
    TextView newsContentTextView;
    ImageView goBackFromDetails, imgOfNews1;
    Button watchLater, learnMore;
    ImageView shareNews;
    DbHelper db;
    CardView shareNewsCV;

    private NewsAdapter.OverlayVisibilityListener overlayVisibilityListener;

    // Hàm thiết lập dữ liệu cho bottom sheet
    public void setNewsData(String imgUrl, String source, String title, String timeAgo, String urlToWeb, String content) {
        this.imgUrl = imgUrl;
        this.newsSource = source;
        this.newsTitle = title;
        this.newsTimeAgo = timeAgo;
        this.urlToWeb = urlToWeb;
        this.newsContent = content;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (getDialog() != null && getDialog().getWindow() != null) {
            // Thiết lập độ mờ và nền trong suốt cho bottom sheet
            getDialog().getWindow().setDimAmount(0.9f);
            getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            View view = getDialog().findViewById(com.google.android.material.R.id.design_bottom_sheet);
            if (view != null) {
                view.getLayoutParams().height = 1800;
                view.requestLayout();
            }
        } else {
            Toast.makeText(getContext(), "Lỗi hiển thị chi tiết tin tức", Toast.LENGTH_SHORT).show();
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Nạp layout cho bottom sheet từ file bottom_sheet_layout.xml
        View view = inflater.inflate(R.layout.bottom_sheet_layout, container, false);

        // Khởi tạo các thành phần giao diện
        newsTitleTextView = view.findViewById(R.id.newsTitle1);
        newsSourceTextView = view.findViewById(R.id.newsSource1);
        newsTimeAgoTextView = view.findViewById(R.id.newsTimeAgo1);
        newsContentTextView = view.findViewById(R.id.newsContent1);
        goBackFromDetails = view.findViewById(R.id.goBackFromDetails);
        imgOfNews1 = view.findViewById(R.id.imgOfNews1);
        watchLater = view.findViewById(R.id.watchLater);
        shareNews = view.findViewById(R.id.shareNews);
        shareNewsCV = view.findViewById(R.id.shareNewsCV);
        learnMore = view.findViewById(R.id.learnMore);

        // Khởi tạo DbHelper để quản lý tin tức xem sau
        db = new DbHelper(getContext());

        // Thiết lập dữ liệu cho các thành phần giao diện
        newsTitleTextView.setText(newsTitle);
        newsSourceTextView.setText(newsSource);
        newsTimeAgoTextView.setText(newsTimeAgo);
        newsContentTextView.setText(newsContent);
        Glide.with(this).load(imgUrl).into(imgOfNews1);

        // Xử lý sự kiện nhấn nút quay lại
        goBackFromDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        // Xử lý sự kiện thêm tin tức vào danh sách xem sau
        watchLater.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.insertSavedNews(imgUrl, newsSource, newsTitle, newsTimeAgo, urlToWeb, newsContent);
                Toast.makeText(getContext(), "Đã thêm vào Xem sau", Toast.LENGTH_SHORT).show();
            }
        });

        // Xử lý sự kiện chia sẻ tin tức
        shareNewsCV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_TEXT, "Xem tin tức này: \n" + urlToWeb);
                Intent chooser = Intent.createChooser(shareIntent, "Chia sẻ tin tức qua...");
                v.getContext().startActivity(chooser);
            }
        });

        // Xử lý sự kiện xem chi tiết tin tức trên web
        learnMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri webpage = Uri.parse(urlToWeb);
                Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
                startActivity(intent);
            }
        });

        return view;
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        if (overlayVisibilityListener != null) {
            overlayVisibilityListener.hideOverlay();
        }
    }

    // Thiết lập listener cho lớp phủ
    public void setOverlayVisibilityListener(NewsAdapter.OverlayVisibilityListener listener) {
        this.overlayVisibilityListener = listener;
    }
}