package com.classicgames.myapplication.ui.views.activity;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.View;

import com.classicgames.myapplication.R;
import com.classicgames.myapplication.data.enums.GameColor;
import com.classicgames.myapplication.databinding.ActivityTrueColorsBinding;
import com.classicgames.myapplication.ui.viewmodels.TrueColorsViewModel;
import com.classicgames.myapplication.ui.dialog.CustomDialog;
import com.classicgames.myapplication.utils.ReviewHelper;

public class TrueColorsActivity extends BaseActivity {

    private TrueColorsViewModel mViewModel;
    private ActivityTrueColorsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(TrueColorsViewModel.class);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_true_colors);
        binding.setViewModel(mViewModel);
        binding.getRoot().setFitsSystemWindows(true);

        setSupportActionBar(binding.TrueColorsToolbar.getRoot());
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        binding.TrueColorsToolbar.ToolbarTitle.setText(R.string.true_colors_game);

        mViewModel.getProgress().observe(this, progress -> binding.TrueColorsProgressBar.setProgress(progress));
        mViewModel.getTrueColor().observe(this, this::changeTrueColorText);
        mViewModel.getFalseColor().observe(this, this::changeFalseColors);
        mViewModel.getCorrectColorsPicked().observe(this, correctColorsPicked -> binding.TrueColorsTvCorrects.setText(String.valueOf(correctColorsPicked)));
        mViewModel.getPoints().observe(this, points -> binding.TrueColorsTvPoints.setText(getResources().getString(R.string.points, points)));
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
        runOnUiThread(() -> {
            CustomDialog.DialogButtonClick gameOverDialog = new CustomDialog.DialogButtonClick() {
                @Override
                public void onPositiveButtonClicked() {
                    startGame();
                }

                @Override
                public void onNegativeButtonClicked() {
                    finish();
                }
            };
            CustomDialog dialog = new CustomDialog(
                    this,
                    getResources().getString(R.string.game_over_points) + " " + mViewModel.getPoints().getValue(),
                    getResources().getString(R.string.game_over_title),
                    gameOverDialog
            );
            dialog.setCancelable(false);
            if (mViewModel.isNewRecord()) {
                dialog.setShareText(getResources().getString(R.string.share_record_points,
                        getResources().getString(R.string.true_colors_game), mViewModel.getPointsValue(),
                        getResources().getString(R.string.store_link)));
                ReviewHelper.requestReview(this);
            }
            dialog.setOnShowListener(dialogInterface -> {
                dialog.getBtPositive().setText(getResources().getString(R.string.play_again));
                dialog.getBtNegative().setText(getResources().getString(R.string.back_menu));
            });
            dialog.show();
        });
    }

    private void livesChanged(int lives){
        if (lives == 2) binding.TrueColorsIvHeart3.setVisibility(View.GONE);
        else if (lives == 1) binding.TrueColorsIvHeart2.setVisibility(View.GONE);
        else if (lives == 0) binding.TrueColorsIvHeart1.setVisibility(View.GONE);
    }

    private void changeFalseColors(final int color){
        binding.TrueColorsProgressBar.setProgressTintList(ColorStateList.valueOf(color));
        binding.TrueColorsTvColor.setTextColor(color);
    }

    private void changeTrueColorText(final int color){
        GameColor gameColor = GameColor.fromColor(this, color);
        if (gameColor != null) {
            binding.TrueColorsTvColor.setText(gameColor.nameRes);
        }
    }

    private void loadRecord(int maxPoints){
        String Points = getResources().getString(R.string.max_points, maxPoints);
        binding.TrueColorsTvMaxPoints.setText(Points);
    }

    @Override
    public void onStop(){
        super.onStop();
        mViewModel.stopTimer();
    }
}