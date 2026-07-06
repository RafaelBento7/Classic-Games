package com.classicgames.myapplication.utils;

import android.app.Activity;

import com.google.android.play.core.review.ReviewManager;
import com.google.android.play.core.review.ReviewManagerFactory;

/**
 * Triggers the Play in-app review flow at a good moment (e.g. a new record).
 * The request is made at most once per process so the user is never nagged;
 * Play itself also applies its own quota and may show nothing.
 */
public final class ReviewHelper {

    private static boolean requestedThisSession;

    private ReviewHelper() {}

    public static void requestReview(Activity activity) {
        if (requestedThisSession || activity == null) return;
        requestedThisSession = true;

        ReviewManager manager = ReviewManagerFactory.create(activity);
        manager.requestReviewFlow().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                manager.launchReviewFlow(activity, task.getResult());
            }
            // If it fails we silently skip — a review prompt is never critical.
        });
    }

    /**
     * Launches the in-app review flow in response to an explicit user action (e.g. a
     * "Rate" button). Unlike {@link #requestReview}, this ignores the once-per-session
     * guard, since the user asked for it. When the flow can't be prepared (debug builds,
     * no Play Store, quota, etc.) {@code onUnavailable} is run so the caller can fall back,
     * e.g. by opening the store listing. Note that Play may still show nothing even on
     * success (for example if the user already reviewed) — that's expected and out of our
     * control.
     */
    public static void launchReview(Activity activity, Runnable onUnavailable) {
        if (activity == null) return;

        ReviewManager manager = ReviewManagerFactory.create(activity);
        manager.requestReviewFlow().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                manager.launchReviewFlow(activity, task.getResult());
            } else if (onUnavailable != null) {
                onUnavailable.run();
            }
        });
    }
}
