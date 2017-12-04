package com.koc.movinfo.TmdbApi;

import android.os.AsyncTask;

import com.koc.movinfo.Utils.Network;

import java.io.InputStream;
import java.net.URL;

/**
 * Created by mochi on 12/11/2017.
 */

/**
 * Make Http request and return response as string
 */
public class Request extends AsyncTask<String, Void, InputStream> {

    public interface OnRequestListener {
        void onFinished(InputStream result);
    }

    OnRequestListener mOnRequestListener;

    public Request(OnRequestListener onRequestListener){
        mOnRequestListener = onRequestListener;
    }

    @Override
    protected InputStream doInBackground(String... urls) {
        URL url = Network.CreateUrl(urls[0]);
        return Network.MakeHttpRequest(url);
    }

    @Override
    protected void onPostExecute(InputStream s) {
        super.onPostExecute(s);
        mOnRequestListener.onFinished(s);
    }
}
