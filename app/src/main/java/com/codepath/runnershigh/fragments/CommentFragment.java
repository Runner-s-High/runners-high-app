package com.codepath.runnershigh.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.codepath.runnershigh.CommentAdapter;
import com.codepath.runnershigh.R;
import com.codepath.runnershigh.models.Comment;
import com.codepath.runnershigh.models.RunData;
import com.parse.ParseQuery;

import org.jetbrains.annotations.NotNull;
import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

public class CommentFragment extends Fragment {
    RecyclerView rvComments;
    Button btnAddComment;

    CommentAdapter commentAdapter;
    List<Comment> comments;
    RunData run;

    public CommentFragment() {
        // Required empty public constructor
    }

    public static CommentFragment newInstance(Bundle args) {
        CommentFragment fragment = new CommentFragment();
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
        return inflater.inflate(R.layout.fragment_comment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rvComments = view.findViewById(R.id.rvComments);
        btnAddComment = view.findViewById(R.id.btnAddComment);

        run = Parcels.unwrap(getArguments().getParcelable("run"));
        comments = new ArrayList<>();
        commentAdapter = new CommentAdapter(getContext(), comments);
        rvComments.setAdapter(commentAdapter);
        rvComments.setLayoutManager(new LinearLayoutManager(getContext()));

        //TODO: Come back here and implement the ability to add actual comments
        btnAddComment.setOnClickListener(v -> Toast.makeText(getContext(), "NOT IMPLEMENTED YET", Toast.LENGTH_SHORT).show());

        queryComments();
    }

    protected void queryComments() {
        ParseQuery<Comment> query = ParseQuery.getQuery(Comment.class);
        query.whereEqualTo(Comment.KEY_RUN, run);
        query.addDescendingOrder(Comment.KEY_CREATED_AT);
        query.findInBackground((commentList, e) -> {
            if(e != null)
                return;

            comments.addAll(commentList);
            commentAdapter.notifyDataSetChanged();
        });
    }
}