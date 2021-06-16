package com.example.dailynews.viewModel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.dailynews.Utils;
import com.example.dailynews.api.ApiClient;
import com.example.dailynews.api.ApiInterface;
import com.example.dailynews.models.Articles;
import com.example.dailynews.models.News;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.dailynews.MainActivity.API_KEY;

public class NewsViewModel extends ViewModel {
    public MutableLiveData <News>newsMutableLiveData=new MutableLiveData<>();

    public void getNews(final String KeyWord) {
        String country = Utils.getCountry();
        ApiInterface apiInterface= ApiClient.getApiClient().create(ApiInterface.class);
        Call<News>call ;
        if (KeyWord!=null){
            call=apiInterface.getnewsSearched(KeyWord,"publishedAt",API_KEY);
        }else {
            call=apiInterface.getnews(country,API_KEY);
        }

        call.enqueue(new Callback<News>() {
            @Override
            public void onResponse(Call<News> call, Response<News> response) {
                if (response.isSuccessful() && response.body().getArticles() != null) {
                    newsMutableLiveData.setValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<News> call, Throwable t) {

            }
        });
    }
}
