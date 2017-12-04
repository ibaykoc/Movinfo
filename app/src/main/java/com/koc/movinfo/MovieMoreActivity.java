package com.koc.movinfo;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.koc.movinfo.Data.CastData;
import com.koc.movinfo.Data.MovieData;
import com.koc.movinfo.Data.SearchMovieResponseData;
import com.koc.movinfo.Data.VideoData;
import com.koc.movinfo.RecyclerAdapter.EnlargableCardAdapter;
import com.koc.movinfo.RecyclerAdapter.MovieMoreCastAdapter;
import com.koc.movinfo.RecyclerAdapter.MovieMoreTrailerAdapter;
import com.koc.movinfo.TmdbApi.GetMovieCast;
import com.koc.movinfo.TmdbApi.GetMovieDetail;
import com.koc.movinfo.TmdbApi.GetMovieVideo;
import com.koc.movinfo.TmdbApi.SearchMovie;
import com.koc.movinfo.TmdbApi.TmdbApi;
import com.koc.movinfo.Utils.Array;

import java.util.Arrays;

public class MovieMoreActivity extends AppCompatActivity {

    MovieData moviedata = new MovieData();
    private ImageView movie_more_backdrop;
    private ImageView movie_more_poster;
    private TextView movie_more_title;
    private TextView movie_more_releaseDate;
    private TextView movie_more_runtime;
    private TextView movie_more_genre;
    private TextView movie_more_rating;
    private TextView movie_more_production_companies;
    private TextView movie_more_overview;

    private MovieMoreTrailerAdapter movie_more_trailer_recyclerView_adapter;
    private MovieMoreCastAdapter movie_more_cast_recyclerView_adapter;
    private EnlargableCardAdapter movie_more_similar_recyclerView_adapter;

