package com.example.application;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.application.sports.Sport;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * This class is responsible for:
 * - loading information from JSON and converting it into an actual list that can be used
 * - saving and loading said list
 * At the moment, these methods are static, but a better solution would probably be for other classes to extend this,
 * or perhaps make this an interface/-s instead which can be then be implemented by classes interested in these functions
 */
public class SportsLoader {
    public SportsLoader(){
    }

    /**
     * JSON content is read from local file
     */
    private static String loadJSONFromAsset(InputStream is)  {
        String json;
        try {
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

    /**
     * JSON content is translated from loadJSONFromAsset
     */
    public static ArrayList<Sport> extractSportsFromJson(InputStream is) throws JSONException {

        JSONArray arr = new JSONArray(loadJSONFromAsset(is));
        ArrayList<Sport> sports = new ArrayList<>();

        for (int i = 0; i < arr.length(); i++) {
            try {
                JSONObject sportObject = arr.getJSONObject(i);

                Sport sport = new Sport();
                sport.setName(sportObject.getString("name").toString());
                sport.setDescription(sportObject.getString("description".toString()));
                sport.setLogo(sportObject.getString("logo"));
                sport.setEmail(sportObject.getString("email"));
                sport.setLink(sportObject.getString("link"));


                JSONArray tagList = sportObject.getJSONArray("tags");
                for(int j = 0; j < tagList.length(); j++)
                    sport.addTag(Tag.valueOf(tagList.getString(j)));
                sports.add(sport);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return sports;
    }

    /**
     * saves a list of sports in SharedPreferences
     * @param sports the list to be saved  (optimizations: could be generic type)
     * @param key the name of the file where information is saved
     * @param key2 the key to the saved information
     * @param ctx the activity of the class that calls this method (required to get SharedPreferences)
     */
    public static void saveList(List<Sport> sports, String key, String key2, Context ctx){
        SharedPreferences.Editor editor = ctx.getSharedPreferences(key, Context.MODE_PRIVATE).edit();
        Gson gson = new Gson();
        String json = gson.toJson(sports);
        editor.putString(key2, json);
        editor.apply();
    }

    /**
     * extracts saved list from SharedPreferences
     * @param key the name of the file where information is saved
     * @param key2 the key to the saved information
     * @param ctx the activity of the class that calls this method (required to get SharedPreferences)
     * @return the saved list (optimizations: could be generic type)
     */
    public static List<Sport>extractSavedSports(String key, String key2, Context ctx){
        ArrayList<Sport>sports;
        SharedPreferences sp = ctx.getSharedPreferences(key, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sp.getString(key2, null);
        Type type = new TypeToken<ArrayList<Sport>>(){}.getType();
        sports = gson.fromJson(json,type);
        if (json==null){
            sports = new ArrayList<>();
        }
        return sports;
    }

}
