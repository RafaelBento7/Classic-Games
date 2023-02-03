package com.classicgames.myapplication.ui.viewmodels;

import android.content.res.ColorStateList;
import android.os.CountDownTimer;
import android.view.View;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.classicgames.myapplication.data.models.TrueColorsModel;

public class TrueColorsViewModel extends ViewModel {
    private final TrueColorsModel game;

    private final MutableLiveData<Integer> progress, trueColor, falseColor, correctColorsPicked, points, lives, record;
    private final MutableLiveData<Boolean> gameOver;
    private CountDownTimer countDownTimer;

    public TrueColorsViewModel(){
        game = new TrueColorsModel();

        progress = new MutableLiveData<>();
        trueColor = new MutableLiveData<>();
        falseColor = new MutableLiveData<>();
        correctColorsPicked = new MutableLiveData<>();
        points = new MutableLiveData<>();
        lives = new MutableLiveData<>();
        gameOver = new MutableLiveData<>();
        record = new MutableLiveData<>();

        record.setValue(game.getRecord());
        correctColorsPicked.setValue(0);
        points.setValue(0);
    }

    public void startGame(){
        game.startGame();
        lives.setValue(game.getLives());
        gameOver.setValue(false);
        startRound();
    }

    public void getModelColors(){
        trueColor.setValue(game.getTrueColor());
        falseColor.setValue(game.getFalseColor());
    }

    private void startRound() {
        getModelColors();
        progress.setValue(100);
        int levelMilSec = game.getCurrentLevelMilSec();
        countDownTimer = new CountDownTimer(levelMilSec, levelMilSec/10) {

            @Override
            public void onTick(long leftTimeInMilliseconds) {
                progress.setValue(progress.getValue() - 10);
            }
            @Override
            public void onFinish() {
                loseLife();
            }
        }.start();
    }

    public void stopTimer(){
        countDownTimer.cancel();
    }

    public void colorPressed(View view){
        ColorStateList colorStateList = view.getBackgroundTintList();
        int color = colorStateList.getDefaultColor();
        System.out.println(color);
        stopTimer();
        if (game.scoredPoint(color)) winPoint();
        else loseLife();
    }

    private void winPoint(){
        game.winPoint(progress.getValue());
        getModelColors();
        points.setValue(game.getPoints());
        correctColorsPicked.setValue(game.getCorrectColorsPicked());
        startRound();
    }

    private void loseLife(){
        game.loseLife();

        if (game.isGameOver()) gameOver();
        else {
            lives.setValue(game.getLives());
            startRound();
        }
    }

    private void gameOver(){
        if (game.isNewRecord()){
            record.setValue(game.getRecord());
        }
        gameOver.setValue(true);
    }

    public LiveData<Integer> getProgress(){
        return progress;
    }

    public LiveData<Integer> getTrueColor(){
        return trueColor;
    }

    public LiveData<Integer> getFalseColor(){
        return falseColor;
    }

    public LiveData<Integer> getCorrectColorsPicked(){
        return correctColorsPicked;
    }

    public LiveData<Integer> getPoints(){
        return points;
    }

    public LiveData<Integer> getLives(){
        return lives;
    }

    public LiveData<Integer> getRecord(){
        return record;
    }

    public LiveData<Boolean> isGameOver(){
        return gameOver;
    }
}
