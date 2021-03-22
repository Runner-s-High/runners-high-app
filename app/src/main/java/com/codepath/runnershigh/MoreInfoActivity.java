package com.codepath.runnershigh;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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
    TextView FinishMessage;

    Button GoBackButton;
    RunData therun;

    BarChart mybarchart;
    ArrayList<BarEntry> barEntryArrayList;
    ArrayList<String> RunLabels;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more_info);

        GoBackButton=findViewById(R.id.nextbutton);

        RunLabels=new ArrayList<>();

        preMood = findViewById(R.id.ivpremood);
        postMood = findViewById(R.id.ivpostmood);
        mybarchart = findViewById(R.id.mybargraph);
        FinishMessage=findViewById(R.id.tvFinish);

        therun = Parcels.unwrap(getIntent().getParcelableExtra("pizza"));
        int prescore = therun.getPreRunMood();
        int postscore = therun.getPostRunMood();

        SetMessage(prescore,postscore);
        SetFaces(prescore, postscore);

        barEntryArrayList = new ArrayList<>();
        barEntryArrayList.add(new BarEntry(0,prescore));
        barEntryArrayList.add(new BarEntry(1,postscore));
        RunLabels.add("PRE-RUN");
        RunLabels.add("POST-RUN");
        BarDataSet barDataSet=new BarDataSet(barEntryArrayList,"Moods");

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

        GoBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent();
                setResult(RESULT_OK,intent);
                finish();
            }
        });


    }

    public void SetFaces(int prescore,int postscore){
        if (prescore == 1) {
            preMood.setImageResource(R.drawable.angriest);

        } else if (prescore == 2) {
            preMood.setImageResource(R.drawable.smallorange);
        } else if (prescore == 3) {
            preMood.setImageResource(R.drawable.mediumface);
        } else if (prescore == 4) {
            preMood.setImageResource(R.drawable.smalllightgreen);
        } else if (prescore == 5) {
            preMood.setImageResource(R.drawable.happiest);
        }


        if (postscore == 1) {
            postMood.setImageResource(R.drawable.angriest);
        } else if (postscore == 2) {
            postMood.setImageResource(R.drawable.smallorange);

        } else if (postscore == 3) {
            postMood.setImageResource(R.drawable.mediumface);

        } else if (postscore == 4) {
            postMood.setImageResource(R.drawable.smalllightgreen);

        } else if (postscore == 5) {
            postMood.setImageResource(R.drawable.happiest);

        }
    }

    public void SetMessage(int prescore,int postscore){
        int result=postscore-prescore;

        FinishMessage.setText("IMPROVEMENT");

        if (result==1)
            FinishMessage.setText("Note:\n\nYour Mood Improved by a score of 1! Way to Go!");
        else if (result==2)
            FinishMessage.setText("Note:\n\nYour Mood Improved by a score of 2! Nice Work!");
        else if (result==3)
            FinishMessage.setText("Note:\n\nYour Mood Improved by a score of 3! Nice Job!");
        else if (result==4)
            FinishMessage.setText("Note:\n\nYour Mood Improved by a score of 4! Way to Go!");
        else
            FinishMessage.setText("Note:\n\nMood didn't improve this time. Don't give up!");


    }

}