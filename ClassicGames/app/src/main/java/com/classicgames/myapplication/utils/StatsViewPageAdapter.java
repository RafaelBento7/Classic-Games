package com.classicgames.myapplication.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.classicgames.myapplication.MyApplication;
import com.classicgames.myapplication.R;
import com.classicgames.myapplication.data.preferences.RecordsPreferences;

import java.util.Locale;

/**
 * Backs the swipeable stats screen. Each page is a per-game layout that is
 * inflated and then populated with that game's records (personal bests) and
 * lifetime stats. Mirrors the structure of {@link HelpViewPageAdapter}.
 */
public class StatsViewPageAdapter extends PagerAdapter {

    private final Context context;

    public StatsViewPageAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        StatsViewPagerObject page = StatsViewPagerObject.values()[position];
        View layout = LayoutInflater.from(context).inflate(page.getLayout(), container, false);
        bind(page, layout);
        applyHintVisibility(layout, position);
        container.addView(layout);
        return layout;
    }

    /** Hide the "previous" hint on the first page and the "next" hint on the last. */
    private void applyHintVisibility(View layout, int position) {
        View prev = layout.findViewById(R.id.Stats_Hint_Prev);
        View next = layout.findViewById(R.id.Stats_Hint_Next);
        if (prev != null) prev.setVisibility(position == 0 ? View.INVISIBLE : View.VISIBLE);
        if (next != null) next.setVisibility(position == getCount() - 1 ? View.INVISIBLE : View.VISIBLE);
    }

    private void bind(StatsViewPagerObject page, View v) {
        RecordsPreferences records = MyApplication.getInstance().getRecords();

        switch (page) {
            case SNAKE:
                setText(v, R.id.Stats_Snake_SmallRecord, records.getSnakeSmallRecord());
                setText(v, R.id.Stats_Snake_MediumRecord, records.getSnakeMediumRecord());
                setText(v, R.id.Stats_Snake_BigRecord, records.getSnakeBigRecord());
                setText(v, R.id.Stats_Snake_Games, records.getSnakeGames());
                setText(v, R.id.Stats_Snake_TotalPoints, records.getSnakeTotalPoints());
                setText(v, R.id.Stats_Snake_ApplesEaten, records.getSnakeApplesEaten());
                setText(v, R.id.Stats_Snake_GoldenEaten, records.getSnakeGoldenEaten());
                setText(v, R.id.Stats_Snake_SmallPlain, records.getSnakeGamesSmallPlain());
                setText(v, R.id.Stats_Snake_SmallObstacles, records.getSnakeGamesSmallObstacles());
                setText(v, R.id.Stats_Snake_MediumPlain, records.getSnakeGamesMediumPlain());
                setText(v, R.id.Stats_Snake_MediumObstacles, records.getSnakeGamesMediumObstacles());
                setText(v, R.id.Stats_Snake_BigPlain, records.getSnakeGamesBigPlain());
                setText(v, R.id.Stats_Snake_BigObstacles, records.getSnakeGamesBigObstacles());
                break;
            case TRUE_COLORS:
                setText(v, R.id.Stats_TrueColors_Record, records.getTrueColorsRecord());
                setText(v, R.id.Stats_TrueColors_Games, records.getTrueColorsGames());
                setText(v, R.id.Stats_TrueColors_TotalCorrect, records.getTrueColorsTotalCorrect());
                setText(v, R.id.Stats_TrueColors_BestRun, records.getTrueColorsBestCorrect());
                setText(v, R.id.Stats_TrueColors_TotalPoints, records.getTrueColorsTotalPoints());
                setText(v, R.id.Stats_TrueColors_AvgCorrect,
                        average(records.getTrueColorsTotalCorrect(), records.getTrueColorsGames()));
                break;
            case MASTERMIND:
                setText(v, R.id.Stats_Mastermind_Time, timer(records.getMastermindRecord()));
                setText(v, R.id.Stats_Mastermind_Attempts, records.getMastermindRecord()[2]);
                setText(v, R.id.Stats_Mastermind_Games, records.getMastermindGames());
                setText(v, R.id.Stats_Mastermind_Wins, records.getMastermindWins());
                setText(v, R.id.Stats_Mastermind_Losses, records.getMastermindLosses());
                setText(v, R.id.Stats_Mastermind_WinRate,
                        percent(records.getMastermindWins(), records.getMastermindGames()));
                setText(v, R.id.Stats_Mastermind_TotalAttempts, records.getMastermindTotalAttempts());
                setText(v, R.id.Stats_Mastermind_AvgAttempts,
                        average(records.getMastermindTotalAttempts(), records.getMastermindGames()));
                break;
            case TICTACTOE:
                setText(v, R.id.Stats_Ttt_Games, records.getTicTacToeGames());
                setText(v, R.id.Stats_Ttt_P1Wins, records.getTicTacToePlayerOneWins());
                setText(v, R.id.Stats_Ttt_P2Wins, records.getTicTacToePlayerTwoWins());
                setText(v, R.id.Stats_Ttt_Draws, records.getTicTacToeDraws());
                setText(v, R.id.Stats_Ttt_P1WinRate,
                        percent(records.getTicTacToePlayerOneWins(), records.getTicTacToeGames()));
                setText(v, R.id.Stats_Ttt_P2WinRate,
                        percent(records.getTicTacToePlayerTwoWins(), records.getTicTacToeGames()));
                setText(v, R.id.Stats_Ttt_DrawRate,
                        percent(records.getTicTacToeDraws(), records.getTicTacToeGames()));
                break;
            case WORDLE:
                setText(v, R.id.Stats_Wordle_PointsRecord, records.getWordleRecord()[2]);
                setText(v, R.id.Stats_Wordle_TimeRecord, timer(records.getWordleRecord()));
                setText(v, R.id.Stats_Wordle_Games, records.getWordleGames());
                setText(v, R.id.Stats_Wordle_WordsGuessed, records.getWordleWordsGuessed());
                setText(v, R.id.Stats_Wordle_CurrentStreak, records.getWordleCurrentStreak());
                setText(v, R.id.Stats_Wordle_BestStreak, records.getWordleBestStreak());
                setText(v, R.id.Stats_Wordle_AvgWords,
                        average(records.getWordleWordsGuessed(), records.getWordleGames()));
                break;
            case HANGMAN:
                setText(v, R.id.Stats_Hangman_PointsRecord, records.getHangmanRecord()[2]);
                setText(v, R.id.Stats_Hangman_TimeRecord, timer(records.getHangmanRecord()));
                setText(v, R.id.Stats_Hangman_Games, records.getHangmanGames());
                setText(v, R.id.Stats_Hangman_WordsGuessed, records.getHangmanWordsGuessed());
                setText(v, R.id.Stats_Hangman_CurrentStreak, records.getHangmanCurrentStreak());
                setText(v, R.id.Stats_Hangman_BestStreak, records.getHangmanBestStreak());
                setText(v, R.id.Stats_Hangman_PerfectWords, records.getHangmanPerfectWords());
                setText(v, R.id.Stats_Hangman_WrongLetters, records.getHangmanWrongLetters());
                setText(v, R.id.Stats_Hangman_AvgWords, average(records.getHangmanWordsGuessed(), records.getHangmanGames()));
                setText(v, R.id.Stats_Hangman_AvgWrong, average(records.getHangmanWrongLetters(), records.getHangmanGames()));
                break;
        }
    }

    /** Whole-number percentage of {@code part} out of {@code whole}, e.g. "67%". */
    private String percent(int part, int whole) {
        if (whole <= 0) return "0%";
        return Math.round(part * 100f / whole) + "%";
    }

    /** One-decimal average of {@code total} over {@code count}, e.g. "2.3". */
    private String average(int total, int count) {
        if (count <= 0) return "0";
        return String.format(Locale.getDefault(), "%.1f", total / (float) count);
    }

    private void setText(View root, @IdRes int id, int value) {
        setText(root, id, String.valueOf(value));
    }

    private void setText(View root, @IdRes int id, String value) {
        TextView tv = root.findViewById(id);
        if (tv != null) tv.setText(value);
    }

    /** records[0] = total minutes, records[1] = seconds. */
    private String timer(int[] records) {
        int totalMinutes = records[0];
        int secondsInt = records[1];
        int hoursInt = totalMinutes / 60;
        int minutesInt = totalMinutes % 60;

        String seconds = secondsInt < 10 ? "0" + secondsInt : String.valueOf(secondsInt);
        String minutes = minutesInt < 10 ? "0" + minutesInt : String.valueOf(minutesInt);

        if (hoursInt == 0)
            return context.getString(R.string.timer_minutes_seconds, minutes, seconds);

        String hours = hoursInt < 10 ? "0" + hoursInt : String.valueOf(hoursInt);
        return context.getString(R.string.timer_hour_minutes_seconds, hours, minutes, seconds);
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return StatsViewPagerObject.values().length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }
}
