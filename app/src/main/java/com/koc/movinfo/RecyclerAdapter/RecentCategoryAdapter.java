//package com.koc.movinfo.RecyclerAdapter;
//
//import android.content.Context;
//import android.graphics.Bitmap;
//import android.support.constraint.ConstraintLayout;
//import android.support.constraint.ConstraintSet;
//import android.support.v7.widget.RecyclerView;
//import android.transition.TransitionManager;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.Button;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import com.koc.movinfo.Database.SearchMovieResponseData;
//import com.koc.movinfo.R;
//import com.koc.movinfo.Database.MovieData;
//import com.koc.movinfo.TmdbApi.TmdbApi;
//import com.koc.movinfo.TmdbApi.DownloadImage;
//import com.koc.movinfo.TmdbApi.GetMovieDetail;
//
//
//public class RecentCategoryAdapter extends RecyclerView.Adapter<
//        RecentCategoryAdapter.RecentCategoryCardHolder> {
//
//    private Context context;
//    private final int totalToShow = 15;
//    private boolean[] animated = new boolean[totalToShow];
//    private MovieData[] movieToShow = new MovieData[totalToShow];
//
//    //For animating
//    private ConstraintSet defaultSet = new ConstraintSet();
//    private ConstraintSet enlargePosterSet = new ConstraintSet();
//    private boolean animationSet = false;
//
//    class RecentCategoryCardHolder extends RecyclerView.ViewHolder {
//
//        private ConstraintLayout constraintRoot;
//        private ImageView recent_category_card_poster;
//        private TextView recent_category_card_title;
//        private TextView recent_category_card_date;
//        private TextView recent_category_card_title_enlarge;
//        private TextView recent_category_card_runtime_enlarge;
//        private TextView recent_category_card_genre_enlarge;
//        private TextView recent_category_card_overview_enlarge;
//        private TextView recent_category_card_rating_enlarge;
//        private Button recent_category_card_button_more_enlarge;
//
//
//        public RecentCategoryCardHolder(View itemView) {
//            super(itemView);
//            recent_category_card_poster = itemView.findViewById(
//                    R.id.recent_category_card_poster);
//            recent_category_card_title = itemView.findViewById(
//                    R.id.recent_category_card_title);
//            recent_category_card_date = itemView.findViewById(
//                    R.id.recent_category_card_release_date);
//            recent_category_card_title_enlarge = itemView.findViewById(
//                    R.id.recent_category_card_title_enlarge);
//            recent_category_card_runtime_enlarge = itemView.findViewById(
//                    R.id.recent_category_card_runtime_enlarge);
//            recent_category_card_genre_enlarge = itemView.findViewById(
//                    R.id.recent_category_card_genre_enlarge);
//            recent_category_card_overview_enlarge = itemView.findViewById(
//                    R.id.recent_category_card_overview_enlarge);
//            recent_category_card_rating_enlarge = itemView.findViewById(
//                    R.id.recent_category_card_rating_enlarge);
//            recent_category_card_button_more_enlarge = itemView.findViewById(
//                    R.id.recent_category_card_button_more_enlarge);
//
//            //Initialize all the constraintSet that responsible for animating
//            constraintRoot = itemView.findViewById(R.id.recent_category_constraintLayout);
//            if (!animationSet) {
//                animationSet = true;
//                defaultSet.clone(constraintRoot);
//                enlargePosterSet.clone(constraintRoot);
//                SetEnlargeSet();
//            }
//
//            constraintRoot.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Log.d("PopularCategory", "clicked index: " + getLayoutPosition());
//                    if (!animated[getAdapterPosition()]) {
//                        EnlargePoster(constraintRoot);
//                        animated[getLayoutPosition()] = true;
//                    } else {
//                        ShrinkPoster(constraintRoot);
//                        animated[getLayoutPosition()] = false;
//                    }
//                }
//            });
//        }
//    }
//
//    public RecentCategoryAdapter(Context context) {
//        this.context = context;
//    }
//
//    @Override
//    public RecentCategoryCardHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        //Create a new layout view which in this case is recent_category_card
//        //to give it to recyclerViewAdapter
//        View view = LayoutInflater.from(parent.getContext()).inflate(
//                R.layout.recent_category_card, parent, false);
//
//        return new RecentCategoryCardHolder(view);
//    }
//
//    @Override
//    public void onBindViewHolder(RecentCategoryCardHolder holder, int position) {
//        Log.d("RecentCategoryAdapter", "onBindViewHolder: " + String.valueOf(position));
//        MovieData currentMovieDataToShow = movieToShow[position];
//
//        //adapter still not get the data
//        if (currentMovieDataToShow == null) {
//            //Obviously disable the more button
//            holder.recent_category_card_button_more_enlarge.setEnabled(false);
//        } else {
//            //Enable more button depend on current movie ready more
//            holder.recent_category_card_button_more_enlarge.setEnabled(
//                    currentMovieDataToShow.videoUpdated && currentMovieDataToShow.imageUpdated);
//        }
//
//        //Set animation
//        if (!animated[position]) {
//            ShrinkPoster(holder.constraintRoot);
//        } else {
//            EnlargePoster(holder.constraintRoot);
//        }
//
//        //If currentMovieDataToShow is null then do nothing
//        if (currentMovieDataToShow != null) {
//
//            //Set poster_image
//            Bitmap currentPoster = currentMovieDataToShow.poster_image;
//            if (currentPoster != null) {
//                holder.recent_category_card_poster.setImageBitmap(currentPoster);
//            } else {
//                holder.recent_category_card_poster.setImageResource(android.R.drawable.bottom_bar);
//            }
//
//            //Set title
//            holder.recent_category_card_title.setText(currentMovieDataToShow.title);
//            holder.recent_category_card_title_enlarge.setText(currentMovieDataToShow.title);
//
//            //Set release date
//            holder.recent_category_card_date.setText(TmdbApi.dateFormatMovinfo.format(
//                    currentMovieDataToShow.release_date));
//
//            //Set enlarge runtime
//            if (currentMovieDataToShow.runtime != null && currentMovieDataToShow.runtime != 0) {
//                holder.recent_category_card_runtime_enlarge.setText(
//                        context.getString(R.string.movie_runtime, currentMovieDataToShow.runtime));
//            }
//
//            //Set enlarge genre
//            //Format genre_ids array to comma separated formatting
//
//            Integer[] currentMovieGenreIds = currentMovieDataToShow.genre_ids;
//            if(currentMovieGenreIds != null && currentMovieGenreIds.length != 0) {
//                String[] currentMovieGenres = new String[currentMovieGenreIds.length];
//                for (int i = 0; i < currentMovieGenres.length; i++) {
//                    currentMovieGenres[i] = TmdbApi.genres.get(currentMovieGenreIds[i]);
//                }
//
//                StringBuilder builder = new StringBuilder();
//                for (String s : currentMovieGenres) {
//                    builder.append(s).append(", ");
//                }
//                builder.delete(builder.length() - 2, builder.length() - 1);
//                if (!builder.toString().equals("null"))
//                    holder.recent_category_card_genre_enlarge.setText(builder.toString());
//            }
//
//            //Set enlarge overview
//            holder.recent_category_card_overview_enlarge.setText(currentMovieDataToShow.overview);
//
//            //Set enlarge rating
//            if (currentMovieDataToShow.vote_average != null && currentMovieDataToShow.vote_average != 0f) {
//                holder.recent_category_card_rating_enlarge.setText(
//                        context.getString(R.string.rating, currentMovieDataToShow.vote_average));
//            }
//
//            return;
//        }
//
//        //Set Default if currentMovieDataToShow is null
//        holder.recent_category_card_poster.setImageResource(android.R.drawable.bottom_bar);
//        holder.recent_category_card_title.setText("");
//        holder.recent_category_card_title_enlarge.setText("");
//        holder.recent_category_card_date.setText("");
//        holder.recent_category_card_runtime_enlarge.setText("");
//        holder.recent_category_card_title.setText("");
//        holder.recent_category_card_genre_enlarge.setText("");
//        holder.recent_category_card_overview_enlarge.setText("");
//        holder.recent_category_card_rating_enlarge.setText("");
//    }
//
//    @Override
//    public int getItemCount() {return totalToShow;}
//
//    public void setData(SearchMovieResponseData data) {
//        if (data == null) return;
//        for (
//                int i = 0;
//                i < data.results.length && i < totalToShow; i++)
//
//        {
//            //If database already have the movie data then get it from database
//            if (TmdbApi.databaseManager.IsAlreadyExist(TmdbApi.databaseManager.TABLE_NAME_MOVIES,
//                    TmdbApi.databaseManager.COLUMN_NAME_ID, String.valueOf(data.results[i].id))) {
//                movieToShow[i] = TmdbApi.databaseManager.GetMovie(data.results[i].id);
//            }
//            //Else create new movie data with data from SearchMovieResponseData
//            else {
//                movieToShow[i] = new MovieData(data.results[i]);
//            }
//            GetMovieDetail(i);
//            UpdateDataImage(i);
////            UpdateDataVideo(movieToShow[i]);
//        }
//
//        notifyDataSetChanged();
//    }
//
//    private void GetMovieDetail(final Integer movieToShowIndex) {
//
//        //If movie data detail is already updated then return
//        if (movieToShow[movieToShowIndex].detailUpdated)
//            return;
//        GetMovieDetail updateMovieDetail = new GetMovieDetail(
//                new GetMovieDetail.GetMovieDetailListener() {
//                    @Override
//                    public void onFinished(Integer currentUpdatedMovieId) {
//
//                        movieToShow[movieToShowIndex] = TmdbApi.databaseManager.GetMovie(
//                                currentUpdatedMovieId);
//
//                        notifyDataSetChanged();
//                    }
//                });
//        updateMovieDetail.execute(movieToShow[movieToShowIndex].id);
//    }
//
//    private void UpdateDataImage(final Integer movieToShowIndex) {
//        //loading the backdrop_image image and poster_image image from the server and we need to
//        //retrieve it with async task and once it's done we have
//        //to update ImageData updatingImage to false, and do notifyDataSetChanged();
//
//        //if movieDataImage is already updated or is updating then do nothing
//        final MovieData currentMovieDataToUpdate = movieToShow[movieToShowIndex];
//        if (currentMovieDataToUpdate.imageUpdated) {
//            return;
//        }
//
//        //Update backdrop_image image
//        if (!currentMovieDataToUpdate.backdropUpdated &&
//                !currentMovieDataToUpdate.updatingImage) {
//            currentMovieDataToUpdate.updatingImage = true;
//            DownloadImage downloadBackdropImage = new DownloadImage(
//                    new DownloadImage.DownloadImageListener() {
//                        @Override
//                        public void onFinished(Bitmap imageRetrieved) {
//                            MovieData currentMovie = TmdbApi.databaseManager.GetMovie(
//                                    currentMovieDataToUpdate.id);
//                            currentMovie.updatingImage = false;
//                            currentMovie.backdrop_image = imageRetrieved;
//                            currentMovie.backdropUpdated = true;
//                            movieToShow[movieToShowIndex] = currentMovie;
//                            TmdbApi.databaseManager.UpdateMovie(currentMovie);
//                            //Update poster_image image
//                            if (!currentMovieDataToUpdate.posterUpdated && !currentMovieDataToUpdate.updatingPosterImage) {
//                                currentMovieDataToUpdate.updatingPosterImage = true;
//                                DownloadImage downloadPosterImage = new DownloadImage(
//                                        new DownloadImage.DownloadImageListener() {
//                                            @Override
//                                            public void onFinished(Bitmap imageRetrieved) {
//                                                MovieData currentMovie = TmdbApi.databaseManager.GetMovie(
//                                                        currentMovieDataToUpdate.id);
//                                                currentMovie.updatingPosterImage = false;
//                                                currentMovie.poster_image = imageRetrieved;
//                                                currentMovie.posterUpdated = true;
//                                                currentMovie.imageUpdated = true;
//                                                movieToShow[movieToShowIndex] = currentMovie;
//                                                TmdbApi.databaseManager.UpdateMovie(currentMovie);
//                                                notifyDataSetChanged();
//                                            }
//                                        });
//                                downloadPosterImage.execute(TmdbApi.GetImageUrlAddress("w342",
//                                        currentMovieDataToUpdate.poster_path));
//                            }
//                            notifyDataSetChanged();
//                        }
//                    });
//            downloadBackdropImage.execute(TmdbApi.GetImageUrlAddress("w780",
//                    currentMovieDataToUpdate.backdrop_path));
//        }
//    }
//
//    private void SetEnlargeSet() {
//        enlargePosterSet.setGuidelinePercent(
//                R.id.recent_category_card_top_part_guideline_bottom, -.005f);
//        enlargePosterSet.setGuidelinePercent(
//                R.id.recent_category_card_bottom_part_divider, 0.8f);
//    }
//
//
//    private void EnlargePoster(ConstraintLayout constraintRoot) {
//        TransitionManager.beginDelayedTransition(constraintRoot);
//        enlargePosterSet.applyTo(constraintRoot);
//    }
//
//    private void ShrinkPoster(ConstraintLayout constraintRoot) {
//        TransitionManager.beginDelayedTransition(constraintRoot);
//        defaultSet.applyTo(constraintRoot);
//    }
//}
