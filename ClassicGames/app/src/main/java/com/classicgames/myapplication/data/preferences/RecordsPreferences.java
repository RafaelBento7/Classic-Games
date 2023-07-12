package com.classicgames.myapplication.data.preferences;

import android.content.Context;
import android.content.SharedPreferences;

public class RecordsPreferences {

    private final SharedPreferences sp;
    private final SharedPreferences.Editor editor;

    public RecordsPreferences(Context context) {
        sp = context.getSharedPreferences("GameRecords", Context.MODE_PRIVATE);
        editor = sp.edit();
    }

    public int getTrueColorsRecord() {
        return sp.getInt("trueColors", 0);
    }

    public void setTrueColorsRecord(int newRecord) {
        editor.putInt("trueColors", newRecord);
        editor.apply();
    }

    public int getSnakeBigRecord() {
        return sp.getInt("snake_big", 0);
    }

    public int getSnakeMediumRecord() {
        return sp.getInt("snake_medium", 0);
    }

    public int getSnakeSmallRecord() {
        return sp.getInt("snake_small", 0);
    }

    public void setSnakeBigRecord(int newRecord) {
        editor.putInt("snake_big", newRecord);
        editor.apply();
    }

    public void setSnakeMediumRecord(int newRecord) {
        editor.putInt("snake_medium", newRecord);
        editor.apply();
    }

    public void setSnakeSmallRecord(int newRecord) {
        editor.putInt("snake_small", newRecord);
        editor.apply();
    }

    public void setMastermindRecord(int minutes, int seconds, int attempts) {
        editor.putInt("mastermind_minutes", minutes);
        editor.putInt("mastermind_seconds", seconds);
        editor.putInt("mastermind_attempts", attempts);
        editor.apply();
    }

    /**
     * @return int[0] = minutes; int[1] = seconds; int[2] = attempts
     */
    public int[] getMastermindRecord() {
        int[] records = new int[3];
        records[0] = sp.getInt("mastermind_minutes", 0);
        records[1] = sp.getInt("mastermind_seconds", 0);
        records[2] = sp.getInt("mastermind_attempts", 0);
        return records;
    }

    public void setWordleRecord(int minutes, int seconds, int points) {
        editor.putInt("wordle_minutes", minutes);
        editor.putInt("wordle_seconds", seconds);
        editor.putInt("wordle_points", points);
        editor.apply();
    }

    /**
     * @return int[0] = minutes; int[1] = seconds; int[2] = points
     */
    public int[] getWordleRecord() {
        int[] records = new int[3];
        records[0] = sp.getInt("wordle_minutes", 0);
        records[1] = sp.getInt("wordle_seconds", 0);
        records[2] = sp.getInt("wordle_points", 0);
        return records;
    }
}
