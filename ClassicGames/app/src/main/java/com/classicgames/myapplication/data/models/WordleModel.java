package com.classicgames.myapplication.data.models;

import com.classicgames.myapplication.MyApplication;

public class WordleModel {

    public enum LETTER_STATE {
        NONE,
        WRONG_POSITION,
        CORRECT,
    }

    private char[] answer;
    private int attempt;
    private int points;

    private int minutesRecord, secondsRecord, pointsRecord;

    public WordleModel() {
        pointsRecord = MyApplication.getInstance().getRecords().getWordleRecord()[2];
        secondsRecord = MyApplication.getInstance().getRecords().getWordleRecord()[1];
        minutesRecord = MyApplication.getInstance().getRecords().getWordleRecord()[0];

    }

    public void StartGame() {
        attempt = 0;
        SetPoints(0);
    }

    public boolean CheckWin(char[] letters) {
        LETTER_STATE[] userAnswer = GetAttemptStatus(letters);
        return userAnswer[0] == LETTER_STATE.CORRECT &&
                userAnswer[1] == LETTER_STATE.CORRECT &&
                userAnswer[2] == LETTER_STATE.CORRECT &&
                userAnswer[3] == LETTER_STATE.CORRECT &&
                userAnswer[4] == LETTER_STATE.CORRECT;
    }

    public LETTER_STATE[] GetAttemptStatus(char[] letters) {
        LETTER_STATE[] states = new LETTER_STATE[5];

        for (int i = 0; i < 5; i++) {
            // If same letter same position
            if (letters[i] == Character.toUpperCase(answer[i])) {
                states[i] = LETTER_STATE.CORRECT;
                continue;
            }
            // If Same letter diff position
            for (int j = 0; j < 5; j++) {
                if (letters[i] == Character.toUpperCase(answer[j])) {
                    states[i] = LETTER_STATE.WRONG_POSITION;
                    break;
                }
            }
            // If diff position and diff letter
            if (states[i] != LETTER_STATE.CORRECT && states[i] != LETTER_STATE.WRONG_POSITION)
                states[i] = LETTER_STATE.NONE;
        }
        return states;
    }

    public boolean IsNewRecord(int points, int minutes, int seconds) {
        if (points == 0) return false;

        if (points > pointsRecord) return true;
        else if (points < pointsRecord) return false;

        if (minutes > minutesRecord) return false;
        else if (minutes < minutesRecord) return true;

        return seconds < secondsRecord;
    }

    public void SaveRecord(int points, int minutes, int seconds) {
        MyApplication.getInstance().getRecords().setWordleRecord(minutes, seconds, points);
        pointsRecord = points;
        secondsRecord = seconds;
        minutesRecord = minutes;
    }

    public boolean IsGameOver() {
        return attempt >= 4;
    }

    public int GetAttempt() {
        return attempt;
    }

    public void SetAttempt(int attempt) {
        this.attempt = attempt;
    }

    public String GetAnswer() {
        return new String(this.answer);
    }

    public void SetAnswer(String word) {
        char[] wordChars = word.toCharArray();
        answer = new char[5];
        this.answer[0] = wordChars[0];
        this.answer[1] = wordChars[1];
        this.answer[2] = wordChars[2];
        this.answer[3] = wordChars[3];
        this.answer[4] = wordChars[4];
    }

    public int GetPoints() {
        return points;
    }

    public void SetPoints(int points) {
        this.points = points;
    }

}
