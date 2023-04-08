package com.classicgames.myapplication.data.models;

import com.classicgames.myapplication.MyApplication;

import java.util.Random;

public class TrueColorsModel {

    private int lives, correctColorsPicked, points, maxPoints;
    private int speedMultiplier, currentLevelMilSec;
    private int falseColor, trueColor;
    private boolean gameOver;

    private final int[] colors;

    public TrueColorsModel(){
        colors = MyApplication.getInstance().getColors();
        maxPoints = MyApplication.getInstance().getRecords().getTrueColorsRecord();
    }

    public void startGame(){
        speedMultiplier = 1;
        gameOver = false;
        lives = 3;
        points = 0;
        correctColorsPicked = 0;
        currentLevelMilSec = 4000;
        generateColors();
    }

    private void generateColors(){
        falseColor = colors[new Random().nextInt(8)];
        trueColor = colors[new Random().nextInt(8)];
    }

    public void winPoint(int timeLeft){
        correctColorsPicked++;
        generateColors();
        points += timeLeft * 0.88 * speedMultiplier;

        if (correctColorsPicked >= 5 && correctColorsPicked < 9 && currentLevelMilSec != 3500) currentLevelMilSec = 3500;
        else if (correctColorsPicked >= 10 && correctColorsPicked < 15 && currentLevelMilSec != 3000) currentLevelMilSec = 3000;
        else if (correctColorsPicked >= 15 && correctColorsPicked < 20 && currentLevelMilSec != 2500) currentLevelMilSec = 2500;
        else if (correctColorsPicked >= 20 && correctColorsPicked < 25 && currentLevelMilSec != 2000) currentLevelMilSec = 2000;
        else if (correctColorsPicked >= 25 && correctColorsPicked < 50 && currentLevelMilSec != 1500) currentLevelMilSec = 1500;
        else if (correctColorsPicked >= 50 && currentLevelMilSec != 1000) currentLevelMilSec = 1000;
    }

    public boolean scoredPoint(int color){
        return color == trueColor;
    }

    public void loseLife(){
        lives--;
        if (lives > 0){
            generateColors();
        } else{
            gameOver = true;
        }
    }

    public boolean isNewRecord() {
        if (points < maxPoints) return false;
        MyApplication.getInstance().getRecords().setTrueColorsRecord(points);
        maxPoints = points;
        return true;
    }

    public int getCurrentLevelMilSec(){
        return currentLevelMilSec;
    }

    public int getFalseColor() {
        return falseColor;
    }

    public int getTrueColor(){
        return trueColor;
    }

    public boolean isGameOver(){
        return gameOver;
    }

    public int getRecord(){
        return maxPoints;
    }

    public int getPoints(){
        return points;
    }

    public int getCorrectColorsPicked(){
        return correctColorsPicked;
    }

    public int getLives(){
        return lives;
    }
}
