package com.koc.movinfo.YoutubeAPI;

/**
 * Created by MochammadIqbal on 16/11/2017.
 * Project Movinfo
 */

public class YoutubeAPI {
    private static final String API_KEY = "AIzaSyAZ9J_ps7rsRiwxPjVvDl6WSr3Tb4sueh8";

    public static String getApiKey() {
        return API_KEY;
    }

    public static String GetThumbnailUrlAddress(String key) {
        return "https://img.youtube.com/vi/" + key + "/0.jpg";
    }

    public static String GetVideoUrl(String youtube_video_key) {
        return "https://www.youtube.com/watch?v=" + youtube_video_key;
    }

    public static String GetVideoDetail(String youtube_video_key) {
        return "https://www.youtube.com/oembed?url=" + GetVideoUrl(youtube_video_key) + "&format=json";
    }
}
