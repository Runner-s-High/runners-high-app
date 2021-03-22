package com.codepath.runnershigh.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;

import com.codepath.runnershigh.R;
import com.codepath.runnershigh.RunData;
import com.codepath.runnershigh.TheAdapter;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;


public class HistoryFragment extends Fragment {
    protected List<RunData> runs;
    private RecyclerView rvruns;
    protected TheAdapter RunAdapter;


    public HistoryFragment() {
        // Required empty public constructor
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rvruns = view.findViewById(R.id.rvRunning);
        runs = new ArrayList<>();
        RunAdapter = new TheAdapter(getContext(), runs);
        rvruns.setAdapter(RunAdapter);
        rvruns.setLayoutManager(new LinearLayoutManager(getContext()));
        queryPosts();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_history, container, false);
    }
                                        //retrives past runs on back4app for the signed in user
    protected void queryPosts() {
        ParseQuery<RunData> query=ParseQuery.getQuery(RunData.class);
        query.include(RunData.KEY_USER);
        query.whereEqualTo(RunData.KEY_USER, ParseUser.getCurrentUser());
        query.addDescendingOrder(RunData.KEY_CREATED_AT);      //orders posts by time
        query.findInBackground(new FindCallback<RunData>() {
            @Override
            public void done(List<RunData> theruns, ParseException e) {
                if (e!=null){
                    return;
                }

                runs.addAll(theruns);
                RunAdapter.notifyDataSetChanged();
            }
        });
    }

}