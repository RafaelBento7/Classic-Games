package com.classicgames.myapplication.utils;

import com.classicgames.myapplication.R;

public enum HelpViewPagerObject {
    SNAKE(R.string.snake_game, R.layout.view_help_snake),
    TRUE_COLORS(R.string.true_colors_game, R.layout.view_help_true_colors),
    MASTERMIND(R.string.mastermind_game, R.layout.view_help_mastermind),
    TICTACTOE(R.string.tic_tac_toe_game, R.layout.view_help_tic_tac_toe),
    WORDLE(R.string.wordle_game, R.layout.view_help_wordle);

    private int title;
    private int layout;

    HelpViewPagerObject(int mTitle, int mLayout ){
        this.layout = mLayout;
        this.title = mTitle;
    }

    public int getTitle() {
        return title;
    }

    public int getLayout() {
        return layout;
    }
}
