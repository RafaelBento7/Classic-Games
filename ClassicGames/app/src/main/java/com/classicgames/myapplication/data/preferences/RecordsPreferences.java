package com.classicgames.myapplication.data.preferences;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Single store for everything we persist per game: personal-best <b>records</b>
 * and lifetime <b>stats</b> (games played, wins, streaks, totals). Stats start at
 * 0 on install — there is no historical backfill.
 */
public class RecordsPreferences {

    private static final String PREFS_NAME = "GameRecords";

    private static final String KEY_TRUE_COLORS = "trueColors";
    private static final String KEY_SNAKE_BIG = "snake_big";
    private static final String KEY_SNAKE_MEDIUM = "snake_medium";
    private static final String KEY_SNAKE_SMALL = "snake_small";
    private static final String KEY_MASTERMIND_MINUTES = "mastermind_minutes";
    private static final String KEY_MASTERMIND_SECONDS = "mastermind_seconds";
    private static final String KEY_MASTERMIND_ATTEMPTS = "mastermind_attempts";
    private static final String KEY_WORDLE_MINUTES = "wordle_minutes";
    private static final String KEY_WORDLE_SECONDS = "wordle_seconds";
    private static final String KEY_WORDLE_POINTS = "wordle_points";

    // --- Stats keys ---
    private static final String SNAKE_GAMES = "snake_games";
    private static final String SNAKE_TOTAL_POINTS = "snake_total_points";
    private static final String TRUE_COLORS_GAMES = "true_colors_games";
    private static final String TRUE_COLORS_TOTAL_CORRECT = "true_colors_total_correct";
    private static final String MASTERMIND_GAMES = "mastermind_games";
    private static final String MASTERMIND_WINS = "mastermind_wins";
    private static final String TTT_GAMES = "ttt_games";
    private static final String TTT_P1_WINS = "ttt_p1_wins";
    private static final String TTT_P2_WINS = "ttt_p2_wins";
    private static final String TTT_DRAWS = "ttt_draws";
    private static final String WORDLE_GAMES = "wordle_games";
    private static final String WORDLE_WORDS_GUESSED = "wordle_words_guessed";
    private static final String WORDLE_CURRENT_STREAK = "wordle_current_streak";
    private static final String WORDLE_BEST_STREAK = "wordle_best_streak";
    private static final String SNAKE_GAMES_SMALL_PLAIN = "snake_games_small_plain";
    private static final String SNAKE_GAMES_SMALL_OBSTACLES = "snake_games_small_obstacles";
    private static final String SNAKE_GAMES_MEDIUM_PLAIN = "snake_games_medium_plain";
    private static final String SNAKE_GAMES_MEDIUM_OBSTACLES = "snake_games_medium_obstacles";
    private static final String SNAKE_GAMES_BIG_PLAIN = "snake_games_big_plain";
    private static final String SNAKE_GAMES_BIG_OBSTACLES = "snake_games_big_obstacles";
    private static final String SNAKE_APPLES_EATEN = "snake_apples_eaten";
    private static final String SNAKE_GOLDEN_EATEN = "snake_golden_eaten";
    private static final String TRUE_COLORS_TOTAL_POINTS = "true_colors_total_points";
    private static final String TRUE_COLORS_BEST_CORRECT = "true_colors_best_correct";
    private static final String MASTERMIND_TOTAL_ATTEMPTS = "mastermind_total_attempts";

    private final SharedPreferences sp;

