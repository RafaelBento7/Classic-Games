package com.classicgames.myapplication.data.models;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.classicgames.myapplication.data.models.WordleModel.LETTER_STATE;

import org.junit.Test;

/** Unit tests for the pure Wordle scoring logic (no Android dependencies). */
public class WordleModelTest {

    private static char[] guess(String word) {
        return word.toUpperCase().toCharArray();
    }

    @Test
    public void exactMatchIsAllCorrectAndAWin() {
        WordleModel model = new WordleModel();
        model.SetAnswer("STARE");
        LETTER_STATE[] states = model.GetAttemptStatus(guess("STARE"));
        assertArrayEquals(new LETTER_STATE[]{
                LETTER_STATE.CORRECT, LETTER_STATE.CORRECT, LETTER_STATE.CORRECT,
                LETTER_STATE.CORRECT, LETTER_STATE.CORRECT
        }, states);
        assertTrue(model.CheckWin(guess("STARE")));
    }

    @Test
    public void answerIsCaseInsensitive() {
        WordleModel model = new WordleModel();
        model.SetAnswer("stare"); // lower-case answer from the dictionary file
        assertTrue(model.CheckWin(guess("STARE")));
    }

    @Test
    public void noSharedLettersAreAllNone() {
        WordleModel model = new WordleModel();
        model.SetAnswer("STARE");
        LETTER_STATE[] states = model.GetAttemptStatus(guess("BLIND"));
        assertArrayEquals(new LETTER_STATE[]{
                LETTER_STATE.NONE, LETTER_STATE.NONE, LETTER_STATE.NONE,
                LETTER_STATE.NONE, LETTER_STATE.NONE
        }, states);
        assertFalse(model.CheckWin(guess("BLIND")));
    }

    @Test
    public void anagramIsAllWrongPosition() {
        WordleModel model = new WordleModel();
        model.SetAnswer("STARE");
        LETTER_STATE[] states = model.GetAttemptStatus(guess("RATES"));
        assertArrayEquals(new LETTER_STATE[]{
                LETTER_STATE.WRONG_POSITION, LETTER_STATE.WRONG_POSITION,
                LETTER_STATE.WRONG_POSITION, LETTER_STATE.WRONG_POSITION,
                LETTER_STATE.WRONG_POSITION
        }, states);
    }

    @Test
    public void mixesCorrectAndNone() {
        WordleModel model = new WordleModel();
        model.SetAnswer("STARE");
        LETTER_STATE[] states = model.GetAttemptStatus(guess("STORE"));
        assertArrayEquals(new LETTER_STATE[]{
                LETTER_STATE.CORRECT, LETTER_STATE.CORRECT, LETTER_STATE.NONE,
                LETTER_STATE.CORRECT, LETTER_STATE.CORRECT
        }, states);
        assertFalse(model.CheckWin(guess("STORE")));
    }

    @Test
    public void gameOverAfterFifthAttempt() {
        WordleModel model = new WordleModel();
        model.SetAttempt(3);
        assertFalse(model.IsGameOver());
        model.SetAttempt(4);
        assertTrue(model.IsGameOver());
    }

    /**
     * Documents the current (simplified) duplicate-letter handling: every
     * position whose letter exists anywhere in the answer is highlighted, even
     * when the answer contains only one such letter. Standard Wordle would grey
     * out the surplus duplicates. Pinned here so the behaviour is a deliberate
     * choice rather than an accident.
     */
    @Test
    public void duplicateGuessLettersAllHighlight() {
        WordleModel model = new WordleModel();
        model.SetAnswer("ABIDE"); // a single 'A'
        LETTER_STATE[] states = model.GetAttemptStatus(guess("AAAAA"));
        assertArrayEquals(new LETTER_STATE[]{
                LETTER_STATE.CORRECT, LETTER_STATE.WRONG_POSITION,
                LETTER_STATE.WRONG_POSITION, LETTER_STATE.WRONG_POSITION,
                LETTER_STATE.WRONG_POSITION
        }, states);
    }
}
