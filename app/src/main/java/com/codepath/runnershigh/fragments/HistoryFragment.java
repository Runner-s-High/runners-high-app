package com.codepath.runnershigh.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.codepath.runnershigh.R;
import com.codepath.runnershigh.RunData;
import com.codepath.runnershigh.TheAdapter;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

/*
Fragment that holds a RecyclerView of past runs completed by the current user.
 */
public class HistoryFragment extends Fragment {
    protected List<RunData> runs;
    private RecyclerView rvruns;
    protected TheAdapter RunAdapter;


    public HistoryFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_history, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rvruns = view.findViewById(R.id.rvRunning);

        //Set up RecyclerView adapter
        runs = new ArrayList<>();
        RunAdapter = new TheAdapter(getContext(), runs);
        rvruns.setAdapter(RunAdapter);
        rvruns.setLayoutManager(new LinearLayoutManager(getContext()));

        //Gather all past runs
        queryPosts();
    }

    //retrieves past runs on back4app for the signed in user
    protected void queryPosts() {
        ParseQuery<RunData> query=ParseQuery.getQuery(RunData.class);
        query.include(RunData.KEY_USER);
        query.whereEqualTo(RunData.KEY_USER, ParseUser.getCurrentUser());
        query.addDescendingOrder(RunData.KEY_CREATED_AT);      //orders posts by time
        query.findInBackground((theruns, e) -> {
            if (e!=null){
                return;
            }

            runs.addAll(theruns);
            RunAdapter.notifyDataSetChanged();
        });
    }

}