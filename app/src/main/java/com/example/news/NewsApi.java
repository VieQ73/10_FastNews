package com.example.fastnews;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;
import java.util.List;

public class NewsApi {
    private static NewsApi instance;
    private final NewsApiService newsApiService;

    private NewsApi() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://gnews.io/api/v4/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        newsApiService = retrofit.create(NewsApiService.class);
    }

    public static NewsApi getInstance() {
        if (instance == null) {
            instance = new NewsApi();
        }
        return instance;
    }

    public NewsApiService getNewsService() {
        return newsApiService;
    }
}

interface NewsApiService {
    @GET("search")
    Call<List<NewsModel>> searchNews(
            @Query("q") String query,
            @Query("lang") String language,
            @Query("country") String country,
            @Query("apikey") String apiKey
    );
}