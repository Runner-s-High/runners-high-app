package com.codepath.runnershigh.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Button;

import com.bumptech.glide.Glide;
import com.codepath.runnershigh.R;
import com.codepath.runnershigh.RunData;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import com.codepath.runnershigh.dialogFragments.PreRunMoodDialogFragment;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class StartRunFragment extends Fragment {
    TextView TotalDistance,TotalTime,LongestRun,CaloriesBurned,MoodIncrease,StressReduction,tvLongestTime,CaloriesAVG;
    TextView TotalDistance2,TotalTime2,LongestRun2,CaloriesBurned2,MoodIncrease2,StressReduction2,
            tvLongestTime2,CaloriesAVG2;

    ArrayList<Double> DistanceArray;
    ArrayList<Double> CalorieArray;
    ArrayList<String> RunTimeArray;
    ArrayList<Integer> PreMoodArray;
    ArrayList<Integer> PostMoodArray;
    ArrayList<Integer> PreStressArray;
    ArrayList<Integer> PostStressArray;



    public StartRunFragment() {
        // Required empty public constructor
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        DistanceArray=new ArrayList<>();
        CalorieArray=new ArrayList<>();
        RunTimeArray=new ArrayList<>();
        PreMoodArray=new ArrayList<>();
        PostMoodArray=new ArrayList<>();
        PreStressArray=new ArrayList<>();
        PostStressArray=new ArrayList<>();


        TotalDistance=view.findViewById(R.id.TVTotalDistance);
        TotalTime=view.findViewById(R.id.TVTotalTime);
        LongestRun=view.findViewById(R.id.TVLongestRun);
        CaloriesBurned=view.findViewById(R.id.TVCaloriesBurned);
        MoodIncrease=view.findViewById(R.id.TVMoodIncrease);
        StressReduction=view.findViewById(R.id.TVStressDecrease);
        tvLongestTime=view.findViewById(R.id.TVLongestTime);
        CaloriesAVG=view.findViewById(R.id.TVCaloriesBurnedAVG);

        TotalDistance2=view.findViewById(R.id.TVTotalDistance2);
        TotalTime2=view.findViewById(R.id.TVTotalTime2);
        LongestRun2=view.findViewById(R.id.TVLongestRun2);
        CaloriesBurned2=view.findViewById(R.id.TVCaloriesBurned2);
        MoodIncrease2=view.findViewById(R.id.TVMoodIncrease2);
        StressReduction2=view.findViewById(R.id.TVStressDecrease2);
        tvLongestTime2=view.findViewById(R.id.TVLongestTime2);
        CaloriesAVG2=view.findViewById(R.id.TVCaloriesBurnedAVG2);


        ParseQuery<RunData> query=ParseQuery.getQuery(RunData.class);
        query.include(RunData.KEY_USER);
        query.setLimit(10);
        query.whereEqualTo(RunData.KEY_USER, ParseUser.getCurrentUser());
        query.addDescendingOrder(RunData.KEY_CREATED_AT);
        query.findInBackground(new FindCallback<RunData>() {
            @Override
            public void done(List<RunData> runs, ParseException e) {
                if (e != null) {
                    return;
                }

                if (runs.size()>=5) {
                    for (int i=0;i<runs.size();i++){
                            DistanceArray.add(runs.get(i).getRunDistance());
                            CalorieArray.add(runs.get(i).getRunCalories());
                            RunTimeArray.add(runs.get(i).getRunTime());
                            PreMoodArray.add(runs.get(i).getPreRunMood());
                            PostMoodArray.add(runs.get(i).getPostRunMood());
                            PreStressArray.add(runs.get(i).getPreRunStress());
                            PostStressArray.add(runs.get(i).getPostRunStress());


                    }
                    CalculateDistances(5);
                    CalculateRunTimes(5);
                    CalculatePsychological(5);
                    CalculateCalories(5);

                    if (runs.size()==10) {
                        CalculateDistances(10);
                        CalculateRunTimes(10);
                        CalculatePsychological(10);
                        CalculateCalories(10);
                    }
                }

                else
                    return;
            }
        });




    }//end onviewcreated

    public void CalculatePsychological(int amount){
        double PreMood,PostMood,PreStress,PostStress;
        PreMood=PostMood=PreStress=PostStress=0;

        for (int i=0;i<amount;i++){
            PreMood+=PreMoodArray.get(i);
            PostMood+=PostMoodArray.get(i);
            PreStress+=PreStressArray.get(i);
            PostStress+=PostStressArray.get(i);
        }

        double moodIncrease=(PostMood-PreMood)/PreMood*100;
        double stressReduction=(PreStress-PostStress)/PreStress*100;

        if (amount==5) {
            MoodIncrease.setText(String.valueOf((int) moodIncrease).toString() + " %");
            StressReduction.setText(String.valueOf((int) stressReduction).toString() + " %");
        }

        else{
            MoodIncrease2.setText(String.valueOf((int) moodIncrease).toString() + " %");
            StressReduction2.setText(String.valueOf((int) stressReduction).toString() + " %");

        }

    }

    public void CalculateCalories(int amount){
        double calories=0;
        for (int i=0;i<amount;i++){
            calories+=CalorieArray.get(i);
            double sauce=CalorieArray.get(i);
            double ignore=CalorieArray.get(i);
        }

        int RoundedCalories=(int)calories;
        int avg=(int)(calories/amount);

        if (amount==5) {
            CaloriesBurned.setText(String.valueOf(RoundedCalories).toString() + " Cal");
            CaloriesAVG.setText(String.valueOf(avg).toString() + " Cal");
        }
        else {
            CaloriesBurned2.setText(String.valueOf(RoundedCalories).toString() + " Cal");
            CaloriesAVG2.setText(String.valueOf(avg).toString() + " Cal");
        }
    }

    public void CalculateRunTimes(int amount){
        int totalseconds=0;
        int LongestRunSeconds=0;
        String CurrentTime;

        int value;
        int TheLongestRun= Integer.parseInt(RunTimeArray.get(0).replaceAll("[^0-9]", ""));

        for (int i=0;i<amount;i++){
            CurrentTime=RunTimeArray.get(i);
            value = Integer.parseInt(CurrentTime.replaceAll("[^0-9]", ""));


            if (value>TheLongestRun) {
                TheLongestRun=value;
            }
            for (int j=0;j<4;j++){
                if (j==0)
                    totalseconds+=value%10;
                else if (j==1)
                    totalseconds+=value%10 * 10;
                else if (j==2)
                    totalseconds+=value%10 *60;
                else if (j==3)
                    totalseconds+=value%10 * 60 * 10;

                value=value/10;

            }

        }

        int total_min=totalseconds/60;
        int seconds=totalseconds-(60*total_min);
        int hours=total_min/60;
        int minutes=total_min-(60*hours);

        for (int j=0;j<4;j++){
            if (j==0)
                LongestRunSeconds+=TheLongestRun%10;
            else if (j==1)
                LongestRunSeconds+=TheLongestRun%10 * 10;
            else if (j==2)
                LongestRunSeconds+=TheLongestRun%10 *60;
            else if (j==3)
                LongestRunSeconds+=TheLongestRun%10 * 60 * 10;

           TheLongestRun=TheLongestRun/10;

        }

        int total_min2=LongestRunSeconds/60;
        int seconds2=LongestRunSeconds-(60*total_min2);
        int hours2=total_min2/60;
        int minutes2=total_min2-(60*hours2);


        if (amount==5) {
            TotalTime.setText(hours + " hrs " + minutes + " min " + seconds + " sec ");
            tvLongestTime.setText(hours2 + " hrs " + minutes2 + " min " + seconds2 + " sec ");
        }
        else {
            TotalTime2.setText(hours + " hrs " + minutes + " min " + seconds + " sec ");
            tvLongestTime2.setText(hours2 + " hrs " + minutes2 + " min " + seconds2 + " sec ");
        }
    }


    public void CalculateDistances(int amount){
        double weeklydistance=0;
        double longestdistance=DistanceArray.get(0);

        for (int i=0;i<amount;i++){
            weeklydistance+=DistanceArray.get(i);

            if (DistanceArray.get(i)>longestdistance )
                longestdistance=DistanceArray.get(i);
        }

        if (amount==5) {
            TotalDistance.setText(String.valueOf(weeklydistance) + " miles");
            LongestRun.setText(String.valueOf(longestdistance) + " miles");
        }
        else{
            TotalDistance2.setText(String.valueOf(weeklydistance) + " miles");
            LongestRun2.setText(String.valueOf(longestdistance) + " miles");
        }
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_start_run, container, false);
    }

}