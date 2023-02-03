package com.classicgames.myapplication.ui.views.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import android.app.AlertDialog;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.View;

import com.classicgames.myapplication.R;
import com.classicgames.myapplication.databinding.ActivityTrueColorsBinding;
import com.classicgames.myapplication.ui.viewmodels.TrueColorsViewModel;

public class TrueColorsActivity extends AppCompatActivity {

    private TrueColorsViewModel mViewModel;
    private ActivityTrueColorsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(TrueColorsViewModel.class);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_true_colors);
        binding.setViewModel(mViewModel);

        mViewModel.getProgress().observe(this, progress -> binding.TrueColorsProgressBar.setProgress(progress));
        mViewModel.getTrueColor().observe(this, this::changeTrueColorText);
        mViewModel.getFalseColor().observe(this, this::changeFalseColors);
        mViewModel.getCorrectColorsPicked().observe(this, correctColorsPicked -> binding.TrueColorsTvCorrects.setText(String.valueOf(correctColorsPicked)));
        mViewModel.getPoints().observe(this, points -> binding.TrueColorsTvPoints.setText(String.valueOf(getResources().getString(R.string.points) + " " + points)));
        mViewModel.getLives().observe(this, this::livesChanged);
        mViewModel.getRecord().observe(this, this::loadRecord);
        mViewModel.isGameOver().observe(this, isGameOver -> {
            if (isGameOver) gameOver();
        });

        startGame();
    }

    private void startGame(){
        binding.TrueColorsIvHeart1.setVisibility(View.VISIBLE);
        binding.TrueColorsIvHeart2.setVisibility(View.VISIBLE);
        binding.TrueColorsIvHeart3.setVisibility(View.VISIBLE);
        mViewModel.startGame();
    }

    private void gameOver() {
        AlertDialog.Builder builder = new AlertDialog.Builder(TrueColorsActivity.this);
        builder.setTitle(getResources().getString(R.string.game_over));
        builder.setCancelable(false);
        builder.setMessage(getResources().getString(R.string.game_over_points) + " " + mViewModel.getPoints().getValue());
        builder.setPositiveButton(getResources().getString(R.string.play_again), (dialog, which) -> startGame());
        builder.setNegativeButton(getResources().getString(R.string.back_menu),(dialog, which)-> finish());
        builder.show();
    }

    private void livesChanged(int lives){
        if (lives == 2) binding.TrueColorsIvHeart3.setVisibility(View.GONE);
        else if (lives == 1) binding.TrueColorsIvHeart2.setVisibility(View.GONE);
        else if (lives == 0) binding.TrueColorsIvHeart1.setVisibility(View.GONE);
    }

    private void changeFalseColors(final int color){
        if (color == getResources().getColor(R.color.purple)) {
            binding.TrueColorsProgressBar.setProgressTintList(ColorStateList.valueOf(getResources().getColor(R.color.purple)));
            binding.TrueColorsTvColor.setTextColor(getResources().getColor(R.color.purple));
        }
        else if (color == getResources().getColor(R.color.green)){
            binding.TrueColorsProgressBar.setProgressTintList(ColorStateList.valueOf(getResources().getColor(R.color.green)));
            binding.TrueColorsTvColor.setTextColor(getResources().getColor(R.color.green));
        }
        else if (color == getResources().getColor(R.color.red)){
            binding.TrueColorsProgressBar.setProgressTintList(ColorStateList.valueOf(getResources().getColor(R.color.red)));
            binding.TrueColorsTvColor.setTextColor(getResources().getColor(R.color.red));
        }
        else if (color == getResources().getColor(R.color.yellow)){
            binding.TrueColorsProgressBar.setProgressTintList(ColorStateList.valueOf(getResources().getColor(R.color.yellow)));
            binding.TrueColorsTvColor.setTextColor(getResources().getColor(R.color.yellow));
        }
        else if (color == getResources().getColor(R.color.blue)) {
            binding.TrueColorsProgressBar.setProgressTintList(ColorStateList.valueOf(getResources().getColor(R.color.blue)));
            binding.TrueColorsTvColor.setTextColor(getResources().getColor(R.color.blue));
        }
        else if (color == getResources().getColor(R.color.brown)){
            binding.TrueColorsProgressBar.setProgressTintList(ColorStateList.valueOf(getResources().getColor(R.color.brown)));
            binding.TrueColorsTvColor.setTextColor(getResources().getColor(R.color.brown));
        }
        else if (color == getResources().getColor(R.color.orange)) {
            binding.TrueColorsProgressBar.setProgressTintList(ColorStateList.valueOf(getResources().getColor(R.color.orange)));
            binding.TrueColorsTvColor.setTextColor(getResources().getColor(R.color.orange));
        }
        else if (color == getResources().getColor(R.color.pink)) {
            binding.TrueColorsProgressBar.setProgressTintList(ColorStateList.valueOf(getResources().getColor(R.color.pink)));
            binding.TrueColorsTvColor.setTextColor(getResources().getColor(R.color.pink));
        }
    }

    private void changeTrueColorText(final int color){
        if (color == getResources().getColor(R.color.purple)) binding.TrueColorsTvColor.setText(getResources().getString(R.string.purple));
        else if (color == getResources().getColor(R.color.green)) binding.TrueColorsTvColor.setText(getResources().getString(R.string.green));
        else if (color == getResources().getColor(R.color.red)) binding.TrueColorsTvColor.setText(getResources().getString(R.string.red));
        else if (color == getResources().getColor(R.color.yellow)) binding.TrueColorsTvColor.setText(getResources().getString(R.string.yellow));
        else if (color == getResources().getColor(R.color.blue)) binding.TrueColorsTvColor.setText(getResources().getString(R.string.blue));
        else if (color == getResources().getColor(R.color.brown)) binding.TrueColorsTvColor.setText(getResources().getString(R.string.brown));
        else if (color == getResources().getColor(R.color.orange)) binding.TrueColorsTvColor.setText(getResources().getString(R.string.orange));
        else if (color == getResources().getColor(R.color.pink)) binding.TrueColorsTvColor.setText(getResources().getString(R.string.pink));
    }

    private void loadRecord(int maxPoints){
        String Points = getResources().getString(R.string.max_points) + " " + maxPoints;
        binding.TrueColorsTvMaxPoints.setText(Points);
    }

    @Override
    public void onStop(){
        super.onStop();
        mViewModel.stopTimer();
    }
}