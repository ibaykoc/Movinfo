package com.koc.movinfo.TmdbApi;

import android.os.AsyncTask;
import android.util.Log;
import android.util.SparseArray;

import com.koc.movinfo.Utils.Network;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.URL;

/**
 * Created by MochammadIqbal on 11/23/2017.
 * Project Movinfo
 */

public class GetGenresFromServer extends AsyncTask<String, Void, SparseArray<String>> {

    public interface GetGenresFromServerListener {
        public void OnFinished(SparseArray<String> genresResult);
    }

    GetGenresFromServerListener mGetGenresFromServerListener;

    public GetGenresFromServer(GetGenresFromServerListener getGenresFromServerListener) {
        mGetGenresFromServerListener = getGenresFromServerListener;
    }

    @Override
    protected SparseArray<String> doInBackground(String... urls) {
        SparseArray<String> result = new SparseArray<>();

        //Downloading image
        try {
            InputStream in = new URL(urls[0]).openStream();
            Log.d("GetGenresFromServer", "Downloading genres from: " + urls[0]);
            String genresRequestResponseJson = Network.ReadFromStream(in);
            in.close();

            //Get genres

            // Create a JSONObject from the JSON response string
            JSONObject baseJsonResponse = new JSONObject(genresRequestResponseJson);

            // Extract the JSONArray associated with the key called "genres",
            // which represents a list genre.
            JSONArray genreArray = baseJsonResponse.getJSONArray("genres");
            // For each genre in the genreArray, get the id and the name
            // and add it to result (SparseArray)
            for (int i = 0; i < genreArray.length(); i++) {
                // Get a single genre at position i within the list of videos
                JSONObject genre = genreArray.getJSONObject(i);

                //Extract the value from the key called "id"
                Integer id = genre.getInt("id");

                //Extract the value from the key called "name"
                String name = genre.getString("name");

                result.put(id, name);
            }
        } catch (Exception e) {
            Log.e("GetGenresFromServer", "Failed get genres. " + e);
        }

        return result;
    }

    @Override
    protected void onPostExecute(SparseArray<String> genresResult) {
        super.onPostExecute(genresResult);
        mGetGenresFromServerListener.OnFinished(genresResult);
    }
}