    public RecordsPreferences(Context context) {
        sp = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    public int getTrueColorsRecord() {
        return sp.getInt(KEY_TRUE_COLORS, 0);
    }

    public void setTrueColorsRecord(int newRecord) {
        sp.edit().putInt(KEY_TRUE_COLORS, newRecord).apply();
    }

    public int getSnakeBigRecord() {
        return sp.getInt(KEY_SNAKE_BIG, 0);
    }

    public int getSnakeMediumRecord() {
        return sp.getInt(KEY_SNAKE_MEDIUM, 0);
    }

    public int getSnakeSmallRecord() {
        return sp.getInt(KEY_SNAKE_SMALL, 0);
    }

    public void setSnakeBigRecord(int newRecord) {
        sp.edit().putInt(KEY_SNAKE_BIG, newRecord).apply();
    }

    public void setSnakeMediumRecord(int newRecord) {
        sp.edit().putInt(KEY_SNAKE_MEDIUM, newRecord).apply();
    }

    public void setSnakeSmallRecord(int newRecord) {
        sp.edit().putInt(KEY_SNAKE_SMALL, newRecord).apply();
    }

    public void setMastermindRecord(int minutes, int seconds, int attempts) {
        sp.edit()
                .putInt(KEY_MASTERMIND_MINUTES, minutes)
                .putInt(KEY_MASTERMIND_SECONDS, seconds)
                .putInt(KEY_MASTERMIND_ATTEMPTS, attempts)
                .apply();
    }

    /**
     * @return int[0] = minutes; int[1] = seconds; int[2] = attempts
     */
    public int[] getMastermindRecord() {
        return new int[]{
                sp.getInt(KEY_MASTERMIND_MINUTES, 0),
                sp.getInt(KEY_MASTERMIND_SECONDS, 0),
                sp.getInt(KEY_MASTERMIND_ATTEMPTS, 0)
        };
    }

    public void setWordleRecord(int minutes, int seconds, int points) {
        sp.edit()
                .putInt(KEY_WORDLE_MINUTES, minutes)
                .putInt(KEY_WORDLE_SECONDS, seconds)
                .putInt(KEY_WORDLE_POINTS, points)
                .apply();
    }

    /**
     * @return int[0] = minutes; int[1] = seconds; int[2] = points
     */
    public int[] getWordleRecord() {
        return new int[]{
                sp.getInt(KEY_WORDLE_MINUTES, 0),
                sp.getInt(KEY_WORDLE_SECONDS, 0),
                sp.getInt(KEY_WORDLE_POINTS, 0)
        };
    }

    // ------------------------------------------------------------------
    // Lifetime stats
    // ------------------------------------------------------------------

    private int getStat(String key) {
        return sp.getInt(key, 0);
    }

    private void addStat(String key, int amount) {
        sp.edit().putInt(key, sp.getInt(key, 0) + amount).apply();
    }

    private void setStat(String key, int value) {
        sp.edit().putInt(key, value).apply();
    }

    // Snake
    public int getSnakeGames() { return getStat(SNAKE_GAMES); }
    public int getSnakeTotalPoints() { return getStat(SNAKE_TOTAL_POINTS); }
    public int getSnakeApplesEaten() { return getStat(SNAKE_APPLES_EATEN); }
    public int getSnakeGoldenEaten() { return getStat(SNAKE_GOLDEN_EATEN); }
    public int getSnakeGamesSmallPlain() { return getStat(SNAKE_GAMES_SMALL_PLAIN); }
    public int getSnakeGamesSmallObstacles() { return getStat(SNAKE_GAMES_SMALL_OBSTACLES); }
    public int getSnakeGamesMediumPlain() { return getStat(SNAKE_GAMES_MEDIUM_PLAIN); }
    public int getSnakeGamesMediumObstacles() { return getStat(SNAKE_GAMES_MEDIUM_OBSTACLES); }
    public int getSnakeGamesBigPlain() { return getStat(SNAKE_GAMES_BIG_PLAIN); }
    public int getSnakeGamesBigObstacles() { return getStat(SNAKE_GAMES_BIG_OBSTACLES); }
    public void recordSnakeGame(int mapSize, boolean obstacles, int applesEaten, int goldenEaten, int points) {
        addStat(SNAKE_GAMES, 1);
        addStat(SNAKE_TOTAL_POINTS, points);
        addStat(SNAKE_APPLES_EATEN, applesEaten);
        addStat(SNAKE_GOLDEN_EATEN, goldenEaten);
        addStat(snakeModeKey(mapSize, obstacles), 1);
    }

    private String snakeModeKey(int mapSize, boolean obstacles) {
        if (mapSize == 1) return obstacles ? SNAKE_GAMES_SMALL_OBSTACLES : SNAKE_GAMES_SMALL_PLAIN;
        if (mapSize == 2) return obstacles ? SNAKE_GAMES_MEDIUM_OBSTACLES : SNAKE_GAMES_MEDIUM_PLAIN;
        return obstacles ? SNAKE_GAMES_BIG_OBSTACLES : SNAKE_GAMES_BIG_PLAIN;
    }

    // True Colors
    public int getTrueColorsGames() { return getStat(TRUE_COLORS_GAMES); }
    public int getTrueColorsTotalCorrect() { return getStat(TRUE_COLORS_TOTAL_CORRECT); }
    public int getTrueColorsTotalPoints() { return getStat(TRUE_COLORS_TOTAL_POINTS); }
    public int getTrueColorsBestCorrect() { return getStat(TRUE_COLORS_BEST_CORRECT); }
    public void recordTrueColorsGame(int correctColors, int points) {
        addStat(TRUE_COLORS_GAMES, 1);
        addStat(TRUE_COLORS_TOTAL_CORRECT, correctColors);
        addStat(TRUE_COLORS_TOTAL_POINTS, points);
        if (correctColors > getStat(TRUE_COLORS_BEST_CORRECT)) setStat(TRUE_COLORS_BEST_CORRECT, correctColors);
    }

    // Mastermind
    public int getMastermindGames() { return getStat(MASTERMIND_GAMES); }
    public int getMastermindWins() { return getStat(MASTERMIND_WINS); }
    public int getMastermindLosses() { return getStat(MASTERMIND_GAMES) - getStat(MASTERMIND_WINS); }
    public int getMastermindTotalAttempts() { return getStat(MASTERMIND_TOTAL_ATTEMPTS); }
    public void recordMastermindGame(boolean won, int attempts) {
        addStat(MASTERMIND_GAMES, 1);
        if (won) addStat(MASTERMIND_WINS, 1);
        addStat(MASTERMIND_TOTAL_ATTEMPTS, attempts);
    }

    // Tic Tac Toe
    public int getTicTacToeGames() { return getStat(TTT_GAMES); }
    public int getTicTacToePlayerOneWins() { return getStat(TTT_P1_WINS); }
    public int getTicTacToePlayerTwoWins() { return getStat(TTT_P2_WINS); }
    public int getTicTacToeDraws() { return getStat(TTT_DRAWS); }
    public void recordTicTacToeWin(int player) {
        addStat(TTT_GAMES, 1);
        if (player == 1) addStat(TTT_P1_WINS, 1);
        else addStat(TTT_P2_WINS, 1);
    }
    public void recordTicTacToeDraw() {
        addStat(TTT_GAMES, 1);
        addStat(TTT_DRAWS, 1);
    }

    // Wordle
    public int getWordleGames() { return getStat(WORDLE_GAMES); }
    public int getWordleWordsGuessed() { return getStat(WORDLE_WORDS_GUESSED); }
    public int getWordleCurrentStreak() { return getStat(WORDLE_CURRENT_STREAK); }
    public int getWordleBestStreak() { return getStat(WORDLE_BEST_STREAK); }
    public void recordWordleWin() {
        addStat(WORDLE_WORDS_GUESSED, 1);
        int streak = getStat(WORDLE_CURRENT_STREAK) + 1;
        setStat(WORDLE_CURRENT_STREAK, streak);
        if (streak > getStat(WORDLE_BEST_STREAK)) setStat(WORDLE_BEST_STREAK, streak);
    }
    public void recordWordleGameOver() {
        addStat(WORDLE_GAMES, 1);
        setStat(WORDLE_CURRENT_STREAK, 0);
    }
}
