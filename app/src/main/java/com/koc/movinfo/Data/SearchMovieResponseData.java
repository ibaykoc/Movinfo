package com.koc.movinfo.Data;

/**
 * Created by mochi on 12/11/2017.
 */

import java.util.Date;

/**
 * Response data result from moview search on TMDB API
 */
public class SearchMovieResponseData {
    public int page = 0;
    public int total_results = 0;
    public int total_pages = 0;
    public Result[] results;


    public SearchMovieResponseData() {
    }

    public Result GetNewEmptyResult(){
        return new Result();
    }

    public class Result {
        public Integer vote_count = null;
        public Integer id = null;
        public Boolean video = false;
        public Float vote_average = null;
        public String title = null;
        public Float popularity = null;
        public String poster_path = null;
        public String original_title = null;
        public String original_language = null;
        public Integer[] genre_ids = null;
        public String backdrop_path = null;
        public Boolean adult = false;
        public String overview = null;
        public Date release_date = null;
    }
}
