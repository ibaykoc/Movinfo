package com.koc.movinfo.Data;

import android.graphics.Bitmap;

import java.util.Date;

public class MovieData {
    public Integer id = null;
    public String backdrop_path = null;
    public Bitmap backdrop_image = null;
    public Integer[] genre_ids = null;
    public String overview = null;
    public String poster_path = null;
    public Bitmap poster_image = null;
    public String[] production_companies = null;
    public Date release_date = null;
    public Integer runtime = null;
    public String title = null;
    public Float vote_average = null;
    public boolean imageUpdated = false;
    public boolean detailUpdated = false;

    public boolean updatingImage = false;

   public MovieData(){

   }

//TODO error commented
//    public MovieData(String title, Float vote_average, String overview, String poster_path,
//                     String backdrop_path, String release_date) {
//        this.title = title;
//        this.vote_average = vote_average;
//        this.overview = overview;
//        this.poster_path = poster_path;
//        this.backdrop_path = backdrop_path;
//        this.release_date = release_date;
//    }

    public MovieData(SearchMovieResponseData.Result result) {
        id = result.id;
        vote_average = result.vote_average;
        title = result.title;
        poster_path = result.poster_path;
        genre_ids = result.genre_ids;
        backdrop_path = result.backdrop_path;
        overview = result.overview;
        release_date = result.release_date;

    }

    //TODO error commented
//    public void UpdateDetail(Integer budget, String[] genres, String homepage, Float popularity,
//                             String[] production_companies, Integer revenue, Integer runtime) {
//        this.budget = budget;
//        this.genre_ids = genres;
//        this.homepage = homepage;
//        this.popularity = popularity;
//        this.production_companies = production_companies;
//        this.revenue = revenue;
//        this.runtime = runtime;
//    }
}
