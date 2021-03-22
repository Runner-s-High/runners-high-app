package com.codepath.runnershigh;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.codepath.runnershigh.fragments.*;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class MainActivity extends AppCompatActivity {
    Context context;

    //Declaring bottom nav Bar
    BottomNavigationView bottomNavigationView;

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

        //Initializing Fragments
        if(homeFragment==null)
            homeFragment = new HomeFragment();

        if(trackMoodFragment==null)
            trackMoodFragment = new TrackMoodFragment();

        if(startRunFragment==null)
            startRunFragment = new StartRunFragment();

        if(historyFragment==null)
            historyFragment=new HistoryFragment();
        //test
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
}