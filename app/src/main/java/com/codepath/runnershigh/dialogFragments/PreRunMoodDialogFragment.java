package com.codepath.runnershigh.dialogFragments;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;


import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.codepath.runnershigh.R;
import com.codepath.runnershigh.RunData;


//Todo: Copy Greg's code for preMood collection to here
public class PreRunMoodDialogFragment extends DialogFragment {
    PreRunMoodDialogFragmentInterface preRunMoodDialogFragmentInterface;
    Button btSubmit;
    ImageButton IB1,IB2,IB3,IB4,IB5;
    boolean moodSet=false;


    RunData runData;
    int preRunMoodRating;


    public PreRunMoodDialogFragment() {
        // Required empty public constructor
    }

    public static PreRunMoodDialogFragment newInstance() {
        PreRunMoodDialogFragment fragment = new PreRunMoodDialogFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_pre_run_mood_dialog, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //Finding Views
        btSubmit = view.findViewById(R.id.btSubmit);
        IB1=view.findViewById(R.id.IB1);
        IB2=view.findViewById(R.id.IB2);
        IB3=view.findViewById(R.id.IB3);
        IB4=view.findViewById(R.id.IB4);
        IB5=view.findViewById(R.id.IB5);

        IB1.setOnClickListener(moodBtnListener);
        IB2.setOnClickListener(moodBtnListener);
        IB3.setOnClickListener(moodBtnListener);
        IB4.setOnClickListener(moodBtnListener);
        IB5.setOnClickListener(moodBtnListener);


        btSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Todo: collect info from views and create mood object, pass mood object as param
                if(moodSet) {
                    preRunMoodDialogFragmentInterface.surveyCompleted(new Bundle());
                    //preRunMoodDialogFragmentInterface.surveyCompleted(preRunMood);
                    dismiss();
                }else{
                    Toast.makeText(getActivity(), "Must Select Mood", Toast.LENGTH_SHORT).show();
                }
            }
        });



    }
/*
    @Override
    public void onResume() {
        super.onResume();
        getDialog().getWindow().setLayout
                (ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }

 */

    View.OnClickListener moodBtnListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            runData=new RunData();
            switch (v.getId()) {
                case R.id.IB1:
                    runData.setPreRunMood(1);
                    break;
                case R.id.IB2:
                    runData.setPreRunMood(2);
                    break;
                case R.id.IB3:
                    runData.setPreRunMood(3);
                    break;
                case R.id.IB4:
                    runData.setPreRunMood(4);
                    break;
                case R.id.IB5:
                    runData.setPreRunMood(5);
                    break;
            }
            enableMoodBtns();
            v.setEnabled(false);
            v.setSelected(true);
            moodSet = true;
        }
    };

    public void enableMoodBtns(){
        IB1.setEnabled(true);
        IB2.setEnabled(true);
        IB3.setEnabled(true);
        IB4.setEnabled(true);
        IB5.setEnabled(true);
        IB1.setSelected(false);
        IB2.setSelected(false);
        IB3.setSelected(false);
        IB4.setSelected(false);
        IB5.setSelected(false);

    }



    //////////////////////////////////////////////////////////////////////
    //                           INTERFACE
    //                        IMPLEMENTATIONS
    //////////////////////////////////////////////////////////////////////

    //Attaching the interface
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if(context instanceof PreRunMoodDialogFragmentInterface){
            preRunMoodDialogFragmentInterface= (PreRunMoodDialogFragmentInterface) context;
        }else{
            throw new RuntimeException(context.toString()+
                    "must implement PreRunMoodDialogFragmentInterface");
        }
    }

    public interface PreRunMoodDialogFragmentInterface{
        public void surveyCompleted(Bundle mood);

        //Todo: change bundle mood to mood class object
        //public void surveyCompleted(Mood preRunMood);

    }
}