package com.codepath.runnershigh;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;

@ParseClassName("RunData")                  //needs to match what object is called on back4app
public class RunData extends ParseObject {          //keys are the names of the columns on back4app

    public static final String KEY_PRERUNMOOD="PreRunMood";
    public static final String KEY_POSTRUNMOOD="PostRunMood";
    public static final String KEY_USER="TheUser";
    public static final String KEY_CREATED_AT="createdAt";
    public static final String KEY_RUN_TIME="RunTime";
    public static final String KEY_RUN_DISTANCE="RunDistance";
    public static final String KEY_RUN_DATE="RunDate";
    public static final String KEY_RUN_CALORIES="Calories";
    public static final String KEY_RUN_NOTE="RunNotes";
    public static final String KEY_USER_IMAGE="ProfileImage";

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


    public String getRunTime(){

        return getString(KEY_RUN_TIME);
    }

    public void setRunTime(String runTime){

        put(KEY_RUN_TIME,runTime);
    }

    public String getRunDate(){
        return getString(KEY_RUN_DATE);
    }

    public void setRunDate(String date){
        put(KEY_RUN_DATE,date);
    }

    public String getRunNote() {
        return getString(KEY_RUN_NOTE);
    }

    public void setRunNote(String note) {
        put(KEY_RUN_NOTE, note);
    }

    public double getRunDistance() {
        return getDouble(KEY_RUN_DISTANCE);
    }

    public void setRunDistance(double distance) {
        put(KEY_RUN_DISTANCE,distance);
    }

    public double getRunCalories() {
        return getDouble(KEY_RUN_CALORIES);
    }

    public void setRunCalories(double calories) {
        put(KEY_RUN_CALORIES, calories);
    }

    public ParseFile getProfileImage(){

        return getParseFile(KEY_USER_IMAGE);
    }

    public void setProfileImage(ParseFile parseFile){

        put(KEY_USER_IMAGE,parseFile);
    }

}
