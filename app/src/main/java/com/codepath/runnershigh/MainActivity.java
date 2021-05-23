package com.codepath.runnershigh;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.codepath.runnershigh.dialogFragments.PreRunMoodDialogFragment;
import com.codepath.runnershigh.fragments.HistoryFragment;
import com.codepath.runnershigh.fragments.HomeFragment;
import com.codepath.runnershigh.fragments.PostRunFragment;
import com.codepath.runnershigh.fragments.ResourcesFragment;
import com.codepath.runnershigh.fragments.RunningFragment;
import com.codepath.runnershigh.fragments.SettingsFragment;
import com.codepath.runnershigh.fragments.StartRunFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/*
This MainActivity is the landing area after a successful sign in. From here, the user can access
a number of fragments through the BottomNavigation and ActionBar at the top. This activity implements
interfaces defined for these fragments in order to centralize the key methods involved with logging
runs.
 */

public class MainActivity extends AppCompatActivity implements
        RunningFragment.RunningFragmentInterface,
        StartRunFragment.StartRunFragmentInterface,
        PreRunMoodDialogFragment.PreRunMoodDialogFragmentInterface,
        PostRunFragment.PostRunFragmentInterface,
        SettingsFragment.SettingsFragmentInterface,
        ResourcesFragment.ResourcesFragmentInterface {

    //Bundle for storing new run info until submission
    Bundle newRunBundle;

    //String constants used as keys for Bundles
    public static final String NEW_RUN_PRE_MOOD ="newrunpremood",
            NEW_RUN_POST_MOOD ="newrunpostmood",
            NEW_RUN_TIME ="newruntime",
            NEW_RUN_DISTANCE="newrundistance",
            NEW_RUN_LAT_LIST="NEW_RUN_LAT_LIST",
            NEW_RUN_LNG_LIST="NEW_RUN_LNG_LIST",
            NEW_RUN_NOTE ="newrunnote",
            NEW_RUN_DATE ="newrundate",
            NEW_RUN_ROUTE="newrunroute",
            NEW_RUN_CALORIES="newruncalories",
            NEW_RUN_PRE_STRESS="newrunprestress",
            NEW_RUN_POST_STRESS="newrunpoststress";

    //Conversion factors
    public static double LBS_TO_KG = 0.4535924;
    public static double MI_TO_KM = 1.609344;

    //Constant codes for
    public static int DISTANCE_MILES = 122;
    public static int DISTANCE_KILOMETERS = 123;

    Context context;
    SharedPreferences prefs;

    //Declaring bottom nav Bar
    BottomNavigationView bottomNavigationView;

    FrameLayout flContainer;


    //Declaring Fragments
    HomeFragment homeFragment;
    StartRunFragment startRunFragment;
    HistoryFragment historyFragment;
    SettingsFragment settingsFragment;
    ResourcesFragment resourcesFragment;

    //Necessary to support profile pics
    public static CircleImageView Menu_Profile_Pic;
    public static ParseFile UserImage;
    public static Uri uri_after_pic_change=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.ic_running_at_finish_line);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setTitle("");
        context=this;

        flContainer=findViewById(R.id.flContainer);

        //Initializing Fragments
        if(homeFragment==null)
            homeFragment = new HomeFragment();
        if(startRunFragment==null)
            startRunFragment = new StartRunFragment();
        if(historyFragment==null)
            historyFragment = new HistoryFragment();
        if(settingsFragment==null)
            settingsFragment=new SettingsFragment();
        if(resourcesFragment==null)
            resourcesFragment=new ResourcesFragment();

        //Setting up BottomNavigation response to clicks
        bottomNavigationView=findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            Fragment fragment;
            switch(item.getItemId()){
                case R.id.itHistory:
                    fragment = new HistoryFragment();

                    break;
                case R.id.itHome:
                    fragment = new HomeFragment();

                    break;
                case R.id.itRun:
                    fragment = startRunFragment;        //new or no?


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
        });

        //Retrieve the shared preferences saved for user
        prefs = getSharedPreferences("settings", Context.MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor;

        //If units are not explicitly in kilometers, use units of miles
        if(prefs.getInt("units", DISTANCE_MILES) == DISTANCE_MILES) {
            prefsEditor = prefs.edit();
            prefsEditor.putInt("units", DISTANCE_MILES);
            prefsEditor.apply();
        }

        //Setting default selection
        bottomNavigationView.setSelectedItemId(R.id.itHome);
                                                                        //change above

    }

    //Set up settings icon in action bar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        ///////////////////////////////////////////////////////   loading profile pic into menu bar

        MenuItem menuitem=menu.findItem(R.id.icProfilePic);
        View view= MenuItemCompat.getActionView(menuitem);
        Menu_Profile_Pic=view.findViewById(R.id.menupic);

        ParseQuery<RunData> query=ParseQuery.getQuery(RunData.class);
        query.include(RunData.KEY_USER);
        query.whereEqualTo(RunData.KEY_USER, ParseUser.getCurrentUser());
        query.setLimit(1);
        query.addDescendingOrder(RunData.KEY_CREATED_AT);
        query.findInBackground((runs, e) -> {
            UserImage=null;


            if (e != null) {
                return;
            }
            if (runs.size()>0) {
                RunData TheRun = runs.get(0);
                UserImage = TheRun.getProfileImage();
            }

            if (UserImage != null) {
                Glide.with(getApplicationContext()).load(UserImage.getUrl()).into(Menu_Profile_Pic);
            }
            else
                Menu_Profile_Pic.setImageResource(R.drawable.trophy);
        });
