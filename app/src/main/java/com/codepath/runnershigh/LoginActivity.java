package com.codepath.runnershigh;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.parse.ParseUser;

/*
This class is the first activity loaded upon app startup. If the user is not currently logged in,
they either sign up or log in to their account, and this attempt is checked against the Parse backend.
The application then transitions to either the SetupProfileActivity (sign up) or MainActivity (login).
 */
public class LoginActivity extends AppCompatActivity {
    public static final String TAG = "LOGIN_ACTIVITY";

    //Layout element references
    ImageView ivLogo;
    ImageView ivLogin;
    EditText etUser;
    EditText etPassword;
    Button btnLogin;
    Button btnSignUp;
    TextView tvBlurb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.Theme_RunnersHigh);
        getSupportActionBar().hide();
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Initializing views
        ivLogo = findViewById(R.id.ivLogo);
        ivLogin = findViewById(R.id.ivLogin);
        etUser = findViewById(R.id.etUser);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnSignUp = findViewById(R.id.btnSignup);
        tvBlurb = findViewById(R.id.tvBlurb);

        //Checking if already logged in
        if(ParseUser.getCurrentUser() != null) {
            Log.i(TAG, "User already logged in; starting MainActivity");
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }
    }

    //onClick method for btnLogin
    public void attemptLogin(View v) {
        //Hides keyboard from view on button press
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }

        String username = etUser.getText().toString();
        String password = etPassword.getText().toString();

        //Attempt to login to user account through query to Parse backend
        ParseUser.logInInBackground(username, password , (user, e) -> {
            if(user != null) {
                //Successful login
                Log.i(TAG, "Successful Parse Login; starting MainActivity");
                etUser.setText("");
                etPassword.setText("");

                //Transition to MainActivity
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                finish();
            }
            else {
                //LogIn failed
                //Display Toast to user
                Toast.makeText(LoginActivity.this, "Login Error", Toast.LENGTH_SHORT).show();
                Log.e(TAG, e.getMessage());
            }
        });
    }

    //onClick method for btnSignUp
    public void attemptSignUp(View v) {
        //Hides keyboard from view on button press
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }

        ParseUser user = new ParseUser();

        user.setUsername(etUser.getText().toString());
        user.setPassword(etPassword.getText().toString());

        //Attempt to create new user entry in Parse backend
        user.signUpInBackground(e -> {
            if (e == null) {
                //User successfully signed up
                Log.i(TAG, "User successfully signed up; starting OnboardingActivity");
                etUser.setText("");
                etPassword.setText("");

                //Transition to SetupProfile Activity
                startActivity(new Intent(LoginActivity.this, SetupProfileActivity.class));
                finish();
            }
            else {
                //Error signing up
                //Display Toast to user
                Toast.makeText(LoginActivity.this, "SignUp Error", Toast.LENGTH_SHORT).show();
                Log.e(TAG, e.getMessage());
            }
        });
    }
}