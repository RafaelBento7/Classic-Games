package com.classicgames.myapplication.data.models;

public class TicTacToeModel {

    private final int[][] board;          // 3x3 Grid
    private int currentPlayer;      // X = 1 = player 1; O = 2 = player 2;

    public TicTacToeModel(){
        board = new int[3][3];
        currentPlayer = 1;
    }

    public int getPlayerTurn(){
        return currentPlayer;
    }

    public void changePlayerTurn(){
        this.currentPlayer = currentPlayer == 1 ? 2 : 1;
    }

    public void playerMove(int row, int col){
        this.board[row][col] = currentPlayer;
    }

    public boolean canPlay(int row, int col){
        return board[row][col] == 0;
    }

    public boolean checkWin(){
        // Rows
        for (int i = 0; i < 3; i++){
            if(board[i][0] == board[i][1] && board[i][1] == board[i][2] && board[i][2] != 0) return true;
        }

        // Columns
        for (int i = 0; i < 3; i++){
            if(board[0][i] == board[1][i] && board[1][i] == board[2][i] && board[2][i] != 0) return true;
        }

        // Diagonals
        if (board[0][0] == board[1][1] && board[1][1] == board[2][2] && board[2][2] != 0) return true;
        else return board[0][2] == board[1][1] && board[1][1] == board[2][0] && board[2][0] != 0;
    }

    public boolean checkDraw(){
        for (int i = 0; i < 3; i++){
            for (int j = 0; j < 3; j++){
                if (board[i][j] == 0) return false;
            }
        }
        return true;
    }

    public void resetBoard(){
        for (int i = 0; i < 3; i++){
            for (int j = 0; j < 3; j++){
                board[i][j] = 0;
            }
        }
    }
}
