package com.example.fastnews;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface NewsApi {
    @GET("top-headlines")
    Call<NewsModel> getTopHeadlines(
            @Query("category") String category,
            @Query("lang") String language,
            @Query("country") String country,
            @Query("apikey") String apiKey
    );

    // Ví dụ về một endpoint khác, ví dụ tìm kiếm tin tức
    @GET("search")
    Call<NewsModel> searchNews(
            @Query("q") String query,
            @Query("lang") String language,
            @Query("apikey") String apiKey
    );
}