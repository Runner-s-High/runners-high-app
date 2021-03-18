package com.codepath.runnershigh;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

//this is a comment by Greg

public class LoginActivity extends AppCompatActivity {
    public static final String TAG = "LOGIN_ACTIVITY";
    ImageView ivLogo;
    EditText etUser;
    EditText etPassword;
    Button btnLogin;
    Button btnSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Initializing views
        ivLogo = findViewById(R.id.ivLogo);
        etUser = findViewById(R.id.etUser);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnSignUp = findViewById(R.id.btnSignup);

        //Checking if already logged in
        if(ParseUser.getCurrentUser() != null) {
            Log.i(TAG, "User already logged in; starting MainActivity");
            startActivity(new Intent(this, MainActivity.class));
        }

        //TODO: Render logo image
        //TODO: (optional) Create splash page
    }

    //onClick method for btnLogin
    public void attemptLogin(View v) {
        String username = etUser.getText().toString();
        String password = etPassword.getText().toString();

        ParseUser.logInInBackground(username, password , new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {
                if(user != null) {
                    //Successful login
                    Log.i(TAG, "Successful Parse Login; starting MainActivity");
                    etUser.setText("");
                    etPassword.setText("");
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                }
                else {
                    //LogIn failed
                    //Display Toast to user
                    Toast.makeText(LoginActivity.this, "Login Error", Toast.LENGTH_SHORT).show();
                    Log.e(TAG, e.getMessage());
                    e.printStackTrace();
                }
            }
        });
    }

    //onClick method for btnSignUp
    public void attemptSignUp(View v) {
        ParseUser user = new ParseUser();

        user.setUsername(etUser.getText().toString());
        user.setPassword(etPassword.getText().toString());

        user.signUpInBackground(new SignUpCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    //User successfully signed up
                    Log.i(TAG, "User successfully signed up; starting MainActivity");
                    etUser.setText("");
                    etPassword.setText("");
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                }
                else {
                    //Error signing up
                    //Display Toast to user
                    Toast.makeText(LoginActivity.this, "SignUp Error", Toast.LENGTH_SHORT).show();
                    Log.e(TAG, e.getMessage());
                    e.printStackTrace();
                }
            }
        });
    }
}