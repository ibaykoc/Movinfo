//package com.koc.movinfo.TmdbApi;
//
//import android.graphics.BitmapFactory;
//import android.os.AsyncTask;
//import android.util.Log;
//
//import com.koc.movinfo.Database.CastData;
//
//import java.io.InputStream;
//import java.net.URL;
//
///**
// * Created by MochammadIqbal on 14/11/2017.
// */
//
//public class UpdatePersonDataImage extends AsyncTask<Integer, Void, Integer> {
//
//    public interface OnUpdateActorDataImageListener {
//        void onFinished(Integer updatedActorDataId);
//    }
//
//    private OnUpdateActorDataImageListener mOnUpdateActorDataImageListener;
//
//    public UpdatePersonDataImage(OnUpdateActorDataImageListener onUpdateActorDataImageListener) {
//        mOnUpdateActorDataImageListener = onUpdateActorDataImageListener;
//    }
//
//    @Override
//    protected Integer doInBackground(Integer... ids) {
//        CastData currentActor = TmdbApi.GetPerson(ids[0]);
//
//        //Updating currentActor picture
//        if (currentActor.havePicture()) {
//            try {
//                InputStream in = new URL(TmdbApi.GetImageUrlAddress("w185",
//                        currentActor.profile_path)).openStream();
//                Log.d("UpdatePersonDataImage", "Get image from: " +
//                        TmdbApi.GetImageUrlAddress("w185",
//                                currentActor.profile_path));
//                currentActor.picture = BitmapFactory.decodeStream(in);
//                in.close();
//            } catch (Exception e) {
//                Log.e("UpdatePersonDataImage", "Failed get backdrop_image image: " + e);
//            } finally {
//                if (currentActor.picture == null)
//                    Log.e("UpdatePersonDataImage", "get null image");
//            }
//        }
//
//        return ids[0];
//    }
//
//    @Override
//    protected void onPostExecute(Integer updatedActorDataId) {
//        super.onPostExecute(updatedActorDataId);
//        mOnUpdateActorDataImageListener.onFinished(updatedActorDataId);
//    }
//}