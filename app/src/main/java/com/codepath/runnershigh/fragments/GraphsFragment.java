package com.codepath.runnershigh.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.codepath.runnershigh.MainActivity;
import com.codepath.runnershigh.R;
import com.codepath.runnershigh.models.RunData;
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

/*
Fragment used to represent graphs page in ResultsFragment in MoreInfoActivity. It contains both a
mood graph and a stress graph filled with the pre- and post-survey parameters
 */
public class GraphsFragment extends Fragment {
    //Layout element references
    BarChart mybarchart, mystresschart;

    RunData run;

    ArrayList<BarEntry> barEntryArrayList;
    ArrayList<String> RunLabels;
    ArrayList<BarEntry> StressbarEntryArrayList;
    ArrayList<String> StressLabels;

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_graphs, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mybarchart = view.findViewById(R.id.mybargraph);
        mystresschart = view.findViewById(R.id.mystressgraph);

        run = Parcels.unwrap(getArguments().getParcelable("run"));

        //Get values of pre/post mood and pre/post stress
        int prescore = run.getPreRunMood();
        int postscore = run.getPostRunMood();
        int prestress = run.getPreRunStress();
        int poststress = run.getPostRunStress();

        //Handle all graph parameters for display
        setupMoodGraph(prescore, postscore);
        setupStressGraph(prestress, poststress);
    }

    //Specifies parameters for the mood graph
    public void setupMoodGraph(int prescore, int postscore) {
        barEntryArrayList = new ArrayList<>();
        RunLabels=new ArrayList<>();

        barEntryArrayList.add(new BarEntry(0,prescore));
        barEntryArrayList.add(new BarEntry(1,postscore));
        RunLabels.add("PRE-RUN");
        RunLabels.add("POST-RUN");
        BarDataSet barDataSet=new BarDataSet(barEntryArrayList,"Moods");

        barDataSet.setColors(MainActivity.getMoodColor(prescore), MainActivity.getMoodColor(postscore));

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
        mybarchart.animateY(1000);      //makes bargraphs grow
        mybarchart.invalidate();

        //set to non scrollable
        mybarchart.setScrollContainer(false);
    }

    //Specifies parameters for the stress graph
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

        barDataSet2.setColors(MainActivity.getStressColor(prestress), MainActivity.getStressColor(poststress));
    }
}