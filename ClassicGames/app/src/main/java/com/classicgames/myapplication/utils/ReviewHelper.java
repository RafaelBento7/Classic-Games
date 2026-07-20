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
}
