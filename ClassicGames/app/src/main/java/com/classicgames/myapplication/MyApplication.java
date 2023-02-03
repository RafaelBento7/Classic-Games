package com.classicgames.myapplication;

import android.app.Application;

import com.classicgames.myapplication.data.preferences.RecordsPreferences;

public class MyApplication extends Application {

    private static MyApplication instance;

    public static MyApplication getInstance() {
        return instance;
    }

    private int[] trueColorsColors;

    private RecordsPreferences recordsPreferences;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        recordsPreferences = new RecordsPreferences(this);
        initializeTrueColorsColors();
    }

    private void initializeTrueColorsColors() {
        trueColorsColors = new int[8];
        trueColorsColors[0] = getResources().getColor(R.color.purple);
        trueColorsColors[1] = getResources().getColor(R.color.green);
        trueColorsColors[2] = getResources().getColor(R.color.red);
        trueColorsColors[3] = getResources().getColor(R.color.yellow);
        trueColorsColors[4] = getResources().getColor(R.color.blue);
        trueColorsColors[5] = getResources().getColor(R.color.brown);
        trueColorsColors[6] = getResources().getColor(R.color.orange);
        trueColorsColors[7] = getResources().getColor(R.color.pink);
    }

    public int[] getTrueColorsColors() {
        return trueColorsColors;
    }

    public RecordsPreferences getRecords() {
        return recordsPreferences;
    }
}
