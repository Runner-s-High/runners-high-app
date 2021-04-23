package com.codepath.runnershigh.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Button;

import com.codepath.runnershigh.R;
import com.codepath.runnershigh.RunData;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import com.codepath.runnershigh.dialogFragments.PreRunMoodDialogFragment;

import java.text.DateFormat;
import java.util.Calendar;



public class StartRunFragment extends Fragment {


    Button btnStart;
    StartRunFragmentInterface startRunFragmentInterface;
    PreRunMoodDialogFragment moodSurvey;

    boolean completedMoodSurvey=false;

    ImageButton IB1,IB2,IB3,IB4,IB5;
    TextView message;
    int prerating,postrating,prerunflag,postrunflag;


    public StartRunFragment() {
        // Required empty public constructor
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        prerating=postrating=prerunflag=postrunflag=0;
        message=view.findViewById(R.id.message);
        IB1=view.findViewById(R.id.IB1);
        IB2=view.findViewById(R.id.IB2);
        IB3=view.findViewById(R.id.IB3);
        IB4=view.findViewById(R.id.IB4);
        IB5=view.findViewById(R.id.IB5);

        btnStart=view.findViewById(R.id.btSubmit);

        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (completedMoodSurvey) {
                    //Todo: pass mood object
                    //startRunFragmentInterface.openRunningFragment(preRunMood);
                    startRunFragmentInterface.openRunningFragment();
                } else {
                    FragmentManager fm = StartRunFragment.this.getChildFragmentManager();
                    moodSurvey = new PreRunMoodDialogFragment();
                    moodSurvey.show(fm, "Survey");
                }


            }
        });

        IB1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (prerunflag==0) {
                    prerating += 1;
                    prerunflag++;
                    message.setText("Way to Finish!!! How are you feeling?");
                }
                else {
                    postrating += 1;
                    ParseUser currentUser= ParseUser.getCurrentUser();
                    PutOnCloud(prerating,postrating,currentUser);
                }
            }
        });

        IB2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (prerunflag==0) {
                    prerating += 2;
                    prerunflag++;
                    message.setText("Way to Finish!!! How are you feeling?");
                }
                else {
                    postrating += 2;
                    ParseUser currentUser= ParseUser.getCurrentUser();
                    PutOnCloud(prerating,postrating,currentUser);
                }
            }
        });

        IB3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (prerunflag==0) {
                    prerating += 3;
                    prerunflag++;
                    message.setText("Way to Finish!!! How are you feeling?");
                }
                else {
                    postrating += 3;
                    ParseUser currentUser= ParseUser.getCurrentUser();
                    PutOnCloud(prerating,postrating,currentUser);
                }
            }
        });

        IB4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (prerunflag==0) {
                    prerating += 4;
                    prerunflag++;
                    message.setText("Way to Finish!!! How are you feeling?");
                }
                else {
                    postrating += 4;
                    ParseUser currentUser= ParseUser.getCurrentUser();
                    PutOnCloud(prerating,postrating,currentUser);
                }
            }
        });

        IB5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (prerunflag==0) {
                    prerating += 5;
                    prerunflag++;
                    message.setText("Way to Finish!!! How are you feeling?");
                }
                else {
                    postrating += 5;
                    ParseUser currentUser= ParseUser.getCurrentUser();
                    PutOnCloud(prerating,postrating,currentUser);
                }
            }
        });
    }

    //Todo: Move PutOnCloud to postRunFragment and upload on save.
                                                                //saving data to back4app
    public void PutOnCloud(int pre, int post, ParseUser user){
        Calendar calendar = Calendar.getInstance();
        String currentDate = DateFormat.getDateInstance(DateFormat.FULL).format(calendar.getTime());

        RunData RD=new RunData();
        RD.setPreRunMood(pre);
        RD.setPostRunMood(post);
        RD.setUser(user);
        RD.setRunDate(currentDate);
        RD.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e!=null){
                    Toast.makeText(getContext(), "Error while saving", Toast.LENGTH_SHORT).show();

                }

            }
        });

                                                    //sending user to recyclerview list of runs
        Fragment fragment=new HistoryFragment();
        getFragmentManager().beginTransaction().replace(R.id.flContainer, fragment).commit();

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_start_run, container, false);
    }



    //////////////////////////////////////////////////////////////////////
    //                           INTERFACE
    //                        IMPLEMENTATIONS
    //////////////////////////////////////////////////////////////////////


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
        public void openRunningFragment();

    }
}