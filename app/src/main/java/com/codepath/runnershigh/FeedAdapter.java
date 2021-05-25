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
import com.parse.ParseException;
import com.parse.ParseUser;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

//TODO: Possibly make fragment to hold everything except User string or Date
//The adapter class which works with the RecyclerView in FeedFragment
public class FeedAdapter extends RecyclerView.Adapter<FeedAdapter.ViewHolder> {
    Context context;
    List<RunData> feed;

    SharedPreferences prefs;

    public FeedAdapter(Context context, List<RunData> feed) {
        this.context = context;
        this.feed = feed;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.feedrun, parent, false);
        prefs = context.getSharedPreferences("settings", Context.MODE_PRIVATE);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FeedAdapter.ViewHolder holder, int position) {
        RunData run = feed.get(position);
        holder.bind(run);
    }

    @Override
    public int getItemCount() {
        return feed.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        //Layout element references
        BarChart bargraphFeed, stressgraphFeed;
        TextView tvUserFeed, tvRelativeTimeFeed, tvTimeRVFeed, tvDistanceRVFeed, tvCaloriesRVFeed, tvDistanceTitleRVFeed;
        Button infobuttonFeed;

        ArrayList<BarEntry> barEntryArrayList;
        ArrayList<String> RunLabels;
        ArrayList<BarEntry> StressbarEntryArrayList;
        ArrayList<String> StressLabels;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvUserFeed = itemView.findViewById(R.id.tvUserFeed);
            tvRelativeTimeFeed = itemView.findViewById(R.id.tvRelativeTimeFeed);
            tvTimeRVFeed = itemView.findViewById(R.id.tvTimeRVFeed);
            tvDistanceRVFeed = itemView.findViewById(R.id.tvDistanceRVFeed);
            tvCaloriesRVFeed = itemView.findViewById(R.id.tvCaloriesRVFeed);
            tvDistanceTitleRVFeed = itemView.findViewById(R.id.tvDistanceTitleRVFeed);
            infobuttonFeed = itemView.findViewById(R.id.infobuttonFeed);
            bargraphFeed = itemView.findViewById(R.id.thebargraphFeed);
            stressgraphFeed = itemView.findViewById(R.id.mystressgraphFeed);
        }

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

            //Setting up proper strings for relative date of run and username
            String userString;
            String dateString;

            ParseUser runUser = run.getUser();
            try {
                runUser = runUser.fetchIfNeeded();
            } catch (ParseException e) {
                e.printStackTrace();
            }
            userString = runUser.getUsername();

            Calendar runDate = Calendar.getInstance();
            Calendar present = Calendar.getInstance();
            runDate.setTime(run.getDateObject());
            int presentDOY = present.get(Calendar.DAY_OF_YEAR);
            int runDOY = runDate.get(Calendar.DAY_OF_YEAR);
            int presentYear = present.get(Calendar.YEAR);
            int runYear = runDate.get(Calendar.YEAR);

            //Defining appropriate relative date strings
            if(presentDOY > runDOY)
                if(presentYear == runYear)
                    dateString = String.format(Locale.ENGLISH, "%dd ago", presentDOY - runDOY);
                else
                    dateString = String.format(Locale.ENGLISH, "%dy ago", presentYear - runYear);
            else if(presentDOY < runDOY)
                if(presentYear == runYear + 1)
                    dateString = String.format(Locale.ENGLISH, "%dd ago", presentDOY + (365 - runDOY));
                else
                    dateString = String.format(Locale.ENGLISH, "%dy ago", presentYear - runYear - 1);
            else
                if(presentYear == runYear)
                    dateString = "Today";
                else
                    dateString = String.format(Locale.ENGLISH, "%dy ago", presentYear - runYear);

            infobuttonFeed.setOnClickListener(v -> {
                Intent intent = new Intent(context, MoreInfoActivity.class);
                intent.putExtra("pizza", Parcels.wrap(run));
                intent.putExtra("user", userString);
                context.startActivity(intent);
            });

            //Initializing fields with data from run
            tvUserFeed.setText(userString);
            tvRelativeTimeFeed.setText(dateString);
            tvTimeRVFeed.setText(run.getRunTime());
            tvDistanceTitleRVFeed.setText(String.format("%s (%s)", context.getString(R.string.distance_label), units));
            tvDistanceRVFeed.setText(String.format(Locale.ENGLISH, "%.2f", run.getRunDistance() * multiplier));
            tvCaloriesRVFeed.setText(String.format(Locale.ENGLISH, "%.1f", run.getRunCalories()));

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
            bargraphFeed.setData(barData);

            Description description=bargraphFeed.getDescription();
            description.setEnabled(false);
            Legend legend=bargraphFeed.getLegend();
            legend.setEnabled(false);
            YAxis leftAxis = bargraphFeed.getAxisLeft();
            YAxis rightAxis = bargraphFeed.getAxisRight();
            rightAxis.setEnabled(false);

            leftAxis.setAxisMaximum(5);
            leftAxis.setAxisMinimum(0);
            leftAxis.setLabelCount(5, true);

            leftAxis.setDrawGridLines(false);
            leftAxis.setDrawZeroLine(true);
            leftAxis.setEnabled(false);

            XAxis xAxis=bargraphFeed.getXAxis();
            xAxis.setValueFormatter(new IndexAxisValueFormatter(RunLabels));
            xAxis.setDrawGridLines(false);
            xAxis.setDrawAxisLine(false);
            xAxis.setGranularity(1f);
            xAxis.setLabelCount(2);

            bargraphFeed.setDrawValueAboveBar(false);
            bargraphFeed.setDrawGridBackground(true);
            bargraphFeed.animateY(1000);
            bargraphFeed.invalidate();

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

            stressgraphFeed.setData(barData2);

            Description description2=stressgraphFeed.getDescription();
            description2.setEnabled(false);
            Legend legend2=stressgraphFeed.getLegend();
            legend2.setEnabled(false);
            YAxis leftAxis2 = stressgraphFeed.getAxisLeft();
            YAxis rightAxis2 = stressgraphFeed.getAxisRight();
            rightAxis2.setEnabled(false);

            leftAxis2.setAxisMaximum(10);
            leftAxis2.setAxisMinimum(0);
            leftAxis2.setLabelCount(10, true);

            leftAxis2.setDrawGridLines(false);
            leftAxis2.setDrawZeroLine(false);
            leftAxis2.setEnabled(false);

            XAxis xAxis2=stressgraphFeed.getXAxis();
            xAxis2.setValueFormatter(new IndexAxisValueFormatter(StressLabels));
            xAxis2.setDrawGridLines(false);
            xAxis2.setDrawAxisLine(false);
            xAxis2.setGranularity(1f);
            xAxis2.setLabelCount(2);

            stressgraphFeed.setDrawValueAboveBar(false);
            stressgraphFeed.setDrawGridBackground(true);
            stressgraphFeed.animateY(1000);
            stressgraphFeed.invalidate();

            barDataSet2.setColors(MainActivity.getStressColor(prestress), MainActivity.getStressColor(poststress));
        }
    }
}
