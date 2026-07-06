package com.classicgames.myapplication.ui.viewmodels;

import android.content.res.ColorStateList;
import android.os.CountDownTimer;
import android.view.View;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.classicgames.myapplication.MyApplication;
import com.classicgames.myapplication.data.models.TrueColorsModel;
import com.classicgames.myapplication.utils.SoundManager;

public class TrueColorsViewModel extends ViewModel {
    private final TrueColorsModel game;

    private final MutableLiveData<Integer> progress, trueColor, falseColor, correctColorsPicked, points, lives, record;
    private final MutableLiveData<Boolean> gameOver;
    private CountDownTimer countDownTimer;
    private boolean newRecord;

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
    }

    public void startGame(){
        game.startGame();
        correctColorsPicked.setValue(0);
        points.setValue(0);
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
        if (lives.getValue() == null || lives.getValue() == 0) {
            return;
        }
        ColorStateList colorStateList = view.getBackgroundTintList();
        int color = colorStateList.getDefaultColor();
        stopTimer();
        if (game.scoredPoint(color)) winPoint();
        else loseLife();
    }

    private void winPoint(){
        SoundManager.play(SoundManager.Sound.CORRECT);
        game.winPoint(progress.getValue());
        getModelColors();
        points.setValue(game.getPoints());
        correctColorsPicked.setValue(game.getCorrectColorsPicked());
        startRound();
    }

    private void loseLife(){
        SoundManager.play(SoundManager.Sound.WRONG);
        game.loseLife();
        lives.setValue(game.getLives());

        if (game.isGameOver()) gameOver();
        else {
            startRound();
        }
    }

    private void gameOver(){
        newRecord = game.isNewRecord();
        SoundManager.play(newRecord ? SoundManager.Sound.RECORD : SoundManager.Sound.GAME_OVER);
        if (newRecord){
            record.setValue(game.getRecord());
        }
        if (MyApplication.getInstance() != null) {
            MyApplication.getInstance().getRecords().recordTrueColorsGame(game.getCorrectColorsPicked(), game.getPoints());
        }
        gameOver.setValue(true);
    }

    public boolean isNewRecord(){
        return newRecord;
    }

    public int getPointsValue(){
        return game.getPoints();
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
