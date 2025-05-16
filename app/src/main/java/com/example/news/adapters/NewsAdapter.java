package com.example.news.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.news.NewsModel;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.news.R;

import java.util.ArrayList;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder> {

    ArrayList<NewsModel.Articles> arrayList;
    Context context;
    String fragment;

    public NewsAdapter(ArrayList<NewsModel.Articles> arrayList, Context context, String fragment) {
        this.arrayList = arrayList;
        this.context = context;
        this.fragment = fragment;
    }

    @NonNull
    @Override
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
        holder.newsTimeAgo.setText(articles.getPublishedAt());

        holder.itemView.setOnClickListener(view ->{

        });
    }

    @Override
    public int getItemCount() {
        return 0;
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

}
