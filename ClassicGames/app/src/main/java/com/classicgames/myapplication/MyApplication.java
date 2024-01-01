package com.classicgames.myapplication;

import android.app.Application;
import android.content.res.Configuration;

import com.classicgames.myapplication.data.preferences.AppPreferences;
import com.classicgames.myapplication.data.preferences.RecordsPreferences;

import java.util.Locale;

public class MyApplication extends Application {

    private static MyApplication instance;

    public static MyApplication getInstance() {
        return instance;
    }

    private int[] colors;

    private RecordsPreferences recordsPreferences;
    private AppPreferences appPreferences;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        recordsPreferences = new RecordsPreferences(this);
        appPreferences = new AppPreferences(this);
        initializeColors();
    }

    private void initializeColors() {
        colors = new int[8];
        colors[0] = getResources().getColor(R.color.purple);
        colors[1] = getResources().getColor(R.color.green);
        colors[2] = getResources().getColor(R.color.red);
        colors[3] = getResources().getColor(R.color.yellow);
        colors[4] = getResources().getColor(R.color.blue);
        colors[5] = getResources().getColor(R.color.brown);
        colors[6] = getResources().getColor(R.color.orange);
        colors[7] = getResources().getColor(R.color.pink);
    }

    public int[] getColors() {
        return colors;
    }

    public RecordsPreferences getRecords() {
        return recordsPreferences;
    }

    public AppPreferences getAppPreferences() {
        return appPreferences;
    }


}
