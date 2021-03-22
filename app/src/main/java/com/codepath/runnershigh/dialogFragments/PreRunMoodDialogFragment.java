package com.codepath.runnershigh.dialogFragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;


import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.codepath.runnershigh.R;


//Todo: Copy Greg's code for preMood collection to here
public class PreRunMoodDialogFragment extends DialogFragment {
    PreRunMoodDialogFragmentInterface preRunMoodDialogFragmentInterface;
    Button btSubmit;



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

        btSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Todo: collect info from views and create mood object, pass mood object as param
                preRunMoodDialogFragmentInterface.surveyCompleted(new Bundle());
                //preRunMoodDialogFragmentInterface.surveyCompleted(preRunMood);


                dismiss();
            }
        });

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