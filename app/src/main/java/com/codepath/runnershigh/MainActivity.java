package com.codepath.runnershigh;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.codepath.runnershigh.fragments.*;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity implements StartRunFragment.StartRunFragmentInterface, RunningFragment.RunningFragmentInterface {
    Context context;

    //Declaring bottom nav Bar
    BottomNavigationView bottomNavigationView;

    FrameLayout flContainer;


    HomeFragment homeFragment;
    StartRunFragment startRunFragment;
    HistoryFragment historyFragment;
    TrackMoodFragment trackMoodFragment;
    RunningFragment runningFragment;


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

        if(runningFragment==null)
            runningFragment=new RunningFragment();




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

                getSupportFragmentManager().beginTransaction().replace(R.id.flContainer,fragment).commit();
                return true;
            }
        });

        //Setting default selection
        bottomNavigationView.setSelectedItemId(R.id.itHome);


    }

    //////////////////////////////////////////////////////////////////////
    //                           INTERFACE
    //                        IMPLEMENTATIONS
    //////////////////////////////////////////////////////////////////////


    @Override
    public void hideNavBar() {
        bottomNavigationView.setVisibility(View.GONE);
    }

    @Override
    public void showNavBar() {
        bottomNavigationView.setVisibility(View.VISIBLE);

    }

    @Override
    public void openRunningFragment() {
        getSupportFragmentManager().beginTransaction().replace(R.id.flContainer,runningFragment).commit();
    }

    @Override
    public void runComplete(Bundle runStats) {
        Toast.makeText(context, "Quitter", Toast.LENGTH_SHORT).show();
        //TODO: navigate to post run fragment
        /*
        in post run fragment the user will see post run stats, and enter post run vibes
        the user will have the option to save the run, if they choose to do so then log run and
        navigate back to home fragment.
         */
    }
}