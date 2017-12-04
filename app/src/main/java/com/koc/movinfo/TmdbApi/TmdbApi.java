package com.koc.movinfo.TmdbApi;

import android.annotation.SuppressLint;
import android.text.TextUtils;
import android.util.Log;
import android.util.SparseArray;

import com.koc.movinfo.Data.CastData;
import com.koc.movinfo.Data.PersonSearchResponseData;
import com.koc.movinfo.Data.SearchMovieResponseData;
import com.koc.movinfo.Utils.Network;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by mochi on 12/11/2017.
 */

public class TmdbApi {
    private static final String API_KEY = "6e353a21123e6742c6885dfd7cb5870a";

    public static final String SORT_BY_POPULARITY_ASCENDED = "popularity.asc";
    public static final String SORT_BY_POPULARITY_DESCENDED = "popularity.desc";
    public static final String SORT_BY_RELEASE_DATE_ASCENDED = "release_date.asc";
    public static final String SORT_BY_RELEASE_DATE_DESCENDED = "release_date.desc";
    public static final String SORT_BY_REVENUE_ASCENDED = "revenue.asc";
    public static final String SORT_BY_REVENUE_DESCENDED = "revenue.desc";
    public static final String SORT_BY_PRIMARY_RELEASE_ASCENDED = "primary_release_date.asc";
    public static final String SORT_BY_PRIMARY_RELEASE_DESCENDED = "primary_release_date.desc";
    public static final String SORT_BY_ORIGINAL_TITLE_ASCENDED = "original_title.asc";
    public static final String SORT_BY_ORIGINAL_TITLE_DESCENDED = "original_title.desc";
    public static final String SORT_BY_VOTE_AVERAGE_ASCENDED = "vote_average.asc";
    public static final String SORT_BY_VOTE_AVERAGE_DESCENDED = "vote_average.desc";
    public static final String SORT_BY_VOTE_COUNT_ASCENDED = "vote_count.asc";
    public static final String SORT_BY_VOTE_COUNT_DESCENDED = "vote_count.desc";

    public static SparseArray<String> genres = new SparseArray<>();

    //For release date formatting
    @SuppressLint("SimpleDateFormat")
    public static SimpleDateFormat dateFormatTMDB = new SimpleDateFormat("yyyy-MM-dd");
    @SuppressLint("SimpleDateFormat")
    public static SimpleDateFormat dateFormatMovinfo = new SimpleDateFormat("dd MMMM yyyy");

    public static void Init() {
        //Init genres
        GetGenresFromServer getGenresFromServer = new GetGenresFromServer(new GetGenresFromServer.GetGenresFromServerListener() {
            @Override
            public void OnFinished(SparseArray<String> genresResult) {
                genres = genresResult;
            }
        });
        getGenresFromServer.execute(GetGenresUrl());
    }

    public static String GetDiscoverURL(String language, String region, String sort_by,
                                        Date primaryReleaseDateGreaterThan,
                                        Date primaryReleaseDateLessThan) {
        String result = "https://api.themoviedb.org/3/discover/movie?api_key=" + API_KEY;

        if (language != null)
            result += "&language=" + language;

        if (region != null)
            result += "&region=" + region;

        if (sort_by != null)
            result += "&sort_by=" + sort_by;

        if(primaryReleaseDateGreaterThan != null)
            result += "&primary_release_date.gte=" +
                    dateFormatTMDB.format(primaryReleaseDateGreaterThan);

        if(primaryReleaseDateLessThan != null)
            result += "&primary_release_date.lte=" +
                    dateFormatTMDB.format(primaryReleaseDateLessThan);
        return result;
    }

    public static String GetGenresUrl() {
        return "https://api.themoviedb.org/3/genre/movie/list?api_key=" + API_KEY;
    }

    public static String GetSearchMovieUrlAddress(String query) {
        return "https://api.themoviedb.org/3/search/movie?api_key=" + API_KEY + "&query=" + query;
    }

    public static String GetMovieDetailUrlAddress(int movieId) {
        return "https://api.themoviedb.org/3/movie/" + String.valueOf(movieId) +
                "?api_key=" + API_KEY;
    }

    public static String GetSimilarMovieUrlAddress(int movieId) {
        return "https://api.themoviedb.org/3/movie/" + String.valueOf(movieId) +
                "/similar?api_key=" + API_KEY;
    }

    public static String GetImageUrlAddress(String imageWidthSize, String imagePath) {
        return "http://image.tmdb.org/t/p/" + imageWidthSize + imagePath;
    }

