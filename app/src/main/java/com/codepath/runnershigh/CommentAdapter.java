package com.codepath.runnershigh;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.codepath.runnershigh.models.Comment;
import com.parse.ParseException;
import com.parse.ParseUser;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder> {
    private final Context context;
    private final List<Comment> comments;

    public CommentAdapter(Context context, List<Comment> comments) {
        this.context = context;
        this.comments = comments;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.eachcomment, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentAdapter.ViewHolder holder, int position) {
        Comment comment = comments.get(position);
        holder.bind(comment);
    }

    @Override
    public int getItemCount() {
        return comments.size();
    }

    public class ViewHolder extends  RecyclerView.ViewHolder {
        //Layout element references
        TextView tvPostingUser, tvCommentTime, tvCommentText;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvPostingUser = itemView.findViewById(R.id.tvPostingUser);
            tvCommentTime = itemView.findViewById(R.id.tvCommentTime);
            tvCommentText = itemView.findViewById(R.id.tvCommentText);
        }

        public void bind(Comment comment) {
            String userString;
            String dateString;

            ParseUser postingUser = comment.getPostingUser();
            try {
                postingUser = postingUser.fetchIfNeeded();
            } catch (ParseException e) {
                e.printStackTrace();
            }
            userString = postingUser.getUsername();

            Calendar commentDate = Calendar.getInstance();
            Calendar present = Calendar.getInstance();
            commentDate.setTime(comment.getDateObject());
            int presentDOY = present.get(Calendar.DAY_OF_YEAR);
            int commentDOY = commentDate.get(Calendar.DAY_OF_YEAR);
            int presentYear = present.get(Calendar.YEAR);
            int commentYear = commentDate.get(Calendar.YEAR);

            //Defining appropriate relative date strings
            if(presentDOY > commentDOY)
                if(presentYear == commentYear)
                    dateString = String.format(Locale.ENGLISH, "%dd ago", presentDOY - commentDOY);
                else
                    dateString = String.format(Locale.ENGLISH, "%dy ago", presentYear - commentYear);
            else if(presentDOY < commentDOY)
                if(presentYear == commentYear + 1)
                    dateString = String.format(Locale.ENGLISH, "%dd ago", presentDOY + (365 - commentDOY));
                else
                    dateString = String.format(Locale.ENGLISH, "%dy ago", presentYear - commentYear - 1);
            else
            if(presentYear == commentYear)
                dateString = "Today";
            else
                dateString = String.format(Locale.ENGLISH, "%dy ago", presentYear - commentYear);

            tvPostingUser.setText(userString);
            tvCommentTime.setText(dateString);
            tvCommentText.setText(comment.getCommentText());
        }
    }
}
