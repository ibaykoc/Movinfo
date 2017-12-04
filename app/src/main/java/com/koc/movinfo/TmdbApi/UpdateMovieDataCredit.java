//package com.koc.movinfo.TmdbApi;
//
//import android.os.AsyncTask;
//import android.util.Log;
//
//import com.koc.movinfo.Database.MovieData;
//import com.koc.movinfo.Database.CastData;
//import com.koc.movinfo.Utils.Network;
//
//import org.json.JSONArray;
//import org.json.JSONObject;
//
//import java.io.InputStream;
//import java.net.URL;
//import java.util.ArrayList;
//
///**
// * Created by MochammadIqbal on 19/11/2017.
// * Project Movinfo
// */
//
//public class UpdateMovieDataCredit extends AsyncTask<Integer, Void, Integer> {
//
//    private OnUpdateMovieDataCreditListener mOnUpdateMovieDataCreditListener;
//
//    public interface OnUpdateMovieDataCreditListener {
//        void onFinished(Integer updatedMovieDataId);
//    }
//
//    public UpdateMovieDataCredit(OnUpdateMovieDataCreditListener onUpdateMovieDataCreditListener){
//        mOnUpdateMovieDataCreditListener = onUpdateMovieDataCreditListener;
//    }
//
//    @Override
//    protected Integer doInBackground(Integer... ids) {
//        Integer currentMovieId = ids[0];
//        MovieData currentMovieData = TmdbApi.GetMovie(currentMovieId);
//
//        //Movie data credit already updated
//        if(currentMovieData.creditUpdated)
//            return -1;
//
//        MovieData.CreditData creditDataResult = currentMovieData.getNewCreditData();
//
//        //Updating currentMovie credit
//        try {
//            //Get current movie credit json response from server
//            InputStream in = new URL(TmdbApi.GetMovieCreditUrl(currentMovieId)).openStream();
//            Log.d("UpdateMovieDataCredit", "Get credit from: " +
//                    TmdbApi.GetMovieCreditUrl(currentMovieId));
//            String jsonResponse = Network.ReadFromStream(in);
//
//            //Close the InputStream after get the json response
//            in.close();
//
//            //Format the response data and add it to movie credit data
//
//            // Create a JSONObject from the JSON response string
//            JSONObject baseJsonResponse = new JSONObject(jsonResponse);
//
//            // Get cast
//            // Extract the JSONArray associated with the key called "cast",
//            // which represents a list cast.
//            JSONArray castArrayJson = baseJsonResponse.getJSONArray("cast");
//            // For each cast in the castArrayJson, get cast data
//            // and add it to credit cast
//            ArrayList<MovieData.CreditData.CastData> castDataList = new ArrayList<>();
//            for (int i = 0; i < castArrayJson.length(); i++) {
//                MovieData.CreditData.CastData castData = creditDataResult.GetNewCastData();
//                // Get a single cast at position i within the list of videos
//                JSONObject cast = castArrayJson.getJSONObject(i);
//
//                //Extract the value from the key called "id"
//                castData.person_id = cast.getInt("id");
//
//                //Extract the value from the key called "character"
//                castData.character = cast.getString("character");
//
//                //Extract the value from the key called "profile_path"
//                String profile_path = cast.getString("profile_path");
//
//                //If there is no profile picture then continue
//                if (profile_path.equals("null")) continue;
//
//                castDataList.add(castData);
//                TmdbApi.AddPerson(castData.person_id, new CastData(cast.getString("name"), profile_path));
//            }
//            creditDataResult.castData = castDataList.toArray(new MovieData.CreditData.CastData[castDataList.size()]);
//            // Get crew
//            // Extract the JSONArray associated with the key called "crew",
//            // which represents a list crew.
//            JSONArray crewArrayJson = baseJsonResponse.getJSONArray("crew");
//            // For each crew in the crewArrayJson, get crew data
//            // and add it to credit crew
//            ArrayList<MovieData.CreditData.CrewData> crewDataList = new ArrayList<>();
//            for (int i = 0; i < crewArrayJson.length(); i++) {
//                MovieData.CreditData.CrewData crewData = creditDataResult.GetNewCrewData();
//                // Get a single crew at position i within the list of crew
//                JSONObject crew = crewArrayJson.getJSONObject(i);
//
//                //Extract the value from the key called "id"
//                crewData.person_id = crew.getInt("id");
//
//                //Extract the value from the key called "job"
//                crewData.job = crew.getString("job");
//
//                //Extract the value from the key called "profile_path"
//                String profile_path = crew.getString("profile_path");
//
//                //If there is no profile picture then continue
//                if (profile_path.equals("null")) continue;
//
//                crewDataList.add(crewData);
//                TmdbApi.AddPerson(crewData.person_id, new CastData(crew.getString("name"), profile_path));
//            }
//            creditDataResult.crewData = crewDataList.toArray(new MovieData.CreditData.CrewData[crewDataList.size()]);
//
//        } catch (Exception e) {
//            Log.e("GetMovieVideo", "Failed get video data: " + e);
//        }
//
//        currentMovieData.creditData = creditDataResult;
//
//        return currentMovieId;
//    }
//
//    @Override
//    protected void onPostExecute(Integer currentUpdatedMovieId) {
//        super.onPostExecute(currentUpdatedMovieId);
//        TmdbApi.GetMovie(currentUpdatedMovieId).creditUpdated = true;
//        mOnUpdateMovieDataCreditListener.onFinished(currentUpdatedMovieId);
//    }
//}
