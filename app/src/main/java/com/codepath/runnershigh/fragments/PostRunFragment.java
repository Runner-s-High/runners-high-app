package com.codepath.runnershigh.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.codepath.runnershigh.MainActivity;
import com.codepath.runnershigh.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/*
This fragment displays after a user ends their run. It features the run route, statistics about the run,
and the post-run survey used to gauge progress.
 */
public class PostRunFragment extends Fragment implements OnMapReadyCallback {
    private static final String TAG = PostRunFragment.class.getCanonicalName();
    PostRunFragmentInterface postRunFragmentInterface;

    //Layout element references
    Button btSave, btExit;
    ImageView ivPreMood;
    ImageButton IB1,IB2,IB3,IB4,IB5;
    TextView tvRunTime, tvRunDistance;
    EditText etNotes;
    SeekBar PreRunseekbar, PostRunseekbar;

    Bundle RunInfo;
    List<LatLng> latLngList;

    MapView mvPostRun;
    GoogleMap mMap;

    boolean moodSet=false;
    int postRunMoodRating;
    int postRunStressRating;

    SharedPreferences prefs;

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
        if(mvPostRun != null)
            mvPostRun.onCreate(savedInstanceState);
        if (getArguments() != null){
            RunInfo = getArguments();
            latLngList=RunInfo.getParcelableArrayList(MainActivity.NEW_RUN_ROUTE);
        }

        prefs = getContext().getSharedPreferences("settings", Context.MODE_PRIVATE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_post_run, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        moodSet=false;
        double multiplier;
        String units;

        //Getting Views
        btExit = view.findViewById(R.id.btExit);
        btSave = view.findViewById(R.id.btSave);
        ivPreMood = view.findViewById(R.id.ivPreMood);
        etNotes = view.findViewById(R.id.etNotes);
        tvRunTime = view.findViewById(R.id.tvRunTime);
        tvRunDistance = view.findViewById(R.id.tvRunDistance);
        mvPostRun = view.findViewById(R.id.mvPostRun);
        PreRunseekbar=view.findViewById(R.id.PreseekBar);
        IB1=view.findViewById(R.id.IB1);
        IB2=view.findViewById(R.id.IB2);
        IB3=view.findViewById(R.id.IB3);
        IB4=view.findViewById(R.id.IB4);
        IB5=view.findViewById(R.id.IB5);
        PostRunseekbar=view.findViewById(R.id.PostseekBar);

        postRunStressRating=5;

        tvRunTime.setText(String.format("Time: %s", RunInfo.get(MainActivity.NEW_RUN_TIME)));

        //Set appropriate distance multipliers based on units
        if(prefs.getInt("units", -1) == MainActivity.DISTANCE_KILOMETERS) {
            multiplier = MainActivity.MI_TO_KM;
            units = "km";
        }
        else {
            multiplier = 1;
            units = "mi";
        }

        tvRunDistance.setText(String.format("Distance: %.2f %s", (double)RunInfo.get(MainActivity.NEW_RUN_DISTANCE) * multiplier, units));

        IB1.setOnClickListener(moodBtnListener);
        IB2.setOnClickListener(moodBtnListener);
        IB3.setOnClickListener(moodBtnListener);
        IB4.setOnClickListener(moodBtnListener);
        IB5.setOnClickListener(moodBtnListener);

        //Setup mapview
        mvPostRun.onCreate(savedInstanceState);
        mvPostRun.getMapAsync(this);

        //Setting up UI
        int premood = RunInfo.getInt(MainActivity.NEW_RUN_PRE_MOOD);
        setFace(premood, ivPreMood);

        int prestressrating = RunInfo.getInt(MainActivity.NEW_RUN_PRE_STRESS);
        PreRunseekbar.setProgress(prestressrating);

        //Setting up onClick Listeners
        btExit.setOnClickListener(v -> {
            AlertDialog.Builder builder= new AlertDialog.Builder(getActivity());
            builder.setMessage("Are you sure you don't want to log this run?");
            builder.setPositiveButton("Yes", (dialog, which) -> postRunFragmentInterface.exitPostRun(false,RunInfo));

            builder.setNegativeButton("No",null);

            builder.create().show();
        });

        btSave.setOnClickListener(v -> {
            if(moodSet) {
                Calendar calendar = Calendar.getInstance();
                String currentDate = DateFormat.getDateInstance(DateFormat.FULL).format(calendar.getTime());
                RunInfo.putString(MainActivity.NEW_RUN_NOTE, etNotes.getText().toString());
                RunInfo.putString(MainActivity.NEW_RUN_DATE, currentDate);

                RunInfo.putInt(MainActivity.NEW_RUN_POST_STRESS,postRunStressRating);

                ArrayList<String> lats = new ArrayList<>();
                ArrayList<String> longs = new ArrayList<>();

                for(LatLng point : latLngList){
                    lats.add(String.valueOf(point.latitude));
                    longs.add(String.valueOf(point.longitude));
                }

                RunInfo.putStringArrayList(MainActivity.NEW_RUN_LAT_LIST, lats);
                RunInfo.putStringArrayList(MainActivity.NEW_RUN_LNG_LIST, longs);


                postRunFragmentInterface.exitPostRun(true, RunInfo);
            }else
                Toast.makeText(getActivity(), "Finish the post run survey", Toast.LENGTH_SHORT).show();
        });

        //user can't slide old seekbar with this
        PreRunseekbar.setOnTouchListener((view1, motionEvent) -> true);

        PostRunseekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                postRunStressRating=progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        mvPostRun.onSaveInstanceState(outState);
    }

    @Override
    public void onResume() {
        super.onResume();
        mvPostRun.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mvPostRun.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mvPostRun.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mvPostRun.onLowMemory();
    }


    public void setFace(int score, ImageView ivMood){
        switch(score){
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
            RunInfo.putInt(MainActivity.NEW_RUN_POST_MOOD,postRunMoodRating);
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
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        Log.d(TAG,"onMapReady");
        // Add a marker in start and stop of run
        if(latLngList.size()>0) {
            mMap.addMarker(new MarkerOptions()
                    .position(latLngList.get(0))
                    .title("Start of Run"));
            Log.d(TAG, latLngList.get(0).toString());

            mMap.addMarker(new MarkerOptions()
                    .position(latLngList.get(latLngList.size() - 1))
                    .title("End of Run"));

            Log.d(TAG, latLngList.get(latLngList.size() - 1).toString());

            //Map route of the run
            PolylineOptions polylineOptions = new PolylineOptions();
            polylineOptions.addAll(latLngList);
            mMap.addPolyline(polylineOptions);
            LatLngBounds.Builder builder = new LatLngBounds.Builder();
            for (LatLng latLng : latLngList) {
                builder.include(latLng);
            }
            mMap.moveCamera(CameraUpdateFactory
                    .newCameraPosition
                            (new CameraPosition
                                    .Builder()
                                    .tilt(0)
                                    .target(builder.build().getCenter())
                                    .build()));

            mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(builder.build(), 100));
        }
    }


    public interface PostRunFragmentInterface{
        void exitPostRun(boolean save,Bundle postRunInfo);
    }
}