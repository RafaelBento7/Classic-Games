package com.classicgames.myapplication.data.models;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import java.util.Locale;

/** Unit tests for the pure Hangman guessing logic (no Android dependencies). */
public class HangmanModelTest {

    private static HangmanModel modelWith(String answer) {
        HangmanModel model = new HangmanModel();
        model.NewWord();
        model.SetAnswer(answer);
        return model;
    }

    /** Feeds a string of letters into the model one guess at a time. */
    private static void guessAll(HangmanModel model, String letters) {
        for (char c : letters.toCharArray()) model.GuessLetter(c);
    }

    @Test
    public void hidesEveryLetterBeforeAnyGuess() {
        HangmanModel model = modelWith("stare");
        assertEquals("_ _ _ _ _", model.GetMaskedWord());
        assertFalse(model.IsWordSolved());
        assertFalse(model.IsGameOver());
    }

    @Test
    public void revealsEveryOccurrenceOfAGuessedLetter() {
        HangmanModel model = modelWith("banana");
        assertTrue(model.GuessLetter('A'));
        assertEquals("_ A _ A _ A", model.GetMaskedWord());
        assertEquals(0, model.GetWrongGuesses());
    }

    @Test
    public void lowerCaseAnswersAreMatchedByUpperCaseGuesses() {
        HangmanModel model = modelWith("stare"); // the dictionary file is lower-case
        assertTrue(model.GuessLetter('S'));
        assertEquals("S _ _ _ _", model.GetMaskedWord());
    }

    @Test
    public void wrongLetterCostsALife() {
        HangmanModel model = modelWith("stare");
        assertFalse(model.GuessLetter('Z'));
        assertEquals(1, model.GetWrongGuesses());
        assertEquals("_ _ _ _ _", model.GetMaskedWord());
    }

    @Test
    public void repeatedWrongLetterIsFreeAndDoesNotCostASecondLife() {
        HangmanModel model = modelWith("stare");
        model.GuessLetter('Z');
        assertTrue(model.GuessLetter('Z')); // ignored, reported as correct
        assertEquals(1, model.GetWrongGuesses());
    }

    @Test
    public void solvingEveryLetterWinsTheWord() {
        HangmanModel model = modelWith("stare");
        guessAll(model, "STARE");
        assertTrue(model.IsWordSolved());
        assertEquals("S T A R E", model.GetMaskedWord());
        assertFalse(model.IsGameOver());
    }

    @Test
    public void gameIsOverOnTheSixthWrongGuess() {
        HangmanModel model = modelWith("stare");
        guessAll(model, "ZXQVB"); // five misses
        assertEquals(5, model.GetWrongGuesses());
        assertFalse(model.IsGameOver());

        model.GuessLetter('N'); // sixth
        assertEquals(HangmanModel.MAX_WRONG, model.GetWrongGuesses());
        assertTrue(model.IsGameOver());
    }

    @Test
    public void newWordClearsTheGallowsAndTheGuessedLetters() {
        HangmanModel model = modelWith("stare");
        guessAll(model, "SZX");
        assertEquals(2, model.GetWrongGuesses());

        model.NewWord();
        model.SetAnswer("crane");
        assertEquals(0, model.GetWrongGuesses());
        assertEquals("_ _ _ _ _", model.GetMaskedWord());
        assertFalse(model.IsLetterGuessed('S'));
    }

    @Test
    public void newWordKeepsTheScore() {
        HangmanModel model = modelWith("stare");
        model.SetPoints(7);
        model.NewWord();
        assertEquals(7, model.GetPoints());
    }

    /**
     * The answer must be upper-cased with Locale.ROOT. Under the Turkish locale
     * the default toUpperCase() maps "i" to the dotted "İ", which would never
     * match the plain 'I' the keyboard sends, making those words unwinnable.
     */
    @Test
    public void wordsWithIAreSolvableUnderTheTurkishLocale() {
        Locale original = Locale.getDefault();
        try {
            Locale.setDefault(new Locale("tr", "TR"));
            HangmanModel model = modelWith("stir");
            guessAll(model, "STIR");
            assertEquals(0, model.GetWrongGuesses());
            assertTrue(model.IsWordSolved());
        } finally {
            Locale.setDefault(original);
        }
    }

    @Test
    public void nonLetterKeysAreIgnored() {
        HangmanModel model = modelWith("stare");
        assertTrue(model.GuessLetter('1'));
        assertEquals(0, model.GetWrongGuesses());
    }
}
