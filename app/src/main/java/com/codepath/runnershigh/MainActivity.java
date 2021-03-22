package com.codepath.runnershigh;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.codepath.runnershigh.dialogFragments.PreRunMoodDialogFragment;
import com.codepath.runnershigh.fragments.*;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class MainActivity extends AppCompatActivity implements
        StartRunFragment.StartRunFragmentInterface,
        RunningFragment.RunningFragmentInterface,
        HomeFragment.HomeFragmentInterface,
        PreRunMoodDialogFragment.PreRunMoodDialogFragmentInterface,
        PostRunFragment.PostRunFragmentInterface {

    //Mood mainPreRunMood;



    Context context;

    //Declaring bottom nav Bar
    BottomNavigationView bottomNavigationView;

    FrameLayout flContainer;


    //Declaring Fragments
    HomeFragment homeFragment;
    StartRunFragment startRunFragment;
    HistoryFragment historyFragment;
    TrackMoodFragment trackMoodFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context=this;

        flContainer=findViewById(R.id.flContainer);

        //Initializing Fragments
        if(homeFragment==null)
            homeFragment = new HomeFragment();

        if(trackMoodFragment==null)
            trackMoodFragment = new TrackMoodFragment();

        if(startRunFragment==null)
            startRunFragment = new StartRunFragment();

        if(historyFragment==null)
            historyFragment=new HistoryFragment();






        bottomNavigationView=findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragment;
                switch(item.getItemId()){
                    case R.id.itHistory:
                        fragment = historyFragment;
                        Toast.makeText(context, "Moving To History Fragment", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.itHome:
                        fragment = homeFragment;
                        Toast.makeText(context, "Moving to Home Fragment", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.itRun:
                        fragment = startRunFragment;
                        Toast.makeText(context, "Moving to Run Fragment", Toast.LENGTH_SHORT).show();

                        break;
                    case R.id.itTrackMood:
                        fragment = trackMoodFragment;
                        Toast.makeText(context, "Moving to TackMood Fragment", Toast.LENGTH_SHORT).show();
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

    //onClick method for settings icon
    public void openSettings(MenuItem m) {
        startActivity(new Intent(this, SettingsActivity.class));
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
    public void runComplete(Bundle runStats) {
        //Todo: runComplete uses RunStats object
    //public void runComplete(RunStats runStats) {
        //TODO: navigate to post run fragment
        //PostRunFragment postRunFragment = PostRunFragment.newInstance(mainPreRunMood, runStats);
        PostRunFragment postRunFragment = new PostRunFragment();
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
    public void surveyCompleted(Bundle preRunMood) {
        //Todo: change param to Mood object
    //public void surveyCompleted(Mood preRunMood) {
        //todo: Store prerun mood, access later @ post run screen
        //ex. mainPreRunMood = preRunMood;
        //preRunMood could be continually passed between all the stages but I think it would just be
        //easier to keep it as a class variable in MainActivity and then access it later
        openRunningFragment();
    }


    //Post Run Fragment Interface
    @Override
    public void exitPostRun() {
        showBottomNav();
        bottomNavigationView.setSelectedItemId(R.id.itHistory);
    }

}