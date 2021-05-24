package com.codepath.runnershigh.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.codepath.runnershigh.FeedAdapter;
import com.codepath.runnershigh.R;
import com.codepath.runnershigh.RunData;
import com.parse.ParseQuery;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/*
This fragment holds a RecyclerView of all runs currently being stored in the Parse backend. Each
run shows the owning user's username, and how long ago the run was. Clicking on More Info takes the
user to MoreInfoActivity with said run.
 */
public class FeedFragment extends Fragment {
    List<RunData> runFeed;
    RecyclerView rvFeed;
    FeedAdapter feedAdapter;


    public FeedFragment() {
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
        return inflater.inflate(R.layout.fragment_feed, container, false);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rvFeed = view.findViewById(R.id.rvFeed);

        runFeed = new ArrayList<>();
        feedAdapter = new FeedAdapter(getContext(), runFeed);
        rvFeed.setAdapter(feedAdapter);
        rvFeed.setLayoutManager(new LinearLayoutManager(getContext()));

        //Query all runs by all users
        queryAllPosts();
    }

    protected void queryAllPosts() {
        ParseQuery<RunData> query = ParseQuery.getQuery(RunData.class);
        query.addDescendingOrder(RunData.KEY_CREATED_AT);
        query.setLimit(25);
        query.findInBackground((runs, e) -> {
            if(e!=null) {
                Toast.makeText(getContext(), "Error fetching runs", Toast.LENGTH_SHORT).show();
                return;
            }

            runFeed.addAll(runs);
            feedAdapter.notifyDataSetChanged();
        });
    }
}