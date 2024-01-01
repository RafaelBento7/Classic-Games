package com.classicgames.myapplication.data.preferences;

import android.content.Context;
import android.content.SharedPreferences;

public class AppPreferences {

    private final SharedPreferences sp;
    private final SharedPreferences.Editor editor;

    public AppPreferences(Context context) {
        sp = context.getSharedPreferences("APP", Context.MODE_PRIVATE);
        editor = sp.edit();
    }

    public int getTrueColorsRecord() {
        return sp.getInt("trueColors", 0);
    }

    public void setTrueColorsRecord(int newRecord) {
        editor.putInt("trueColors", newRecord);
        editor.apply();
    }

    public String getLanguage() {
        return sp.getString("language", "en");
    }


    public void saveLanguage(String languageCode) {
        editor.putString("language", languageCode);
        editor.apply();
    }
}
