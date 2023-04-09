package com.classicgames.myapplication.utils;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.classicgames.myapplication.R;

public class CustomDialog extends Dialog {

    private Activity activity;
    private Button btPositive, btNegative;
    private TextView tvMessage, tvTitle;
    private String message, title;

    private DialogButtonClick listener;

    public CustomDialog(Activity activity, String message, String title, DialogButtonClick listener) {
        super(activity);
        this.activity = activity;
        this.message = message;
        this.listener = listener;
        this.title = title;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.custom_dialog);
        btPositive = findViewById(R.id.CustomDialog_Bt_Positive);
        btPositive.setOnClickListener(v -> {
            listener.onPositiveButtonClicked();
            dismiss();
        });
        btNegative = findViewById(R.id.CustomDialog_Bt_Negative);
        btNegative.setOnClickListener(v -> {
            listener.onNegativeButtonClicked();
            dismiss();
        });
        tvMessage = findViewById(R.id.CustomDialog_Tv_Message);
        if (this.title != null) {
            tvTitle = findViewById(R.id.CustomDialog_Tv_Title);
            tvTitle.setText(title);
        } else findViewById(R.id.CustomDialog_Layout_Title).setVisibility(View.GONE);

        tvMessage.setText(message);
    }

    public Button getBtPositive() {
        return btPositive;
    }

    public Button getBtNegative() {
        return btNegative;
    }

    public interface DialogButtonClick {
        void onPositiveButtonClicked();

        void onNegativeButtonClicked();
    }
}
