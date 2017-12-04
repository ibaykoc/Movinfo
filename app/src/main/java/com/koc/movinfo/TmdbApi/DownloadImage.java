package com.koc.movinfo.TmdbApi;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import java.io.InputStream;
import java.net.URL;

public class DownloadImage extends AsyncTask<String, Void, Bitmap> {

    public interface DownloadImageListener {
        void onFinished(Bitmap imageRetrieved);
    }

    private DownloadImageListener mDownloadImageListener;

    public DownloadImage(DownloadImageListener downloadImageListener) {
        mDownloadImageListener = downloadImageListener;
    }

    @Override
    protected Bitmap doInBackground(String... urls) {

        if(urls[0].equals("null")) return null;

        Bitmap result = null;

        //Downloading image
        try {
            InputStream in = new URL(urls[0]).openStream();
            Log.d("DownloadImage", "Downloading image from: " + urls[0]);
            result = BitmapFactory.decodeStream(in);
            in.close();
        } catch (Exception e) {
            Log.e("DownloadImage", "Failed downloading image: " + e);
        }

        return result;
    }

    @Override
    protected void onPostExecute(Bitmap imageRetrieved) {
        super.onPostExecute(imageRetrieved);
        mDownloadImageListener.onFinished(imageRetrieved);
    }
}
