package com.codepath.runnershigh;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.codepath.runnershigh.fragments.*;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    Context context;

    //Declaring bottom nav Bar
    BottomNavigationView bottomNavigationView;


    HomeFragment homeFragment;
    StartRunFragment startRunFragment;
    HistoryFragment historyFragment;
    TrackMoodFragment trackMoodFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context=this;

        //TODO: initialize fragment objects

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

                //TODO: Replace frameLayout with fragment
                getSupportFragmentManager().beginTransaction().replace(R.id.flContainer,fragment).commit();
                return true;
            }
        });

        //Setting default selection
        bottomNavigationView.setSelectedItemId(R.id.itHome);


    }
}