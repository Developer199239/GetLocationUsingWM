package com.surroundapps.getlocationusingwm;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class SessionHelper {
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private Context context;
    private final String PREF_NAME = "getlocationusingwm";

    private static final String KEY_LOCATION_DATA = "user_name";
    private static final String KEY_COUNT = "count";

    public SessionHelper(Context context) {
        this.context = context;
        preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = preferences.edit();
    }

    public void clearSession() {
        preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor.clear();
        editor.apply();
    }

    public SharedPreferences getSharedPreferences(Context context) {
        if(preferences == null) {
            return preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        }
        return preferences;
    }

    public void setLocationData(Location location) {
        String locationDataStr = preferences.getString(KEY_LOCATION_DATA,"");
        if(locationDataStr.length() > 0) {
            locationDataStr = locationDataStr+","+location.getLatitude()+","+location.getLongitude()+","+System.currentTimeMillis();
        } else {
           locationDataStr =  location.getLatitude()+","+location.getLongitude()+","+System.currentTimeMillis();
        }
        editor.putString(KEY_LOCATION_DATA,locationDataStr);
        editor.commit();
    }

    public List<String> getLocationData() {
        String locationDataStr = preferences.getString(KEY_LOCATION_DATA,"");
        List<String> data = Arrays.asList(locationDataStr.split(","));
        return data;
    }

    public void setCnt() {
        int preCnt = preferences.getInt(KEY_COUNT,0);
        preCnt++;
        editor.putInt(KEY_COUNT,preCnt);
        editor.commit();
    }

    public int getCnt() {
        return preferences.getInt(KEY_COUNT,0);
    }

}
