package com.classicgames.myapplication.ui.viewmodels;

import android.os.Handler;
import android.os.Looper;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.classicgames.myapplication.data.models.HangmanModel;

public class HangmanViewModel extends ViewModel {

    /** What a single letter press did to the run. */
    public enum GUESS_RESULT {
        ALREADY_GUESSED,
        CORRECT,
        WRONG,
        WORD_SOLVED,
        GAME_OVER,
    }

    private final HangmanModel game;
    private Handler timerHandler;
    private Runnable timerRunnable;

    private final MutableLiveData<Integer> seconds, minutes, points, wrongGuesses;
    private final MutableLiveData<String> maskedWord;

    // Per-run totals, reported to the lifetime stats when the run ends.
    private int wordsSolved;
    private int wrongLetters;
    private int perfectWords;

    public HangmanViewModel() {
        game = new HangmanModel();

        seconds = new MutableLiveData<>();
        minutes = new MutableLiveData<>();
        points = new MutableLiveData<>();
        wrongGuesses = new MutableLiveData<>();
        maskedWord = new MutableLiveData<>();
    }

    private void InitTimer() {
        seconds.setValue(0);
        minutes.setValue(0);
        if (timerHandler != null) timerHandler.removeCallbacks(timerRunnable);
        timerHandler = new Handler(Looper.getMainLooper());
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
        wordsSolved = 0;
        wrongLetters = 0;
        perfectWords = 0;
        points.setValue(game.GetPoints());
        wrongGuesses.setValue(game.GetWrongGuesses());
    }

    /** Puts a fresh word on the board and clears the gallows. */
    public void SetNewAnswer(String word) {
        game.SetAnswer(word);
        game.NewWord();
        wrongGuesses.setValue(game.GetWrongGuesses());
        maskedWord.setValue(game.GetMaskedWord());
    }

    public GUESS_RESULT GuessLetter(char letter) {
        if (game.IsLetterGuessed(letter)) return GUESS_RESULT.ALREADY_GUESSED;

        boolean correct = game.GuessLetter(letter);
        maskedWord.setValue(game.GetMaskedWord());

        if (correct) {
            if (!game.IsWordSolved()) return GUESS_RESULT.CORRECT;

            wordsSolved++;
            if (game.GetWrongGuesses() == 0) perfectWords++;
            game.SetPoints(game.GetPoints() + 1);
            points.setValue(game.GetPoints());
            return GUESS_RESULT.WORD_SOLVED;
        }

        wrongLetters++;
        wrongGuesses.setValue(game.GetWrongGuesses());
        return game.IsGameOver() ? GUESS_RESULT.GAME_OVER : GUESS_RESULT.WRONG;
    }

    public void SkipWord() {
        game.SetPoints(game.GetPoints() - 1);
        points.setValue(game.GetPoints());
    }

    public void GameOver() {
        if (timerHandler != null) timerHandler.removeCallbacks(timerRunnable);
    }

    public boolean IsNewRecord() {
        return game.IsNewRecord(points.getValue(), minutes.getValue(), seconds.getValue());
    }

    public void SaveNewRecord() {
        game.SaveRecord(points.getValue(), minutes.getValue(), seconds.getValue());
    }

    public String GetAnswer() {
        return game.GetAnswer();
    }

    public int GetWordsSolved() {
        return wordsSolved;
    }

    public int GetWrongLetters() {
        return wrongLetters;
    }

    public int GetPerfectWords() {
        return perfectWords;
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

    public LiveData<Integer> GetWrongGuesses() {
        return this.wrongGuesses;
    }

    public LiveData<String> GetMaskedWord() {
        return this.maskedWord;
    }

    public LiveData<Integer> GetSeconds() {
        return this.seconds;
    }

    public LiveData<Integer> GetMinutes() {
        return this.minutes;
    }
}
