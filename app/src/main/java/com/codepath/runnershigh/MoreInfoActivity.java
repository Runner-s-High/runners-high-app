package com.codepath.runnershigh;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.runnershigh.fragments.PostRunFragment;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
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

public class MoreInfoActivity extends AppCompatActivity implements OnMapReadyCallback {
    public static final String TAG = MoreInfoActivity.class.getCanonicalName();

    ImageView preMood;
    ImageView postMood;
    TextView tvTimeMI, tvDistanceMI, tvCaloriesMI, tvDateMI, tvNoteMI;
    MapView mvMoreInfo;
    GoogleMap mMap;

    Button GoBackButton;
    RunData therun;

    BarChart mybarchart;
    ArrayList<BarEntry> barEntryArrayList;
    ArrayList<String> RunLabels;
    List<LatLng> latLngList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more_info);
        getSupportActionBar().hide();

        GoBackButton=findViewById(R.id.nextbutton);

        RunLabels=new ArrayList<>();

        preMood = findViewById(R.id.ivpremood);
        postMood = findViewById(R.id.ivpostmood);
        mybarchart = findViewById(R.id.mybargraph);
        tvTimeMI = findViewById(R.id.tvTimeMI);
        tvDistanceMI = findViewById(R.id.tvDistanceMI);
        tvCaloriesMI = findViewById(R.id.tvCaloriesMI);
        tvDateMI = findViewById(R.id.tvDateMI);
        tvNoteMI = findViewById(R.id.tvNoteMI);
        mvMoreInfo = findViewById(R.id.mvMoreInfo);

        mvMoreInfo.onCreate(savedInstanceState);

        therun = Parcels.unwrap(getIntent().getParcelableExtra("pizza"));
        int prescore = therun.getPreRunMood();
        int postscore = therun.getPostRunMood();

        tvTimeMI.setText(therun.getRunTime());
        tvDistanceMI.setText(String.format("%.2f", therun.getRunDistance()));
        tvCaloriesMI.setText(String.format("%.1f", therun.getRunCalories()));
        tvDateMI.setText(therun.getRunDate());
        tvNoteMI.setText(therun.getRunNote());

        barEntryArrayList = new ArrayList<>();

        barEntryArrayList.add(new BarEntry(0,prescore));
        barEntryArrayList.add(new BarEntry(1,postscore));
        RunLabels.add("PRE-RUN");
        RunLabels.add("POST-RUN");
        BarDataSet barDataSet=new BarDataSet(barEntryArrayList,"Moods");

        SetFaces(prescore, postscore,barDataSet);      //

        // barDataSet.setColors(Color.RED,Color.GREEN);

        BarData barData=new BarData(barDataSet);
        barData.setDrawValues(false);
        barData.setHighlightEnabled(false);
        mybarchart.setData(barData);

        Description description=mybarchart.getDescription();
        description.setEnabled(false);
        Legend legend=mybarchart.getLegend();
        legend.setEnabled(false);
        YAxis leftAxis = mybarchart.getAxisLeft();
        YAxis rightAxis = mybarchart.getAxisRight();
        rightAxis.setEnabled(false);

        leftAxis.setAxisMaximum(5);
        leftAxis.setAxisMinimum(0);
        leftAxis.setLabelCount(5, true);

        leftAxis.setDrawGridLines(false);
        leftAxis.setDrawZeroLine(true);
        leftAxis.setEnabled(false);

        XAxis xAxis=mybarchart.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(RunLabels));
        xAxis.setDrawGridLines(false);
        xAxis.setDrawAxisLine(false);
        xAxis.setGranularity(1f);
        xAxis.setLabelCount(2);

        mybarchart.setDrawValueAboveBar(false);
        mybarchart.setDrawGridBackground(true);
        mybarchart.animateY(2000);      //makes bargraphs grow
        mybarchart.invalidate();

        //set to non scrollable
        mybarchart.setScrollContainer(false);

        //SetBarGraphColors(barDataSet);          //Greg 3/24 -created function for changing colors of bar graph


        GoBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent();
                setResult(RESULT_OK,intent);
                finish();
            }
        });


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

    public void SetFaces(int prescore, int postscore, BarDataSet bardataset){
        int leftgraphcolor = 0;
        int rightgraphcolor=0;

        switch(prescore) {
            case 1:
                preMood.setImageResource(R.drawable.mood1);
                preMood.setColorFilter(Color.parseColor("#F44336"));
                leftgraphcolor=Color.RED;
                break;
            case 2:
                preMood.setImageResource(R.drawable.mood2);
                preMood.setColorFilter(Color.parseColor("#FF9800"));
                leftgraphcolor=Color.rgb(255,165,0);
                break;
            case 3:
                preMood.setImageResource(R.drawable.mood3);
                preMood.setColorFilter(Color.parseColor("#FFEB3B"));
                leftgraphcolor=Color.YELLOW;
                break;
            case 4:
                preMood.setImageResource(R.drawable.mood4);
                preMood.setColorFilter(Color.parseColor("#8BC34A"));
                leftgraphcolor=Color.rgb(173,255,47);
                break;
            case 5:
                preMood.setImageResource(R.drawable.mood5);
                preMood.setColorFilter(Color.parseColor("#4CAF50"));
                leftgraphcolor=Color.GREEN;
                break;
        }

        switch(postscore) {
            case 1:
                postMood.setImageResource(R.drawable.mood1);
                postMood.setColorFilter(Color.parseColor("#F44336"));
                rightgraphcolor=Color.RED;
                break;
            case 2:
                postMood.setImageResource(R.drawable.mood2);
                postMood.setColorFilter(Color.parseColor("#FF9800"));
                rightgraphcolor=Color.rgb(255,165,0);
                break;
            case 3:
                postMood.setImageResource(R.drawable.mood3);
                postMood.setColorFilter(Color.parseColor("#FFEB3B"));
                rightgraphcolor=Color.YELLOW;
                break;
            case 4:
                postMood.setImageResource(R.drawable.mood4);
                postMood.setColorFilter(Color.parseColor("#8BC34A"));
                rightgraphcolor=Color.rgb(173,255,47);
                break;
            case 5:
                postMood.setImageResource(R.drawable.mood5);
                postMood.setColorFilter(Color.parseColor("#4CAF50"));
                rightgraphcolor=Color.GREEN;
                break;
        }

        bardataset.setColors(leftgraphcolor,rightgraphcolor);
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
}