package com.codepath.runnershigh.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.codepath.runnershigh.R;

public class PostRunFragment extends Fragment {
    PostRunFragmentInterface postRunFragmentInterface;

    Button btSave;
    Button btExit;


    public PostRunFragment() {
        // Required empty public constructor
    }


    public static PostRunFragment newInstance(String param1, String param2) {
        PostRunFragment fragment = new PostRunFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //if (getArguments() != null)
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_post_run, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        btExit = view.findViewById(R.id.btExit);
        btSave = view.findViewById(R.id.btSave);

        btExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder= new AlertDialog.Builder(getActivity());
                builder.setMessage("Are you sure you don't want to log this run?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getContext(), "user clicks exit w/o saving", Toast.LENGTH_SHORT).show();
                        postRunFragmentInterface.exitPostRun();
                    }
                });

                builder.setNegativeButton("No",null);

                builder.create().show();
            }
        });

        btSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Todo: collect all the run info from views and upload run to parse
                postRunFragmentInterface.exitPostRun();
                Toast.makeText(getContext(), "user clicks save, move to history screen", Toast.LENGTH_SHORT).show();

            }
        });

    }


    //Attaching Interface
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if(context instanceof PostRunFragmentInterface){
            postRunFragmentInterface = (PostRunFragmentInterface) context;
        }else{
            throw new RuntimeException(context.toString()
                    +"must implement PostRunFragmentInterface");
        }
    }

    public interface PostRunFragmentInterface{
        public void exitPostRun();
    }

}