package com.codepath.runnershigh;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;

@ParseClassName("RunData")                  //needs to match what object is called on back4app
public class RunData extends ParseObject {          //keys are the names of the columns on back4app

    public static final String KEY_PRERUNMOOD="PreRunMood";
    public static final String KEY_POSTRUNMOOD="PostRunMood";
    public static final String KEY_USER="TheUser";
    public static final String KEY_CREATED_AT="createdAt";

    public int getPreRunMood(){
        return getInt(KEY_PRERUNMOOD);

    }

    public void setPreRunMood(int prerunmood){

        put(KEY_PRERUNMOOD,prerunmood);
    }

    public int getPostRunMood(){

        return getInt(KEY_POSTRUNMOOD);

    }

    public void setPostRunMood(int postrunmood){

        put(KEY_POSTRUNMOOD,postrunmood);
    }

    public ParseUser getUser(){

        return getParseUser(KEY_USER);
    }

    public void setUser(ParseUser user){

        put(KEY_USER,user);
    }

}