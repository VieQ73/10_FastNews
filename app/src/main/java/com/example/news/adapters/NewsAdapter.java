package com.example.news.adapters;

import android.content.Context;
<<<<<<< HEAD
import android.icu.text.SimpleDateFormat;
=======
import android.content.Intent;
>>>>>>> 483bc3c5061cd5878fe0d84af5d33002a66d6423
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

<<<<<<< HEAD
import com.bumptech.glide.Glide;
import com.example.news.NewsModel;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.news.R;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;
import java.util.TimeZone;
import java.util.logging.Logger;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder> {

    ArrayList<NewsModel.Articles> arrayList;
    Context context;
    String fragment;

    public NewsAdapter(ArrayList<NewsModel.Articles> arrayList, Context context, String fragment) {
        this.arrayList = arrayList;
        this.context = context;
=======
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.fastnews.NewsModel; // Package sai, mô phỏng lỗi
import com.example.news.R;
import com.example.news.utils.NewsDetailBottomSheet;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsViewHolder> {
    private List<com.example.fastnews.NewsModel> newsList;
    private Context context;
    private String fragment;

    public NewsAdapter(Context context, List<com.example.fastnews.NewsModel> newsList, String fragment) {
        this.context = context;
        this.newsList = newsList;
>>>>>>> 483bc3c5061cd5878fe0d84af5d33002a66d6423
        this.fragment = fragment;
    }

    @NonNull
    @Override
<<<<<<< HEAD
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.news_item_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        NewsModel.Articles articles = arrayList.get(position);
        Glide.with(holder.itemView.getContext())
                .load(articles.getUrlToImage())
                .placeholder(com.google.android.material.R.drawable.abc_ic_star_black_16dp)
                .error(androidx.constraintlayout.widget.R.drawable.abc_ic_star_black_16dp)
                .into(holder.newsImg);
        holder.newsTitle.setText(articles.getTitle());
        holder.newsSource.setText(articles.getSource().getName());
        holder.newsTimeAgo.setText(getTimeAgo(articles.getPublishedAt()));

        holder.itemView.setOnClickListener(view ->{

=======
    public NewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.news_item_layout, parent, false);
        return new NewsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NewsViewHolder holder, int position) {
        com.example.fastnews.NewsModel news = newsList.get(position);
        holder.title.setText(news.getTitle());
        holder.newsSource.setText(news.getSource().getName());
        holder.newsTimeAgo.setText(timeDifference(news.getPublishedAt()));

        // Tải hình ảnh bằng Glide
        Glide.with(context)
                .load(news.getImageUrl())
                .placeholder(R.drawable.news_placeholder_img)
                .into(holder.image);

        holder.itemView.setOnClickListener(v -> {
            NewsDetailBottomSheet bottomSheet = new NewsDetailBottomSheet();
            bottomSheet.setNewsData(
                    news.getImageUrl(),
                    news.getSource().getName(),
                    news.getTitle(),
                    timeDifference(news.getPublishedAt()),
                    news.getUrl(),
                    news.getContent()
            );
            bottomSheet.show(((AppCompatActivity) context).getSupportFragmentManager(), "newsDetailBottomSheet");
        });

        holder.shareButton.setOnClickListener(v -> {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            String shareText = news.getTitle() + "\n" + news.getUrl();
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareText);
            context.startActivity(Intent.createChooser(shareIntent, "Chia sẻ tin tức"));
>>>>>>> 483bc3c5061cd5878fe0d84af5d33002a66d6423
        });
    }

    @Override
    public int getItemCount() {
<<<<<<< HEAD
        return arrayList != null ? arrayList.size() : 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView newsImg;
        TextView newsTitle, newsSource, newsTimeAgo;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            newsImg = itemView.findViewById(R.id.newsImg);
            newsTitle = itemView.findViewById(R.id.newsTitle);
            newsSource = itemView.findViewById(R.id.newsSource);
            newsTimeAgo = itemView.findViewById(R.id.newsTimeAgo);
        }
    }

    public interface OverlayVisibilityListener {
        void showOverlay();
        void hideOverlay();
    }

    public static String getTimeAgo(String time) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault());

        try {
            Date past = sdf.parse(time);
            Date now = new Date();

            long seconds = (now.getTime() - past.getTime()) / 1000;

            if (seconds < 60)
                return "Vài giây trước";
            else if (seconds < 3600)
                return (seconds / 60) + " phút trước";
            else if (seconds < 86400)
                return (seconds / 3600) + " giờ trước";
            else
                return (seconds / 86400) + " ngày trước";

        } catch (ParseException e) {
            Logger.getLogger(Objects.requireNonNull(e.getMessage()));
        }

        return time;
    }

}
=======
        return newsList.size();
    }

    // Hàm tính thời gian chênh lệch
    public static String timeDifference(String dateTimeString) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault());
            Date date = sdf.parse(dateTimeString);
            long diffInMillis = System.currentTimeMillis() - date.getTime();
            long minutes = diffInMillis / (1000 * 60);
            if (minutes < 60) {
                return minutes + " phút trước";
            } else {
                return (minutes / 60) + " giờ trước";
            }
        } catch (ParseException e) {
            return dateTimeString;
        }
    }

    static class NewsViewHolder extends RecyclerView.ViewHolder {
        TextView title, newsSource, newsTimeAgo;
        ImageView image, shareButton;

        NewsViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.news_title);
            newsSource = itemView.findViewById(R.id.newsSource);
            newsTimeAgo = itemView.findViewById(R.id.newsTimeAgo);
            image = itemView.findViewById(R.id.news_image);
            shareButton = itemView.findViewById(R.id.share_button);
        }
    }
}
>>>>>>> 483bc3c5061cd5878fe0d84af5d33002a66d6423
