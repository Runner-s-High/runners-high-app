package com.codepath.runnershigh;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.codepath.runnershigh.fragments.ResultsFragment;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/*
This activity holds a detailed view of an individual run that was saved to the Parse backend.
The user can see their route on the map, statistics about their run, and graphs of their pre- and post-
mood/stress surveys.
 */
public class MoreInfoActivity extends AppCompatActivity implements OnMapReadyCallback {
    public static final String TAG = MoreInfoActivity.class.getCanonicalName();
    //Layout element references
    TextView tvDateMI, tvUserMI;
    MapView mvMoreInfo;
    GoogleMap mMap;
    Button GoBackButton;

    RunData therun;
    String userString;

    List<LatLng> latLngList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more_info);
        Objects.requireNonNull(getSupportActionBar()).hide();

        GoBackButton=findViewById(R.id.nextbutton);
        tvDateMI = findViewById(R.id.tvDateMI);
        tvUserMI = findViewById(R.id.tvUserMI);
        mvMoreInfo = findViewById(R.id.mvMoreInfo);

        //Call onCreate for MapView
        if(mvMoreInfo != null) {
            mvMoreInfo.onCreate(savedInstanceState);
            mvMoreInfo.getMapAsync(this);
        }

        //Get the Parcelable run
        therun = Parcels.unwrap(getIntent().getParcelableExtra("pizza"));
        userString = getIntent().getStringExtra("user");

        //Setting up the latLngList used in onMapReady
        List<String> lats = therun.getRunLatList();
        List<String> longs = therun.getRunLngList();

        latLngList = new ArrayList<>();

        if(lats != null && longs != null)
            for(int i = 0; i < lats.size(); ++i) {
                double lat = Double.parseDouble(lats.get(i));
                double lng = Double.parseDouble(longs.get(i));

                latLngList.add(new LatLng(lat, lng));
            }

        tvUserMI.setText(userString);
        tvDateMI.setText(therun.getRunDate());

        GoBackButton.setOnClickListener(v -> {

            Intent intent=new Intent();
            setResult(RESULT_OK,intent);
            finish();
        });

        Bundle args = setupArgs(new Bundle());
        ResultsFragment resultsFragment = new ResultsFragment();
        resultsFragment.setArguments(args);

        //Setup ResultsFragment used to hold ViewPager and TabLayout
        getSupportFragmentManager().beginTransaction()
                .setReorderingAllowed(true)
                .replace(R.id.fragmentPrimary, resultsFragment)
                .commit();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mvMoreInfo.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mvMoreInfo.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mvMoreInfo.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mvMoreInfo.onLowMemory();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        //Mark waypoints for beginning and end of run
        if (latLngList.size() > 0) {
            mMap.addMarker(new MarkerOptions()
                    .position(latLngList.get(0))
                    .title("Start of Run"));
            Log.d(TAG, latLngList.get(0).toString());

            mMap.addMarker(new MarkerOptions()
                    .position(latLngList.get(latLngList.size() - 1))
                    .title("End of Run"));

            Log.d(TAG, latLngList.get(latLngList.size() - 1).toString());

            //Create polyline to map the running route
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

    //Assigns key-value args for Bundle to give to ResultsFragment
    public Bundle setupArgs(Bundle args) {
        args.putString("time", therun.getRunTime());
        args.putDouble("distance", therun.getRunDistance());
        args.putDouble("calories", therun.getRunCalories());
        args.putString("note", therun.getRunNote());
        args.putInt("premood", therun.getPreRunMood());
        args.putInt("postmood", therun.getPostRunMood());
        args.putInt("prestress", therun.getPreRunStress());
        args.putInt("poststress", therun.getPostRunStress());

        return args;
    }
}