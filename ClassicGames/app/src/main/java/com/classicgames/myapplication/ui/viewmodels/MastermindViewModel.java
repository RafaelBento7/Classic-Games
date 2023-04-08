package com.classicgames.myapplication.ui.viewmodels;

import android.os.Handler;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.classicgames.myapplication.data.models.MastermindAttempt;
import com.classicgames.myapplication.data.models.MastermindModel;

import java.util.ArrayList;

public class MastermindViewModel extends ViewModel {
    private final MastermindModel game;
    private Handler timerHandler;
    private Runnable timerRunnable;
    private final ArrayList<MastermindAttempt> mastermindAttempts;
    private boolean newRecord;

    private final MutableLiveData<Integer> seconds, minutes, attempt, currentAttemptTruePosition, currentAttemptWrongPosition;
    private final MutableLiveData<Boolean> gameOver, win;

    public MastermindViewModel() {
        game = new MastermindModel();
        mastermindAttempts = new ArrayList<>();

        seconds = new MutableLiveData<>();
        minutes = new MutableLiveData<>();
        attempt = new MutableLiveData<>();
        currentAttemptTruePosition = new MutableLiveData<>();
        currentAttemptWrongPosition = new MutableLiveData<>();
        gameOver = new MutableLiveData<>();
        win = new MutableLiveData<>();
    }

    private void initTimer() {
        seconds.setValue(0);
        minutes.setValue(0);
        if (timerHandler != null) timerHandler.removeCallbacks(timerRunnable);
        timerHandler = new Handler();
        timerRunnable = new Runnable() {
            @Override
            public void run() {
                if (seconds.getValue() == null || minutes.getValue() == null) return;
                seconds.setValue(seconds.getValue() + 1);
                if (seconds.getValue() == 60) {
                    minutes.setValue(minutes.getValue() + 1);
                    seconds.setValue(0);
                }
                timerHandler.postDelayed(this, 1000);
            }
        };
        timerHandler.post(timerRunnable);
    }

    public void setNewAttemptImages(ImageView first, ImageView second, ImageView third,
                                    ImageView forth, TextView truePosition, TextView wrongPosition) {
        mastermindAttempts.add(new MastermindAttempt(first, second, third, forth, truePosition, wrongPosition));
    }

    public void buttonPressed(int color) {
        game.play(color);
        if (game.getColorsPicked() == 4) {
            game.checkSolution();
            currentAttemptWrongPosition.setValue(game.getLastAttemptWrongPosition());
            currentAttemptTruePosition.setValue(game.getLastAttemptTruePosition());
            if (game.isVictory()) win();
            else {
                attempt.setValue(game.getAttempt());
                if (game.isGameOver())
                    gameOver();
            }
        }
    }

    public MastermindAttempt getCurrentMastermindAttempt() {
        return mastermindAttempts.get(game.getAttempt());
    }

    public MastermindAttempt getMastermindAttempt(int position) {
        return mastermindAttempts.get(position);
    }

    public void startGame() {
        gameOver.setValue(false);
        win.setValue(false);
        newRecord = false;
        attempt.setValue(0);
        initTimer();
        game.startGame();
    }

    public void gameOver() {
        timerHandler.removeCallbacks(timerRunnable);
        gameOver.setValue(true);
    }

    public void win() {
        timerHandler.removeCallbacks(timerRunnable);
        if (game.isNewRecord(seconds.getValue(), minutes.getValue(), attempt.getValue()))
            newRecord = true;
        win.setValue(true);
    }

    public boolean isNewRecord() {
        return newRecord;
    }

    public int[] getRecords() {
        return game.getRecords();
    }

    public int getColorPicked() {
        return game.getColorsPicked();
    }

    public int[] getSolution() {
        return game.getSolution();
    }

    public LiveData<Integer> getSeconds() {
        return this.seconds;
    }

    public LiveData<Integer> getMinutes() {
        return this.minutes;
    }

    public LiveData<Integer> getAttempt() {
        return this.attempt;
    }

    public LiveData<Integer> getCurrentAttemptTruePosition() {
        return this.currentAttemptTruePosition;
    }

    public LiveData<Integer> getCurrentAttemptWrongPosition() {
        return this.currentAttemptWrongPosition;
    }

    public LiveData<Boolean> isGameOver() {
        return this.gameOver;
    }

    public LiveData<Boolean> isWin() {
        return this.win;
    }
}
