package com.classicgames.myapplication.utils;

import android.content.Context;
import android.media.MediaPlayer;

import com.classicgames.myapplication.MyApplication;

/**
 * Looping background music, separate from SoundManager so both play at once.
 *
 * The track is loaded by name from res/raw, so the app runs (silently) even
 * before bg_music.ogg exists. Music plays only while enabled in Settings, the
 * app is in the foreground, and the main screen has been reached.
 *
 * Gapless looping: setLooping() is not gapless (it seeks back to the start,
 * causing an audible pause). Instead we keep two players and chain them with
 * setNextMediaPlayer(), so the next one starts the instant the current one ends.
 * See onLoopBoundary for how the chain is kept alive.
 */
public final class MusicManager {

    private static final String TRACK_NAME = "bg_music";

    private final Context appContext;
    private MediaPlayer playerA;
    private MediaPlayer playerB;
    /** The player currently playing (or paused); its partner is queued behind it. */
    private MediaPlayer active;
    private int resId;
    private boolean triedToLoad;

    private boolean foreground;
    private boolean reachedMain;

    public MusicManager(Context context) {
        this.appContext = context.getApplicationContext();
    }

    /** Called when the app moves to/from the foreground (from lifecycle callbacks). */
    public void setForeground(boolean value) {
        foreground = value;
        update();
    }

    /** Called once the main screen is shown so music never plays over the splash. */
    public void notifyMainReached() {
        reachedMain = true;
        update();
    }

    /** Re-evaluates playback after the user toggles the music setting. */
    public void refreshFromSettings() {
        update();
    }

    private boolean musicEnabled() {
        MyApplication app = MyApplication.getInstance();
        return app != null && app.getAppPreferences().isMusicEnabled();
    }

    private void update() {
        boolean shouldPlay = foreground && reachedMain && musicEnabled();
        if (shouldPlay) {
            ensurePlayer();
            if (active != null && !active.isPlaying()) active.start();
        } else if (active != null && active.isPlaying()) {
            active.pause();
        }
    }

    private void ensurePlayer() {
        if (triedToLoad) return;
        triedToLoad = true;
        resId = appContext.getResources().getIdentifier(TRACK_NAME, "raw", appContext.getPackageName());
        if (resId == 0) return;

        playerA = buildPlayer();
        playerB = buildPlayer();
        if (playerA == null || playerB == null) {
            releasePlayers();
            return;
        }
        // Queue B behind A; the completion handler keeps the chain going forever.
        playerA.setNextMediaPlayer(playerB);
        active = playerA;
    }

    private MediaPlayer buildPlayer() {
        MediaPlayer mp = MediaPlayer.create(appContext, resId);
        if (mp != null) {
            mp.setVolume(0.4f, 0.4f);
            mp.setOnCompletionListener(onLoopBoundary);
        }
        return mp;
    }

    /**
     * A player just ended; its partner is already playing gaplessly. Build a fresh
     * copy and queue it behind the partner. We recreate rather than rewind because
     * a completed player isn't reliably accepted by setNextMediaPlayer, which would
     * break the chain. The rebuild happens while the partner plays, so there's no gap.
     */
    private final MediaPlayer.OnCompletionListener onLoopBoundary = finished -> {
        MediaPlayer partner = (finished == playerA) ? playerB : playerA;
        if (partner == null) return;
        active = partner;
        finished.release();

        MediaPlayer fresh = buildPlayer();
        if (finished == playerA) playerA = fresh; else playerB = fresh;
        if (fresh != null) partner.setNextMediaPlayer(fresh);
    };

    private void releasePlayers() {
        if (playerA != null) { playerA.release(); playerA = null; }
        if (playerB != null) { playerB.release(); playerB = null; }
        active = null;
    }
}
