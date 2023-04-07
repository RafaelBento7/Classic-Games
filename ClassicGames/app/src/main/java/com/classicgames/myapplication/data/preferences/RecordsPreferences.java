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

    public int getSnakeBigRecord(){
        return sp.getInt("snake_big",0);
    }

    public int getSnakeMediumRecord(){
        return sp.getInt("snake_medium",0);
    }

    public int getSnakeSmallRecord(){
        return sp.getInt("snake_small",0);
    }

    public void setSnakeBigRecord(int newRecord){
        editor.putInt("snake_big", newRecord);
        editor.apply();
    }

    public void setSnakeMediumRecord(int newRecord){
        editor.putInt("snake_medium", newRecord);
        editor.apply();
    }

    public void setSnakeSmallRecord(int newRecord){
        editor.putInt("snake_small", newRecord);
        editor.apply();
    }
}
