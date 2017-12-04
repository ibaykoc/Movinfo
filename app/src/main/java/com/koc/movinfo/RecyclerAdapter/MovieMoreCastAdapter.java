package com.koc.movinfo.RecyclerAdapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.koc.movinfo.Data.CastData;
import com.koc.movinfo.R;
import com.koc.movinfo.TmdbApi.DownloadImage;
import com.koc.movinfo.TmdbApi.TmdbApi;

/**
 * Created by MochammadIqbal on 19/11/2017.
 * Project Movinfo
 */

public class MovieMoreCastAdapter extends RecyclerView.Adapter<MovieMoreCastAdapter.CastCardHolder> {
    private Context context;
    private CastData[] castToShow = null;

    class CastCardHolder extends RecyclerView.ViewHolder {

        private ImageView cast_card_picture;
        private TextView cast_card_character;
        private TextView cast_card_name;


        CastCardHolder(View itemView) {
            super(itemView);
            cast_card_picture = itemView.findViewById(
                    R.id.cast_card_picture);
            cast_card_character = itemView.findViewById(
                    R.id.cast_card_character);
            cast_card_name = itemView.findViewById(
                    R.id.cast_card_name);
        }
    }

    public MovieMoreCastAdapter(Context context) {
        this.context = context;
    }

    @Override
    public CastCardHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.cast_card, parent, false);
        CastCardHolder holder = new CastCardHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(CastCardHolder holder, int position) {
        Log.d("MovieMoreCastAdapter", "onBindViewHolder.position: " + String.valueOf(position));

        if (castToShow == null) {
            //Set Default
            holder.cast_card_picture.setImageResource(android.R.drawable.bottom_bar);
            holder.cast_card_character.setText("");
            holder.cast_card_name.setText("");
            return;
        }

        CastData currentCast = castToShow[position];

        GetProfileImage(currentCast);

        //If data is not yet available then do nothing
        if (currentCast != null) {

            //Set Picture
            Bitmap currentPicture = currentCast.profile_picture;

            //If currentPerson picture is available
            if (currentPicture != null) {
                holder.cast_card_picture.setImageBitmap(currentPicture);
            } else {
                holder.cast_card_picture.setImageResource(android.R.drawable.bottom_bar);
            }

            //Assign person character to TextView
            holder.cast_card_character.setText(currentCast.character_name);

            //Assign person name to TextView
            holder.cast_card_name.setText(currentCast.name);
        }
    }

    @Override
    public int getItemCount() {
        if (castToShow == null) return 0;
        return castToShow.length;
    }

    public void setData(CastData[] data) {
        if (data == null) return;
        castToShow = data;
        notifyDataSetChanged();
    }

    private void GetProfileImage(final CastData castDataToUpdate) {
        if (castDataToUpdate.imageUpdated || castDataToUpdate.updatingImage)
            return;

        if(castDataToUpdate.profile_path.equals("null")){
            castDataToUpdate.imageUpdated = true;
            return;
        }

        castDataToUpdate.updatingImage = true;
        DownloadImage downloadProfilePicture = new DownloadImage(new DownloadImage.DownloadImageListener() {
            @Override
            public void onFinished(Bitmap imageRetrieved) {
                castDataToUpdate.profile_picture = imageRetrieved;
                castDataToUpdate.updatingImage = false;
                castDataToUpdate.imageUpdated = true;
                notifyDataSetChanged();
            }
        });
        downloadProfilePicture.execute(TmdbApi.GetImageUrlAddress(
                "w185",castDataToUpdate.profile_path));
    }
}
