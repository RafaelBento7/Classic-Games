package com.classicgames.myapplication.data.enums;

import android.content.Context;

import androidx.annotation.ColorRes;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.core.content.ContextCompat;

import com.classicgames.myapplication.R;

/**
 * Catalogue of every colour the games can use, each paired with its display-name
 * string. This list is meant to grow: when a future game needs more colours, just
 * append new entries here.
 *
 * <p>Existing games keep working because they ask for a fixed number of colours
 * via {@link #palette(Context, int)} (the count they were designed around) rather
 * than "all currently defined colours". Always <b>append</b> new entries so the
 * leading colours stay stable for those games.
 */
public enum GameColor {
    PURPLE(R.color.purple, R.string.purple),
    GREEN(R.color.green, R.string.green),
    RED(R.color.red, R.string.red),
    YELLOW(R.color.yellow, R.string.yellow),
    BLUE(R.color.blue, R.string.blue),
    BROWN(R.color.brown, R.string.brown),
    ORANGE(R.color.orange, R.string.orange),
    PINK(R.color.pink, R.string.pink);

    @ColorRes public final int colorRes;
    @StringRes public final int nameRes;

    GameColor(@ColorRes int colorRes, @StringRes int nameRes) {
        this.colorRes = colorRes;
        this.nameRes = nameRes;
    }

    /** Resolved ARGB values for the whole catalogue, in declaration order. */
    public static int[] palette(Context context) {
        return palette(context, values().length);
    }

    /**
     * Resolved ARGB values for the first {@code count} colours of the catalogue.
     * A game requests the exact number it was built for, so adding new colours
     * later never changes its behaviour. {@code count} is clamped to [0, size].
     */
    public static int[] palette(Context context, int count) {
        GameColor[] all = values();
        count = Math.max(0, Math.min(count, all.length));
        int[] colors = new int[count];
        for (int i = 0; i < count; i++) {
            colors[i] = ContextCompat.getColor(context, all[i].colorRes);
        }
        return colors;
    }

    /** Finds the catalogue entry whose resolved colour equals {@code colorInt}, or null. */
    @Nullable
    public static GameColor fromColor(Context context, int colorInt) {
        for (GameColor color : values()) {
            if (ContextCompat.getColor(context, color.colorRes) == colorInt) {
                return color;
            }
        }
        return null;
    }
}
