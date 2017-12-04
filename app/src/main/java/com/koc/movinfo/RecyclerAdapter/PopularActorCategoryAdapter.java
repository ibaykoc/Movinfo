//package com.koc.movinfo.RecyclerAdapter;
//
//import android.graphics.Bitmap;
//import android.support.v7.widget.RecyclerView;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import com.koc.movinfo.R;
//import com.koc.movinfo.Database.CastData;
//import com.koc.movinfo.Database.PersonSearchResponseData;
//import com.koc.movinfo.TmdbApi.TmdbApi;
//import com.koc.movinfo.TmdbApi.UpdatePersonDataImage;
//
///**
// * Created by MochammadIqbal on 14/11/2017.
// */
//
//public class PopularActorCategoryAdapter extends
//        RecyclerView.Adapter<PopularActorCategoryAdapter.PopularActorCategoryHolder> {
//    private final int totalToShow = 15;
//    private Integer[] actorToShowIds = new Integer[totalToShow];
//
//    class PopularActorCategoryHolder extends RecyclerView.ViewHolder {
//
//        private ImageView popular_actor_picture;
//        private TextView popular_actor_name;
//
//
//        public PopularActorCategoryHolder(View itemView) {
//            super(itemView);
//            popular_actor_picture = itemView.findViewById(
//                    R.id.popular_actor_picture);
//            popular_actor_name = itemView.findViewById(
//                    R.id.popular_actor_name);
//        }
//    }
//
//    @Override
//    public PopularActorCategoryHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(parent.getContext()).inflate(
//                R.layout.popular_actor_category, parent, false);
//        PopularActorCategoryHolder holder = new PopularActorCategoryHolder(view);
//        return holder;
//    }
//
//    @Override
//    public void onBindViewHolder(PopularActorCategoryHolder holder, int position) {
//        Log.d("onBindViewHolder", String.valueOf(position));
//
//        Integer currentActorIdToShow = actorToShowIds[position];
//
//        //If data is not yet receive then do nothing
//        if (currentActorIdToShow != null) {
//
//            //Get CastData to show by getting it from actorDatabase and passing the movieToShowId
//            CastData currentActor = TmdbApi.GetPerson(currentActorIdToShow);
//
//            //If CastData requested not in the database then do nothing
//            if (currentActor != null) {
//
//                //Set Picture
//                Bitmap currentPicture = currentActor.picture;
//                if (currentPicture != null) {
//                    holder.popular_actor_picture.setImageBitmap(currentPicture);
//                } else {
//                    holder.popular_actor_picture.setImageResource(android.R.drawable.bottom_bar);
//                }
//
//                holder.popular_actor_name.setText(currentActor.name);
//                return;
//            }
//        }
//
//        //Set Default
//        holder.popular_actor_picture.setImageResource(android.R.drawable.bottom_bar);
//        holder.popular_actor_name.setText("");
//    }
//
//    @Override
//    public int getItemCount() {
//        return totalToShow;
//    }
//
//    public void setData(PersonSearchResponseData data) {
//        if (data == null) return;
//        for (int i = 0; i < data.getActorIds().length && i < totalToShow; i++) {
//            actorToShowIds[i] = data.getActorIds()[i];
//            UpdateDataImage(actorToShowIds[i]);
//            notifyDataSetChanged();
//        }
//    }
//
//    private void UpdateDataImage(Integer personDataIdToUpdate) {
//        CastData currentPerson = TmdbApi.GetPerson(personDataIdToUpdate);
//        //loading the person picture from the server and we need to
//        //retrieve it with async task and once it's done we have
//        //to update ImageData updatingBackdropImage to false, and do notifyDataSetChanged();
//        if (!currentPerson.updatingBackdropImage && !currentPerson.imageUpdated) {
//            currentPerson.updatingBackdropImage = true;
//            UpdatePersonDataImage updatePersonDataImage = new UpdatePersonDataImage(
//                    new UpdatePersonDataImage.OnUpdateActorDataImageListener() {
//                        @Override
//                        public void onFinished(Integer updatedActorDataId) {
//                            TmdbApi.GetPerson(updatedActorDataId).updatingBackdropImage = false;
//                            notifyDataSetChanged();
//                        }
//                    });
//            updatePersonDataImage.execute(personDataIdToUpdate);
//        }
//    }
//}
