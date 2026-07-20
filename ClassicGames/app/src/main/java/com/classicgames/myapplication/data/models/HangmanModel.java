package com.classicgames.myapplication.data.models;

import com.classicgames.myapplication.MyApplication;

import java.util.Locale;

/**
 * Endless hangman: the player guesses one word after another, scoring a point
 * for each word solved. The gallows resets on every new word, so a run only
 * ends when a single word is missed {@link #MAX_WRONG} times.
 */
public class HangmanModel {

    /** Wrong guesses allowed per word. Matches hangman_0..hangman_6 drawables. */
    public static final int MAX_WRONG = 6;

    private static final int ALPHABET = 26;

    private char[] answer;
    private final boolean[] guessed = new boolean[ALPHABET];
    private int wrongGuesses;
    private int points;

    private int minutesRecord, secondsRecord, pointsRecord;
    private boolean recordLoaded;

    private void ensureRecordLoaded() {
        if (recordLoaded) return;
        int[] record = MyApplication.getInstance().getRecords().getHangmanRecord();
        minutesRecord = record[0];
        secondsRecord = record[1];
        pointsRecord = record[2];
        recordLoaded = true;
    }

    public void StartGame() {
        points = 0;
        NewWord();
    }

    /** Clears the gallows and the guessed letters, keeping the current score. */
    public void NewWord() {
        wrongGuesses = 0;
        for (int i = 0; i < ALPHABET; i++) guessed[i] = false;
    }

    public void SetAnswer(String word) {
        answer = word.toUpperCase(Locale.ROOT).toCharArray();
    }

    /**
     * Registers a guess.
     *
     * @return true when the letter is in the answer. Letters already guessed
     * are ignored and reported as correct so they never cost a life.
     */
    public boolean GuessLetter(char letter) {
        int index = Index(letter);
        if (index < 0 || guessed[index]) return true;

        guessed[index] = true;
        if (Contains(letter)) return true;

        wrongGuesses++;
        return false;
    }

    public boolean IsLetterGuessed(char letter) {
        int index = Index(letter);
        return index >= 0 && guessed[index];
    }

    public boolean Contains(char letter) {
        for (char c : answer) {
            if (c == letter) return true;
        }
        return false;
    }

    /** True once every letter of the answer has been guessed. */
    public boolean IsWordSolved() {
        for (char c : answer) {
            if (!guessed[Index(c)]) return false;
        }
        return true;
    }

    /** True when the current word ran out of lives, which ends the run. */
    public boolean IsGameOver() {
        return wrongGuesses >= MAX_WRONG;
    }

    /** The answer with unguessed letters hidden, e.g. "_ R A _ G _". */
    public String GetMaskedWord() {
        StringBuilder masked = new StringBuilder();
        for (int i = 0; i < answer.length; i++) {
            if (i > 0) masked.append(' ');
            masked.append(guessed[Index(answer[i])] ? answer[i] : '_');
        }
        return masked.toString();
    }

    public boolean IsNewRecord(int points, int minutes, int seconds) {
        ensureRecordLoaded();
        if (points == 0) return false;

        if (points > pointsRecord) return true;
        else if (points < pointsRecord) return false;

        if (minutes > minutesRecord) return false;
        else if (minutes < minutesRecord) return true;

        return seconds < secondsRecord;
    }

    public void SaveRecord(int points, int minutes, int seconds) {
        MyApplication.getInstance().getRecords().setHangmanRecord(minutes, seconds, points);
        pointsRecord = points;
        secondsRecord = seconds;
        minutesRecord = minutes;
        recordLoaded = true;
    }

    private int Index(char letter) {
        int index = letter - 'A';
        return index >= 0 && index < ALPHABET ? index : -1;
    }

    public String GetAnswer() {
        return new String(answer);
    }

    public int GetWrongGuesses() {
        return wrongGuesses;
    }

    public int GetPoints() {
        return points;
    }

    public void SetPoints(int points) {
        this.points = points;
    }
}
