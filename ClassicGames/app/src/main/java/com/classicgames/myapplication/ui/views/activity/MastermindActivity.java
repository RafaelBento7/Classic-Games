package com.classicgames.myapplication.ui.views.activity;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.classicgames.myapplication.R;
import com.classicgames.myapplication.databinding.ActivityMastermindBinding;
import com.classicgames.myapplication.ui.viewmodels.MastermindViewModel;
import com.classicgames.myapplication.utils.CustomDialog;

import java.util.Locale;

public class MastermindActivity extends AppCompatActivity {

    private ActivityMastermindBinding binding;
    private MastermindViewModel viewModel;

    private boolean canPlay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMastermindBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initializeViewModel();
        startGame(null);
    }

    private void initializeViewModel() {
        viewModel = new MastermindViewModel();

        viewModel.setNewAttemptImages(binding.firstLeftOne, binding.firstLeftTwo, binding.firstLeftThree, binding.firstLeftFour, binding.firstTruePosition, binding.firstWrongPosition);
        viewModel.setNewAttemptImages(binding.secondLeftOne, binding.secondLeftTwo, binding.secondLeftThree, binding.secondLeftFour, binding.secondTruePosition, binding.secondWrongPosition);
        viewModel.setNewAttemptImages(binding.thirdLeftOne, binding.thirdLeftTwo, binding.thirdLeftThree, binding.thirdLeftFour, binding.thirdTruePosition, binding.thirdWrongPosition);
        viewModel.setNewAttemptImages(binding.forthLeftOne, binding.forthLeftTwo, binding.forthLeftThree, binding.forthLeftFour, binding.forthTruePosition, binding.forthWrongPosition);
        viewModel.setNewAttemptImages(binding.fifthLeftOne, binding.fifthLeftTwo, binding.fifthLeftThree, binding.fifthLeftFour, binding.fifthTruePosition, binding.fifthWrongPosition);
        viewModel.setNewAttemptImages(binding.sixthLeftOne, binding.sixthLeftTwo, binding.sixthLeftThree, binding.sixthLeftFour, binding.sixthTruePosition, binding.sixthWrongPosition);
        viewModel.setNewAttemptImages(binding.seventhLeftOne, binding.seventhLeftTwo, binding.seventhLeftThree, binding.seventhLeftFour, binding.seventhTruePosition, binding.seventhWrongPosition);
        viewModel.setNewAttemptImages(binding.eighthLeftOne, binding.eighthLeftTwo, binding.eighthLeftThree, binding.eighthLeftFour, binding.eighthTruePosition, binding.eighthWrongPosition);
        viewModel.setNewAttemptImages(binding.ninthLeftOne, binding.ninthLeftTwo, binding.ninthLeftThree, binding.ninthLeftFour, binding.ninthTruePosition, binding.ninthWrongPosition);
        viewModel.setNewAttemptImages(binding.tenthLeftOne, binding.tenthLeftTwo, binding.tenthLeftThree, binding.tenthLeftFour, binding.tenthTruePosition, binding.tenthWrongPosition);

        viewModel.getAttempt().observe(this, attempt -> binding.mastermindAttempts.setText(getResources().getString(R.string.attempts_left, (10 - attempt))));
        viewModel.getSeconds().observe(this, seconds -> {
            Integer minutes = viewModel.getMinutes().getValue();
            binding.mastermindTimer.setText(String.format(Locale.getDefault(), "%d:%02d", minutes, seconds));
        });
        viewModel.getCurrentAttemptTruePosition().observe(this, truePosition -> {
            if (viewModel.getAttempt().getValue() == null) return;
            String trueP = truePosition.toString();
            viewModel.getMastermindAttempt(viewModel.getAttempt().getValue()).getTruePosition().setVisibility(View.VISIBLE);
            viewModel.getMastermindAttempt(viewModel.getAttempt().getValue()).getTruePosition().setText(trueP);
        });
        viewModel.getCurrentAttemptWrongPosition().observe(this, wrongPosition -> {
            if (viewModel.getAttempt().getValue() == null) return;
            String wrongP = wrongPosition.toString();
            viewModel.getMastermindAttempt(viewModel.getAttempt().getValue()).getWrongPosition().setVisibility(View.VISIBLE);
            viewModel.getMastermindAttempt(viewModel.getAttempt().getValue()).getWrongPosition().setText(wrongP);

        });
        viewModel.isGameOver().observe(this, isGameOver -> {
            if (!isGameOver) return;
            gameOver();
        });
        viewModel.isWin().observe(this, isWin -> {
            if (!isWin) return;
            win();
        });
    }

    private void clearImageViewsAttempts() {
        for (int i = 1; i < 10; i++) {
            viewModel.getMastermindAttempt(i).getFirst().setVisibility(View.GONE);
            viewModel.getMastermindAttempt(i).getSecond().setVisibility(View.GONE);
            viewModel.getMastermindAttempt(i).getThird().setVisibility(View.GONE);
            viewModel.getMastermindAttempt(i).getForth().setVisibility(View.GONE);
            viewModel.getMastermindAttempt(i).getTruePosition().setVisibility(View.GONE);
            viewModel.getMastermindAttempt(i).getWrongPosition().setVisibility(View.GONE);
        }
        viewModel.getMastermindAttempt(0).getFirst().setBackgroundTintList(ColorStateList.valueOf(Color.BLACK));
        viewModel.getMastermindAttempt(0).getSecond().setBackgroundTintList(ColorStateList.valueOf(Color.BLACK));
        viewModel.getMastermindAttempt(0).getThird().setBackgroundTintList(ColorStateList.valueOf(Color.BLACK));
        viewModel.getMastermindAttempt(0).getForth().setBackgroundTintList(ColorStateList.valueOf(Color.BLACK));
        viewModel.getMastermindAttempt(0).getTruePosition().setText("0");
        viewModel.getMastermindAttempt(0).getWrongPosition().setText("0");
    }

    public void startGame(View view) {
        hideSolution();
        canPlay = true;
        clearImageViewsAttempts();
        viewModel.startGame();
    }

    private void showSolution() {
        int[] solution = viewModel.getSolution();
        binding.solutionOne.setBackgroundTintList(ColorStateList.valueOf(solution[0]));
        binding.solutionTwo.setBackgroundTintList(ColorStateList.valueOf(solution[1]));
        binding.solutionThree.setBackgroundTintList(ColorStateList.valueOf(solution[2]));
        binding.solutionFour.setBackgroundTintList(ColorStateList.valueOf(solution[3]));
        binding.solutionOne.setText("");
        binding.solutionTwo.setText("");
        binding.solutionThree.setText("");
        binding.solutionFour.setText("");
    }

    private void hideSolution() {
        binding.solutionOne.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.black)));
        binding.solutionTwo.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.black)));
        binding.solutionThree.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.black)));
        binding.solutionFour.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.black)));
        binding.solutionOne.setText("?");
        binding.solutionTwo.setText("?");
        binding.solutionThree.setText("?");
        binding.solutionFour.setText("?");
    }

    public void checkPlay(View view) {
        if (!canPlay) return;

        ColorStateList colorStateList = view.getBackgroundTintList();
        int color = colorStateList.getDefaultColor();
        int colorPicked = viewModel.getColorPicked();

        switch (colorPicked) {
            case 0:
                viewModel.getCurrentMastermindAttempt().getFirst().setVisibility(View.VISIBLE);
                viewModel.getCurrentMastermindAttempt().getFirst().setBackgroundTintList(ColorStateList.valueOf(color));
                break;
            case 1:
                viewModel.getCurrentMastermindAttempt().getSecond().setVisibility(View.VISIBLE);
                viewModel.getCurrentMastermindAttempt().getSecond().setBackgroundTintList(ColorStateList.valueOf(color));
                break;
            case 2:
                viewModel.getCurrentMastermindAttempt().getThird().setVisibility(View.VISIBLE);
                viewModel.getCurrentMastermindAttempt().getThird().setBackgroundTintList(ColorStateList.valueOf(color));
                break;
            case 3:
                viewModel.getCurrentMastermindAttempt().getForth().setVisibility(View.VISIBLE);
                viewModel.getCurrentMastermindAttempt().getForth().setBackgroundTintList(ColorStateList.valueOf(color));
                break;
        }
        viewModel.buttonPressed(color);
    }

    private void gameOver() {
        Toast.makeText(this, R.string.game_over, Toast.LENGTH_SHORT).show();
        showSolution();
        canPlay = false;
    }

    private void win() {
        CustomDialog.DialogButtonClick gameOverDialog = new CustomDialog.DialogButtonClick() {
            @Override
            public void onPositiveButtonClicked() {
                startGame(null);
            }

            @Override
            public void onNegativeButtonClicked() {
                finish();
            }
        };

        String message;
        if (!viewModel.isNewRecord()) message = getResources().getString(R.string.mastermind_no_record);
        else message= getResources().getString(R.string.mastermind_new_record, viewModel.getRecords()[0],viewModel.getRecords()[1],viewModel.getRecords()[2]);

        CustomDialog dialog = new CustomDialog(
                this,
                message,
                gameOverDialog
        );
        dialog.setCancelable(false);
        dialog.setOnShowListener(dialogInterface -> {
            dialog.getBtPositive().setText(getResources().getString(R.string.play_again));
            dialog.getBtNegative().setText(getResources().getString(R.string.back_menu));
        });
        dialog.show();
        showSolution();
        canPlay = false;
    }
}