    public boolean finishAfterTransition = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_more);

        //Get intent from previous activity
        Intent callerIntent = getIntent();

        //Get movie id to show
        moviedata.id = callerIntent.getIntExtra("MOVIE_ID_TO_SHOW", -1);

        //If movie id is incorrect then do nothing
        if (moviedata.id == -1) return;

        Bitmap backdropReceived = null;
        try{
            backdropReceived = (Bitmap)Bridge.item.poll();
        }catch (Exception ignored){

        }

        Bitmap posterReceived = null;
        try{
            posterReceived = (Bitmap)Bridge.item.poll();
        }catch (Exception ignored){

        }


        moviedata.backdrop_image = backdropReceived;
        moviedata.poster_image = posterReceived;
        movie_more_backdrop = findViewById(R.id.movie_more_backdrop);
        movie_more_poster = findViewById(R.id.movie_more_poster);

        movie_more_backdrop.setImageBitmap(moviedata.backdrop_image);
        movie_more_poster.setImageBitmap(moviedata.poster_image);

        movie_more_title = findViewById(R.id.movie_more_title);
        movie_more_releaseDate = findViewById(R.id.movie_more_releaseDate);
        movie_more_runtime = findViewById(R.id.movie_more_runtime);
        movie_more_genre = findViewById(R.id.movie_more_genre);
        movie_more_rating = findViewById(R.id.movie_more_rating);
        movie_more_production_companies = findViewById(R.id.movie_more_productionCompanies);
        movie_more_overview = findViewById(R.id.movie_more_overview);
        InitMovieMoreTrailerAdapter();

        GetMovieDetail getMovieDetail = new GetMovieDetail(
                new GetMovieDetail.GetMovieDetailListener() {
                    @Override
                    public void onFinished(Integer currentUpdatedMovieId) {

                        movie_more_title.setText(moviedata.title);
                        movie_more_releaseDate.setText(TmdbApi.dateFormatMovinfo.format(
                                moviedata.release_date));
                        if (moviedata.runtime != null && moviedata.runtime != 0) {
                            movie_more_runtime.setText(getString(R.string.movie_runtime, moviedata.runtime));
                        }else{
                            movie_more_runtime.setText("");
                        }
                        Integer[] currentMovieGenreIds = moviedata.genre_ids;
                        if (currentMovieGenreIds != null && currentMovieGenreIds.length != 0) {
                            String[] currentMovieGenres = new String[currentMovieGenreIds.length];
                            for (int i = 0; i < currentMovieGenres.length; i++) {
                                currentMovieGenres[i] = TmdbApi.genres.get(currentMovieGenreIds[i]);
                            }

                            StringBuilder builder = new StringBuilder();
                            for (String s : currentMovieGenres) {
                                builder.append(s).append(", ");
                            }
                            builder.delete(builder.length() - 2, builder.length() - 1);
                            if (!builder.toString().equals("null"))
                                movie_more_genre.setText(builder.toString());
                        }
                        if (moviedata.vote_average != null && moviedata.vote_average != 0f) {
                            movie_more_rating.setText(
                                    getString(R.string.rating, moviedata.vote_average));
                        }else{
                            movie_more_rating.setText("");
                        }

                        movie_more_production_companies.setText(
                                Array.ArrayToCSV(moviedata.production_companies));


                        movie_more_overview.setText(moviedata.overview);
                    }
                });
        getMovieDetail.execute(moviedata);

        GetMovieVideo getMovieVideo = new GetMovieVideo(new GetMovieVideo.GetMovieVideoListener() {
            @Override
            public void onFinished(VideoData[] video_datas_received) {
                movie_more_trailer_recyclerView_adapter.setData(video_datas_received);
            }
        });

        getMovieVideo.execute(moviedata.id);

        InitMovieMoreCastAdapter();

        GetMovieCast getMovieCast = new GetMovieCast(new GetMovieCast.GetMovieCastListener() {
            @Override
            public void onFinished(CastData[] castDataReceived) {
                movie_more_cast_recyclerView_adapter.setData(castDataReceived);
            }
        });

        getMovieCast.execute(moviedata);

        InitSimilarMovieCategory();
        UpdateSimilarMovieData();
    }

    private void InitMovieMoreTrailerAdapter() {
        //Get movie_more_trailer_recyclerView reference
        RecyclerView movie_more_trailer_recyclerView = findViewById(
                R.id.movie_more_trailer_recyclerView);
        movie_more_trailer_recyclerView.setHasFixedSize(true);

        // specify an adapter
        movie_more_trailer_recyclerView_adapter = new MovieMoreTrailerAdapter(this);
        movie_more_trailer_recyclerView.setAdapter(movie_more_trailer_recyclerView_adapter);

        // use a linear layout manager
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this,
                LinearLayoutManager.HORIZONTAL, false);

        //Prevent weird matching parent stuff
        mLayoutManager.setAutoMeasureEnabled(false);
        movie_more_trailer_recyclerView.setLayoutManager(mLayoutManager);
    }

    private void InitMovieMoreCastAdapter() {
        //Get movie_more_cast_recyclerView reference
        RecyclerView movie_more_cast_recyclerView = (RecyclerView) findViewById(
                R.id.movie_more_cast_recyclerView);
        movie_more_cast_recyclerView.setHasFixedSize(true);

        // specify an adapter
        movie_more_cast_recyclerView_adapter = new MovieMoreCastAdapter(this);
        movie_more_cast_recyclerView.setAdapter(movie_more_cast_recyclerView_adapter);

        // use a linear layout manager
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this,
                LinearLayoutManager.HORIZONTAL, false);

        //Prevent weird matching parent stuff
        mLayoutManager.setAutoMeasureEnabled(false);
        movie_more_cast_recyclerView.setLayoutManager(mLayoutManager);
    }

    private void InitSimilarMovieCategory() {
        //Get popular_category_recycleView reference
        RecyclerView movie_more_similar_recyclerView = findViewById(
                R.id.movie_more_similar_recyclerView);
        movie_more_similar_recyclerView.setHasFixedSize(true);

        // specify an adapter
        movie_more_similar_recyclerView_adapter = new EnlargableCardAdapter(
                this, true);
        movie_more_similar_recyclerView.setAdapter(movie_more_similar_recyclerView_adapter);

        // use a linear layout manager
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this,
                LinearLayoutManager.HORIZONTAL, false);

        //Prevent weird matching parent stuff
        mLayoutManager.setAutoMeasureEnabled(false);
        movie_more_similar_recyclerView.setLayoutManager(mLayoutManager);

    }

    private void UpdateSimilarMovieData() {
        SearchMovie getSimilarMovie = new SearchMovie(new SearchMovie.SearchMovieListener() {
            @Override
            public void onFinished(SearchMovieResponseData result) {
                movie_more_similar_recyclerView_adapter.setData(result);
            }
        });
        getSimilarMovie.execute(TmdbApi.GetSimilarMovieUrlAddress(moviedata.id));
    }

    @Override
    protected void onStop() {
        super.onStop();
    }
}
