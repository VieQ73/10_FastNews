package com.example.fastnews;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class NewsModel {

    @SerializedName("totalArticles")
    private int totalArticles;

    @SerializedName("articles")
    private List<Article> articles;

    public int getTotalArticles() {
        return totalArticles;
    }

    public List<Article> getArticles() {
        return articles;
    }

    public static class Article {
        @SerializedName("title")
        private String title;
        @SerializedName("description")
        private String description;
        @SerializedName("url")
        private String url;
        @SerializedName("image")
        private String image;
        @SerializedName("publishedAt")
        private String publishedAt;

        @SerializedName("source")
        private Source source;

        public static class Source {
            @SerializedName("name")
            private String name;

            public String getName() {
                return name;
            }
        }

        public String getTitle() {
            return title;
        }

        public String getDescription() {
            return description;
        }

        public String getUrl() {
            return url;
        }

        public String getImage() {
            return image;
        }

        public String getPublishedAt() {
            return publishedAt;
        }

        public Source getSource() {
            return source;
        }
    }
}