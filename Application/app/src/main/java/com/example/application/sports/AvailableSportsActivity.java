package com.example.application.sports;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.FrameLayout;

import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.application.R;
import com.example.application.SportsLoader;
import com.example.application.Tag;
import com.example.application.animations.Animations;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.List;

/**
 * This class more or less extracts the information that's stored in sports.json and puts it into a
 * Arraylist so that it can be used later. It also serves as the base for a new activity
 */
public class AvailableSportsActivity extends AppCompatActivity {
    /**
     * the framework of the list displaying the sports
     */
    private RecyclerView recyclerView;
    private FrameLayout backgroundFilter;

    private List<Tag> savedTags;
    private Fragment filterFragment;
    private static boolean filterExpanded;
    private CheckBox favoriteCheckBox;
    protected List<Sport> sports;
    private List<Sport>favouriteSports;

    /**
     * the URL for our JSON-file
     * For every update to the JSON-file, a new URL has to be generated so there is probably a better solution
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_available_sports);

        TextView textView = findViewById(R.id.toolbarText);
        textView.setText("Sports and Committees");


        recyclerView = findViewById(R.id.sportsList);
        filterFragment = new filterSports();
        filterExpanded = false;

        favoriteCheckBox = findViewById(R.id.favoriteCheckBox);


        try {
            extractSports();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction().replace(R.id.frameLayout, filterFragment).commit();
        RelativeLayout filterButton = findViewById(R.id.filterButton);

        filterButton.setOnClickListener(v -> {
            if(filterExpanded){
                Animations.toggleArrow(findViewById(R.id.downFilterImg), false);
                Animations.collapse(findViewById(R.id.frameLayout));
                filterExpanded = false;
            } else {
                Animations.toggleArrow(findViewById(R.id.downFilterImg), true);
                Animations.expand(findViewById(R.id.frameLayout));
                filterExpanded = true;
            }
        });
        favoriteCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                try {
                    extractSports();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if(isChecked){
                    refreshView(favouriteSports);
                }
                else{
                    refreshView(sports);
                }
            }
        });
        refreshView(sports);
    }


    /**
     * JSON content is read from local file
     */
    private String loadJSONFromAsset()  {
        String json;
        try {
            InputStream is = getAssets().open("sports.json");
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
    private void extractSports() throws JSONException {
        sports = SportsLoader.extractSavedSports("SavedSportsFile", "SavedSportsKey", this);
        favouriteSports = SportsLoader.extractSavedSports("SavedFavouritesFile", "SavedFavouritesKey", this);
    }
    private void refreshView(List<Sport>theSports){
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        SportsAdapter sportsAdapter = new SportsAdapter(getApplicationContext(), theSports, favoriteCheckBox.isChecked());
        recyclerView.setItemViewCacheSize(theSports.size());
        recyclerView.setAdapter(sportsAdapter);
    }
    public boolean getStateOfFavoriteCheckBox(){
        return favoriteCheckBox.isChecked();
    }

    public void goBack(View view){
        this.onBackPressed();
    }

    /**
     * This method returns a new copy of the full sports list to the fragment
     * This was done to remove an alibi problem
     * @return a new list of the sport
     */
    protected List<Sport> getSportsList(){
        return new ArrayList<>(sports);
    }

    protected void setSavedTags(List<Tag> t){
        savedTags = t;

    }

    protected List<Tag> getSavedTags(){
        return savedTags;
    }

    protected static void toggleFilterExpanded() {
        filterExpanded = !filterExpanded;
    }
}