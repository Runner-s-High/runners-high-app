package com.codepath.runnershigh.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextClock;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.codepath.runnershigh.MainActivity;
import com.codepath.runnershigh.R;
import com.codepath.runnershigh.RunData;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/*
The default fragment for MainActivity which shows a motivational quote/tip and charts on data from
the last 5 and 10 runs
 */
public class HomeFragment extends Fragment {
    //Layout element references
    TextView TotalDistance,TotalTime,LongestRun,CaloriesBurned,MoodIncrease,StressReduction,tvLongestTime,CaloriesAVG;
    TextView TotalDistance2,TotalTime2,LongestRun2,CaloriesBurned2,MoodIncrease2,StressReduction2,
            tvLongestTime2,CaloriesAVG2;
    TextView RandomQuote;
    TextClock tcDate;
    TextClock tcTime;

    ArrayList<Double> DistanceArray;
    ArrayList<Double> CalorieArray;
    ArrayList<String> RunTimeArray;
    ArrayList<Integer> PreMoodArray;
    ArrayList<Integer> PostMoodArray;
    ArrayList<Integer> PreStressArray;
    ArrayList<Integer> PostStressArray;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //Gather string resources for quotes and tips
        Resources resources = getResources();
        List<String> Quotes = Arrays.asList(resources.getStringArray(R.array.Quotes));
        List<String> tips = Arrays.asList(resources.getStringArray(R.array.tips));
        Random random =new Random();

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

        tcDate = view.findViewById(R.id.tcDate_startfragment);
        tcTime = view.findViewById(R.id.tcTime_startfragment);

        RandomQuote=view.findViewById(R.id.tvquote);

        tcDate.setFormat12Hour("EEE, MMM d, ''yy");
        tcTime.setFormat12Hour("h:mm a");

        if(random.nextInt() % 2 == 0) {
            RandomQuote.setText(Quotes.get(
                    random.nextInt(
                            Quotes.size())));
        }
        else {
            RandomQuote.setText(tips.get(random.nextInt(tips.size())));

        }

        //Query the last 10 runs the user completed
        ParseQuery<RunData> query=ParseQuery.getQuery(RunData.class);
        query.include(RunData.KEY_USER);
        query.setLimit(10);
        query.whereEqualTo(RunData.KEY_USER, ParseUser.getCurrentUser());
        query.addDescendingOrder(RunData.KEY_CREATED_AT);
        query.findInBackground((runs, e) -> {
            if (e != null) {
                return;
            }

            //Perform math operations on these runs
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
        });


    }

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
            MoodIncrease.setText((int) moodIncrease + " %");
            StressReduction.setText((int) stressReduction + " %");
        }

        else{
            MoodIncrease2.setText((int) moodIncrease + " %");
            StressReduction2.setText((int) stressReduction + " %");

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
            CaloriesBurned.setText(RoundedCalories + " Cal");
            CaloriesAVG.setText(avg + " Cal");
        }
        else {
            CaloriesBurned2.setText(RoundedCalories + " Cal");
            CaloriesAVG2.setText(avg + " Cal");
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
                else
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
            else
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

        SharedPreferences prefs = getContext().getSharedPreferences("settings", Context.MODE_PRIVATE);
        String units;

        for (int i=0;i<amount;i++){
            weeklydistance+=DistanceArray.get(i);

            if (DistanceArray.get(i)>longestdistance )
                longestdistance=DistanceArray.get(i);
        }

        if(prefs.getInt("units", -1) == MainActivity.DISTANCE_KILOMETERS) {
            weeklydistance *= MainActivity.MI_TO_KM;
            longestdistance *= MainActivity.MI_TO_KM;
            units = "kilometers";
        }
        else
            units = "miles";

        if (amount==5) {
            TotalDistance.setText(String.format("%.2f %s", weeklydistance, units));
            LongestRun.setText(String.format("%.2f %s", longestdistance, units));
        }
        else{
            TotalDistance2.setText(String.format("%.2f %s", weeklydistance, units));
            LongestRun2.setText(String.format("%.2f %s", longestdistance, units));
        }
    }




}