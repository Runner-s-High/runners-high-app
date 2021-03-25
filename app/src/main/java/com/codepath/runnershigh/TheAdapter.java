package com.codepath.runnershigh;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.RelativeLayout;
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

import org.parceler.Parcels;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

    //layout resource file "eachrun" defines the layout for each view in recyclerview

public class TheAdapter extends RecyclerView.Adapter<TheAdapter.ViewHolder> {

    private Context context;
    private List<RunData> runs;


    public TheAdapter(Context context, List<RunData> runs) {
        this.context = context;
        this.runs = runs;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.eachrun, parent, false);
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

        TextView TheDate;

        Button infobutton;

        BarChart TheBarChart;
        ArrayList<BarEntry> barEntryArrayList;
        ArrayList<String> RunLabels;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

           TheDate=itemView.findViewById(R.id.tvDate);

            TheBarChart = itemView.findViewById(R.id.thebargraph);
            infobutton = itemView.findViewById(R.id.infobutton);

        }
                        //when clicking on an item in recyclerview
        public void bind(RunData run) {
            infobutton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, MoreInfoActivity.class);
                    intent.putExtra("pizza", Parcels.wrap(run));
                    context.startActivity(intent);
                }
            });

                            // Greg 3/24 removed scores and swapped with date
            TheDate.setText(run.getRunDate());

            String a= String.valueOf(run.getPreRunMood());
            String b=String.valueOf(run.getPostRunMood());


            RunLabels=new ArrayList<>();
            int prescore = run.getPreRunMood();
            int postscore = run.getPostRunMood();

            barEntryArrayList = new ArrayList<>();
            barEntryArrayList.add(new BarEntry(0,prescore));
            barEntryArrayList.add(new BarEntry(1,postscore));
            RunLabels.add("PRE-RUN");
            RunLabels.add("POST-RUN");
            BarDataSet barDataSet=new BarDataSet(barEntryArrayList,"Moods");

            BarData barData=new BarData(barDataSet);
            barData.setDrawValues(false);

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
            SetGraphColors(prescore,postscore,barDataSet);         //Greg-function defined below
        }

        public void SetGraphColors(int prescore,int postscore,BarDataSet bardataset){
            int leftgraphcolor,rightgraphcolor;
            leftgraphcolor=rightgraphcolor=0;

            if (prescore == 1) {

                leftgraphcolor= Color.RED;
            }
            else if (prescore == 2) {

                leftgraphcolor=Color.YELLOW;
            }
            else if (prescore == 3) {

                leftgraphcolor=Color.YELLOW;
            }
            else if (prescore == 4) {

                leftgraphcolor=Color.GREEN;
            }
            else if (prescore == 5) {

                leftgraphcolor=Color.GREEN;
            }


            if (postscore == 1) {

                rightgraphcolor=Color.RED;
            }
            else if (postscore == 2) {

                rightgraphcolor=Color.YELLOW;
            } else if (postscore == 3) {

                rightgraphcolor=Color.YELLOW;

            } else if (postscore == 4) {

                rightgraphcolor=Color.GREEN;
            }
            else if (postscore == 5) {

                rightgraphcolor=Color.GREEN;

            }
            bardataset.setColors(leftgraphcolor,rightgraphcolor);
        }

    }




}