    public static String GetVideoUrlAddress(Integer movieIdToUpdate) {
        return "https://api.themoviedb.org/3/movie/" + String.valueOf(movieIdToUpdate) +
                "/videos?api_key=" + API_KEY;
    }

    public static String GetMovieCreditUrl(Integer movieIdToUpdate) {
        return "https://api.themoviedb.org/3/movie/" + String.valueOf(movieIdToUpdate) +
                "/credits?api_key=" + API_KEY;
    }

    public static SearchMovieResponseData ExtractSearchMovieResponse(
            String discoverMovieResponse) {

        if (TextUtils.isEmpty(discoverMovieResponse)) {
            return null;
        }

        SearchMovieResponseData searchMovieResponseData = new SearchMovieResponseData();

        // Try to parse the JSON response string. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.
        try {

            // Create a JSONObject from the JSON response string
            JSONObject baseJsonResponse = new JSONObject(discoverMovieResponse);

            // Extract the value for the key called "page"
            searchMovieResponseData.page = baseJsonResponse.getInt("page");

            // Extract the value for the key called "total_results"
            searchMovieResponseData.total_results = baseJsonResponse.getInt(
                    "total_results");

            if (searchMovieResponseData.total_results == 0)
                return null;

            // Extract the value for the key called "total_pages"
            searchMovieResponseData.total_pages = baseJsonResponse.getInt("total_pages");

            // Extract the JSONArray associated with the key called "results",
            // which represents a list of movies.
            JSONArray movieArray = baseJsonResponse.getJSONArray("results");

            //Initialize Result array in SearchMovieResponseData
            searchMovieResponseData.results = new SearchMovieResponseData.Result[
                    movieArray.length()];

            // For each movie in the movieArray, create Result object
            for (int i = 0; i < movieArray.length(); i++) {

                SearchMovieResponseData.Result result = searchMovieResponseData.
                        GetNewEmptyResult();

                // Get a single movie at position i within the list of movies
                JSONObject currentMovie = movieArray.getJSONObject(i);

                // Extract the value for the key called "poster_path"
                String poster_pathResult = currentMovie.getString("poster_path");
                result.poster_path = poster_pathResult.equals("null") ? null : poster_pathResult;

                // Extract the value for the key called "adult"
                result.adult = currentMovie.getBoolean("adult");

                // Extract the value for the key called "overview"
                String overview = currentMovie.getString("overview");
                result.overview = (overview.isEmpty() || overview.equals("null")) ?
                        null : overview;

                // Extract the value for the key called "release_date"
                String release_date = currentMovie.getString("release_date");
                result.release_date = (release_date.isEmpty() || release_date.equals("null")) ?
                        null : TmdbApi.dateFormatTMDB.parse(release_date);

                // Extract the JSONArray associated with the key called "genre_ids",
                // which represents a list of genre of the movie.
                JSONArray movieGenreArray = currentMovie.getJSONArray("genre_ids");

                //Initialize genre_ids array in result
                result.genre_ids = new Integer[movieGenreArray.length()];

                // For each genre in the movieGenreArray, get the genre id
                for (int j = 0; j < movieGenreArray.length(); j++) {
                    //Extract the value fro the key called "genre_ids"
                    result.genre_ids[j] = movieGenreArray.getInt(j);
                }

                //Extract the value fro the key called "id"
                result.id = currentMovie.getInt("id");

                //Extract the value fro the key called "original_title"
                result.original_title = currentMovie.getString("original_title");

                //Extract the value fro the key called "original_language"
                result.original_language = currentMovie.getString("original_language");

                // Extract the value for the key called "title"
                result.title = currentMovie.getString("title");

                // Extract the value for the key called "backdrop_path"
                String backdrop_pathResult = currentMovie.getString("backdrop_path");
                result.backdrop_path = backdrop_pathResult.equals("null") ? null : backdrop_pathResult;

                // Extract the value for the key called "popularity"
                result.popularity = Float.parseFloat(currentMovie.getString("popularity"));

                // Extract the value for the key called "vote_count"
                result.vote_count = currentMovie.getInt("vote_count");

                // Extract the value for the key called "video"
                result.video = currentMovie.getBoolean("video");

                // Extract the value for the key called "vote_average"
                result.vote_average = Float.parseFloat(currentMovie.getString("vote_average"));

                // Add the new Result to SearchMovieResponseData
                searchMovieResponseData.results[i] = result;
            }

        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e("SearchMovie",
                    "Problem parsing discoverMovieResponse JSON. ", e);
        } catch (ParseException e) {
            Log.e("SearchMovie", "Problem parsing the release_date.", e);
        }

        // Return SearchMovieResponseData
        return searchMovieResponseData;
    }
}
