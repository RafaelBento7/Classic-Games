package com.classicgames.myapplication.data.models;

import com.classicgames.myapplication.MyApplication;

import java.util.Random;

public class MastermindModel {

    private int attempt, lastAttemptTruePosition, lastAttemptWrongPosition, colorsPicked;
    private final int[] records;
    private int[] solution, attemptColors;
    private final int[] colors;

    public MastermindModel() {
        colors = MyApplication.getInstance().getColors();
        records = MyApplication.getInstance().getRecords().getMastermindRecord();
    }

    public void startGame() {
        attempt = 0;
        colorsPicked = 0;
        generateSolution();
        attemptColors = new int[4];
    }

    private void generateSolution() {
        solution = new int[4];

        for (int i = 0; i < 4; i++) {
            int randomNumber = new Random().nextInt(8);
            solution[i] = colors[randomNumber];
            //Check if the color already exists
            boolean conflict = true;
            do {
                if (!hasCollision(solution[i], i)) conflict = false;
                else solution[i] = colors[new Random().nextInt(8)];
            } while (conflict);

        }
    }

    private boolean hasCollision(int color, int limit) {
        for (int j = 0; j < limit; j++) {
            if (solution[j] == color) {
                return true;
            }
        }
        return false;
    }

    public void play(int color) {
        attemptColors[colorsPicked] = color;
        colorsPicked++;
    }

    public void checkSolution() {
        lastAttemptTruePosition = 0;
        lastAttemptWrongPosition = 0;

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if (solution[i] == attemptColors[j]) {
                    if (i == j)
                        lastAttemptTruePosition++;
                    else lastAttemptWrongPosition++;
                    break;
                }
            }
        }
        attemptColors = new int[4];
        colorsPicked = 0;
        attempt++;
    }

    public boolean isNewRecord(Integer seconds, Integer minutes, Integer attempt) {
        boolean newRecord = false;
        if (records[0] == 0 && records[1] == 0)  newRecord = true;          // first record
        else if (records[0] >= (int) minutes) {                             // same or less minutes
            if (records[0] == (int) minutes) {                              // same minutes // checks seconds
                if (records[1] >= (int) seconds) {                          // less or same seconds
                    if (records[1] != (int) seconds) newRecord = true;      // less seconds
                    else if (records[2] > attempt) newRecord = true;        // same seconds // checks attempts
                }
            } else newRecord = true;                                        // less minutes
        }

        if (!newRecord) return false;

        MyApplication.getInstance().getRecords().setMastermindRecord((int) minutes, (int) seconds, (int) attempt);
        records[0] = (int) minutes;
        records[1] = (int) seconds;
        records[2] = (int) attempt;

        return true;
    }

    public boolean isVictory() {
        return lastAttemptTruePosition == 4;
    }

    public boolean isGameOver() {
        return attempt >= 10;
    }

    public int[] getRecords() {
        return records;
    }

    public int getLastAttemptTruePosition() {
        return lastAttemptTruePosition;
    }

    public int getLastAttemptWrongPosition() {
        return lastAttemptWrongPosition;
    }

    public int[] getSolution() {
        return solution;
    }

    public int getColorsPicked() {
        return colorsPicked;
    }

    public int getAttempt() {
        return attempt;
    }
}