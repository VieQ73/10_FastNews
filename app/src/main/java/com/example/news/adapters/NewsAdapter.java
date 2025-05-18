package com.example.news.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.news.R;
import com.example.news.utils.NewsDetailBottomSheet;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsViewHolder> {
    private List<com.example.news.NewsModel> newsList;
    private Context context;
    private String fragment;

    public NewsAdapter(Context context, List<com.example.news.NewsModel> newsList, String fragment) {
        this.context = context;
        this.newsList = newsList;
        this.fragment = fragment;
    }

    @NonNull
    @Override
    public NewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.news_item_layout, parent, false);
        return new NewsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NewsViewHolder holder, int position) {
        com.example.news.NewsModel news = newsList.get(position);
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
        });
    }

    @Override
    public int getItemCount() {
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