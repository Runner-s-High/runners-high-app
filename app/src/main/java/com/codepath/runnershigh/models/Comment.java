package com.codepath.runnershigh.models;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.util.Date;

@ParseClassName("Comment")
public class Comment extends ParseObject {
    public static final String KEY_POSTING_USER = "PostingUser";
    public static final String KEY_RUN = "Run";
    public static final String KEY_COMMENT_TEXT = "CommentText";

    public ParseUser getPostingUser() {
        return getParseUser(KEY_POSTING_USER);
    }

    public void setPostingUser(ParseUser user) {
        put(KEY_POSTING_USER, user);
    }

    public RunData getRun() {
        return (RunData) getParseObject(KEY_RUN);
    }

    public void setRun(RunData run) {
        put(KEY_RUN, run);
    }

    public String getCommentText() {
        return getString(KEY_COMMENT_TEXT);
    }

    public void setCommentText(String comment) {
        put(KEY_COMMENT_TEXT, comment);
    }

    public Date getDateObject() {
        return getCreatedAt();
    }
}
