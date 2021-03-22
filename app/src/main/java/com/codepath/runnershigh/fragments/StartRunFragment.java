package com.codepath.runnershigh.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.codepath.runnershigh.R;


public class StartRunFragment extends Fragment{
    Button btnStart;
    StartRunFragmentInterface startRunFragmentInterface;


    public StartRunFragment() {
        // Required empty public constructor
    }

    public static StartRunFragment newInstance(String param1, String param2) {
        StartRunFragment fragment = new StartRunFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
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
        return inflater.inflate(R.layout.fragment_start_run, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        btnStart=view.findViewById(R.id.btnStart);

        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startRunFragmentInterface.hideNavBar();
                startRunFragmentInterface.openRunningFragment();
            }
        });
    }


    //Attaching the interface
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if(context instanceof StartRunFragmentInterface){
            startRunFragmentInterface=(StartRunFragmentInterface)context;
        }else{
            throw new RuntimeException(context.toString()+
                    "must implement StartRunFragmentInterface");
        }
    }

    //Creating Interface for fragment communication
    public interface StartRunFragmentInterface{
        public void hideNavBar();
        public void showNavBar();

        public void openRunningFragment();

    }
}