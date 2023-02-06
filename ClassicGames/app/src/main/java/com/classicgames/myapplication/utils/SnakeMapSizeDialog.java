package com.classicgames.myapplication.utils;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.Toast;

import com.classicgames.myapplication.R;

public class SnakeMapSizeDialog extends Dialog implements View.OnClickListener {

    private final Activity activity;
    private RadioButton small, medium, big;
    private int mapLevel = 0;

    public SnakeMapSizeDialog(Activity activity) {
        super(activity);
        this.activity = activity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.snake_map_size_dialog);
        findViewById(R.id.SnakeMapSize_Bt_Next).setOnClickListener(this);
        findViewById(R.id.SnakeMapSize_Bt_Cancel).setOnClickListener(this);
        small = findViewById(R.id.SnakeMapSize_Rb_Small);
        medium = findViewById(R.id.SnakeMapSize_Rb_Medium);
        big = findViewById(R.id.SnakeMapSize_Rb_Big);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.SnakeMapSize_Bt_Next){
            if (small.isChecked()) mapLevel = 1;
            else if (medium.isChecked()) mapLevel = 2;
            else if (big.isChecked()) mapLevel = 3;

            if (mapLevel != 0) dismiss();
            else Toast.makeText(activity, R.string.snake_select_map_size, Toast.LENGTH_SHORT).show();

        } else dismiss();
    }

    public int getMapLevel(){
        return mapLevel;
    }
}
