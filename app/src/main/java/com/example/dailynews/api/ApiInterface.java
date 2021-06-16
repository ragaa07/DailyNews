package com.example.dailynews.api;
import com.example.dailynews.models.News;
import com.example.dailynews.models.Source;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiInterface {
    @GET("top-headlines")
    Call<News> getnews(
              @Query("country") String country,
              @Query("apiKey") String apiKey
    );
    @GET("everything")
    Call<News>getnewsSearched(
            @Query("q") String keyWord,
            @Query("sortBy") String sortBy,
            @Query("apiKey") String apiKey
    );
}
