package com.codepath.runnershigh;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.parse.ParseUser;

public class SettingsActivity extends AppCompatActivity {
    Button btnLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        btnLogout = findViewById(R.id.bntLogout);
    }

    //onClick method for btnLogout
    public void logOut(View v) {
        ParseUser.logOut();
        //TODO: Figure out way to finish this and MainActivity; just go to LoginActivity
    }
}