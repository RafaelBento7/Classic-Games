package com.classicgames.myapplication;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.classicgames.myapplication.data.preferences.AppPreferences;
import com.classicgames.myapplication.data.preferences.RecordsPreferences;
import com.classicgames.myapplication.utils.MusicManager;
import com.classicgames.myapplication.utils.SoundManager;

public class MyApplication extends Application {

    private static MyApplication instance;

    public static MyApplication getInstance() {
        return instance;
    }

    private RecordsPreferences recordsPreferences;
    private AppPreferences appPreferences;
    private SoundManager soundManager;
    private MusicManager musicManager;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        recordsPreferences = new RecordsPreferences(this);
        appPreferences = new AppPreferences(this);
        appPreferences.ensureLanguageDefault();   // open in the system language on first launch
        soundManager = new SoundManager(this);
        musicManager = new MusicManager(this);
        registerMusicLifecycle();
    }

    /**
     * Tracks how many activities are started so background music pauses when the
     * app goes to the background and resumes when it returns to the foreground.
     */
    private void registerMusicLifecycle() {
        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            private int startedActivities = 0;

            @Override public void onActivityStarted(@NonNull Activity activity) {
                if (startedActivities == 0) musicManager.setForeground(true);
                startedActivities++;
            }

            @Override public void onActivityStopped(@NonNull Activity activity) {
                startedActivities--;
                if (startedActivities <= 0) {
                    startedActivities = 0;
                    musicManager.setForeground(false);
                }
            }

            @Override public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle savedInstanceState) { }
            @Override public void onActivityResumed(@NonNull Activity activity) { }
            @Override public void onActivityPaused(@NonNull Activity activity) { }
            @Override public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle outState) { }
            @Override public void onActivityDestroyed(@NonNull Activity activity) { }
        });
    }

    public RecordsPreferences getRecords() {
        return recordsPreferences;
    }

    public AppPreferences getAppPreferences() {
        return appPreferences;
    }

    public SoundManager getSoundManager() {
        return soundManager;
    }

    public MusicManager getMusicManager() {
        return musicManager;
    }
}
