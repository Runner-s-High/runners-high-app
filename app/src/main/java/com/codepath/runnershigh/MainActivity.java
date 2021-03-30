package com.codepath.runnershigh;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.codepath.runnershigh.dialogFragments.PreRunMoodDialogFragment;
import com.codepath.runnershigh.fragments.HistoryFragment;
import com.codepath.runnershigh.fragments.HomeFragment;
import com.codepath.runnershigh.fragments.PostRunFragment;
import com.codepath.runnershigh.fragments.RunningFragment;
import com.codepath.runnershigh.fragments.SettingsFragment;
import com.codepath.runnershigh.fragments.StartRunFragment;
import com.codepath.runnershigh.fragments.TrackMoodFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.parse.LogOutCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements
        StartRunFragment.StartRunFragmentInterface,
        RunningFragment.RunningFragmentInterface,
        HomeFragment.HomeFragmentInterface,
        PreRunMoodDialogFragment.PreRunMoodDialogFragmentInterface,
        PostRunFragment.PostRunFragmentInterface,
        SettingsFragment.SettingsFragmentInterface {

    //Bundle for storing new run info until submission
    Bundle newRunBundle;
    public static final String NEW_RUN_PRE_MOOD ="newrunpremood",
            NEW_RUN_POST_MOOD ="newrunpostmood",
            NEW_RUN_TIME ="newruntime",
            NEW_RUN_DISTANCE="newrundistance",
            NEW_RUN_LATLNG_LIST="NEW_RUN_LATLNG_LIST",
            NEW_RUN_LAT_LIST="NEW_RUN_LAT_LIST",
            NEW_RUN_LNG_LIST="NEW_RUN_LNG_LIST",
            NEW_RUN_NOTE ="newrunnote";



    Context context;

    //Declaring bottom nav Bar
    BottomNavigationView bottomNavigationView;

    FrameLayout flContainer;


    //Declaring Fragments
    HomeFragment homeFragment;
    StartRunFragment startRunFragment;
    HistoryFragment historyFragment;
    TrackMoodFragment trackMoodFragment;
    SettingsFragment settingsFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.logo);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        context=this;

        flContainer=findViewById(R.id.flContainer);

        //Initializing Fragments
        if(homeFragment==null)
            homeFragment = new HomeFragment();

        if(trackMoodFragment==null)
            trackMoodFragment = new TrackMoodFragment();

        if(startRunFragment==null)
            startRunFragment = new StartRunFragment();


        if(settingsFragment==null)
            settingsFragment=new SettingsFragment();






        bottomNavigationView=findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragment;
                switch(item.getItemId()){
                    case R.id.itHistory:
                        fragment = new HistoryFragment();

                        break;
                    case R.id.itHome:
                        fragment = new HomeFragment();

                        break;
                    case R.id.itRun:
                        fragment = startRunFragment;


                        break;
                    case R.id.itTrackMood:
                        fragment = trackMoodFragment;
                        break;
                    default:
                        throw new IllegalStateException("Unexpected value: " + item.getItemId());
                }
                getSupportFragmentManager()
                        .beginTransaction()
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                        .replace(R.id.flContainer,fragment)
                        .commit();
                return true;
            }
        });



        //Setting default selection
        bottomNavigationView.setSelectedItemId(R.id.itHome);


    }

    //Set up settings icon in action bar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()) {
            case R.id.icSettings:
                openSettingsFragment();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }

        return true;
    }

    public void hideBottomNav(){
        bottomNavigationView.animate()
                .translationY(bottomNavigationView.getHeight())
                .alpha(0.0f)
                .setDuration(300)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        bottomNavigationView.setVisibility(View.GONE);
                    }
                });
    }

    public void showBottomNav(){
        bottomNavigationView.animate()
                .translationY(0)
                .alpha(1.0f)
                .setDuration(300)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        bottomNavigationView.setVisibility(View.VISIBLE);
                    }
                });
    }

    public RunData runDataFromBundle(Bundle runBundle){
        RunData runData= new RunData();
        runData.setPreRunMood(runBundle.getInt(NEW_RUN_PRE_MOOD));

        runData.setPostRunMood(runBundle.getInt(NEW_RUN_POST_MOOD));

        runData.setRunTime(runBundle.getString(NEW_RUN_TIME));

        runData.setUser(ParseUser.getCurrentUser());
        return runData;
    }

    public void saveRunData(RunData runData){
        runData.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if(e!=null)
                    Toast.makeText(context, "Error Saving Run", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(context, "Run Saved", Toast.LENGTH_SHORT).show();
            }
        });
    }

    //////////////////////////////////////////////////////////////////////
    //                           INTERFACE
    //                        IMPLEMENTATIONS
    //////////////////////////////////////////////////////////////////////



    @Override
    public void openRunningFragment() {
        hideBottomNav();
        RunningFragment runningFragment = new RunningFragment();
        getSupportFragmentManager()
                .beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .replace(R.id.flContainer,runningFragment)
                .commit();
    }

    @Override
    public void openSettingsFragment() {
        Fragment fragment = settingsFragment;
        getSupportFragmentManager()
                .beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .replace(R.id.flContainer,fragment)
                .commit();
    }

    @Override
    public void runComplete(String runtime,String distance,List<LatLng> latLngList) {
        newRunBundle.putString(NEW_RUN_TIME,runtime);
        newRunBundle.putString(NEW_RUN_DISTANCE,distance);
/*
        ArrayList<Double> latList= new ArrayList<>();
        ArrayList<Double> lngList= new ArrayList<>();
        for(int i = 0;i < latLngList.size();i++){
            latList.add(latLngList.get(i).latitude);
            lngList.add(latLngList.get(i).longitude);
        }
        
 */

        newRunBundle.putParcelableArrayList(NEW_RUN_LATLNG_LIST, (ArrayList<? extends Parcelable>) latLngList);

        PostRunFragment postRunFragment = PostRunFragment.newInstance(newRunBundle);
        getSupportFragmentManager()
                .beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .replace(R.id.flContainer,postRunFragment)
                .commit();
    }

    @Override
    public void startRun() {
        bottomNavigationView.setSelectedItemId(R.id.itRun);
    }

    @Override
    public void surveyCompleted(int preRunMood) {
        newRunBundle = new Bundle();
        newRunBundle.putInt(NEW_RUN_PRE_MOOD,preRunMood);
        openRunningFragment();
    }


    //Post Run Fragment Interface
    @Override
    public void exitPostRun(boolean save, Bundle completeRunInfo) {
        if(save){
            saveRunData(runDataFromBundle(completeRunInfo));
            //Todo: Make a new RunData object with all the info from the complete run and upload it

        }
        showBottomNav();
        bottomNavigationView.setSelectedItemId(R.id.itHistory);
    }

    //Logs user out of Parse account, sends back to LoginActivity
    @Override
    public void logOut() {
        ParseUser.logOutInBackground(new LogOutCallback() {
            @Override
            public void done(ParseException e) {
                if(e == null) {
                    Log.i(SettingsFragment.TAG, "Successfully logged out Parse User");
                    Toast.makeText(MainActivity.this, "Logging out...", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                    finish();
                }
                else {
                    Log.e(SettingsFragment.TAG, e.getMessage());
                    Toast.makeText(MainActivity.this, "Error logging out", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}