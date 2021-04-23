package com.codepath.runnershigh.fragments;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextClock;
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
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Random;


public class StartRunFragment extends Fragment {

    public static final int REQUEST_CODE_LOCATION_PERMISSION=1;

    Button btStart;
    StartRunFragmentInterface startRunFragmentInterface;
    boolean completedMoodSurvey=false;
    PreRunMoodDialogFragment moodSurvey;


    public StartRunFragment() {
        // Required empty public constructor
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);
        btStart = view.findViewById(R.id.btSubmit);

        btStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(ContextCompat.checkSelfPermission(
                        getActivity().getApplicationContext(),
                        Manifest.permission.ACCESS_FINE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(
                            getActivity(),
                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                                    Manifest.permission.ACCESS_COARSE_LOCATION},
                            REQUEST_CODE_LOCATION_PERMISSION);
                }else {

                    if (completedMoodSurvey) {
                        completedMoodSurvey = false;
                        startRunFragmentInterface.openRunningFragment();
                    } else {
                        FragmentManager fm = StartRunFragment.this.getChildFragmentManager();
                        moodSurvey = new PreRunMoodDialogFragment();
                        moodSurvey.show(fm, "Survey");
                    }
                }
            }
        });



    }//end onviewcreated


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_start_run, container, false);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==REQUEST_CODE_LOCATION_PERMISSION){
            if(grantResults[0]==PackageManager.PERMISSION_GRANTED){
                if (completedMoodSurvey) {
                    completedMoodSurvey = false;
                    startRunFragmentInterface.openRunningFragment();
                } else {
                    FragmentManager fm = StartRunFragment.this.getChildFragmentManager();
                    moodSurvey = new PreRunMoodDialogFragment();
                    moodSurvey.show(fm, "Survey");
                }
            }
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if(context instanceof StartRunFragmentInterface){
            startRunFragmentInterface=(StartRunFragmentInterface) context;
        }else{
            throw new RuntimeException(context.toString()+
                    "must implement HomeFragmentInterface");
        }
    }

    public interface StartRunFragmentInterface{
        public void startRun();
        public void openRunningFragment();

    }

}