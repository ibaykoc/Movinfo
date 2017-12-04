package com.koc.movinfo.RecyclerAdapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.koc.movinfo.Data.VideoData;
import com.koc.movinfo.R;
import com.koc.movinfo.YoutubeVideoPlayerActivity;

/**
 * Created by MochammadIqbal on 16/11/2017.
 * Project Movinfo
 */

public class MovieMoreTrailerAdapter extends
        RecyclerView.Adapter<MovieMoreTrailerAdapter.MovieMoreTrailerCardHolder> {

    private Context context;
    private VideoData[] videoDatasToShow = null;

    class MovieMoreTrailerCardHolder extends RecyclerView.ViewHolder {

        ImageView movie_more_video_thumbnail;
        TextView trailer_card_title;
        TextView trailer_card_author_name;


        MovieMoreTrailerCardHolder(View itemView) {
            super(itemView);
            movie_more_video_thumbnail = itemView.findViewById(
                    R.id.trailer_card_video_thumbnail);
            trailer_card_title = itemView.findViewById(
                    R.id.trailer_card_title);
            trailer_card_author_name = itemView.findViewById(
                    R.id.trailer_card_author_name);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    GoToMovieMore(videoDatasToShow[getLayoutPosition()]);
                }
            });

        }
    }

    public MovieMoreTrailerAdapter(Context context) {
        this.context = context;
    }

    @Override
    public MovieMoreTrailerCardHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //Create a new layout view which in this case is trailer_card
        //to give it to recyclerViewAdapter
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.trailer_card, parent, false);

        return new MovieMoreTrailerCardHolder(view);
    }

    @Override
    public void onBindViewHolder(MovieMoreTrailerCardHolder holder, int position) {
        if (videoDatasToShow == null)
            return;
        VideoData currentVideoData = videoDatasToShow[position];
        holder.movie_more_video_thumbnail.setImageBitmap(currentVideoData.thumbnail);
        holder.trailer_card_title.setText(currentVideoData.title);
        holder.trailer_card_author_name.setText(currentVideoData.author_name);
    }

    @Override
    public int getItemCount() {
        return (videoDatasToShow == null) ? 0 : videoDatasToShow.length;
    }

    public void setData(VideoData[] data) {
        if (data == null) return;
        videoDatasToShow = data;
        notifyDataSetChanged();
    }

    //For transition to movie movie more
    private void GoToMovieMore(VideoData videoToShowMore) {

        Intent goToYoutubeVideoPlayer = new Intent(context, YoutubeVideoPlayerActivity.class);
        goToYoutubeVideoPlayer.putExtra("YOUTUBE_KEY_TO_PLAY", videoToShowMore.key);

        context.startActivity(goToYoutubeVideoPlayer);
    }
}
