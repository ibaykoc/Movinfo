package com.koc.movinfo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.koc.movinfo.Data.SearchMovieResponseData;
import com.koc.movinfo.RecyclerAdapter.EnlargableCardAdapter;
import com.koc.movinfo.TmdbApi.SearchMovie;
import com.koc.movinfo.TmdbApi.TmdbApi;

public class MovieSearchActivity extends AppCompatActivity {

    String query = null;

    private EnlargableCardAdapter movie_search_result_recyclerView_adapter;
    private TextView movie_search_total_result_textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_search);
        //Get intent from previous activity
        Intent callerIntent = getIntent();

        query = callerIntent.getStringExtra("QUERY");
        setTitle("Result for: " + query);

        if (query == null) {
            finish();
        }

        InitMovieSearchResult();

        movie_search_total_result_textView = findViewById(R.id.movie_search_total_result_textView);

        SearchMovie searchMovie = new SearchMovie(new SearchMovie.SearchMovieListener() {
            @Override
            public void onFinished(SearchMovieResponseData result) {
                if(result != null) {
                    movie_search_total_result_textView.setText(
                            getString(R.string.total_movie_found, result.total_results));
                    movie_search_result_recyclerView_adapter.setData(result);
                }else{
                    movie_search_total_result_textView.setText(
                            getString(R.string.total_movie_found,0));
                }
            }
        });

        searchMovie.execute(TmdbApi.GetSearchMovieUrlAddress(
                query.replace(" ", "%20")));
    }

    private void InitMovieSearchResult() {
        //Get popular_category_recycleView reference
        RecyclerView movie_search_result_recyclerView = findViewById(
                R.id.movie_search_result_recyclerView);
        movie_search_result_recyclerView.setHasFixedSize(true);

        // specify an adapter
        movie_search_result_recyclerView_adapter = new EnlargableCardAdapter(
                this, false);
        movie_search_result_recyclerView.setAdapter(movie_search_result_recyclerView_adapter);

        // use a linear layout manager
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false);

        //Prevent weird matching parent stuff
        mLayoutManager.setAutoMeasureEnabled(false);
        movie_search_result_recyclerView.setLayoutManager(mLayoutManager);

    }
}
