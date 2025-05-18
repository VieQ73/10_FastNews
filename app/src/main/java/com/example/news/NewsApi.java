package com.example.news;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

    public interface NewsApi {
        @GET("top-headlines")
        Call<NewsModel> getNewsByCategory(
                @Query("token") String apiKey,
                @Query("category") String category,
                @Query("lang") String language,
                @Query("country") String country,
                @Query("max") int max
        );

        @GET("search")
        Call<NewsModel> getNewsByKeywords(
                @Query("token") String apiKey,
                @Query("q") String keywords,
                @Query("lang") String language,
                @Query("country") String country,
                @Query("max") int max
        );
    }
}
