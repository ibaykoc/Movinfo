package com.koc.movinfo.Utils;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by mochi on 12/11/2017.
 */

public class Network {

    private static final String LOG_TAG = "Network";

    public static String ReadFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        if (inputStream != null) {
            inputStream.close();
        }
        return output.toString();
    }

//    public static String MakeHttpRequest(URL url){
//        String jsonResponse = "";
//
//        // If the URL is null, then return early.
//        if (url == null) {
//            return jsonResponse;
//        }
//
//        Log.d(LOG_TAG,"Trying to request: " + url.toString());
//        HttpURLConnection urlConnection = null;
//        InputStream inputStream = null;
//        try {
//            urlConnection = (HttpURLConnection) url.openConnection();
//            urlConnection.setReadTimeout(10000 /* milliseconds */);
//            urlConnection.setConnectTimeout(15000 /* milliseconds */);
//            urlConnection.setRequestMethod("GET");
//            urlConnection.connect();
//
//            // If the request was successful (response code 200),
//            // then read the input stream and parse the response.
//            if (urlConnection.getResponseCode() == 200) {
//                inputStream = urlConnection.getInputStream();
//                jsonResponse = readFromStream(inputStream);
//            } else {
//                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
//            }
//        } catch (IOException e) {
//            Log.e(LOG_TAG, "Problem retrieving the JSON results.", e);
//        } finally {
//            if (urlConnection != null) {
//                urlConnection.disconnect();
//            }
//            if (inputStream != null) {
//                // Closing the input stream could throw an IOException, which is why
//                // the makeHttpRequest(URL url) method signature specifies than an IOException
//                // could be thrown.
//                try {
//                    inputStream.close();
//                } catch (IOException e) {
//                    Log.e(LOG_TAG, "Problem closing inputStream", e);
//                }
//            }
//        }
//        return jsonResponse;
//    }

    public static InputStream MakeHttpRequest(URL url){
        InputStream responseStream = null;

        // If the URL is null, then return early.
        if (url == null) {
            return responseStream;
        }

        Log.d(LOG_TAG,"Trying to request: " + url.toString());
        HttpURLConnection urlConnection = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // If the request was successful (response code 200),
            // then read the input stream and parse the response.
            if (urlConnection.getResponseCode() == 200) {
                responseStream = urlConnection.getInputStream();
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving InputStream response.", e);
        }
        return responseStream;
    }

    public static URL CreateUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Problem building the URL ", e);
        }
        return url;
    }

    public static String[] JsonArrayToStringArray(JSONArray jsonArray){
        List<String> list = new ArrayList<String>();
        for(int i = 0; i < jsonArray.length(); i++){
            try {
                list.add(jsonArray.get(i).toString());
            } catch (JSONException e) {
                Log.e(LOG_TAG, "Problem JsonArrayToStringArray ", e);
            }
        }
        return list.toArray(new String[list.size()]);
    }

}
