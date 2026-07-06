package com.classicgames.myapplication.ui.dialog;


import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.Window;

import com.classicgames.myapplication.R;
import com.classicgames.myapplication.utils.SoundManager;

public class LanguagePickerDialog extends Dialog {

    private LanguageDialogButtonClick listener;

    public LanguagePickerDialog(Activity activity, LanguageDialogButtonClick listener) {
        super(activity);
        this.listener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_language_picker);
        findViewById(R.id.LanguageDialog_ConsLayout_Pt).setOnClickListener(v -> notifyLanguage("pt"));
        findViewById(R.id.LanguageDialog_ConsLayout_En).setOnClickListener(v -> notifyLanguage("en"));
        findViewById(R.id.LanguageDialog_ConsLayout_Fr).setOnClickListener(v -> notifyLanguage("fr"));
        findViewById(R.id.LanguageDialog_ConsLayout_Es).setOnClickListener(v -> notifyLanguage("es"));
        findViewById(R.id.LanguageDialog_ConsLayout_De).setOnClickListener(v -> notifyLanguage("de"));
        findViewById(R.id.LanguageDialog_ConsLayout_Zh).setOnClickListener(v -> notifyLanguage("zh-CN"));
        findViewById(R.id.LanguageDialog_ConsLayout_Ja).setOnClickListener(v -> notifyLanguage("ja"));
        findViewById(R.id.LanguageDialog_ConsLayout_Ru).setOnClickListener(v -> notifyLanguage("ru"));
        findViewById(R.id.LanguageDialog_ConsLayout_It).setOnClickListener(v -> notifyLanguage("it"));
        findViewById(R.id.LanguageDialog_ConsLayout_Nl).setOnClickListener(v -> notifyLanguage("nl"));
        findViewById(R.id.LanguageDialog_ConsLayout_Ko).setOnClickListener(v -> notifyLanguage("ko"));
        findViewById(R.id.LanguageDialog_ConsLayout_PtBr).setOnClickListener(v -> notifyLanguage("pt-BR"));
        findViewById(R.id.LanguageDialog_ConsLayout_Pl).setOnClickListener(v -> notifyLanguage("pl"));
        findViewById(R.id.LanguageDialog_ConsLayout_Tr).setOnClickListener(v -> notifyLanguage("tr"));
    }

    private void notifyLanguage(String code) {
        SoundManager.play(SoundManager.Sound.CLICK);
        if (listener != null) listener.OnLanguageClicked(code);
    }

    public interface LanguageDialogButtonClick {
        void OnLanguageClicked(String languageCode);
    }
}