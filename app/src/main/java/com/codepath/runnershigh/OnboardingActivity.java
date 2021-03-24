package com.codepath.runnershigh;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class OnboardingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getSupportActionBar().hide();
        View decorView = getWindow().getDecorView();
        // TODO: Figure out non-deprecated way to do this
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onboarding);
    }

    //onClick method for btnProceed; moves to MainActivity
    public void proceed(View v) {
        finish();
        startActivity(new Intent(this, MainActivity.class));
    }
}