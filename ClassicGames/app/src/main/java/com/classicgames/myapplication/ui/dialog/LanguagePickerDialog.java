package com.classicgames.myapplication.ui.dialog;


import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.Window;

import com.classicgames.myapplication.R;

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
        findViewById(R.id.LanguageDialog_ConsLayout_Pt).setOnClickListener(v -> {
            if (listener != null)
                listener.OnLanguageClicked("pt");
        });

        findViewById(R.id.LanguageDialog_ConsLayout_En).setOnClickListener(v -> {
            if (listener != null)
                listener.OnLanguageClicked("en");
        });
    }

    public interface LanguageDialogButtonClick {
        void OnLanguageClicked(String languageCode);
    }
}