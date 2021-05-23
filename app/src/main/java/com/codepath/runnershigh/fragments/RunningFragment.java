package com.codepath.runnershigh.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.Messenger;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Chronometer;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.codepath.runnershigh.MainActivity;
import com.codepath.runnershigh.R;
import com.codepath.runnershigh.services.RunnersHighLocationService;
import com.google.android.gms.maps.model.LatLng;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

/*
This is the fragment that the user sees while they are in the process of running. It has a timer,
distance counter and pace counter. The user can pause, resume and stop the run.
 */
public class RunningFragment extends Fragment {
    RunningFragmentInterface runningFragmentInterface;

    //Layout element references
    ImageButton ibPauseResume, ibStop;
    TextView tvDistance, tvPace, tvDistanceLabel, tvPaceLabel;

    Chronometer cmTime;
    //true if chronometer is ticking, false if paused
    boolean ticking;
    long pauseOffset=0;

    Float totalDistance=0f;
    Location lastLocation;
    List<LatLng> latLngList;

    SharedPreferences prefs;

    //Used for location updates
    Handler handler = new Handler(Looper.getMainLooper()){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            if (msg != null) {
                Bundle data = msg.getData();
                Location location = data.getParcelable(RunnersHighLocationService.LOCATION_PARCELABLE);
                totalDistance=data.getFloat(RunnersHighLocationService.TOTAL_DISTANCE);
                if (ticking) {
                    double multiplier;
                    SharedPreferences prefs = getContext().getSharedPreferences("settings", Context.MODE_PRIVATE);

                    //If kilometer setting, use appropriate multiplier for distance
                    if(prefs.getInt("units", -1) == MainActivity.DISTANCE_KILOMETERS)
                        multiplier = MainActivity.MI_TO_KM;
                    else
                        multiplier = 1;

                    tvDistance.setText(String.format("%.2f", totalDistance * multiplier));
                    //Pace acts a little wonky but I think that has to do with testing
                    //on an emulator
                    float pace = location.getSpeed() * 2.23694f;
                    tvPace.setText(String.format("%.2f", pace * multiplier));
                }
                LatLng latLng = new LatLng(location.getLatitude(),location.getLongitude());
                latLngList.add(latLng);
                lastLocation=location;

            }
        }
    };

    public RunningFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        latLngList = new ArrayList<>();
        prefs = getContext().getSharedPreferences("settings", Context.MODE_PRIVATE);
        super.onCreate(savedInstanceState); }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_running, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //Finding Views
        cmTime = view.findViewById(R.id.cmTime);
        ibPauseResume=view.findViewById(R.id.ibPauseResume);
        ibStop=view.findViewById(R.id.ibStop);
        tvDistance=view.findViewById(R.id.tvDistance);
        tvPace=view.findViewById(R.id.tvPace);
        tvDistanceLabel = view.findViewById(R.id.tvDistanceLabel);
        tvPaceLabel = view.findViewById(R.id.tvPaceLabel);

        tvDistance.setText(getString(R.string.blank_value));
        tvPace.setText(getString(R.string.blank_value));

        //Depending on units used, get correct label string
        if(prefs.getInt("units", -1) == MainActivity.DISTANCE_KILOMETERS)
        {
            tvDistanceLabel.setText(R.string.kilometers);
            tvPaceLabel.setText(R.string.pace_kph);
        }
        else
        {
            tvDistanceLabel.setText(R.string.miles);
            tvPaceLabel.setText(R.string.pace_mph);
        }

        //Setting onClickListeners
        ibPauseResume.setOnClickListener(v -> {
            if(ticking){
                pause();
            }else{
                play();
            }
        });

        ibStop.setOnClickListener(v -> stop());

        play();
    }

    private void startLocationUpdates(){
        Intent i =new Intent(getActivity(),RunnersHighLocationService.class);
        i.putExtra("messenger",new Messenger(handler));
        i.setAction(RunnersHighLocationService.START_LOCATION_UPDATE);
        getActivity().startService(i);
    }

    private void stopLocationUpdates(){
        Intent i =new Intent(getActivity(),RunnersHighLocationService.class);
        i.putExtra("messenger",new Messenger(handler));
        i.setAction(RunnersHighLocationService.STOP_LOCATION_UPDATE);
        getActivity().startService(i);
    }

    private void stopLocationService(){
        Intent i =new Intent(getActivity(),RunnersHighLocationService.class);
        i.putExtra("messenger",new Messenger(handler));
        i.setAction(RunnersHighLocationService.STOP_LOCATION_SERVICE);
        getActivity().startService(i);
    }

    private void pause() {
        ibPauseResume.setImageResource(R.drawable.ic_baseline_play_circle_filled_24);
        cmTime.stop();
        pauseOffset = SystemClock.elapsedRealtime() - cmTime.getBase();
        ticking=false;
    }

    private void play() {
        ibPauseResume.setImageResource(R.drawable.ic_baseline_pause_circle_filled_24);
        cmTime.setBase(SystemClock.elapsedRealtime()-pauseOffset);
        cmTime.start();
        ticking=true;
        startLocationUpdates();
    }

    private void stop() {
        if(ticking)
            pause();

        //display dialog "are you sure you want to end your run"
        AlertDialog.Builder builder= new AlertDialog.Builder(getActivity());
        builder.setMessage("Are you sure you want to end this run?");
        builder.setPositiveButton("Yes", (dialog, which) -> {
            Toast.makeText(getContext(), "user clicks yes, move to post run screen", Toast.LENGTH_SHORT).show();
            //TODO: build a post run class for all post run info

            stopLocationService();

            //Calculate calories based on pace
            int weight = ParseUser.getCurrentUser().getInt("weight");
            double calories = calculateCalories(pauseOffset, totalDistance, weight);

            //Finish the run and head to PostRunFragment
            runningFragmentInterface.runComplete(cmTime.getText().toString(),
                    totalDistance,
                    calories,
                    latLngList);
        });

        builder.setNegativeButton("No", (dialog, which) -> Toast.makeText(getContext(), "if the user clicks no the run continues", Toast.LENGTH_SHORT).show());
        builder.create().show();
    }

    //Attaching the interface
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if(context instanceof RunningFragmentInterface){
            runningFragmentInterface= (RunningFragmentInterface) context;
        }else{
            throw new RuntimeException(context.toString()+
                    "must implement RunningFragmentInterface");
        }
    }

    //Calculate approximate calories burned based on average pace
    public double calculateCalories(long time, double distance, int weight) {
        double minutes = (double)time / 60000;  //Convert ms to min
        int avgPace = (int)Math.round(minutes / distance);
        double MET;

        //Rounds the average pace to nearest whole number to get MET value
        switch (avgPace) {
            case 15:
            case 14:
                MET = 6.0;
                break;
            case 13:
            case 12:
                MET = 8.3;
                break;
            case 11:
            case 10:
                MET = 9.8;
                break;
            case 9:
                MET = 10.5;
                break;
            case 8:
                MET = 11.8;
                break;
            case 7:
                MET = 12.3;
                break;
            case 6:
                MET = 14.5;
                break;
            case 5:
                MET = 19.0;
                break;
            default:
                Toast.makeText(getActivity(),"No MET value", Toast.LENGTH_SHORT).show();
                MET = 0;
        }

        return (MET * 3.5 * MainActivity.LBS_TO_KG * weight * minutes / 200);
    }

    public interface RunningFragmentInterface{
        void runComplete(String runtime,double distance,double calories, List<LatLng> latLngList);
    }
}