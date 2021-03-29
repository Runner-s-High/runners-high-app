package com.codepath.runnershigh.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.hardware.SensorManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.Messenger;
import android.os.SystemClock;
import android.text.format.Time;
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

import com.codepath.runnershigh.R;
import com.codepath.runnershigh.services.RunnersHighLocationService;


public class RunningFragment extends Fragment {
    public static final String TAG=RunningFragment.class.getCanonicalName();

    ImageButton ibPauseResume;
    ImageButton ibStop;

    Chronometer cmTime;
    //true if chronometer is ticking, false if paused
    boolean ticking;
    long pauseOffset=0;

    RunningFragmentInterface runningFragmentInterface;

    TextView tvDistance;
    TextView tvPace;

    Handler handler = new Handler(Looper.getMainLooper()){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            if (msg != null) {
                Bundle data = msg.getData();
                Location location = data.getParcelable(RunnersHighLocationService.LOCATION_PARCELABLE);
                totalDistance=data.getFloat(RunnersHighLocationService.TOTAL_DISTANCE);
                if (ticking) {
                    tvDistance.setText(String.format("%.2f", totalDistance));
                    Float pace = location.getSpeed() * 2.23694f;
                    tvPace.setText(String.format("%.2f", pace));
                }
                lastLocation=location;
            }
        }
    };

    Float totalDistance=0f;
    Location lastLocation;

    public RunningFragment() {
        // Required empty public constructor
    }

    public static RunningFragment newInstance(String param1, String param2) {
        RunningFragment fragment = new RunningFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) { super.onCreate(savedInstanceState); }

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

        //Setting onClickListeners
        ibPauseResume.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ticking){
                    pause();
                }else{
                    play();
                }
            }
        });

        ibStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stop();
            }
        });


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
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getContext(), "user clicks yes, move to post run screen", Toast.LENGTH_SHORT).show();
                //releaseLocationManager();
                //TODO: build a post run class for all post run info

                stopLocationService();
                runningFragmentInterface.runComplete(cmTime.getText().toString());
            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getContext(), "if the user clicks no the run continues", Toast.LENGTH_SHORT).show();
            }
        });
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

    public interface RunningFragmentInterface{
        public void runComplete(String runtime);
        //Todo: implement with RunStats Object
        //public void runComplete(RunStats runStats);
    }
}