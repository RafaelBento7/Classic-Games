package com.classicgames.myapplication.data.models;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

/**
 * Unit tests for the Mastermind solving logic. A fake 8-colour palette is injected
 * so no Android Context is needed; the colour values are just distinct ints.
 */
public class MastermindModelTest {

    private static final int[] PALETTE = {1, 2, 3, 4, 5, 6, 7, 8};

    private MastermindModel game;

    @Before
    public void setUp() {
        game = new MastermindModel(PALETTE);
        game.startGame();
    }

    @Test
    public void solutionHasFourUniqueColoursFromPalette() {
        int[] solution = game.getSolution();
        assertEquals(4, solution.length);
        for (int i = 0; i < 4; i++) {
            assertTrue(contains(PALETTE, solution[i]));
            for (int j = i + 1; j < 4; j++) {
                assertNotEquals("colours must be unique", solution[i], solution[j]);
            }
        }
    }

    @Test
    public void exactGuessIsAVictory() {
        playAttempt(game.getSolution());
        assertEquals(4, game.getLastAttemptTruePosition());
        assertEquals(0, game.getLastAttemptWrongPosition());
        assertTrue(game.isVictory());
    }

    @Test
    public void rotatedGuessIsAllWrongPosition() {
        int[] s = game.getSolution();
        playAttempt(new int[]{s[1], s[2], s[3], s[0]}); // every colour present, none placed right
        assertEquals(0, game.getLastAttemptTruePosition());
        assertEquals(4, game.getLastAttemptWrongPosition());
        assertFalse(game.isVictory());
    }

    @Test
    public void partialGuessCountsBothCorrectAndMisplaced() {
        int[] s = game.getSolution();
        playAttempt(new int[]{s[0], s[1], s[3], s[2]}); // first two right, last two swapped
        assertEquals(2, game.getLastAttemptTruePosition());
        assertEquals(2, game.getLastAttemptWrongPosition());
    }

    @Test
    public void attemptCounterIncrementsPerCheck() {
        assertEquals(0, game.getAttempt());
        playAttempt(new int[]{PALETTE[0], PALETTE[0], PALETTE[0], PALETTE[0]});
        assertEquals(1, game.getAttempt());
    }

    @Test
    public void gameIsOverAfterTenAttempts() {
        for (int i = 0; i < 10; i++) {
            assertFalse(game.isGameOver());
            playAttempt(new int[]{PALETTE[0], PALETTE[0], PALETTE[0], PALETTE[0]});
        }
        assertTrue(game.isGameOver());
    }

    private void playAttempt(int[] colors) {
        for (int color : colors) game.play(color);
        game.checkSolution();
    }

    private static boolean contains(int[] array, int value) {
        for (int v : array) if (v == value) return true;
        return false;
    }
}
