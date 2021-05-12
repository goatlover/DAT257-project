package com.example.application.recommended;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;

import com.example.application.R;

import org.json.JSONException;

import java.util.ArrayList;

//TODO add explaining comment what this class should do
public class RecommendedActivity extends AppCompatActivity {

    private boolean TAKEN_QUIZ;
    private Toolbar toolbar;
    private TextView textView;
    private RadioButton recommendedButton;
    private RadioButton favouritesButton;

    //TODO add comment explaining what this method does
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //TODO remove the line below if it is unnecessary
        //setContentView(R.layout.activity_recommended);



        SharedPreferences prefs = getSharedPreferences("Save", MODE_PRIVATE);
        TAKEN_QUIZ = prefs.getBoolean("takenQuiz", false);

        // TODO explain what happens here?
        if(TAKEN_QUIZ){
            setContentView(R.layout.activity_recommended_empty);
            FragmentManager fm = getSupportFragmentManager();
            fm.beginTransaction().replace(R.id.ActivityRecommendedLayout, new QuizRecommended()).commit();
        } else {
            setContentView(R.layout.activity_recommended);
        }
        recommendedButton = findViewById(R.id.recommendedButton);
        favouritesButton = findViewById(R.id.favouritesButton);
        recommendedButton.setChecked(true);
        toolbar = findViewById(R.id.customToolbar);
        textView = (TextView) findViewById(R.id.toolbarText);
        textView.setText("Your Sports");
        recommendedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getSupportFragmentManager();
                fm.beginTransaction().replace(R.id.ActivityRecommendedLayout, new QuizRecommended()).commit();
                System.out.println("clicked on recommended");
            }
        });
        favouritesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getSupportFragmentManager();
                fm.beginTransaction().replace(R.id.ActivityRecommendedLayout, new Favourites()).commit();
                System.out.println("clicked on favourites");
            }
        });
    }

    //TODO add comment explaining the method here
    public void openQuizWizard(View view) {
        Intent intent = new Intent(this, Wizard.class);
        startActivity(intent);
    }

    //TODO add comment explaining the method here
    public void goBack(View view){
        this.onBackPressed();
    }
}