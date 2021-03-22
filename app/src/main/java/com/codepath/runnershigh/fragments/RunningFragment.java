package com.codepath.runnershigh.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.os.SystemClock;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Chronometer;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.codepath.runnershigh.R;


public class RunningFragment extends Fragment {
    public static final String
            KEY_TIME="TIME",
            KEY_DISTANCE="DISTANCE",
            KEY_PACE="PACE",
            KEY_GEOLOGS="GEOLOGS";

    ImageButton ibPauseResume;
    ImageButton ibStop;

    Chronometer cmTime;
    //true if chronometer is ticking, false if paused
    boolean ticking;
    long pauseOffset=0;

    RunningFragmentInterface runningFragmentInterface;

    TextView tvDistance;
    TextView tvPace;


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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

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
                //TODO: end run, get all the run data and all that jazz, intent back to mainActivity
                Toast.makeText(getContext(), "YOU CLICKED YES!", Toast.LENGTH_SHORT).show();
                Bundle runStats = new Bundle();

                runStats.putString(KEY_TIME, cmTime.getText().toString());
                runStats.putString(KEY_DISTANCE, tvDistance.getText().toString());
                runStats.putString(KEY_PACE, tvPace.getText().toString());



                //runStats.putParcelable(KEY_GEOLOGS, PLACEHOLDER);
                //TODO: build a post run class for all post run info
                //runStats.putParcelable(KEY_RUNITEM, RUNCLASS);

                runningFragmentInterface.runComplete(runStats);
            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getContext(), "YOU CLICKED NO!", Toast.LENGTH_SHORT).show();
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
        public void runComplete(Bundle runStats);
    }
}