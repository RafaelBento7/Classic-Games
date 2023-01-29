package com.classicgames.myapplication;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

public class TrueColorGame extends AppCompatActivity {

    private ProgressBar progressBar;
    private Button bPurple,bGreen,bRed,bYellow,bBlue,bBrown,bOrange,bPink;
    private TextView textViewPoints,textViewColor,textViewMaxPoints,textViewCorrects;
    private ImageView heart1,heart2,heart3;
    private CountDownTimer countDownTimer;

    private int randomTrueColor, randomFalseColor;
    private int progress,lives,corrects,levelMilSec,maxPoints,points;

    private SharedPreferences save,load;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_true_color_game);
        getComponents();
        loadRecord();
        buttonClicks();
        startGame();
    }

    private void buttonClicks() {
        bPurple.setOnClickListener(view -> checkColor(1));
        bGreen.setOnClickListener(view -> checkColor(2));
        bRed.setOnClickListener(view -> checkColor(3));
        bYellow.setOnClickListener(view -> checkColor(4));
        bBlue.setOnClickListener(view -> checkColor(5));
        bBrown.setOnClickListener(view -> checkColor(6));
        bOrange.setOnClickListener(view -> checkColor(7));
        bPink.setOnClickListener(view -> checkColor(8));
    }

    private void getComponents() {
        progressBar = findViewById(R.id.trueColorProgressBar);
        textViewPoints = findViewById(R.id.trueColorPoints);
        textViewColor = findViewById(R.id.trueColorColor);
        textViewMaxPoints = findViewById(R.id.trueColorMaxPoints);
        textViewCorrects = findViewById(R.id.trueColorCorrects);
        heart1 = findViewById(R.id.trueColorHeart1);
        heart2 = findViewById(R.id.trueColorHeart2);
        heart3 = findViewById(R.id.trueColorHeart3);
        bPurple = findViewById(R.id.trueColorPurple);
        bGreen = findViewById(R.id.trueColorGreen);
        bRed = findViewById(R.id.trueColorRed);
        bYellow = findViewById(R.id.trueColorYellow);
        bBlue = findViewById(R.id.trueColorBlue);
        bBrown = findViewById(R.id.trueColorBrown);
        bOrange = findViewById(R.id.trueColorOrange);
        bPink = findViewById(R.id.trueColorPink);
        save = getSharedPreferences("TrueColorsGame", Context.MODE_PRIVATE);
        load = getApplicationContext().getSharedPreferences("TrueColorsGame",Context.MODE_PRIVATE);
    }

    private void startGame(){
        lives = 3;
        points = 0;
        heart1.setVisibility(View.VISIBLE);
        heart2.setVisibility(View.VISIBLE);
        heart3.setVisibility(View.VISIBLE);
        corrects = 0;
        levelMilSec = 4000;
        textViewCorrects.setText(String.valueOf(corrects));
        randomColors();
        changeColors();
        changeTextColor();
        startTimer();
    }

    private void startTimer(){
        progress=100;
        progressBar.setProgress(100);
        countDownTimer = new CountDownTimer(levelMilSec, levelMilSec/10) {

            @Override
            public void onTick(long leftTimeInMilliseconds) {
                progress-=10;
                progressBar.setProgress(progress);
            }
            @Override
            public void onFinish() {
                loseLife();
            }
        }.start();
    }

    private void gameOver() {
        if (points > maxPoints){
            saveRecord();
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(TrueColorGame.this);
        builder.setTitle(getResources().getString(R.string.game_over));
        builder.setCancelable(false);
        builder.setMessage(getResources().getString(R.string.game_over_points)+" "+points);
        builder.setPositiveButton(getResources().getString(R.string.play_again), (dialog, which) -> startGame());
        builder.setNegativeButton(getResources().getString(R.string.back_menu),(dialog, which)-> finish());
        builder.show();
    }

    private void checkColor(int color) {
        countDownTimer.cancel();
        if (color == randomTrueColor) winPoint();
        else loseLife();
    }

    private void winPoint(){
        corrects++;
        randomColors();
        changeColors();
        changeTextColor();
        textViewCorrects.setText(String.valueOf(corrects));
        int speedMultiplier=1;
        if(levelMilSec == 3000) speedMultiplier = 2;
        else if(levelMilSec == 2500) speedMultiplier = 3;
        else if(levelMilSec == 2000) speedMultiplier = 4;
        else if(levelMilSec == 1500) speedMultiplier = 5;
        else if(levelMilSec == 1000) speedMultiplier = 6;
        points += progress * 0.88 * speedMultiplier;
        String pointsString = getResources().getString(R.string.points) +  points;
        textViewPoints.setText(pointsString);
        if (corrects >= 5 && corrects < 9 && levelMilSec != 3500) levelMilSec = 3500;
        else if (corrects >= 10 && corrects < 15 && levelMilSec != 3000) levelMilSec = 3000;
        else if (corrects >= 15 && corrects < 20 && levelMilSec != 2500) levelMilSec = 2500;
        else if (corrects >= 20 && corrects < 25 && levelMilSec != 2000) levelMilSec = 2000;
        else if (corrects >= 25 && corrects < 50 && levelMilSec != 1500) levelMilSec = 1500;
        else if (corrects >= 50 && levelMilSec != 1000) levelMilSec = 1000;
        startTimer();
    }

    private void loseLife(){
        lives--;
        if (lives == 2) heart3.setVisibility(View.GONE);
        else if (lives == 1) heart2.setVisibility(View.GONE);
        else heart1.setVisibility(View.GONE);
        if (lives > 0){
            randomColors();
            changeColors();
            changeTextColor();
            startTimer();
        }
        else gameOver();
    }

    private void changeColors(){
        switch (randomFalseColor){
            case 1:
                progressBar.setProgressTintList(ColorStateList.valueOf(getResources().getColor(R.color.purple)));
                textViewColor.setTextColor(getResources().getColor(R.color.purple));
                break;
            case 2:
                progressBar.setProgressTintList(ColorStateList.valueOf(getResources().getColor(R.color.green)));
                textViewColor.setTextColor(getResources().getColor(R.color.green));
                break;
            case 3:
                progressBar.setProgressTintList(ColorStateList.valueOf(getResources().getColor(R.color.red)));
                textViewColor.setTextColor(getResources().getColor(R.color.red));
                break;
            case 4:
                progressBar.setProgressTintList(ColorStateList.valueOf(getResources().getColor(R.color.yellow)));
                textViewColor.setTextColor(getResources().getColor(R.color.yellow));
                break;
            case 5:
                progressBar.setProgressTintList(ColorStateList.valueOf(getResources().getColor(R.color.blue)));
                textViewColor.setTextColor(getResources().getColor(R.color.blue));
                break;
            case 6:
                progressBar.setProgressTintList(ColorStateList.valueOf(getResources().getColor(R.color.brown)));
                textViewColor.setTextColor(getResources().getColor(R.color.brown));
                break;
            case 7:
                progressBar.setProgressTintList(ColorStateList.valueOf(getResources().getColor(R.color.orange)));
                textViewColor.setTextColor(getResources().getColor(R.color.orange));
                break;
            case 8:
                progressBar.setProgressTintList(ColorStateList.valueOf(getResources().getColor(R.color.pink)));
                textViewColor.setTextColor(getResources().getColor(R.color.pink));
                break;
        }
    }

    private void changeTextColor(){
        switch (randomTrueColor){
            case 1:
                textViewColor.setText(getResources().getString(R.string.purple));
                break;
            case 2:
                textViewColor.setText(getResources().getString(R.string.green));
                break;
            case 3:
                textViewColor.setText(getResources().getString(R.string.red));
                break;
            case 4:
                textViewColor.setText(getResources().getString(R.string.yellow));
                break;
            case 5:
                textViewColor.setText(getResources().getString(R.string.blue));
                break;
            case 6:
                textViewColor.setText(getResources().getString(R.string.brown));
                break;
            case 7:
                textViewColor.setText(getResources().getString(R.string.orange));
                break;
            case 8:
                textViewColor.setText(getResources().getString(R.string.pink));
                break;
        }
    }

    private void randomColors(){
        randomFalseColor = new Random().nextInt(8)+1;
        randomTrueColor = new Random().nextInt(8)+1;
    }

    private void loadRecord(){
        maxPoints = load.getInt("recordv2",0);
        String Points = getResources().getString(R.string.max_points)+" "+maxPoints;
        textViewMaxPoints.setText(Points);
    }

    private void saveRecord(){
        runOnUiThread(()->{
            Toast.makeText(this, "NEW RECORD: " + points, Toast.LENGTH_SHORT).show();
            SharedPreferences.Editor editor = save.edit();
            editor.putInt("recordv2",points);
            editor.apply();
            loadRecord();
        });
    }

    @Override
    public void onStop(){
        super.onStop();
        countDownTimer.cancel();
    }
}