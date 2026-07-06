package com.classicgames.myapplication.data.models;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

/**
 * Unit tests for the True Colors logic. A fake palette is injected so no Android
 * Context is needed, and the record store no-ops without MyApplication.
 */
public class TrueColorsModelTest {

    private static final int[] PALETTE = {1, 2, 3, 4, 5, 6, 7, 8};

    private TrueColorsModel game;

    @Before
    public void setUp() {
        game = new TrueColorsModel(PALETTE);
        game.startGame();
    }

    @Test
    public void startGameResetsState() {
        assertEquals(3, game.getLives());
        assertEquals(0, game.getPoints());
        assertEquals(0, game.getCorrectColorsPicked());
        assertEquals(4000, game.getCurrentLevelMilSec());
        assertFalse(game.isGameOver());
    }

    @Test
    public void scoredPointOnlyForTrueColor() {
        int trueColor = game.getTrueColor();
        assertTrue(game.scoredPoint(trueColor));

        int other = trueColor == PALETTE[0] ? PALETTE[1] : PALETTE[0];
        assertFalse(game.scoredPoint(other));
    }

    @Test
    public void losingAllLivesEndsGame() {
        game.loseLife();
        assertEquals(2, game.getLives());
        assertFalse(game.isGameOver());

        game.loseLife();
        game.loseLife();
        assertEquals(0, game.getLives());
        assertTrue(game.isGameOver());
    }

    @Test
    public void winPointAddsScoreAndIncrementsCount() {
        game.winPoint(100); // 100 * 0.88 * 1
        assertEquals(88, game.getPoints());
        assertEquals(1, game.getCorrectColorsPicked());
    }

    @Test
    public void difficultyRampsUpAfterFivePoints() {
        for (int i = 0; i < 5; i++) game.winPoint(10);
        assertEquals(5, game.getCorrectColorsPicked());
        assertEquals(3500, game.getCurrentLevelMilSec());
    }

    @Test
    public void beatingTheRecordIsReported() {
        game.winPoint(100); // 88 points, record starts at 0
        assertTrue(game.isNewRecord());
        assertEquals(88, game.getRecord());
    }

    /**
     * A score equal to the current record is NOT a new record (strict &gt;), and a
     * 0-point game never counts. Pins the corrected behaviour.
     */
    @Test
    public void tiedOrZeroScoreIsNotARecord() {
        game.winPoint(100);
        assertTrue(game.isNewRecord());    // 88 > 0
        assertFalse(game.isNewRecord());   // 88 == 88, no longer a record
    }

    @Test
    public void zeroPointGameIsNotARecord() {
        // Fresh game, no points scored.
        assertFalse(game.isNewRecord());
    }
}
