package com.koc.movinfo.RecyclerAdapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.koc.movinfo.Bridge;
import com.koc.movinfo.Data.SearchMovieResponseData;
import com.koc.movinfo.MovieMoreActivity;
import com.koc.movinfo.R;
import com.koc.movinfo.Data.MovieData;
import com.koc.movinfo.TmdbApi.DownloadImage;
import com.koc.movinfo.TmdbApi.TmdbApi;

import java.util.ArrayList;
import java.util.Objects;

/**
 * Created by MochammadIqbal on 13/11/2017.
 * Project Movinfo
 */

public class EnlargableCardAdapter extends
        RecyclerView.Adapter<EnlargableCardAdapter.PopularCategoryCardHolder> {

    private Context context;
    private boolean closeOnMovieMore = false;
    private ArrayList<Boolean> enlarge = new ArrayList<>();
    private ArrayList<MovieData> movieToShow = new ArrayList<>();

    //For animating
    private ConstraintSet defaultSet = new ConstraintSet();
    private ConstraintSet enlargeSet = new ConstraintSet();
    private boolean animationSet = false;

    class PopularCategoryCardHolder extends RecyclerView.ViewHolder {

        private ConstraintLayout constraintRoot;
        private ImageView popular_category_card_backdrop;
        private ImageView popular_category_card_poster;
        private TextView popular_category_card_title_shrink;
        private TextView popular_category_card_title_enlarge;
        private TextView popular_category_card_overview_enlarge;
        private TextView popular_category_card_releaseDate_enlarge;
        private TextView popular_category_card_runtime_enlarge;
        private TextView popular_category_card_genre_enlarge;
        private TextView popular_category_card_rating_enlarge;
        private Button popular_category_card_button_more_enlarge;

        PopularCategoryCardHolder(View itemView) {
            super(itemView);

            popular_category_card_backdrop = itemView.findViewById(
                    R.id.popular_category_card_backdrop);
            popular_category_card_poster = itemView.findViewById(
                    R.id.popular_category_card_poster);
            popular_category_card_title_shrink = itemView.findViewById(
                    R.id.popular_category_card_title_shrink);
            popular_category_card_title_enlarge = itemView.findViewById(
                    R.id.popular_category_card_title_enlarge);
            popular_category_card_overview_enlarge = itemView.findViewById(
                    R.id.popular_category_card_overview_enlarge);
            popular_category_card_releaseDate_enlarge = itemView.findViewById(
                    R.id.popular_category_card_releaseDate_enlarge);
            popular_category_card_runtime_enlarge = itemView.findViewById(
                    R.id.popular_category_card_runtime_enlarge);
            popular_category_card_genre_enlarge = itemView.findViewById(
                    R.id.popular_category_card_genre_enlarge);
            popular_category_card_rating_enlarge = itemView.findViewById(
                    R.id.popular_category_card_rating_enlarge);
            popular_category_card_button_more_enlarge = itemView.findViewById(
                    R.id.popular_category_card_button_more_enlarge);

            if(closeOnMovieMore) popular_category_card_poster.setTransitionName("");

            //Set more button onClickListener to go to movieMoreActivity
            popular_category_card_button_more_enlarge.setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            GoToMovieMore(movieToShow.get(getLayoutPosition()), popular_category_card_poster);
                        }
                    });

            //Initialize all the constraintSet that responsible for animating
            constraintRoot = itemView.findViewById(R.id.popular_category_constraintLayout);
            if (!animationSet) {
                animationSet = true;
                defaultSet.clone(constraintRoot);
                enlargeSet.clone(constraintRoot);
                SetEnlargeSet();
            }

            //Set poster_image onClickListener to enlarge or shrink
            popular_category_card_poster.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("PopularCategory", "clicked index: " + getLayoutPosition());
                    if (!enlarge.get(getLayoutPosition())) {
                        Enlarge(constraintRoot);
                        enlarge.set(getLayoutPosition(), true);
                    } else {
                        Shrink(constraintRoot);
                        enlarge.set(getLayoutPosition(), false);
                    }
                }
            });

        }
    }

    public EnlargableCardAdapter(Context context, boolean closeOnMovieMore) {
        this.context = context;
        this.closeOnMovieMore = closeOnMovieMore;
    }

    @Override
    public PopularCategoryCardHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //Create a new layout view which in this case is popular_category_card
        //to give it to recyclerViewAdapter
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.popular_category_card, parent, false);

        return new PopularCategoryCardHolder(view);
    }

    @Override
    public void onBindViewHolder(final PopularCategoryCardHolder holder, int position) {
        Log.d("EnlargableCardAdapter", "onBindViewHolder: " + String.valueOf(position));
        MovieData currentMovieDataToShow = movieToShow.get(position);

        //Set animation
        if (!enlarge.get(position)) {
            Shrink(holder.constraintRoot);
        } else {
            Enlarge(holder.constraintRoot);
        }

        if (currentMovieDataToShow != null) {

            if (!currentMovieDataToShow.imageUpdated) {
                UpdateDataImage(currentMovieDataToShow);
            }

            //Set shrink title
            holder.popular_category_card_title_shrink.setText(currentMovieDataToShow.title);

            //Set enlarge title
            holder.popular_category_card_title_enlarge.setText(currentMovieDataToShow.title);

            //Set enlarge release date
            if (currentMovieDataToShow.release_date != null) {
                holder.popular_category_card_releaseDate_enlarge.setText(
                        TmdbApi.dateFormatMovinfo.format(currentMovieDataToShow.release_date));
            }else{
                holder.popular_category_card_releaseDate_enlarge.setText("");
            }

            //Set enlarge overview
            if (currentMovieDataToShow.overview != null) {
                holder.popular_category_card_overview_enlarge.setText(currentMovieDataToShow.overview);
            }else{
                holder.popular_category_card_overview_enlarge.setText("");
            }

            //Set enlarge runtime
            if (currentMovieDataToShow.runtime != null && currentMovieDataToShow.runtime != 0) {
                holder.popular_category_card_runtime_enlarge.setText(
                        context.getString(R.string.movie_runtime, currentMovieDataToShow.runtime));
            }else{
                holder.popular_category_card_runtime_enlarge.setText("");
            }

            //Set enlarge rating
            if (currentMovieDataToShow.vote_average != null && currentMovieDataToShow.vote_average != 0f) {
                holder.popular_category_card_rating_enlarge.setText(
                        context.getString(R.string.rating, currentMovieDataToShow.vote_average));
            }else{
                holder.popular_category_card_rating_enlarge.setText("");
            }

            //Set enlarge genre
            //Format genre_ids array to comma separated formatting
            ArrayList<String> movieGenre = new ArrayList<>();
            Integer[] currentMovieGenreIds = currentMovieDataToShow.genre_ids;
            if (currentMovieGenreIds != null && currentMovieGenreIds.length != 0) {
                for (int i = 0; i < currentMovieGenreIds.length; i++) {
                    String genre = TmdbApi.genres.get(currentMovieGenreIds[i]);
                    if (genre == null || genre.equals("null"))
                        continue;
                    movieGenre.add(genre);
                }
                StringBuilder builder = new StringBuilder();
                for (String s : movieGenre) {
                    builder.append(s).append(", ");
                }
                builder.delete(builder.length() - 2, builder.length() - 1);
                if (!builder.toString().equals("null"))
                    holder.popular_category_card_genre_enlarge.setText(builder.toString());
            }

            //Set backdrop_image
            Bitmap currentBackdrop = currentMovieDataToShow.backdrop_image;
            if (currentBackdrop != null) {
                holder.popular_category_card_backdrop.setImageBitmap(currentBackdrop);
            } else {
                holder.popular_category_card_backdrop.setImageResource(android.R.drawable.bottom_bar);
            }
            Bitmap currentPoster = currentMovieDataToShow.poster_image;

            //Set poster_image
            if (currentPoster != null) {
                holder.popular_category_card_poster.setImageBitmap(currentPoster);
            } else {
                holder.popular_category_card_poster.setImageResource(android.R.drawable.bottom_bar);
            }

            holder.popular_category_card_title_shrink.setText(currentMovieDataToShow.title);
            holder.popular_category_card_button_more_enlarge.setEnabled(
                    currentMovieDataToShow.imageUpdated);
            return;
        }

        //Set Default
        holder.popular_category_card_backdrop.setImageResource(android.R.drawable.bottom_bar);
        holder.popular_category_card_poster.setImageResource(android.R.drawable.bottom_bar);
        holder.popular_category_card_title_shrink.setText("");
        holder.popular_category_card_title_enlarge.setText("");
        holder.popular_category_card_overview_enlarge.setText("");
        holder.popular_category_card_releaseDate_enlarge.setText("");
        holder.popular_category_card_runtime_enlarge.setText("");
        holder.popular_category_card_button_more_enlarge.setEnabled(false);
    }

    @Override
    public int getItemCount() {
        return movieToShow.size();
    }

    public void setData(SearchMovieResponseData data) {
        if (data == null) return;

        for (int i = 0; i < data.results.length; i++) {
            movieToShow.add(new MovieData(data.results[i]));
            enlarge.add(false);
        }

        notifyDataSetChanged();

    }

    private void UpdateDataImage(final MovieData moviedataToUpdate) {

        if (moviedataToUpdate.imageUpdated || moviedataToUpdate.updatingImage) {
            return;
        }

        moviedataToUpdate.updatingImage = true;
        //Update backdrop_image image
        if (moviedataToUpdate.backdrop_path != null && !moviedataToUpdate.backdrop_path.equals("null")) {
            DownloadImage downloadBackdropImage = new DownloadImage(
                    new DownloadImage.DownloadImageListener() {
                        @Override
                        public void onFinished(Bitmap imageRetrieved) {
                            moviedataToUpdate.backdrop_image = imageRetrieved;
                            notifyDataSetChanged();
                        }
                    });
            downloadBackdropImage.execute(TmdbApi.GetImageUrlAddress("w780",
                    moviedataToUpdate.backdrop_path));
        } else {
            moviedataToUpdate.backdrop_image = null;
        }
        //Update poster_image image
        if (moviedataToUpdate.poster_path != null && !moviedataToUpdate.poster_path.equals("null")) {
            DownloadImage downloadPosterImage = new DownloadImage(
                    new DownloadImage.DownloadImageListener() {
                        @Override
                        public void onFinished(Bitmap imageRetrieved) {
                            moviedataToUpdate.poster_image = imageRetrieved;
                            moviedataToUpdate.imageUpdated = true;
                            moviedataToUpdate.updatingImage = false;
                            notifyDataSetChanged();
                        }
                    });
            downloadPosterImage.execute(TmdbApi.GetImageUrlAddress("w185",
                    moviedataToUpdate.poster_path));
        } else {
            moviedataToUpdate.poster_image = null;
            moviedataToUpdate.imageUpdated = true;
            moviedataToUpdate.updatingImage = false;
        }
    }


    //For animation
    private void SetEnlargeSet() {
        enlargeSet.setGuidelinePercent(
                R.id.popular_category_card_poster_guideline_start, 0f);
        enlargeSet.setGuidelinePercent(
                R.id.popular_category_card_poster_guideline_end, 0.1f);
        enlargeSet.setGuidelinePercent(
                R.id.popular_category_card_poster_guideline_top, 0f);
        enlargeSet.setGuidelinePercent(
                R.id.popular_category_card_poster_guideline_bottom, 0.4f);
        enlargeSet.setGuidelinePercent(
                R.id.popular_category_card_backdrop_guideline_bottom, 0f);
        enlargeSet.setGuidelinePercent(
                R.id.popular_category_card_enlarge_guideline_bottom_divider, 0.85f);
    }

    private void Enlarge(ConstraintLayout constraintRoot) {
        TransitionManager.beginDelayedTransition(constraintRoot);
        enlargeSet.applyTo(constraintRoot);
    }

    private void Shrink(ConstraintLayout constraintRoot) {
        TransitionManager.beginDelayedTransition(constraintRoot);
        defaultSet.applyTo(constraintRoot);
    }

    //For transition to movie movie more
    private void GoToMovieMore(MovieData movieToShowMore, View posterIdToTransition) {
        Intent goToMovieMore = new Intent(context, MovieMoreActivity.class);
        goToMovieMore.putExtra("MOVIE_ID_TO_SHOW", movieToShowMore.id);

        if (movieToShowMore.backdrop_image != null) {
            Bridge.item.add(movieToShowMore.backdrop_image);
        } else {
            Bridge.item.add(-1);
        }

        if (movieToShowMore.poster_image != null) {
            Bridge.item.add(movieToShowMore.poster_image);
        } else {
            Bridge.item.add(-1);
        }

        if (!closeOnMovieMore) {
            // Pass data object in the bundle and populate details activity.
            ActivityOptionsCompat options = ActivityOptionsCompat.
                    makeSceneTransitionAnimation((Activity) context,
                            posterIdToTransition,
                            ViewCompat.getTransitionName(posterIdToTransition));
            context.startActivity(goToMovieMore, options.toBundle());
        } else {
            ActivityCompat.finishAfterTransition((Activity) context);
            context.startActivity(goToMovieMore);
        }
    }
}
