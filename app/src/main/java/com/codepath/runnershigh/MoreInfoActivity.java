package com.codepath.runnershigh;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;

import org.parceler.Parcels;

import java.util.ArrayList;

public class MoreInfoActivity extends AppCompatActivity {

    ImageView preMood;
    ImageView postMood;
    TextView FinishMessage, tvTimeMI, tvDistanceMI, tvCaloriesMI, tvDateMI, tvNoteMI;

    Button GoBackButton;
    RunData therun;

    BarChart mybarchart;
    ArrayList<BarEntry> barEntryArrayList;
    ArrayList<String> RunLabels;


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
        FinishMessage=findViewById(R.id.tvFinish);
        tvTimeMI = findViewById(R.id.tvTimeMI);
        tvDistanceMI = findViewById(R.id.tvDistanceMI);
        tvCaloriesMI = findViewById(R.id.tvCaloriesMI);
        tvDateMI = findViewById(R.id.tvDateMI);
        tvNoteMI = findViewById(R.id.tvNoteMI);

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


        SetMessage(prescore,postscore);     //Greg- These functions were created and defined below
        SetFaces(prescore, postscore,barDataSet);      //

       // barDataSet.setColors(Color.RED,Color.GREEN);

        BarData barData=new BarData(barDataSet);
        barData.setDrawValues(false);
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

    public void SetFaces(int prescore,int postscore,BarDataSet bardataset){
        int leftgraphcolor = 0;
        int rightgraphcolor=0;

        switch(prescore) {
            case 1:
                preMood.setImageResource(R.drawable.mood1);
                leftgraphcolor=Color.RED;
                break;
            case 2:
                preMood.setImageResource(R.drawable.mood2);
                leftgraphcolor=Color.rgb(255,165,0);
                break;
            case 3:
                preMood.setImageResource(R.drawable.mood3);
                leftgraphcolor=Color.YELLOW;
                break;
            case 4:
                preMood.setImageResource(R.drawable.mood4);
                leftgraphcolor=Color.rgb(173,255,47);
                break;
            case 5:
                preMood.setImageResource(R.drawable.mood5);
                leftgraphcolor=Color.GREEN;
                break;
        }

        switch(postscore) {
            case 1:
                postMood.setImageResource(R.drawable.mood1);
                rightgraphcolor=Color.RED;
                break;
            case 2:
                postMood.setImageResource(R.drawable.mood2);
                rightgraphcolor=Color.rgb(255,165,0);
                break;
            case 3:
                postMood.setImageResource(R.drawable.mood3);
                rightgraphcolor=Color.YELLOW;
                break;
            case 4:
                postMood.setImageResource(R.drawable.mood4);
                rightgraphcolor=Color.rgb(173,255,47);
                break;
            case 5:
                postMood.setImageResource(R.drawable.mood5);
                rightgraphcolor=Color.GREEN;
                break;
        }

        bardataset.setColors(leftgraphcolor,rightgraphcolor);
    }

    public void SetMessage(int prescore,int postscore){
        int result=postscore-prescore;

        FinishMessage.setText("IMPROVEMENT");

        switch(result) {
            case 1:
                FinishMessage.setText("Note:\n\nYour Mood Improved by a score of 1! Way to Go!");
                break;
            case 2:
                FinishMessage.setText("Note:\n\nYour Mood Improved by a score of 2! Nice Work!");
                break;
            case 3:
                FinishMessage.setText("Note:\n\nYour Mood Improved by a score of 3! Nice Job!");
                break;
            case 4:
                FinishMessage.setText("Note:\n\nYour Mood Improved by a score of 4! Way to Go!");
                break;
            default:
                FinishMessage.setText("Note:\n\nMood didn't improve this time. Don't give up!");
        }
    }

}