package com.example.dailynews;

import android.app.ActivityOptions;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;

import com.example.dailynews.adabters.Adapter;
import com.example.dailynews.models.Articles;
import com.example.dailynews.models.News;
import com.example.dailynews.viewModel.NewsViewModel;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.view.ViewCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

public class MainActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {
    public static final String API_KEY = "52f5ee9f072444f5820b4d1bd3bac159";
    private TextView topheadlines;
    private List<Articles>articles;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private Adapter adapter;
    private NewsViewModel newsViewModel;
    private SwipeRefreshLayout swipeRefreshLayout;
    private String tag = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        topheadlines = findViewById(R.id.top_headLines);
        swipeRefreshLayout = findViewById(R.id.swip_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorAccent);
        newsViewModel = ViewModelProviders.of(this).get(NewsViewModel.class);
        recyclerView = findViewById(R.id.recyclar);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        onLoadindSwipeRefresh(null);

    }


    public void loadNews(String keyWord) {
        topheadlines.setVisibility(View.INVISIBLE);
        swipeRefreshLayout.setRefreshing(true);
        newsViewModel.getNews(keyWord);
        newsViewModel.newsMutableLiveData.observe(this, new Observer<News>() {
            @Override
            public void onChanged(News news) {
                articles=news.getArticles();
                adapter = new Adapter(news.getArticles(), MainActivity.this);
                recyclerView.setAdapter(adapter);
                topheadlines.setVisibility(View.VISIBLE);
                swipeRefreshLayout.setRefreshing(false);
                initListner();
            }
        });
    }

    public void initListner(){
        adapter.setOnItemClickListener(new Adapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                ImageView imageView=view.findViewById(R.id.img);
                Intent intent=new Intent(MainActivity.this,NewsDetailActivity.class);
                Articles article= articles.get(position);
                intent.putExtra("url",article.getUrl());
                intent.putExtra("title",article.getTitle());
                intent.putExtra("img",article.getUrlToImage());
                intent.putExtra("date",article.getPublishedAt());
                intent.putExtra("source",article.getSource().getName());
                intent.putExtra("author",article.getAuthor());
                ActivityOptionsCompat activityOptionsCompat=ActivityOptionsCompat.makeSceneTransitionAnimation(MainActivity.this,imageView,"img");
                startActivity(intent,activityOptionsCompat.toBundle());
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        MenuItem menuItem = menu.findItem(R.id.action_search);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setQueryHint("Search Latest News....");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                if (s.length() > 2) {
                    onLoadindSwipeRefresh(s);
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                onLoadindSwipeRefresh(s);
                return false;
            }
        });

        return true;
    }


    @Override
    public void onRefresh() {
        loadNews(null);
    }

    public void onLoadindSwipeRefresh(final String keyWord) {
        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                loadNews(keyWord);
            }
        });
    }

}
