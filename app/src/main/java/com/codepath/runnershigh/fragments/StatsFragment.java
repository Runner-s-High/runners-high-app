package com.codepath.runnershigh.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.codepath.runnershigh.MainActivity;
import com.codepath.runnershigh.R;

//This fragment holds the stats from the run (e.g. time, distance, calories)
public class StatsFragment extends Fragment {
    ImageView preMood, postMood;
    TextView tvTimeMI, tvDistanceMI, tvCaloriesMI, tvNoteMI, tvDistanceLabelMI;

    SharedPreferences prefs;

    public StatsFragment() {
    }

    public static StatsFragment newInstance(Bundle statsBundle) {
        StatsFragment fragment = new StatsFragment();

        fragment.setArguments(statsBundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prefs = getContext().getSharedPreferences("settings", Context.MODE_PRIVATE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_stats, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        double multiplier;  //Multiplier for distance unit conversions
        String units;   //String holding distance units name

        //Check distance units setting
        if(prefs.getInt("units", -1) == MainActivity.DISTANCE_KILOMETERS) {
            multiplier = MainActivity.MI_TO_KM;
            units = "KM";
        }
        else {
            multiplier = 1;
            units = "MI";
        }

        preMood = view.findViewById(R.id.ivpremood);
        postMood = view.findViewById(R.id.ivpostmood);
        tvTimeMI = view.findViewById(R.id.tvTimeMI);
        tvDistanceMI = view.findViewById(R.id.tvDistanceMI);
        tvCaloriesMI = view.findViewById(R.id.tvCaloriesMI);
        tvNoteMI = view.findViewById(R.id.tvNoteMI);
        tvDistanceLabelMI = view.findViewById(R.id.tvDistanceLabelMI);

        tvTimeMI.setText(getArguments().getString("time"));
        tvDistanceMI.setText(String.format("%.2f", getArguments().getDouble("distance") * multiplier));
        tvCaloriesMI.setText(String.format("%.1f", getArguments().getDouble("calories") * multiplier));
        tvNoteMI.setText(getArguments().getString("note"));
        tvDistanceLabelMI.setText(String.format("%s (%s)", getString(R.string.distance_label), units));

        //Assign face colors
        SetFace(getArguments().getInt("premood"), preMood);
        SetFace(getArguments().getInt("postmood"), postMood);
    }

    //Assigns correct colors to faces in this fragment
    public void SetFace(int score, ImageView ivMood){
        switch(score) {
            case 1:
                ivMood.setImageResource(R.drawable.mood1);
                ivMood.setColorFilter(Color.parseColor("#F44336"));
                break;
            case 2:
                ivMood.setImageResource(R.drawable.mood2);
                ivMood.setColorFilter(Color.parseColor("#FF9800"));
                break;
            case 3:
                ivMood.setImageResource(R.drawable.mood3);
                ivMood.setColorFilter(Color.parseColor("#FFEB3B"));
                break;
            case 4:
                ivMood.setImageResource(R.drawable.mood4);
                ivMood.setColorFilter(Color.parseColor("#8BC34A"));
                break;
            case 5:
                ivMood.setImageResource(R.drawable.mood5);
                ivMood.setColorFilter(Color.parseColor("#4CAF50"));
                break;
        }
    }
}