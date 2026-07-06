package com.classicgames.myapplication.ui.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.classicgames.myapplication.R;
import com.classicgames.myapplication.utils.ShareHelper;
import com.classicgames.myapplication.utils.SoundManager;

public class CustomDialog extends Dialog {

    private Activity activity;
    private Button btPositive, btNegative, btShare;
    private TextView tvMessage, tvTitle;
    private String message, title, shareText;

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
        setContentView(R.layout.dialog_custom);
        btPositive = findViewById(R.id.CustomDialog_Bt_Positive);
        btPositive.setOnClickListener(v -> {
            SoundManager.play(SoundManager.Sound.CLICK);
            if (listener != null)
                listener.onPositiveButtonClicked();
            dismiss();
        });
        btNegative = findViewById(R.id.CustomDialog_Bt_Negative);
        btNegative.setOnClickListener(v -> {
            SoundManager.play(SoundManager.Sound.CLICK);
            if (listener != null)
                listener.onNegativeButtonClicked();
            dismiss();
        });
        btShare = findViewById(R.id.CustomDialog_Bt_Share);
        if (shareText != null) {
            btShare.setVisibility(View.VISIBLE);
            btShare.setOnClickListener(v -> {
                SoundManager.play(SoundManager.Sound.CLICK);
                ShareHelper.shareScore(activity, shareText);
            });
        } else {
            btShare.setVisibility(View.GONE);
        }

        tvMessage = findViewById(R.id.CustomDialog_Tv_Message);
        if (this.title != null) {
            tvTitle = findViewById(R.id.CustomDialog_Tv_Title);
            tvTitle.setText(title);
        } else findViewById(R.id.CustomDialog_Layout_Title).setVisibility(View.GONE);

        tvMessage.setText(message);
    }

    /**
     * Sets the text shared when the user taps the Share button. Must be called
     * before {@link #show()}; a non-null value reveals the otherwise-hidden button.
     */
    public void setShareText(String shareText) {
        this.shareText = shareText;
    }

    public Button getBtPositive() {
        return btPositive;
    }

    public Button getBtNegative() {
        return btNegative;
    }

    public Button getBtShare() {
        return btShare;
    }

    public interface DialogButtonClick {
        void onPositiveButtonClicked();

        void onNegativeButtonClicked();
    }
}
