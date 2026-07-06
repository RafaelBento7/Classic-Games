package com.classicgames.myapplication.data.preferences;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class AppPreferences {

    private static final String KEY_LANGUAGE = "language";

    /** Language codes the app ships translations for (must match the picker + values-* folders). */
    private static final List<String> SUPPORTED_LANGUAGES = Arrays.asList("en", "pt", "pt-BR", "fr", "es", "de", "zh-CN", "ja", "ru", "it", "nl", "ko", "pl", "tr");

    private final SharedPreferences sp;
    private final SharedPreferences.Editor editor;

    public AppPreferences(Context context) {
        sp = context.getSharedPreferences("APP", Context.MODE_PRIVATE);
        editor = sp.edit();
    }

    public String getLanguage() {
        return sp.getString(KEY_LANGUAGE, "en");
    }

    public void saveLanguage(String languageCode) {
        editor.putString(KEY_LANGUAGE, languageCode);
        editor.apply();
    }

    /**
     * On first launch, pick the device's system language if we support it, otherwise fall back to English.
     */
    public void ensureLanguageDefault() {
        if (sp.contains(KEY_LANGUAGE)) return;
        saveLanguage(resolveSystemLanguage());
    }

    private String resolveSystemLanguage() {
        Locale system = Locale.getDefault();
        String language = system.getLanguage();   // e.g. "pt", "zh", "en"
        String country = system.getCountry();     // e.g. "BR", "CN"

        // Region-specific variants first.
        if ("pt".equals(language) && "BR".equals(country)) return "pt-BR";
        if ("zh".equals(language)) return "zh-CN";   // only Simplified Chinese is shipped

        if (SUPPORTED_LANGUAGES.contains(language)) return language;
        return "en";
    }

    public boolean isSfxEnabled() {
        return sp.getBoolean("sfx_enabled", true);
    }

    public void setSfxEnabled(boolean enabled) {
        editor.putBoolean("sfx_enabled", enabled);
        editor.apply();
    }

    public boolean isMusicEnabled() {
        return sp.getBoolean("music_enabled", true);
    }

    public void setMusicEnabled(boolean enabled) {
        editor.putBoolean("music_enabled", enabled);
        editor.apply();
    }
}
