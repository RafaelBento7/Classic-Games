package com.classicgames.myapplication.data.models;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

/** Unit tests for the pure Tic-Tac-Toe game logic. */
public class TicTacToeModelTest {

    @Test
    public void startsWithPlayerOne() {
        assertEquals(1, new TicTacToeModel().getPlayerTurn());
    }

    @Test
    public void changePlayerTurnToggles() {
        TicTacToeModel model = new TicTacToeModel();
        model.changePlayerTurn();
        assertEquals(2, model.getPlayerTurn());
        model.changePlayerTurn();
        assertEquals(1, model.getPlayerTurn());
    }

    @Test
    public void canPlayOnEmptyCellButNotOnOccupied() {
        TicTacToeModel model = new TicTacToeModel();
        assertTrue(model.canPlay(0, 0));
        model.playerMove(0, 0);
        assertFalse(model.canPlay(0, 0));
    }

    @Test
    public void detectsRowWin() {
        TicTacToeModel model = new TicTacToeModel();
        model.playerMove(1, 0);
        model.playerMove(1, 1);
        model.playerMove(1, 2);
        assertTrue(model.checkWin());
    }

    @Test
    public void detectsColumnWin() {
        TicTacToeModel model = new TicTacToeModel();
        model.playerMove(0, 2);
        model.playerMove(1, 2);
        model.playerMove(2, 2);
        assertTrue(model.checkWin());
    }

    @Test
    public void detectsMainDiagonalWin() {
        TicTacToeModel model = new TicTacToeModel();
        model.playerMove(0, 0);
        model.playerMove(1, 1);
        model.playerMove(2, 2);
        assertTrue(model.checkWin());
    }

    @Test
    public void detectsAntiDiagonalWin() {
        TicTacToeModel model = new TicTacToeModel();
        model.playerMove(0, 2);
        model.playerMove(1, 1);
        model.playerMove(2, 0);
        assertTrue(model.checkWin());
    }

    @Test
    public void noWinOnEmptyBoard() {
        assertFalse(new TicTacToeModel().checkWin());
    }

    @Test
    public void mixedLineIsNotAWin() {
        TicTacToeModel model = new TicTacToeModel();
        model.playerMove(0, 0);          // player 1
        model.changePlayerTurn();
        model.playerMove(0, 1);          // player 2
        model.changePlayerTurn();
        model.playerMove(0, 2);          // player 1
        assertFalse(model.checkWin());
    }

    @Test
    public void fullBoardWithoutLineIsADraw() {
        TicTacToeModel model = new TicTacToeModel();
        // X O X
        // X X O
        // O X O   -> board full, no three-in-a-row
        int[][] layout = {
                {1, 2, 1},
                {1, 1, 2},
                {2, 1, 2}
        };
        for (int r = 0; r < 3; r++) {
            for (int c = 0; c < 3; c++) {
                if (layout[r][c] == 2) model.changePlayerTurn();
                model.playerMove(r, c);
                if (layout[r][c] == 2) model.changePlayerTurn();
            }
        }
        assertTrue(model.checkDraw());
        assertFalse(model.checkWin());
    }

    @Test
    public void emptyBoardIsNotADraw() {
        assertFalse(new TicTacToeModel().checkDraw());
    }

    @Test
    public void resetBoardClearsEveryCell() {
        TicTacToeModel model = new TicTacToeModel();
        model.playerMove(0, 0);
        model.playerMove(2, 2);
        model.resetBoard();
        for (int r = 0; r < 3; r++) {
            for (int c = 0; c < 3; c++) {
                assertTrue(model.canPlay(r, c));
            }
        }
    }
}
