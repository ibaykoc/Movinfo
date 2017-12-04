package com.koc.movinfo.TmdbApi;

import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import com.koc.movinfo.Data.VideoData;
import com.koc.movinfo.Utils.Network;
import com.koc.movinfo.YoutubeAPI.YoutubeAPI;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by MochammadIqbal on 16/11/2017.
 * Project Movinfo
 */

public class GetMovieVideo extends AsyncTask<Integer, Void, VideoData[]> {

    public interface GetMovieVideoListener {
        void onFinished(VideoData[] video_datas_received);
    }

    private GetMovieVideoListener mGetMovieVideoListener;

    public GetMovieVideo(GetMovieVideoListener getMovieVideoListener) {
        mGetMovieVideoListener = getMovieVideoListener;
    }

    @Override
    protected VideoData[] doInBackground(Integer... movieIds) {
        ArrayList<VideoData> video_datas = new ArrayList<>();

        try {
            InputStream in = new URL(TmdbApi.GetVideoUrlAddress(movieIds[0])).openStream();
            Log.d("GetMovieVideo", "Get video from: " +
                    TmdbApi.GetVideoUrlAddress(movieIds[0]));
            String jsonResponse = Network.ReadFromStream(in);

            in.close();


            // Create a JSONObject from the JSON response string
            JSONObject baseJsonResponse = new JSONObject(jsonResponse);

            // Get videos
            // Extract the JSONArray associated with the key called "results",
            // which represents a list genre_ids.
            JSONArray resultsArray = baseJsonResponse.getJSONArray("results");
            // For each video in the resultsArray, get the string key
            // and add it to videos database


            for (int i = 0; i < resultsArray.length(); i++) {
                VideoData vd = new VideoData();
                // Get a single videoKey at position i within the list of videos
                JSONObject video = resultsArray.getJSONObject(i);


                //Extract the value from the key called "key"
                vd.key = video.getString("key");

                //get video thumbnail
                try {
                    InputStream inImage = new URL(YoutubeAPI.GetThumbnailUrlAddress(vd.key)).openStream();
                    Log.d("GetMovieVideo", "Get thumbnail from: " +
                            YoutubeAPI.GetThumbnailUrlAddress(vd.key));
                    vd.thumbnail = BitmapFactory.decodeStream(inImage);
                    inImage.close();
                } catch (Exception e) {
                    Log.e("GetMovieVideo", "Failed to get thumbnail image from: " +
                            YoutubeAPI.GetThumbnailUrlAddress(vd.key) + ", " + e);
                    continue;
                }

                //get video detail
                try {
                    InputStream inDetail = new URL(YoutubeAPI.GetVideoDetail(vd.key)).openStream();
                    Log.d("GetMovieVideo", "Get video detail from: " +
                            YoutubeAPI.GetVideoDetail(vd.key));
                    String videoDetailResponseJson = Network.ReadFromStream(inDetail);
                    inDetail.close();

                    // Create a JSONObject from the videoDetailResponseJson
                    JSONObject baseDetailJsonResponse = new JSONObject(videoDetailResponseJson);

                    //Extract the value from the key called "author_name"
                    vd.author_name = baseDetailJsonResponse.getString("author_name");

                    //Extract the value from the key called "title"
                    vd.title = baseDetailJsonResponse.getString("title");
                } catch (Exception e) {
                    Log.e("GetMovieVideo", "Failed to get title and author name from: " +
                            YoutubeAPI.GetVideoDetail(vd.key) + ", " + e);
                }

                video_datas.add(vd);
            }
        } catch (Exception e) {
            Log.e("GetMovieVideo", "Failed get video data: " + e);
        }

        return video_datas.toArray(new VideoData[video_datas.size()]);
    }

    @Override
    protected void onPostExecute(VideoData[] video_datas_received) {
        super.onPostExecute(video_datas_received);
        mGetMovieVideoListener.onFinished(video_datas_received);
    }
}
