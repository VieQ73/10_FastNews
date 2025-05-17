package com.example.news.utils;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;

import com.bumptech.glide.Glide;
import com.example.news.R;
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
    ImageView goBackFromDetails, imgOfNew1, shareNews;
    Button watchLater,learnMore;
    CardView shareNewsCV;
    DbHelper db;

    public void setNewsData(String imgURL, String source, String title, String timeAgo, String urlToWeb, String content){
        this.imgUrl = imgURL;
        this.newsSource = source;
        this.newsTitle = title;
        this.newsTimeAgo = timeAgo;
        this.newsContent = content;
        this.urlToWeb = imgURL;
    }


    public NewsDetailBottomSheet() {
        // Required empty public constructor
    }

//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.bottom_sheet_layout, container, false);

        newsSourceTextView = view.findViewById(R.id.newsSource1);
        newsTitleTextView = view.findViewById(R.id.newsTitle1);
        newsTimeAgoTextView = view.findViewById(R.id.newsTimeAgo1);
        newsContentTextView = view.findViewById(R.id.newsContent1);
        goBackFromDetails = view.findViewById(R.id.goBackFromDetails);
        imgOfNew1 = view.findViewById(R.id.imgOfNews1);
        watchLater = view.findViewById(R.id.watchLater);
        shareNewsCV = view.findViewById(R.id.shareNewsCV);
        learnMore = view.findViewById(R.id.learnMore);

        // Gán dữ liệu lên giao diện
        newsSourceTextView.setText(newsSource);
        newsTitleTextView.setText(newsTitle);
        newsTimeAgoTextView.setText(newsTimeAgo);
        newsContentTextView.setText(newsContent);
        Glide.with(getContext()).load(imgUrl).into(imgOfNew1);

        db = new DbHelper(getContext());

        watchLater.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db.saveNews(imgUrl, newsSource, newsTitle, newsTimeAgo, urlToWeb, newsContent);
                Toast.makeText(getContext(), "Đã thêm vào Xem sau", Toast.LENGTH_SHORT).show();
            }
        });

        goBackFromDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        shareNewsCV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (urlToWeb != null && !urlToWeb.isEmpty()) {
                    Intent shareIntent = new Intent(Intent.ACTION_SEND);
                    shareIntent.setType("text/plain");
                    shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Tin tức:");
                    shareIntent.putExtra(Intent.EXTRA_TEXT, newsTitle + "\n" + urlToWeb);
                    startActivity(Intent.createChooser(shareIntent, "Chia sẻ qua"));
                } else {
                    Toast.makeText(getContext(), "Không có link để chia sẻ", Toast.LENGTH_SHORT).show();
                }
            }
        });

        learnMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (urlToWeb != null && !urlToWeb.isEmpty()) {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(urlToWeb));
                    startActivity(browserIntent);
                } else {
                    Toast.makeText(getContext(), "Không có link để xem chi tiết", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }
}