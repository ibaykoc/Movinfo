package com.koc.movinfo.TmdbApi;

import android.os.AsyncTask;
import android.util.Log;

import com.koc.movinfo.Data.CastData;
import com.koc.movinfo.Data.MovieData;
import com.koc.movinfo.Utils.Network;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by MochammadIqbal on 11/26/2017.
 * Project Movinfo
 */

public class GetMovieCast extends AsyncTask<MovieData, Void, CastData[]> {

    public interface GetMovieCastListener {
        void onFinished(CastData[] castDataReceived);
    }

    private GetMovieCastListener mGetMovieCastListener;

    public GetMovieCast(GetMovieCastListener getMovieCastListener) {
        mGetMovieCastListener = getMovieCastListener;
    }

    @Override
    protected CastData[] doInBackground(MovieData... moviedataToUpdates) {

        MovieData moviedata = moviedataToUpdates[0];

        ArrayList<CastData> result = new ArrayList<>();

        try {
            //Get current movie detail json response from server
            InputStream in = new URL(TmdbApi.GetMovieCreditUrl(moviedata.id)).openStream();
            Log.d("GetMovieCast", "Get movie cast from: " +
                    TmdbApi.GetMovieCreditUrl(moviedata.id));
            String jsonResponse = Network.ReadFromStream(in);

            //Close the InputStream after get the json response
            in.close();

            // Create a JSONObject from the JSON response string
            JSONObject baseJsonResponse = new JSONObject(jsonResponse);

            // Extract the JSONArray associated with the key called "cast",
            // which represents a list of cast of the movie.
            JSONArray movieCastArray = baseJsonResponse.getJSONArray("cast");

            // For each cast in the movieCastArray, get the cast
            for (int i = 0; i < movieCastArray.length(); i++) {

                CastData cd = new CastData();

                // Get a single cast at position i within the list of cast
                JSONObject cast = movieCastArray.getJSONObject(i);

                //Extract the value from the key called "name"
                cd.name = cast.getString("name");

                //Extract the value from the key called "character"
                cd.character_name = cast.getString("character");

                //Extract the value from the key called "profile_path"
                cd.profile_path = cast.getString("profile_path");

                result.add(cd);
            }

        } catch (Exception e) {
            Log.e("GetMovieCast", "Failed to update movie detail: \"" + moviedata.title +"\", " + e);
        }
        return result.toArray(new CastData[result.size()]);
    }

    @Override
    protected void onPostExecute(CastData[] castDataReceived) {
        super.onPostExecute(castDataReceived);
        mGetMovieCastListener.onFinished(castDataReceived);
    }
}
