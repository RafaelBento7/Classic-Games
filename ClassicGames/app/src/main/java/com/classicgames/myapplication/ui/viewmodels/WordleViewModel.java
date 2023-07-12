package com.classicgames.myapplication.ui.viewmodels;

import android.os.Handler;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.classicgames.myapplication.data.models.WordleModel;

public class WordleViewModel extends ViewModel {
    private final WordleModel game;
    private Handler timerHandler;
    private Runnable timerRunnable;

    private char[] currentAttempt;
    private int currentAttemptPos;

    private final MutableLiveData<Integer> seconds, minutes, points;

    public WordleViewModel() {
        game = new WordleModel();

        seconds = new MutableLiveData<>();
        minutes = new MutableLiveData<>();
        points = new MutableLiveData<>();
    }

    private void InitTimer() {
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

    public void StartGame() {
        InitTimer();

        game.StartGame();
        currentAttempt = new char[5];
        currentAttemptPos = 0;
        game.SetPoints(0);
        points.setValue(game.GetPoints());
    }

    public void SetNewAnswer(String word) {
        game.SetAnswer(word);
    }

    public void KeyPressed(char letter) {
        // if the char is backspace
        if ((int) letter > 100) {
            if (currentAttemptPos <= 0) return;

            currentAttempt[currentAttemptPos - 1] = ' ';
            currentAttemptPos--;
            return;
        }

        // if can't write another letter
        if (currentAttemptPos >= 5)
            return;

        currentAttempt[currentAttemptPos] = letter;
        currentAttemptPos++;
    }

    public void SetNextAttempt() {
        game.SetAttempt(game.GetAttempt() + 1);
        currentAttemptPos = 0;
        currentAttempt = new char[5];
    }

    public void WinRound() {
        NewRound();
        game.SetPoints(game.GetPoints() + 1);
        points.setValue(game.GetPoints());
    }

    public void NewRound() {
        currentAttemptPos = 0;
        currentAttempt = new char[5];
        game.SetAttempt(0);
    }

    public void SkipRound() {
        NewRound();
        game.SetPoints(game.GetPoints() - 1);
        points.setValue(game.GetPoints());
    }

    public void GameOver() {
        timerHandler.removeCallbacks(timerRunnable);
    }

    public boolean IsNewRecord() {
        return game.IsNewRecord(points.getValue(), minutes.getValue(), seconds.getValue());
    }

    public void SaveNewRecord() {
        game.SaveRecord(points.getValue(), minutes.getValue(), seconds.getValue());
    }

    public boolean IsWin() {
        return game.CheckWin(currentAttempt);
    }

    public boolean IsGameOver() {
        return game.IsGameOver();
    }

    public String GetAnswer() {
        return game.GetAnswer();
    }

    public char[] GetCurrentAttempt() {
        return currentAttempt;
    }

    public int GetAttempt() {
        return game.GetAttempt();
    }

    public int GetCurrentAttemptPos() {
        return this.currentAttemptPos;
    }

    public WordleModel.LETTER_STATE[] GetCurrentAttemptStatus() {
        return game.GetAttemptStatus(currentAttempt);
    }

    public String GetTimer() {
        int min = minutes.getValue();
        String minStr;
        if (min < 10) minStr = "0" + min;
        else minStr = String.valueOf(min);

        int sec = seconds.getValue();
        String secStr;
        if (sec < 10) secStr = "0" + sec;
        else secStr = String.valueOf(sec);

        return minStr + ":" + secStr;
    }

    public LiveData<Integer> GetPoints() {
        return this.points;
    }

    public LiveData<Integer> GetSeconds() {
        return this.seconds;
    }

    public LiveData<Integer> GetMinutes() {
        return this.minutes;
    }
}
