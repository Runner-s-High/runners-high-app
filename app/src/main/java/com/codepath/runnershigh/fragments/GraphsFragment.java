package com.codepath.runnershigh.fragments;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codepath.runnershigh.R;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class GraphsFragment extends Fragment {
    ArrayList<BarEntry> barEntryArrayList;
    ArrayList<String> RunLabels;

    ArrayList<BarEntry> StressbarEntryArrayList;
    ArrayList<String> StressLabels;

    BarChart mybarchart, mystresschart;

    public GraphsFragment() {
    }

    public static GraphsFragment newInstance(Bundle graphBundle) {
        GraphsFragment fragment = new GraphsFragment();

        fragment.setArguments(graphBundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_graphs, container, false);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mybarchart = view.findViewById(R.id.mybargraph);
        mystresschart = view.findViewById(R.id.mystressgraph);

        int prescore = getArguments().getInt("premood");
        int postscore = getArguments().getInt("postmood");
        int prestress = getArguments().getInt("prestress");
        int poststress = getArguments().getInt("poststress");

        setupBarGraph(prescore, postscore);
        setupStressGraph(prestress, poststress);
    }

    public void setupBarGraph(int prescore, int postscore) {
        barEntryArrayList = new ArrayList<>();
        RunLabels=new ArrayList<>();

        barEntryArrayList.add(new BarEntry(0,prescore));
        barEntryArrayList.add(new BarEntry(1,postscore));
        RunLabels.add("PRE-RUN");
        RunLabels.add("POST-RUN");
        BarDataSet barDataSet=new BarDataSet(barEntryArrayList,"Moods");

        barDataSet.setColors(getBarColor(prescore), getBarColor(postscore));

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
    }

    public void setupStressGraph(int prestress, int poststress) {
        StressLabels=new ArrayList<>();
        StressbarEntryArrayList = new ArrayList<>();
        StressbarEntryArrayList.add(new BarEntry(0,prestress));       //data goes here from cloud
        StressbarEntryArrayList.add(new BarEntry(1,poststress));
        StressLabels.add(" ");
        StressLabels.add(" ");

        BarDataSet barDataSet2=new BarDataSet(StressbarEntryArrayList,"STRESS LEVELS");
        BarData barData2=new BarData(barDataSet2);
        barData2.setDrawValues(false);
        barData2.setBarWidth(0.4f);
        barData2.setHighlightEnabled(false);

        mystresschart.setData(barData2);

        Description description2=mystresschart.getDescription();
        description2.setEnabled(false);
        Legend legend2=mystresschart.getLegend();
        legend2.setEnabled(false);
        YAxis leftAxis2 = mystresschart.getAxisLeft();
        YAxis rightAxis2 = mystresschart.getAxisRight();
        rightAxis2.setEnabled(false);

        leftAxis2.setAxisMaximum(10);
        leftAxis2.setAxisMinimum(0);
        leftAxis2.setLabelCount(10, true);

        leftAxis2.setDrawGridLines(false);
        leftAxis2.setDrawZeroLine(false);
        leftAxis2.setEnabled(false);

        XAxis xAxis2=mystresschart.getXAxis();
        xAxis2.setValueFormatter(new IndexAxisValueFormatter(StressLabels));
        xAxis2.setDrawGridLines(false);
        xAxis2.setDrawAxisLine(false);
        xAxis2.setGranularity(1f);
        xAxis2.setLabelCount(2);

        mystresschart.setDrawValueAboveBar(false);
        mystresschart.setDrawGridBackground(true);
        mystresschart.animateY(1000);
        mystresschart.invalidate();

        barDataSet2.setColors(getStressColor(prestress), getStressColor(poststress));
    }

    public int getBarColor(int score) {
        int graphcolor = 0;

        switch(score) {
            case 1:
                graphcolor=Color.RED;
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

    public int getStressColor(int score) {
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