package com.example.news;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsViewHolder> {
    private List<com.example.fastnews.NewsModel> newsList;
    private Context context;

    public NewsAdapter(Context context, List<com.example.fastnews.NewsModel> newsList) {
        this.context = context;
        this.newsList = newsList;
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
        com.example.fastnews.NewsModel news = newsList.get(position);
        holder.title.setText(news.getTitle());
        // Tải hình ảnh bằng Glide
        Glide.with(context)
                .load(news.getImageUrl())
                .placeholder(R.drawable.news_placeholder_img)
                .into(holder.image);

        // Xử lý nút chia sẻ: Chia sẻ tiêu đề và URL bài viết qua Intent
        holder.shareButton.setOnClickListener(v -> {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            String shareText = String.format(
                    context.getString(R.string.share_news_text),
                    news.getTitle(),
                    news.getUrl()
            );
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareText);
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, news.getTitle());
            context.startActivity(Intent.createChooser(shareIntent, "Chia sẻ qua"));
        });
    }

    @Override
    public int getItemCount() {
        return newsList.size();
    }

    static class NewsViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        ImageView image;
        ImageView shareButton;

        NewsViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.news_title);
            image = itemView.findViewById(R.id.news_image);
            shareButton = itemView.findViewById(R.id.share_button);
        }
    }
}