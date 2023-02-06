package com.classicgames.myapplication.utils;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.classicgames.myapplication.R;

public class CustomDialog extends Dialog {

    private Activity activity;
    private Button btPositive, btNegative;
    private TextView tvMessage;
    private String message;

    private DialogButtonClick listener;

    public CustomDialog (Activity activity, String message, DialogButtonClick listener){
        super(activity);
        this.activity = activity;
        this.message = message;
        this.listener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.custom_dialog);
        btPositive = findViewById(R.id.CustomDialog_Bt_Positive);
        btPositive.setOnClickListener(v -> listener.onPositiveButtonClicked());
        btNegative = findViewById(R.id.CustomDialog_Bt_Negative);
        btNegative.setOnClickListener(v -> listener.onNegativeButtonClicked());
        tvMessage = findViewById(R.id.CustomDialog_Tv_Message);
        tvMessage.setText(message);
    }

    public Button getBtPositive(){
        return btPositive;
    }

    public Button getBtNegative(){
        return btNegative;
    }

    public interface DialogButtonClick {
        void onPositiveButtonClicked();
        void onNegativeButtonClicked();
    }
}
