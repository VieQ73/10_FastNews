package com.example.news.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.news.NewsModel;
import com.example.news.R;
import com.example.news.data.DbHelper;
import com.example.news.utils.NewsDetailBottomSheet;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder> {
    ArrayList<NewsModel.Articles> arrayList;
    Context context;
    String fragment;
    DbHelper db;
    OverlayVisibilityListener listener;

    // Constructor khởi tạo adapter với danh sách tin tức, context, tên fragment và listener
    public NewsAdapter(ArrayList<NewsModel.Articles> arrayList, Context context, String fragment, OverlayVisibilityListener listener) {
        this.arrayList = arrayList;
        this.context = context;
        this.fragment = fragment;
        this.listener = listener;
        db = new DbHelper(context);
    }

    // Interface để quản lý hiển thị lớp phủ
    public interface OverlayVisibilityListener {
        void showOverlay();
        void hideOverlay();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Nạp layout cho mỗi tin tức từ file news_item_layout.xml
        View view = LayoutInflater.from(context).inflate(R.layout.news_item_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        NewsModel.Articles articles = arrayList.get(position);
        holder.newsTitle.setText(articles.getTitle());
        holder.newsSource.setText(articles.getSource().getName());
        holder.newsTimeAgo.setText(timeDifference(articles.getPublishedAt()));

        // Tải hình ảnh tin tức bằng Glide
        Glide.with(holder.itemView.getContext()).load(articles.getUrlToImage()).placeholder(R.drawable.news_placeholder_img).into(holder.newsImg);

        // Xử lý sự kiện nhấn vào tin tức để hiển thị chi tiết
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
                        "https://cdn.pixabay.com/photo/2015/02/15/09/33/news-636978_1280.jpg",
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

        // Xử lý sự kiện nhấn giữ để xóa tin tức trong danh sách xem sau
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
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
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    // Hàm tính thời gian chênh lệch từ thời điểm xuất bản
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
        }
        return dateTimeString;
    }

    // ViewHolder cho mỗi tin tức
    public class ViewHolder extends RecyclerView.ViewHolder {
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
}