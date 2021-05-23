package com.codepath.runnershigh.dialogFragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.codepath.runnershigh.R;

//Todo: Copy Greg's code for preMood collection to here

/*
This fragment is shown before the run begins to get the pre-run survey from the user on their mood and
stress level. After completion, the run will begin.
 */
public class PreRunMoodDialogFragment extends DialogFragment {
    PreRunMoodDialogFragmentInterface preRunMoodDialogFragmentInterface;
    Button btSubmit;
    ImageButton IB1,IB2,IB3,IB4,IB5;
    boolean moodSet=false;
    int preRunMoodRating;
    int preRunStressRating;

    SeekBar seekbar;

    public PreRunMoodDialogFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        preRunStressRating=5;
        btSubmit = view.findViewById(R.id.btSubmit);
        IB1=view.findViewById(R.id.IB1);
        IB2=view.findViewById(R.id.IB2);
        IB3=view.findViewById(R.id.IB3);
        IB4=view.findViewById(R.id.IB4);
        IB5=view.findViewById(R.id.IB5);
        seekbar=view.findViewById(R.id.seekBar);

        IB1.setOnClickListener(moodBtnListener);
        IB2.setOnClickListener(moodBtnListener);
        IB3.setOnClickListener(moodBtnListener);
        IB4.setOnClickListener(moodBtnListener);
        IB5.setOnClickListener(moodBtnListener);

        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                preRunStressRating=progress;
                //Toast.makeText(getApplicationContext(), String.valueOf(progress),Toast.LENGTH_LONG).show();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        btSubmit.setOnClickListener(v -> {
            if(moodSet) {
                preRunMoodDialogFragmentInterface.surveyCompleted(preRunMoodRating,preRunStressRating);
                dismiss();
            }else{
                Toast.makeText(getActivity(), "Must Select Mood", Toast.LENGTH_SHORT).show();
            }
        });
    }

    View.OnClickListener moodBtnListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.IB1:
                    preRunMoodRating=1;
                    break;
                case R.id.IB2:
                    preRunMoodRating=2;
                    break;
                case R.id.IB3:
                    preRunMoodRating=3;
                    break;
                case R.id.IB4:
                    preRunMoodRating=4;
                    break;
                case R.id.IB5:
                    preRunMoodRating=5;
            }
            unselectMoodButtons();
            v.setSelected(true);
            moodSet = true;
        }
    };

    //Toggles off all mood buttons to ensure a maximum of one button is selected at a time
    public void unselectMoodButtons(){
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
        void surveyCompleted(int preMoodScore, int preRunStressRating);

        //Todo: change bundle mood to mood class object
        //public void surveyCompleted(Mood preRunMood);
    }
}