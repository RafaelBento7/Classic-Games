package com.classicgames.myapplication.utils;

import android.content.Context;
import android.content.Intent;

import com.classicgames.myapplication.R;

/** Fires a plain-text share intent so the player can brag about a new record. */
public final class ShareHelper {

    private ShareHelper() {}

    public static void shareScore(Context context, String text) {
        if (context == null || text == null) return;

        Intent send = new Intent(Intent.ACTION_SEND);
        send.setType("text/plain");
        send.putExtra(Intent.EXTRA_TEXT, text);

        Intent chooser = Intent.createChooser(send, context.getString(R.string.share_score_title));
        // Allow starting from a non-Activity context (e.g. a Dialog's base context).
        chooser.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(chooser);
    }
}
