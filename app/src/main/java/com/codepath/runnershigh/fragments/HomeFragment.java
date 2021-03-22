package com.codepath.runnershigh.fragments;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextClock;
import android.widget.TextView;

import com.codepath.runnershigh.R;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Random;


public class HomeFragment extends Fragment {
    TextClock tcDate;
    TextClock tcTime;

    TextView tvQuote;

    Button btStart;

    HomeFragmentInterface homeFragmentInterface;

    public HomeFragment() {
        // Required empty public constructor
    }

    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
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
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tcDate = view.findViewById(R.id.tcDate);
        tcTime = view.findViewById(R.id.tcTime);
        tvQuote = view.findViewById(R.id.tvQuote);
        btStart = view.findViewById(R.id.btStart);

        //Set format for date and time
        tcDate.setFormat12Hour("EEE, MMM d, ''yy");
        tcTime.setFormat12Hour("h:mm a");

        //Get quotes from resources and set text view to random quote
        Resources resources = getResources();
        List<String> Quotes = Arrays.asList(resources.getStringArray(R.array.Quotes));
        Random random =new Random();
        tvQuote.setText(Quotes.get(
                random.nextInt(
                        Quotes.size())));

        //TODO: set up navigation to start run
        btStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                homeFragmentInterface.startRun();
            }
        });

        //TODO: set up most recent run

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if(context instanceof HomeFragmentInterface){
            homeFragmentInterface=(HomeFragmentInterface) context;
        }else{
            throw new RuntimeException(context.toString()+
                    "must implement HomeFragmentInterface");
        }
    }

    public interface HomeFragmentInterface{
        public void startRun();
    }


}