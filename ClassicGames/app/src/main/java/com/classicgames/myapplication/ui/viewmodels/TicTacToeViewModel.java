package com.classicgames.myapplication.ui.viewmodels;

import androidx.lifecycle.ViewModel;

import com.classicgames.myapplication.data.models.TicTacToeModel;

public class TicTacToeViewModel extends ViewModel {
    private final TicTacToeModel game;

    public TicTacToeViewModel(){
        game = new TicTacToeModel();
    }

    public int getPlayerTurn(){
        return game.getPlayerTurn();
    }

    public void newPlayerMove(int row, int col){
        game.playerMove(row, col);
    }

    public boolean checkWin(){
        return game.checkWin();
    }

    public boolean checkDraw(){
        return game.checkDraw();
    }

    public void changePlayerTurn(){
        game.changePlayerTurn();
    }

    public boolean canPlay(int row, int col){
        return game.canPlay(row , col);
    }

    public void setNewGame(){
        game.resetBoard();
    }
}
