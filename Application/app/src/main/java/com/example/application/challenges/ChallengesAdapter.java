package com.example.application.challenges;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;
import com.example.application.R;
import java.util.List;

/**
 * paints CIS Missions
 */
public class ChallengesAdapter extends RecyclerView.Adapter<ChallengesAdapter.ViewHolder>  {
    private final LayoutInflater inflater;
    private final List<Challenge> challenges;
    private final ChallengesActivity parent;

    /**
     *
     * @param ctx the activity within which ChallengesAdapter is used
     * @param challenges the CIS Missions that the recyclerView should paint
     * @param parent a ChallengesActivity that is used to access a few methods
     */
    public ChallengesAdapter(Context ctx, List<Challenge> challenges,ChallengesActivity parent){
        this.inflater = LayoutInflater.from(ctx);
        this.challenges = challenges;

        this.parent = parent;

    }

    /**
     * See RecyclerView.java for more information.
     * @param parent
     * @param viewType
     * @return view based on custom_challenges
     */
    @NonNull
    @Override
    public ChallengesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.custom_challenges,parent,false);
        return new ViewHolder(view);
    }

    /**
     * Adds data to the cards.
     * @param holder
     * @param position
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull ChallengesAdapter.ViewHolder holder, int position) {
        String description = challenges.get(position).getDescription();
        holder.title.setText(challenges.get(position).getTitle());
        holder.endDate.setText(challenges.get(position).getPrettyEndDate());
        holder.location.setText(challenges.get(position).getLocation());
        holder.description.setText(description);
        holder.description.setVisibility(View.GONE);

        addDescriptionListener(holder, position);
        addCheckBoxListener(holder.itemView,position);

        if(challenges.get(position).getLocation().equals(" ")) {
            holder.locationLogo.setVisibility(View.INVISIBLE);
        }

        if(challenges.get(position).isCompleted()){
            CheckBox checkBox = holder.itemView.findViewById(R.id.checkBox);
            checkBox.setChecked(true);
        }

    }
    private void addDescriptionListener(ChallengesAdapter.ViewHolder holder, int position){
        holder.itemView.setOnClickListener(v -> {
            if(holder.description.getVisibility() == View.GONE && !challenges.get(position).getDescription().equals(" ")) {
                holder.description.setVisibility(View.VISIBLE);
                holder.title.setSingleLine(false);
            } else {
                holder.description.setVisibility(View.GONE);
                holder.title.setSingleLine(true);
            }


        });
    }

    /**
     * sets listener to checkbox
     * @param view the current view
     * @param position position in recyclerView
     */
    //might be some problems if there are more than 9 CIS Mission because default recyclerView reuses "position" after 9 objects.
    //To fix this problem: would have to add setItemViewCacheSize to recyclerview
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void addCheckBoxListener(View view, int position){
        CheckBox checkBox = view.findViewById(R.id.checkBox);
        Challenge challenge = challenges.get(position);

        checkBox.setOnClickListener(v -> {

            challenge.setCompleted(checkBox.isChecked());
            parent.refresh(view);
            parent.saveCompletedMission();


        });

    }

    /**
     * get the amount of challenges in challenges-list
     * @return size of list that is painted in recyclerview
     */
    @Override
    public int getItemCount() {
        return challenges.size();
    }

    /**
     * Assigns values to and holds attributes necessary for the cards in the Challenges tab
     */
    protected static class ViewHolder extends RecyclerView.ViewHolder{

        TextView title, endDate, location, description;
        ImageView locationLogo;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            endDate = itemView.findViewById(R.id.endDate);
            location = itemView.findViewById(R.id.location);
            locationLogo = itemView.findViewById(R.id.locationLogo);
            description = itemView.findViewById(R.id.description);

            addListener(itemView);
        }

        private void addListener(View itemView) {
            itemView.setOnClickListener(v -> Toast.makeText(v.getContext(), "Do Something With this Click", Toast.LENGTH_SHORT).show());
        }

    }
}
