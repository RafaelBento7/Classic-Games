package com.classicgames.myapplication.utils;

import android.app.Activity;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.classicgames.myapplication.R;

/**
 * A custom, app-themed replacement for {@link android.widget.Toast}/Snackbar.
 *
 * <p>It always appears at the bottom of the foreground activity and, unlike a
 * Toast, it never queues: showing a new message instantly removes the previous
 * one, so rapid taps can't pile up a backlog of messages.
 */
public final class MessageBar {

    private static final String TAG = "app_message_bar";
    private static final int DEFAULT_DURATION_MS = 2200;

    private static final Handler HANDLER = new Handler(Looper.getMainLooper());

    private MessageBar() {}

    public static void show(Activity activity, CharSequence text) {
        show(activity, text, DEFAULT_DURATION_MS);
    }

    public static void show(Activity activity, int textRes) {
        if (activity == null) return;
        show(activity, activity.getString(textRes), DEFAULT_DURATION_MS);
    }

    public static void show(Activity activity, CharSequence text, int durationMs) {
        if (activity == null || activity.isFinishing()) return;

        ViewGroup root = activity.findViewById(android.R.id.content);
        if (root == null) return;

        // Drop any message currently on screen (and its pending auto-dismiss)
        // so the newest message always wins instead of waiting in a queue.
        HANDLER.removeCallbacksAndMessages(null);
        View existing = root.findViewWithTag(TAG);
        if (existing != null) root.removeView(existing);

        View view = LayoutInflater.from(activity).inflate(R.layout.view_message_bar, root, false);
        view.setTag(TAG);
        ((TextView) view.findViewById(R.id.MessageBar_Tv)).setText(text);
        root.addView(view);

        view.setAlpha(0f);
        view.setTranslationY(40f);
        view.animate().alpha(1f).translationY(0f).setDuration(180).start();

        HANDLER.postDelayed(() -> dismiss(view), durationMs);
    }

    private static void dismiss(View view) {
        if (view == null || view.getParent() == null) return;
        view.animate().alpha(0f).translationY(40f).setDuration(180)
                .withEndAction(() -> {
                    ViewGroup parent = (ViewGroup) view.getParent();
                    if (parent != null) parent.removeView(view);
                }).start();
    }
}
