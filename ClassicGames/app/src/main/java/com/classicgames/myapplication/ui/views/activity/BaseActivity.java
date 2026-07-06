package com.classicgames.myapplication.ui.views.activity;

import android.content.Context;
import android.content.res.Configuration;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.classicgames.myapplication.MyApplication;
import com.classicgames.myapplication.utils.SoundManager;

import java.util.Locale;

/**
 * Base for every activity. Applies the user's saved language in
 * {@link #attachBaseContext(Context)} so it is in effect before any resources
 * are resolved, replacing the deprecated Resources#updateConfiguration approach.
 */
public class BaseActivity extends AppCompatActivity {

    @Override
    protected void attachBaseContext(Context newBase) {
        String languageCode = "en";
        if (MyApplication.getInstance() != null) {
            languageCode = MyApplication.getInstance().getAppPreferences().getLanguage();
        }
        super.attachBaseContext(wrapLocale(newBase, languageCode));
    }

    /**
     * Re-applies the language at runtime. The new value must already be persisted
     * (see AppPreferences#saveLanguage); recreating re-runs attachBaseContext.
     */
    public void setLocale(String languageCode) {
        recreate();
    }

    /** Adds a click sound to the toolbar Up/back button shared by every game screen. */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            SoundManager.play(SoundManager.Sound.CLICK);
        }
        return super.onOptionsItemSelected(item);
    }

    private static Context wrapLocale(Context context, String languageCode) {
        // Codes may carry a region, e.g. "zh-CN" or "pt-BR"; split so the right
        // resource folder (values-zh-rCN / values-pt-rBR) is selected.
        String[] parts = languageCode.split("-");
        Locale locale = parts.length == 2 ? new Locale(parts[0], parts[1]) : new Locale(parts[0]);
        Locale.setDefault(locale);
        Configuration configuration = new Configuration(context.getResources().getConfiguration());
        configuration.setLocale(locale);
        return context.createConfigurationContext(configuration);
    }
}
