package com.codepath.runnershigh.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
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
import com.parse.ParseUser;

import org.jetbrains.annotations.NotNull;
import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.INPUT_METHOD_SERVICE;

/*
This fragment holds a comment thread for the current run inside of ResultsFragment in MoreInfoActivity.
The user can add a comment using an EditText box.
 */
public class CommentFragment extends Fragment {
    //Layout element references
    RecyclerView rvComments;
    Button btnAddComment;
    EditText etComment;

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
        etComment = view.findViewById(R.id.etComment);

        run = Parcels.unwrap(getArguments().getParcelable("run"));
        comments = new ArrayList<>();
        commentAdapter = new CommentAdapter(getContext(), comments);
        rvComments.setAdapter(commentAdapter);
        rvComments.setLayoutManager(new LinearLayoutManager(getContext()));

        //Setting up action for when addComment button clicked
        btnAddComment.setOnClickListener(v -> {
            //Hides keyboard
            InputMethodManager inputManager = (InputMethodManager) requireActivity().getSystemService(INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(requireActivity().getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

            String commentText = etComment.getText().toString();

            if(commentText.equals("")) {
                Toast.makeText(getContext(), "Please type a comment", Toast.LENGTH_SHORT).show();
                return;
            }

            //Clear text box
            etComment.setText("");

            Comment comment = new Comment();
            comment.setCommentText(commentText);
            comment.setPostingUser(ParseUser.getCurrentUser());
            comment.setRun(run);

            //Save comment to backend
            comment.saveInBackground(e -> {
                if(e != null)
                    return;

                Toast.makeText(getContext(), "Comment saved", Toast.LENGTH_SHORT).show();
                comments.add(comment);
                commentAdapter.notifyDataSetChanged();
            });
        });

        queryComments();
    }

    //Gather all comments associated with this run
    protected void queryComments() {
        ParseQuery<Comment> query = ParseQuery.getQuery(Comment.class);
        query.whereEqualTo(Comment.KEY_RUN, run);
        query.addAscendingOrder(Comment.KEY_CREATED_AT);
        query.findInBackground((commentList, e) -> {
            if(e != null)
                return;

            comments.addAll(commentList);
            commentAdapter.notifyDataSetChanged();
        });
    }
}