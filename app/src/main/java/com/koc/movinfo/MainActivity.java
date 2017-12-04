package com.koc.movinfo;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.SearchView;

import com.koc.movinfo.Data.SearchMovieResponseData;
import com.koc.movinfo.RecyclerAdapter.EnlargableCardAdapter;
import com.koc.movinfo.TmdbApi.SearchMovie;
import com.koc.movinfo.TmdbApi.TmdbApi;

import java.util.Calendar;
import java.util.Date;


public class MainActivity extends AppCompatActivity {

    private Context context;

    private EnlargableCardAdapter popular_category_recycleView_adapter;
    private EnlargableCardAdapter now_playing_category_recyclerView_adapter;
    private EnlargableCardAdapter upcoming_category_recyclerView_adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = this;

        TmdbApi.Init();

        InitPopularCategory();
        InitNowPlayingCategory();
        InitUpcomingCategory();

        UpdatePopularCategoryData();
        UpdateNowPlayingCategoryData();
        UpdateUpcomingCategoryData();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_search, menu);
        MenuItem search_menu = menu.findItem(R.id.menu_search);
        SearchView view_search = (SearchView) search_menu.getActionView();

        view_search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Intent goToMovieSearch = new Intent(MainActivity.this, MovieSearchActivity.class);
                goToMovieSearch.putExtra("QUERY", query);
                startActivity(goToMovieSearch);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return true;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    private void InitPopularCategory() {
        //Get popular_category_recycleView reference
        RecyclerView popular_category_recycler_view = findViewById(
                R.id.popular_category_recyclerView);
        popular_category_recycler_view.setHasFixedSize(true);

        // specify an adapter
        popular_category_recycleView_adapter = new EnlargableCardAdapter(
                this, false);
        popular_category_recycler_view.setAdapter(popular_category_recycleView_adapter);

        // use a linear layout manager
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this,
                LinearLayoutManager.HORIZONTAL, false);

        //Prevent weird matching parent stuff
        mLayoutManager.setAutoMeasureEnabled(false);
        popular_category_recycler_view.setLayoutManager(mLayoutManager);
    }

    private void InitNowPlayingCategory() {
        //Get recent_category_recyclerView reference
        RecyclerView now_playing_category_recyclerView = findViewById(
                R.id.now_playing_category_recyclerView);
        now_playing_category_recyclerView.setHasFixedSize(true);

        // specify an adapter
        now_playing_category_recyclerView_adapter = new EnlargableCardAdapter(this, false);
        now_playing_category_recyclerView.setAdapter(now_playing_category_recyclerView_adapter);

        // use a linear layout manager
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this,
                LinearLayoutManager.HORIZONTAL, false);

        //Prevent weird matching parent stuff
        mLayoutManager.setAutoMeasureEnabled(false);
        now_playing_category_recyclerView.setLayoutManager(mLayoutManager);
    }

    private void InitUpcomingCategory() {
        //Get recent_category_recyclerView reference
        RecyclerView upcoming_category_recyclerView = findViewById(
                R.id.upcoming_category_recyclerView);
        upcoming_category_recyclerView.setHasFixedSize(true);

        // specify an adapter
        upcoming_category_recyclerView_adapter = new EnlargableCardAdapter(this, false);
        upcoming_category_recyclerView.setAdapter(upcoming_category_recyclerView_adapter);

        // use a linear layout manager
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this,
                LinearLayoutManager.HORIZONTAL, false);

        //Prevent weird matching parent stuff
        mLayoutManager.setAutoMeasureEnabled(false);
        upcoming_category_recyclerView.setLayoutManager(mLayoutManager);
    }

    private void UpdatePopularCategoryData() {
        SearchMovie discoverPopularMovie = new SearchMovie(new SearchMovie.SearchMovieListener() {
            @Override
            public void onFinished(SearchMovieResponseData result) {
                //TmdbApi.databaseManager.AddMovie(result);
                popular_category_recycleView_adapter.setData(result);
            }
        });
        discoverPopularMovie.execute(TmdbApi.GetDiscoverURL(
                null, null, TmdbApi.SORT_BY_POPULARITY_DESCENDED,
                null, null));
    }

    private void UpdateNowPlayingCategoryData() {
        SearchMovie discoverNowPlayingMovie = new SearchMovie(new SearchMovie.SearchMovieListener() {
            @Override
            public void onFinished(SearchMovieResponseData result) {
                now_playing_category_recyclerView_adapter.setData(result);
            }
        });
        Calendar cal = Calendar.getInstance();
        Date now = cal.getTime();
        cal.add(Calendar.MONTH, -1);
        Date pastMonth = cal.getTime();
        discoverNowPlayingMovie.execute(TmdbApi.GetDiscoverURL(null, null,
                TmdbApi.SORT_BY_POPULARITY_DESCENDED, pastMonth, now));
    }

    private void UpdateUpcomingCategoryData() {
        SearchMovie discoverUpcomingMovie = new SearchMovie(new SearchMovie.SearchMovieListener() {
            @Override
            public void onFinished(SearchMovieResponseData result) {
                upcoming_category_recyclerView_adapter.setData(result);
            }
        });
        discoverUpcomingMovie.execute(TmdbApi.GetDiscoverURL(null, null,
                TmdbApi.SORT_BY_POPULARITY_DESCENDED, Calendar.getInstance().getTime(),
                null));
    }
}

