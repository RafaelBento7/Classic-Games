package com.classicgames.myapplication.utils;

import com.classicgames.myapplication.R;

/** The per-game pages shown in the swipeable stats/records screen. */
public enum StatsViewPagerObject {
    SNAKE(R.layout.view_stats_snake),
    TRUE_COLORS(R.layout.view_stats_true_colors),
    MASTERMIND(R.layout.view_stats_mastermind),
    TICTACTOE(R.layout.view_stats_tic_tac_toe),
    WORDLE(R.layout.view_stats_wordle);

    private final int layout;

    StatsViewPagerObject(int layout) {
        this.layout = layout;
    }

    public int getLayout() {
        return layout;
    }
}
