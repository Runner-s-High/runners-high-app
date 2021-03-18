package com.codepath.runnershigh;

import com.parse.Parse;
import android.app.Application;

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        //Set up application connection to Parse
        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("iNbpvLj0Ds2ImY7ycmAF0DTcZ3ki4co7oENhNnUi")
                .clientKey("Dj0n8lGEjZayoltjaDNwIq12NONXyEt2b4fpvZ42")
                .server("https://parseapi.back4app.com")
                .build()
        );
    }
}
