package com.codepath.runnershigh;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.codepath.runnershigh.fragments.PostRunFragment;
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
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import org.parceler.Parcels;

import java.util.List;

//TODO: Implement a viewpager to switch between stats and graph, rather than ScrollView

public class MoreInfoActivity extends AppCompatActivity implements OnMapReadyCallback {
    public static final String TAG = MoreInfoActivity.class.getCanonicalName();
    TextView tvDateMI;
    MapView mvMoreInfo;
    GoogleMap mMap;

    Button GoBackButton;
    RunData therun;

    List<LatLng> latLngList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more_info);
        getSupportActionBar().hide();

        GoBackButton=findViewById(R.id.nextbutton);

        tvDateMI = findViewById(R.id.tvDateMI);
        mvMoreInfo = findViewById(R.id.mvMoreInfo);

        mvMoreInfo.onCreate(savedInstanceState);

        therun = Parcels.unwrap(getIntent().getParcelableExtra("pizza"));

        tvDateMI.setText(therun.getRunDate());

        GoBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent();
                setResult(RESULT_OK,intent);
                finish();
            }
        });

        Bundle args = setupArgs(new Bundle());

//        args.putParcelable("run", getIntent().getParcelableExtra("pizza"));

        ResultsFragment resultsFragment = new ResultsFragment();
        resultsFragment.setArguments(args);

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

        if (latLngList.size() > 0) {
            mMap.addMarker(new MarkerOptions()
                    .position(latLngList.get(0))
                    .title("Start of Run"));
            Log.d(TAG, latLngList.get(0).toString());

            mMap.addMarker(new MarkerOptions()
                    .position(latLngList.get(latLngList.size() - 1))
                    .title("End of Run"));

            Log.d(TAG, latLngList.get(latLngList.size() - 1).toString());

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