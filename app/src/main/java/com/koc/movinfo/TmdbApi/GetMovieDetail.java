package com.koc.movinfo.TmdbApi;

import android.os.AsyncTask;
import android.util.Log;

import com.koc.movinfo.Data.MovieData;
import com.koc.movinfo.Utils.Network;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.URL;
import java.util.Date;

/**
 * Created by MochammadIqbal on 16/11/2017.
 * Project Movinfo
 */

public class GetMovieDetail extends AsyncTask<MovieData, Void, Integer> {

    public interface GetMovieDetailListener {
        void onFinished(Integer currentUpdatedMovieId);
    }

    private GetMovieDetailListener mGetMovieDetailListener;

    public GetMovieDetail(GetMovieDetailListener getMovieDetailListener) {
        mGetMovieDetailListener = getMovieDetailListener;
    }

    @Override
    protected Integer doInBackground(MovieData... moviedataToUpdates) {

        MovieData moviedata = moviedataToUpdates[0];

        try {
            //Get current movie detail json response from server
            InputStream in = new URL(TmdbApi.GetMovieDetailUrlAddress(moviedata.id)).openStream();
            Log.d("GetMovieDetail", "Get movie detail from: " +
                    TmdbApi.GetMovieDetailUrlAddress(moviedata.id));
            String jsonResponse = Network.ReadFromStream(in);

            //Close the InputStream after get the json response
            in.close();

            //Format the response data and add it to movie detail data
            String title;
            Date releaseDate;
            Integer runtime;
            Integer[] genre_ids;
            Float vote_average;
            String[] production_companies;
            String overview;

            // Create a JSONObject from the JSON response string
            JSONObject baseJsonResponse = new JSONObject(jsonResponse);

            //Get title
            // Extract the value for the key called "title"
            title = baseJsonResponse.getString("title");

            //Get releaseDate
            // Extract the value for the key called "release_date"
            releaseDate = TmdbApi.dateFormatTMDB.parse(
                    baseJsonResponse.getString("release_date"));

            //Get runtime
            // Extract the value for the key called "runtime"
            String runtimeResult = baseJsonResponse.getString("runtime");
            runtime = runtimeResult.equals("null")? null : Integer.parseInt(runtimeResult);

            // Extract the JSONArray associated with the key called "genres",
            // which represents a list of genre of the movie.
            JSONArray movieGenreArray = baseJsonResponse.getJSONArray("genres");

            //Initialize genre_ids array in result
            genre_ids = new Integer[movieGenreArray.length()];

            // For each genre in the movieGenreArray, get the genre id
            for (int i = 0; i < movieGenreArray.length(); i++) {
                //Extract the value fro the key called "genre_ids"
                // Get a single production_company at position i within the list of movies
                JSONObject genre = movieGenreArray.getJSONObject(i);
                //Extract the value from the key called "id"
                genre_ids[i] = genre.getInt("id");
            }

            //Get vote_average
            // Extract the value for the key called "vote_average"
            vote_average = Float.parseFloat(baseJsonResponse.getString("vote_average"));

            //Get production_companies
            // Extract the JSONArray associated with the key called "production_companies",
            // which represents a list production_company.
            JSONArray production_companiesArray = baseJsonResponse.getJSONArray("production_companies");
            // For each production_company in the production_companiesArray, get the string name
            // and add it to production_companies
            production_companies = new String[production_companiesArray.length()];
            for (int i = 0; i < production_companiesArray.length(); i++) {
                // Get a single production_company at position i within the list of movies
                JSONObject production_company = production_companiesArray.getJSONObject(i);
                //Extract the value from the key called "id"
                production_companies[i] = production_company.getString("name");
            }

            //Get overview
            // Extract the value for the key called "overview"
            overview = baseJsonResponse.getString("overview");


            moviedata.title = title;
            moviedata.release_date = releaseDate;
            moviedata.runtime = runtime;
            moviedata.genre_ids = genre_ids;
            moviedata.vote_average = vote_average;
            moviedata.production_companies = production_companies;
            moviedata.overview = overview;
            moviedata.detailUpdated = true;

        } catch (Exception e) {
            Log.e("GetMovieDetail", "Failed to update movie detail: " + moviedata.title +", " + e);
        }
        return moviedata.id;
    }

    @Override
    protected void onPostExecute(Integer currentUpdatedMovieId) {
        super.onPostExecute(currentUpdatedMovieId);
        mGetMovieDetailListener.onFinished(currentUpdatedMovieId);
    }
}