//////////////////////////////////////////////////////////////////////////////////////////////////


        return super.onCreateOptionsMenu(menu);
    }

    //Handling clicks on icons in the top menu bar
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()) {
            case R.id.icSettings:
                openSettingsFragment();
                break;
            case R.id.icResources:
                openResourcesFragment();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }

        return true;
    }

    //Hides BottomNavigation for any fragment where it isn't necessary
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

    //Shows BottomNavigation again if currently hidden
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

    //Takes values from Bundle passed in and returns RunData object with appropriate fields
    public RunData runDataFromBundle(Bundle runBundle){
        RunData runData= new RunData();

        //Survey parameters
        runData.setPreRunMood(runBundle.getInt(NEW_RUN_PRE_MOOD));
        runData.setPostRunMood(runBundle.getInt(NEW_RUN_POST_MOOD));
        runData.setPreRunStress(runBundle.getInt(NEW_RUN_PRE_STRESS));
        runData.setPostRunStress(runBundle.getInt(NEW_RUN_POST_STRESS));

        //Run parameters
        runData.setRunTime(runBundle.getString(NEW_RUN_TIME));
        runData.setRunDistance(runBundle.getDouble(NEW_RUN_DISTANCE));
        runData.setRunNote(runBundle.getString(NEW_RUN_NOTE));
        runData.setRunDate(runBundle.getString(NEW_RUN_DATE));
        runData.setRunCalories(runBundle.getDouble(NEW_RUN_CALORIES));
        runData.setUser(ParseUser.getCurrentUser());
        runData.setRunLatList(runBundle.getStringArrayList(NEW_RUN_LAT_LIST));
        runData.setRunLngList(runBundle.getStringArrayList(NEW_RUN_LNG_LIST));

        //Profile picture check
        if (SettingsFragment.ProfilePicture!=null)
            runData.setProfileImage(SettingsFragment.ProfilePicture);

        return runData;
    }

    //Saves data wrapped in RunData object to Parse backend
    public void saveRunData(RunData runData){
        runData.saveInBackground(e -> {
            if(e!=null) {
                Toast.makeText(context, "Error Saving Run", Toast.LENGTH_SHORT).show();
            }
            else
                Toast.makeText(context, "Run Saved", Toast.LENGTH_SHORT).show();
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


    //Opens Resources Fragment when icon clicked
    @Override
    public void openResourcesFragment() {
        Fragment fragment = resourcesFragment;
        getSupportFragmentManager()
                .beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .replace(R.id.flContainer,fragment)
                .commit();
    }

    @Override
    public void runComplete(String runtime, double rundistance, double calories, List<LatLng> latLngList) {

        newRunBundle.putString(NEW_RUN_TIME,runtime);
        newRunBundle.putDouble(NEW_RUN_DISTANCE, rundistance);
        newRunBundle.putDouble(NEW_RUN_CALORIES, calories);
        newRunBundle.putParcelableArrayList(NEW_RUN_ROUTE, (ArrayList<? extends Parcelable>) latLngList);
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
    public void surveyCompleted(int preRunMood,int preRunStress) {      //new
        newRunBundle = new Bundle();
        newRunBundle.putInt(NEW_RUN_PRE_MOOD,preRunMood);
        newRunBundle.putInt(NEW_RUN_PRE_STRESS,preRunStress);       //new
        openRunningFragment();
    }


    //Post Run Fragment Interface
    @Override
    public void exitPostRun(boolean save, Bundle completeRunInfo) {
        if(save){
            saveRunData(runDataFromBundle(completeRunInfo));
        }
        showBottomNav();
        bottomNavigationView.setSelectedItemId(R.id.itHistory);
    }

    //Logs user out of Parse account, sends back to LoginActivity
    @Override
    public void logOut() {
        ParseUser.logOutInBackground(e -> {
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
        });
    }

    //Gets appropriate color for mood graph
    public static int getMoodColor(int score) {
        int graphcolor = 0;

        switch(score) {
            case 1:
                graphcolor= Color.RED;
                break;
            case 2:
                graphcolor=Color.rgb(255,165,0);
                break;
            case 3:
                graphcolor=Color.YELLOW;
                break;
            case 4:
                graphcolor=Color.rgb(173,255,47);
                break;
            case 5:
                graphcolor=Color.GREEN;
                break;
        }

        return graphcolor;
    }

    //Gets appropriate color for stress graph
    public static int getStressColor(int score) {
        int graphcolor = 0;

        switch(score) {
            case 10:
            case 9:
                graphcolor=Color.RED;
                break;
            case 8:
            case 7:
                graphcolor=Color.rgb(255,165,0);
                break;
            case 6:
            case 5:
                graphcolor=Color.YELLOW;
                break;
            case 4:
            case 3:
                graphcolor=Color.rgb(173,255,47);
                break;
            case 2:
            case 1:
                graphcolor=Color.GREEN;
                break;
        }

        return graphcolor;
    }
}