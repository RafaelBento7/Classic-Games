package com.classicgames.myapplication.utils;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.SoundPool;

import com.classicgames.myapplication.MyApplication;

import java.util.EnumMap;
import java.util.Map;

/**
 * Low-latency sound effects via {@link SoundPool}.
 *
 * <p>Effects are loaded by <b>resource name</b> from {@code res/raw} using
 * {@link android.content.res.Resources#getIdentifier}, so the app compiles and
 * runs even before the audio files exist — any missing sound is simply silent.
 * To enable a sound, drop an {@code .ogg}/{@code .wav} file in {@code res/raw}
 * with the matching name below:
 *
 * <ul>
 *     <li>{@code sfx_click}        — button / key taps</li>
 *     <li>{@code sfx_correct}      — correct pick</li>
 *     <li>{@code sfx_wrong}        — wrong pick / life lost</li>
 *     <li>{@code sfx_win}          — round / game won</li>
 *     <li>{@code sfx_game_over}    — game over</li>
 *     <li>{@code sfx_record}       — new personal-best record</li>
 *     <li>{@code sfx_snake_point}        — snake normal point eaten</li>
 *     <li>{@code sfx_snake_golden_point} — snake golden point eaten</li>
 *     <li>{@code sfx_draw}         — tic-tac-toe draw</li>
 * </ul>
 *
 * Playback respects the user's SFX toggle in Settings.
 */
public final class SoundManager {

    public enum Sound {
        CLICK("sfx_click"),
        CORRECT("sfx_correct"),
        WRONG("sfx_wrong"),
        WIN("sfx_win"),
        GAME_OVER("sfx_game_over"),
        RECORD("sfx_record"),
        SNAKE_POINT("sfx_snake_point"),
        SNAKE_GOLDEN_POINT("sfx_snake_golden_point"),
        DRAW("sfx_draw");

        private final String resName;

        Sound(String resName) {
            this.resName = resName;
        }
    }

    private final SoundPool soundPool;
    private final Map<Sound, Integer> soundIds = new EnumMap<>(Sound.class);

    public SoundManager(Context context) {
        AudioAttributes attributes = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_GAME)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build();
        soundPool = new SoundPool.Builder()
                .setMaxStreams(4)
                .setAudioAttributes(attributes)
                .build();

        for (Sound sound : Sound.values()) {
            int resId = context.getResources().getIdentifier(sound.resName, "raw", context.getPackageName());
            if (resId != 0) soundIds.put(sound, soundPool.load(context, resId, 1));
        }
    }

    /** Convenience entry point: plays through the app-wide SoundManager, if any. */
    public static void play(Sound sound) {
        MyApplication app = MyApplication.getInstance();
        if (app != null && app.getSoundManager() != null) {
            app.getSoundManager().playInternal(sound);
        }
    }

    private void playInternal(Sound sound) {
        MyApplication app = MyApplication.getInstance();
        if (app == null || !app.getAppPreferences().isSfxEnabled()) return;

        Integer id = soundIds.get(sound);
        if (id != null) soundPool.play(id, 1f, 1f, 1, 0, 1f);
    }
}
