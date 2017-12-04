package com.koc.movinfo.TmdbApi;

import android.os.AsyncTask;
import android.util.Log;

import com.koc.movinfo.Data.SearchMovieResponseData;
import com.koc.movinfo.Utils.Network;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class SearchMovie extends AsyncTask<String, Void, SearchMovieResponseData> {

    public interface SearchMovieListener {
        void onFinished(SearchMovieResponseData result);
    }

    private SearchMovieListener mSearchMovieListener;

    public SearchMovie(SearchMovieListener searchMovieListener) {
        mSearchMovieListener = searchMovieListener;
    }

    @Override
    protected SearchMovieResponseData doInBackground(String... urls) {

        URL url = Network.CreateUrl(urls[0]);

        InputStream response = Network.MakeHttpRequest(url);

        String responseAsString = null;

        //Convert InputStream response to string
        try {
            responseAsString = Network.ReadFromStream(response);
        } catch (IOException e) {
            Log.e("SearchMovie", "Error converting stream into String, " + e);
        }
        return TmdbApi.ExtractSearchMovieResponse(responseAsString);
    }

    @Override
    protected void onPostExecute(SearchMovieResponseData result) {
        super.onPostExecute(result);
        mSearchMovieListener.onFinished(result);
    }
}
