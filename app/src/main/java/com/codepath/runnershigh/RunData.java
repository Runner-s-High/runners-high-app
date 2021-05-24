package com.codepath.runnershigh;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.util.Date;
import java.util.List;

/*
This java class represents the RunData class stored on the Parse backend. Setters and getters
defined within
 */
@ParseClassName("RunData")                  //needs to match what object is called on back4app
public class RunData extends ParseObject {
    //keys are the names of the columns on back4app
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
    public static final String KEY_PRE_RUN_STRESS="PreRunStress";
    public static final String KEY_POST_RUN_STRESS="PostRunStress";
    public static final String KEY_RUN_LAT_LIST="RunLatList";
    public static final String KEY_RUN_LNG_LIST="RunLngList";

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

    public void setPreRunStress(int prerunstress){
        put(KEY_PRE_RUN_STRESS,prerunstress);
    }

    public int getPreRunStress(){
        return getInt(KEY_PRE_RUN_STRESS);
    }

    public void setPostRunStress(int postrunstress){
        put(KEY_POST_RUN_STRESS,postrunstress);
    }

    public int getPostRunStress(){
        return getInt(KEY_POST_RUN_STRESS);
    }

    public List<String> getRunLatList() {
        return (List<String>) get(KEY_RUN_LAT_LIST);
    }

    public void setRunLatList(List<String> lats) {
        put(KEY_RUN_LAT_LIST, lats);
    }

    public List<String> getRunLngList() {
        return (List<String>) get(KEY_RUN_LNG_LIST);

    }

    public void setRunLngList(List<String> longs) {
        put(KEY_RUN_LNG_LIST, longs);
    }

    public Date getDateObject() {
        return getCreatedAt();
    }
}
