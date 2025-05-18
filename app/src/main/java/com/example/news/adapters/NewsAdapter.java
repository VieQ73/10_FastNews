package com.example.news.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.news.NewsModel;
import com.example.news.R;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsViewHolder> {

    ArrayList<NewsModel.Articles> articleList;
    Context context;

    String fragment;

    DbHelper db;
    OverlayVisibilityListener listener;

    public NewsAdapter(ArrayList<NewsModel.Articles> articleList, Context context, String fragment) {
        this.articleList = articleList;
        this.context = context;
        this.fragment = fragment;
    }

    public NewsAdapter(ArrayList<NewsModel.Articles> articleList, Context context, String fragment, DbHelper db, OverlayVisibilityListener listener) {
        this.articleList = articleList;
        this.context = context;
        this.fragment = fragment;
        this.db = db;
        this.listener = listener;
    }

    public interface OverlayVisibilityListener {
        void showOverlay();
        void hideOverlay();
    }

    @NonNull
    @Override
    public NewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.news_item_layout, parent, false);
        return new NewsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NewsViewHolder holder, int position) {
        NewsModel.Articles article = articleList.get(position);
        holder.title.setText(article.getTitle());
        holder.newsSource.setText(article.getSource().getName());
        holder.newsTimeAgo.setText(getTimeAgo(article.getPublishedAt()));

        Glide.with(context)
                .load(article.getUrlToImage())
                .placeholder(R.drawable.news_placeholder_img)
                .into(holder.image);

        holder.itemView.setOnClickListener(v -> {
            NewsDetailBottomSheet bottomSheet = new NewsDetailBottomSheet();
            if (articles.getUrlToImage() != null) {
                bottomSheet.setNewsData(
                        articles.getUrlToImage(),
                        articles.getSource().getName(),
                        articles.getTitle(),
                        timeDifference(articles.getPublishedAt()),
                        articles.getUrl(),
                        articles.getContent()
                );
            } else {
                bottomSheet.setNewsData(
                        "https://apdl.lu/wp-content/uploads/2017/09/news-636978_1280.jpg",
                        articles.getSource().getName(),
                        articles.getTitle(),
                        timeDifference(articles.getPublishedAt()),
                        articles.getUrl(),
                        articles.getContent()
                );
            }
            bottomSheet.setOverlayVisibilityListener(listener);
            bottomSheet.show(((AppCompatActivity) context).getSupportFragmentManager(), "newsDetailBottomSheet");
            listener.showOverlay();
        });

        holder.itemView.setOnLongClickListener(view -> {
            if (fragment.equals("Xem sau")) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(context)
                        .setTitle("Xóa khỏi Xem sau")
                        .setMessage("Bạn có chắc chắn muốn xóa tin tức này không?")
                        .setCancelable(false)
                        .setPositiveButton("Có", (dialogInterface, i) -> {
                            db.deleteSavedNews(articles.getUrl());
                            arrayList.remove(arrayList.get(position));
                            notifyItemRemoved(holder.getAdapterPosition());
                            notifyItemRangeChanged(position, arrayList.size());
                        })
                        .setNegativeButton("Không", (dialogInterface, i) -> {
                            // Không làm gì
                        });
                dialog.show();
            }
            return true;
        });

        holder.shareButton.setOnClickListener(v -> {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            String shareText = article.getTitle() + "\n" + article.getUrl();
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareText);
            context.startActivity(Intent.createChooser(shareIntent, "Chia sẻ tin tức"));
        });
    }

    @Override
    public int getItemCount() {
        return articleList != null ? articleList.size() : 0;
    }

    public class NewsViewHolder extends RecyclerView.ViewHolder {
        TextView title, newsSource, newsTimeAgo;
        ImageView image, shareButton;

        public NewsViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.news_title);
            newsSource = itemView.findViewById(R.id.newsSource);
            newsTimeAgo = itemView.findViewById(R.id.newsTimeAgo);
            image = itemView.findViewById(R.id.news_image);
            shareButton = itemView.findViewById(R.id.share_button);
        }
    }

    // Chuyển đổi thời gian đăng sang định dạng "x phút trước"
    public static String getTimeAgo(String dateTimeString) {
        try {
            SimpleDateFormat sdf = null;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault());

                Date date = sdf.parse(dateTimeString);
                long diff = System.currentTimeMillis() - date.getTime();

                long seconds = diff / 1000;
                if (seconds < 60) return "Vài giây trước";
                if (seconds < 3600) return (seconds / 60) + " phút trước";
                if (seconds < 86400) return (seconds / 3600) + " giờ trước";
                return (seconds / 86400) + " ngày trước";
            }
        } catch (ParseException e) {
            return dateTimeString;
        }

        return dateTimeString;
    }
}
