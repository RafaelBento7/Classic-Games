package com.classicgames.myapplication.data.models;

import android.widget.ImageView;
import android.widget.TextView;

public class MastermindAttempt {
    private ImageView first, second, third, forth;
    private TextView truePosition, wrongPosition;

    // Attempt Constructor
    public MastermindAttempt(ImageView first, ImageView second, ImageView third, ImageView forth, TextView truePosition, TextView wrongPosition){
        this.setFirst(first);
        this.setSecond(second);
        this.setThird(third);
        this.setForth(forth);
        this.setTruePosition(truePosition);
        this.setWrongPosition(wrongPosition);
    }

    public ImageView getFirst() {
        return first;
    }

    public void setFirst(ImageView first) {
        this.first = first;
    }

    public ImageView getSecond() {
        return second;
    }

    public void setSecond(ImageView second) {
        this.second = second;
    }

    public ImageView getThird() {
        return third;
    }

    public void setThird(ImageView third) {
        this.third = third;
    }

    public ImageView getForth() {
        return forth;
    }

    public void setForth(ImageView forth) {
        this.forth = forth;
    }

    public TextView getTruePosition() {
        return truePosition;
    }

    public void setTruePosition(TextView truePosition) {
        this.truePosition = truePosition;
    }

    public TextView getWrongPosition() {
        return wrongPosition;
    }

    public void setWrongPosition(TextView wrongPosition) {
        this.wrongPosition = wrongPosition;
    }
}
