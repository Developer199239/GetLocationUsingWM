package com.surroundapps.getlocationusingwm;

import android.app.Application;

public class App extends Application {

    private static App instance;

    public static App getApplication() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }

}
