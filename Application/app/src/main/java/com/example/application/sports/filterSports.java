package com.example.application.sports;

import android.graphics.Color;

import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.application.R;
import com.example.application.Tag;
import com.example.application.animations.Animations;
import com.google.android.flexbox.FlexboxLayout;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link filterSports#newInstance} factory method to
 * create an instance of this fragment.
 */
public class filterSports extends Fragment {

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private Button saveButton, clearButton;
    private ArrayList<Button> allButtons;
    private ArrayList<Tag> savedButtons;
    private List<Sport> sports;
    private RecyclerView recyclerView;
    private SportsAdapter sportsAdapter;
    private FrameLayout background;


    private String mParam1;
    private String mParam2;

    public filterSports() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment filterSports.
     */
    public static filterSports newInstance(String param1, String param2) {
        filterSports fragment = new filterSports();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        allButtons = new ArrayList<>();
        savedButtons = new ArrayList<>();
        sports = new ArrayList<>();
    }

    
    //Sets the view and binds the buttons, does more or less everything
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_filter_sports, container, false);
        List<Tag> tags = new ArrayList<Tag>(EnumSet.allOf(Tag.class));
        FlexboxLayout flexboxLayout = rootView.findViewById(R.id.flexLayout);
        FlexboxLayout.LayoutParams lp = new FlexboxLayout.LayoutParams(FlexboxLayout.LayoutParams.WRAP_CONTENT, FlexboxLayout.LayoutParams.WRAP_CONTENT);
        lp.setMargins(10, 10, 10, 10);

        for (int i = 0; i < tags.size(); i++) {
            Button b = new Button(getActivity());
            allButtons.add(b);
            b.setText(tags.get(i).toString());
            b.setTextSize(20);
            b.setBackground(this.getResources().getDrawable(R.drawable.tag_button));
            b.setPadding(25, 5, 25, 5);
            b.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(b.isSelected()){
                        setNotSelected(b);
                    }
                    else {
                       setSelected(b);
                    }
                }
            });

            flexboxLayout.addView(b, lp);
        }

        //Sets the saved buttons when exiting the fragment
        setSavedButtons();

        background = rootView.findViewById(R.id.filterBackground);
        background.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideFilter();
            }
        });

        //Save the filter
        saveButton = rootView.findViewById(R.id.saveButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                savedButtons.clear();
                //Add all selected buttons
                addAllSelected();

                //Gets the whole list unfiltered
                sports = ((AvailableSportsActivity) requireActivity()).getSportsList();


                removeFromSportList();

                //Update recycler view (Same as Activity)
                recyclerView = getActivity().findViewById(R.id.sportsList);
                sportsAdapter = new SportsAdapter(getActivity().getApplicationContext(), sports, ((AvailableSportsActivity) requireActivity()).getStateOfFavoriteCheckBox());
                recyclerView.setItemViewCacheSize(sports.size());
                recyclerView.setAdapter(sportsAdapter);

                //No sports text
                ifSportEmpty(sports);

                //Save tags
                ((AvailableSportsActivity) requireActivity()).setSavedTags(savedButtons);

                //Makes fragment invisible
                hideFilter();

            }
        });

        //Clears the selected tags
        clearButton = rootView.findViewById(R.id.clearButton);
        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for(Button b : allButtons){
                    setNotSelected(b);
                }
            }
        });


        // Inflate the layout for this fragment
        return rootView;
    }

    private void addAllSelected(){
        for (Button b : allButtons) {
            if(b.isSelected()){
                savedButtons.add(Tag.valueOf(b.getText().toString()));
            }
        }
    }

    private void removeFromSportList(){
        //Adds all the sports that does not have the wanted tags
        List<Sport> removeList = new ArrayList<>();
        for(Sport s : sports){
            if(!s.getTags().containsAll(savedButtons)){
                removeList.add(s);
            }
        }
        //Remove from list
        sports.removeAll(removeList);
    }

    private void ifSportEmpty(List<Sport> sports){
        if(sports.isEmpty()){
            getActivity().findViewById(R.id.noSportsFound).setVisibility(View.VISIBLE);
        }
        else{
            getActivity().findViewById(R.id.noSportsFound).setVisibility(View.GONE);
        }
    }

    private void setSelected(Button b){
        b.setBackground(getResources().getDrawable(R.drawable.tag_button_pressed));
        b.setTextColor(Color.WHITE);
        b.setSelected(true);
    }

    private void setNotSelected(Button b){
        b.setBackground(getResources().getDrawable(R.drawable.tag_button));
        b.setTextColor(Color.BLACK);
        b.setSelected(false);
    }

    private void setSavedButtons(){
        List<Tag> savedTags = ((AvailableSportsActivity) requireActivity()).getSavedTags();
        for(Button b : allButtons){
            try{
                if(savedTags.contains(Tag.valueOf(b.getText().toString()))){
                    setSelected(b);
                }
            }catch(NullPointerException e){
                System.out.println("This is not a Error, just bypass a empty list for bootup");
            }

        }
    }

    private void hideFilter(){
        AvailableSportsActivity.toggleFilterExpanded();
        Animations.toggleArrow(requireActivity().findViewById(R.id.downFilterImg), false);
        Animations.collapse(requireActivity().findViewById(R.id.frameLayout));
    }

}