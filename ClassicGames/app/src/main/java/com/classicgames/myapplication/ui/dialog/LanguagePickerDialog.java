package com.classicgames.myapplication.ui.dialog;


import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.Window;

import com.classicgames.myapplication.R;

public class LanguagePickerDialog extends Dialog {


    public LanguagePickerDialog(Activity activity) {
        super(activity);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_language_picker_dialog);
    }
}