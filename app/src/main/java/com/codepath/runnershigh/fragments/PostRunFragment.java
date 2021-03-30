package com.codepath.runnershigh.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import com.codepath.runnershigh.MainActivity;
import com.codepath.runnershigh.R;

public class PostRunFragment extends Fragment implements OnMapReadyCallback {
    PostRunFragmentInterface postRunFragmentInterface;

    Button btSave;
    Button btExit;

    ImageView ivPreMood;

    ImageButton IB1,IB2,IB3,IB4,IB5;
    boolean moodSet=false;
    int postRunMoodRating;

    TextView tvRunTime;
    TextView tvRunDistance;
    EditText etNotes;

    Bundle RunInfo;

    MapView mvPostRun;
    GoogleMap mMap;


    public PostRunFragment() {
        // Required empty public constructor
    }


    public static PostRunFragment newInstance(Bundle newRunBundle) {
        PostRunFragment fragment = new PostRunFragment();

        fragment.setArguments(newRunBundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null)
            RunInfo = getArguments();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_post_run, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        moodSet=false;
        super.onViewCreated(view, savedInstanceState);
        //Getting Views
        btExit = view.findViewById(R.id.btExit);
        btSave = view.findViewById(R.id.btSave);
        ivPreMood = view.findViewById(R.id.ivPreMood);
        etNotes = view.findViewById(R.id.etNotes);
        tvRunTime = view.findViewById(R.id.tvRunTime);
        tvRunDistance = view.findViewById(R.id.tvRunDistance);
        mvPostRun = view.findViewById(R.id.mvPostRun);

        tvRunTime.setText("Time: "+RunInfo.get(MainActivity.NEWRUNTIME));
        tvRunDistance.setText("Distance: " + RunInfo.get(MainActivity.NEWRUNDISTANCE));

        IB1=view.findViewById(R.id.IB1);
        IB2=view.findViewById(R.id.IB2);
        IB3=view.findViewById(R.id.IB3);
        IB4=view.findViewById(R.id.IB4);
        IB5=view.findViewById(R.id.IB5);

        IB1.setOnClickListener(moodBtnListener);
        IB2.setOnClickListener(moodBtnListener);
        IB3.setOnClickListener(moodBtnListener);
        IB4.setOnClickListener(moodBtnListener);
        IB5.setOnClickListener(moodBtnListener);

        mvPostRun.onCreate(savedInstanceState);
        mvPostRun.getMapAsync(this);

        //Setting up UI
        setPreMoodImage();

        //Setting up onClick Listeners
        btExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder= new AlertDialog.Builder(getActivity());
                builder.setMessage("Are you sure you don't want to log this run?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        postRunFragmentInterface.exitPostRun(false,RunInfo);
                    }
                });

                builder.setNegativeButton("No",null);

                builder.create().show();
            }
        });

        btSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(moodSet) {
                    RunInfo.putString(MainActivity.NEWRUNNOTE, etNotes.getText().toString());
                    RunInfo.putString(MainActivity.NEWRUNDATE, "Thursday, March 28, 2021");
                    postRunFragmentInterface.exitPostRun(true, RunInfo);
                }else
                    Toast.makeText(getActivity(), "Finish the post run survey", Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void setPreMoodImage(){
        int premood = RunInfo.getInt(MainActivity.NEWRUNPREMOOD);
        switch(premood){
            case 1:
                ivPreMood.setImageResource(R.drawable.mood1);
                ivPreMood.setColorFilter(Color.parseColor("#FF99C9"));
                break;
            case 2:
                ivPreMood.setImageResource(R.drawable.mood2);
                ivPreMood.setColorFilter(Color.parseColor("#6A9DB8"));
                break;
            case 3:
                ivPreMood.setImageResource(R.drawable.mood3);
                ivPreMood.setColorFilter(Color.parseColor("#87DBA8"));
                break;
            case 4:
                ivPreMood.setImageResource(R.drawable.mood4);
                ivPreMood.setColorFilter(Color.parseColor("#C288E5"));
                break;
            case 5:
                ivPreMood.setImageResource(R.drawable.mood5);
                ivPreMood.setColorFilter(Color.parseColor("#FFEE58"));
                break;
        }

    }

    View.OnClickListener moodBtnListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.IB1:
                    postRunMoodRating=1;
                    break;
                case R.id.IB2:
                    postRunMoodRating=2;
                    break;
                case R.id.IB3:
                    postRunMoodRating=3;
                    break;
                case R.id.IB4:
                    postRunMoodRating=4;
                    break;
                case R.id.IB5:
                    postRunMoodRating=5;
            }
            unselectMoodButtons();
            v.setSelected(true);
            moodSet = true;
            RunInfo.putInt(MainActivity.NEWRUNPOSTMOOD,postRunMoodRating);
        }
    };

    public void unselectMoodButtons(){
        IB1.setSelected(false);
        IB2.setSelected(false);
        IB3.setSelected(false);
        IB4.setSelected(false);
        IB5.setSelected(false);

    }


    //Attaching Interface
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if(context instanceof PostRunFragmentInterface){
            postRunFragmentInterface = (PostRunFragmentInterface) context;
        }else{
            throw new RuntimeException(context.toString()
                    +"must implement PostRunFragmentInterface");
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions()
                .position(sydney)
                .title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }

    public interface PostRunFragmentInterface{
        public void exitPostRun(boolean save,Bundle postRunInfo);
    }

}