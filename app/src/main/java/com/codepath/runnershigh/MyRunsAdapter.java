package com.codepath.runnershigh;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.parse.ParseUser;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

//layout resource file "eachrun" defines the layout for each view in recyclerview
//Adapter for RecyclerView of past runs in HistoryFragment in MainActivity
public class MyRunsAdapter extends RecyclerView.Adapter<MyRunsAdapter.ViewHolder> {
    private final Context context;
    private final List<RunData> runs;

    SharedPreferences prefs;

    public MyRunsAdapter(Context context, List<RunData> runs) {
        this.context = context;
        this.runs = runs;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.eachrun, parent, false);
        prefs = context.getSharedPreferences("settings", Context.MODE_PRIVATE);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        RunData run = runs.get(position);
        holder.bind(run);
    }

    @Override
    public int getItemCount() {
        return runs.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        //Layout element references
        TextView TheDate, tvTimeRV, tvDistanceRV, tvCaloriesRV, tvDistanceTitleRV;
        Button infobutton;
        BarChart TheBarChart, StressBarChart;

        ArrayList<BarEntry> barEntryArrayList;
        ArrayList<String> RunLabels;
        ArrayList<BarEntry> StressbarEntryArrayList;
        ArrayList<String> StressLabels;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            TheDate=itemView.findViewById(R.id.tvDate);
            tvTimeRV=itemView.findViewById(R.id.tvTimeRV);
            tvDistanceRV=itemView.findViewById(R.id.tvDistanceRV);
            tvCaloriesRV=itemView.findViewById(R.id.tvCaloriesRV);
            tvDistanceTitleRV = itemView.findViewById(R.id.tvDistanceTitleRV);

            TheBarChart = itemView.findViewById(R.id.thebargraph);
            infobutton = itemView.findViewById(R.id.infobutton);
            StressBarChart=itemView.findViewById(R.id.mystressgraph);

        }

        //when clicking on an item in recyclerview
        public void bind(RunData run) {
            double multiplier;
            String units;

            //Set appropriate unit strings based on saved settings
            if (prefs.getInt("units", -1) == MainActivity.DISTANCE_KILOMETERS) {
                multiplier = MainActivity.MI_TO_KM;
                units = "KM";
            } else {
                multiplier = 1;
                units = "MI";
            }

            infobutton.setOnClickListener(v -> {
                Intent intent = new Intent(context, MoreInfoActivity.class);
                intent.putExtra("pizza", Parcels.wrap(run));
                intent.putExtra("user", ParseUser.getCurrentUser().getUsername());
                context.startActivity(intent);
            });

            //Initializing fields with data from run
            TheDate.setText(run.getRunDate());
            tvTimeRV.setText(run.getRunTime());
            tvDistanceTitleRV.setText(String.format("%s (%s)", context.getString(R.string.distance_label), units));
            tvDistanceRV.setText(String.format(Locale.ENGLISH,"%.2f", run.getRunDistance() * multiplier));
            tvCaloriesRV.setText(String.format(Locale.ENGLISH, "%.1f", run.getRunCalories()));

            int prescore = run.getPreRunMood();
            int postscore = run.getPostRunMood();
            int prestress = run.getPreRunStress();
            int poststress = run.getPostRunStress();

            setupMoodGraph(prescore, postscore);
            setupStressGraph(prestress, poststress);
        }

        public void setupMoodGraph(int prescore, int postscore) {
            barEntryArrayList = new ArrayList<>();
            RunLabels=new ArrayList<>();

            barEntryArrayList.add(new BarEntry(0,prescore));
            barEntryArrayList.add(new BarEntry(1,postscore));
            RunLabels.add("PRE-RUN");
            RunLabels.add("POST-RUN");
            BarDataSet barDataSet=new BarDataSet(barEntryArrayList,"Moods");

            BarData barData=new BarData(barDataSet);
            barData.setDrawValues(false);
            barData.setHighlightEnabled(false);
            TheBarChart.setData(barData);

            Description description=TheBarChart.getDescription();
            description.setEnabled(false);
            Legend legend=TheBarChart.getLegend();
            legend.setEnabled(false);
            YAxis leftAxis = TheBarChart.getAxisLeft();
            YAxis rightAxis = TheBarChart.getAxisRight();
            rightAxis.setEnabled(false);

            leftAxis.setAxisMaximum(5);
            leftAxis.setAxisMinimum(0);
            leftAxis.setLabelCount(5, true);

            leftAxis.setDrawGridLines(false);
            leftAxis.setDrawZeroLine(true);
            leftAxis.setEnabled(false);

            XAxis xAxis=TheBarChart.getXAxis();
            xAxis.setValueFormatter(new IndexAxisValueFormatter(RunLabels));
            xAxis.setDrawGridLines(false);
            xAxis.setDrawAxisLine(false);
            xAxis.setGranularity(1f);
            xAxis.setLabelCount(2);

            TheBarChart.setDrawValueAboveBar(false);
            TheBarChart.setDrawGridBackground(true);
            TheBarChart.animateY(1000);
            TheBarChart.invalidate();

            barDataSet.setColors(MainActivity.getMoodColor(prescore), MainActivity.getMoodColor(postscore));

        }

        public void setupStressGraph(int prestress, int poststress) {
            //stress bargraph
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

            StressBarChart.setData(barData2);

            Description description2=StressBarChart.getDescription();
            description2.setEnabled(false);
            Legend legend2=StressBarChart.getLegend();
            legend2.setEnabled(false);
            YAxis leftAxis2 = StressBarChart.getAxisLeft();
            YAxis rightAxis2 = StressBarChart.getAxisRight();
            rightAxis2.setEnabled(false);

            leftAxis2.setAxisMaximum(10);
            leftAxis2.setAxisMinimum(0);
            leftAxis2.setLabelCount(10, true);

            leftAxis2.setDrawGridLines(false);
            leftAxis2.setDrawZeroLine(false);
            leftAxis2.setEnabled(false);

            XAxis xAxis2=StressBarChart.getXAxis();
            xAxis2.setValueFormatter(new IndexAxisValueFormatter(StressLabels));
            xAxis2.setDrawGridLines(false);
            xAxis2.setDrawAxisLine(false);
            xAxis2.setGranularity(1f);
            xAxis2.setLabelCount(2);

            StressBarChart.setDrawValueAboveBar(false);
            StressBarChart.setDrawGridBackground(true);
            StressBarChart.animateY(1000);
            StressBarChart.invalidate();

            barDataSet2.setColors(MainActivity.getStressColor(prestress), MainActivity.getStressColor(poststress));
        }
    }
}