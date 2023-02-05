package com.classicgames.myapplication.data.preferences;

import android.content.Context;
import android.content.SharedPreferences;

public class RecordsPreferences {

    private final SharedPreferences sp;
    private final SharedPreferences.Editor editor;

    public RecordsPreferences(Context context){
        sp = context.getSharedPreferences("GameRecords", Context.MODE_PRIVATE);
        editor = sp.edit();
    }

    public int getTrueColorsRecord(){
        return sp.getInt("trueColors",0);
    }

    public void setTrueColorsRecord(int newRecord){
        editor.putInt("trueColors", newRecord);
        editor.apply();
    }
}
