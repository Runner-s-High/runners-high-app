package com.codepath.runnershigh;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;

/*
This activity is seen when a user signs up for the application for the first time. It gathers some
basic information, like the user's name and weight and then transitions into MainActivity
 */
public class SetupProfileActivity extends AppCompatActivity {
    public static final String TAG = "SetupProfileActivity";

    //Layout element references
    EditText etFirstName;
    EditText etLastName;
    EditText etWeight;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup_profile);

        etFirstName = findViewById(R.id.etFirstName);
        etLastName = findViewById(R.id.etLastName);
        etWeight = findViewById(R.id.etWeight);
    }

    //onClick method for submission button; saves user data to Parse backend
    public void submitProfile(View v) {
        ParseUser parseUser = ParseUser.getCurrentUser();
        parseUser.put("firstName", etFirstName.getText().toString());
        parseUser.put("lastName", etLastName.getText().toString());
        parseUser.put("weight", Integer.parseInt(etWeight.getText().toString()));
        parseUser.saveInBackground(e -> {
            if(e == null) {
                Log.d(TAG, "Successful user update");
                startActivity(new Intent(SetupProfileActivity.this, MainActivity.class));
                finish();
            }
            else {
                Toast.makeText(SetupProfileActivity.this, "Error. Try again", Toast.LENGTH_SHORT).show();
            }
        });
    }
}