package com.classicgames.myapplication.data.models;

public class SnakePiece {
    private int positionX, positionY;
    public SnakePiece(int x, int y){
        this.positionX = x;
        this.positionY = y;
    }

    public int getPositionX() {
        return positionX;
    }

    public void setPositionX(int positionX) {
        this.positionX = positionX;
    }

    public int getPositionY() {
        return positionY;
    }

    public void setPositionY(int positionY) {
        this.positionY = positionY;
    }
}
