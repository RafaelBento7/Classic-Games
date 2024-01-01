package com.classicgames.myapplication.ui.views.activity;


import android.annotation.SuppressLint;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.classicgames.myapplication.R;
import com.classicgames.myapplication.data.models.WordleModel;
import com.classicgames.myapplication.databinding.ActivityWordleBinding;
import com.classicgames.myapplication.ui.viewmodels.WordleViewModel;
import com.classicgames.myapplication.ui.dialog.CustomDialog;
import com.classicgames.myapplication.utils.FileHelper;
import com.google.android.material.snackbar.Snackbar;

import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;

public class WordleActivity extends AppCompatActivity {

    private ActivityWordleBinding binding;
    private WordleViewModel viewModel;

    private FileHelper dictionary;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityWordleBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        InitializeViewModel();
        binding.WordleBtCheck.setOnClickListener(v -> {
            if (viewModel.GetCurrentAttemptPos() > 4) {
                if (viewModel.IsWin()) Win();
                else if (viewModel.IsGameOver()) GameOver();
                else {
                    ChangeColorStates();
                    viewModel.SetNextAttempt();
                    WriteAttempt();
                }
            } else {
                Toast.makeText(this, getResources().getString(R.string.wordle_check_error), Toast.LENGTH_SHORT).show();
            }
        });

        binding.WordleBtSkip.setOnClickListener(v -> Skip());

        // Opens the words files
        try {
            AssetManager assetManager = getAssets();
            InputStream inputStream = assetManager.open("wordle_words_en.txt");
            dictionary = new FileHelper(inputStream);
        } catch (IOException e) {
            CustomDialog dialog = new CustomDialog(
                    this,
                    getResources().getString(R.string.wordle_file_error),
                    getResources().getString(R.string.game_over_title),
                    null
            );
            dialog.setCancelable(false);
            dialog.setOnShowListener(dialogInterface -> {
                dialog.getBtNegative().setText(getResources().getString(R.string.play_again));
                dialog.getBtPositive().setVisibility(View.GONE);
            });
            dialog.show();
        }

        if (dictionary != null) {
            SetNewAnswer();
            viewModel.StartGame();
        }
    }

    private void InitializeViewModel() {
        viewModel = new WordleViewModel();

        viewModel.GetSeconds().observe(this, seconds -> {
            Integer minutes = viewModel.GetMinutes().getValue();
            binding.WordleTvTimer.setText(String.format(Locale.getDefault(), "%d:%02d", minutes, seconds));
        });

        viewModel.GetPoints().observe(this, points -> binding.WordleTvPoints.setText(getResources().getString(R.string.points, points)));
    }

    private void SetNewAnswer() {
        if (dictionary != null) {
            viewModel.SetNewAnswer(dictionary.GetWord());
        }
    }

    private void Skip() {
        if (viewModel.GetPoints().getValue() != null && viewModel.GetPoints().getValue() == 0) {
            Toast.makeText(this, getResources().getString(R.string.wordle_cant_skip), Toast.LENGTH_SHORT).show();
            return;
        }
        viewModel.SkipRound();
        ClearAttempts();
        ClearColorLetters();
        SetNewAnswer();
    }

    private void Win() {
        ClearAttempts();
        Snackbar snackbar = Snackbar.make(binding.WordleLayout, getResources().getString(R.string.wordle_win, viewModel.GetAnswer()), Snackbar.LENGTH_LONG);
        snackbar.show();
        viewModel.WinRound();
        ClearColorLetters();
        SetNewAnswer();
    }

    private void GameOver() {
        viewModel.GameOver();
        binding.WordleBtSkip.setEnabled(false);
        binding.WordleBtCheck.setEnabled(false);

        CustomDialog.DialogButtonClick gameOverDialog = new CustomDialog.DialogButtonClick() {
            @Override
            public void onPositiveButtonClicked() {
                ClearAttempts();
                ClearColorLetters();
                if (dictionary != null) {
                    SetNewAnswer();
                    viewModel.StartGame();
                }
                binding.WordleBtCheck.setEnabled(true);
                binding.WordleBtSkip.setEnabled(true);
            }

            @Override
            public void onNegativeButtonClicked() {
                finish();
            }
        };

        String message;
        int points = viewModel.GetPoints().getValue();
        if (viewModel.IsNewRecord()) {
            message = getResources().getString(R.string.wordle_game_over_record, points, viewModel.GetTimer(), viewModel.GetAnswer());
            viewModel.SaveNewRecord();
        } else
            message = getResources().getString(R.string.wordle_game_over_no_record, points, viewModel.GetTimer(), viewModel.GetAnswer());

        CustomDialog dialog = new CustomDialog(
                this,
                message,
                getResources().getString(R.string.game_over_title),
                gameOverDialog
        );
        dialog.setCancelable(false);
        dialog.setOnShowListener(dialogInterface -> {
            dialog.getBtPositive().setText(getResources().getString(R.string.play_again));
            dialog.getBtNegative().setText(getResources().getString(R.string.back_menu));
        });
        dialog.show();
    }

    private void ChangeColorStates() {
        WordleModel.LETTER_STATE[] currentAttemptStates = viewModel.GetCurrentAttemptStatus();
        char[] currentAttemptLetters = viewModel.GetCurrentAttempt();

        for (int i = 0; i < 5; i++) {
            int colorID;
            if (currentAttemptStates[i] == WordleModel.LETTER_STATE.NONE)
                colorID = getResources().getColor(R.color.wordle_wrong_letter);

            else if (currentAttemptStates[i] == WordleModel.LETTER_STATE.WRONG_POSITION)
                colorID = getResources().getColor(R.color.wordle_wrong_position);

            else if (currentAttemptStates[i] == WordleModel.LETTER_STATE.CORRECT)
                colorID = getResources().getColor(R.color.wordle_correct);
            else colorID = getResources().getColor(R.color.soft_orange_500);


            SetColorLetter(colorID, (int) currentAttemptLetters[i]);
            SetRectangleColor(viewModel.GetAttempt(), colorID, i);
        }
    }

    private void SetRectangleColor(int attempt, int colorID, int pos) {
        switch (attempt) {
            case 0:
                switch (pos) {
                    case 0:
                        binding.WordleFirstAttemptFirst.setBackgroundColor(colorID);
                        break;
                    case 1:
                        binding.WordleFirstAttemptSecond.setBackgroundColor(colorID);
                        break;
                    case 2:
                        binding.WordleFirstAttemptThird.setBackgroundColor(colorID);
                        break;
                    case 3:
                        binding.WordleFirstAttemptForth.setBackgroundColor(colorID);
                        break;
                    case 4:
                        binding.WordleFirstAttemptFifth.setBackgroundColor(colorID);
                        break;
                }
                break;
            case 1:
                switch (pos) {
                    case 0:
                        binding.WordleSecondAttemptFirst.setBackgroundColor(colorID);
                        break;
                    case 1:
                        binding.WordleSecondAttemptSecond.setBackgroundColor(colorID);
                        break;
                    case 2:
                        binding.WordleSecondAttemptThird.setBackgroundColor(colorID);
                        break;
                    case 3:
                        binding.WordleSecondAttemptForth.setBackgroundColor(colorID);
                        break;
                    case 4:
                        binding.WordleSecondAttemptFifth.setBackgroundColor(colorID);
                        break;
                }
                break;
            case 2:
                switch (pos) {
                    case 0:
                        binding.WordleThirdAttemptFirst.setBackgroundColor(colorID);
                        break;
                    case 1:
                        binding.WordleThirdAttemptSecond.setBackgroundColor(colorID);
                        break;
                    case 2:
                        binding.WordleThirdAttemptThird.setBackgroundColor(colorID);
                        break;
                    case 3:
                        binding.WordleThirdAttemptForth.setBackgroundColor(colorID);
                        break;
                    case 4:
                        binding.WordleThirdAttemptFifth.setBackgroundColor(colorID);
                        break;
                }
                break;
            case 3:
                switch (pos) {
                    case 0:
                        binding.WordleForthAttemptFirst.setBackgroundColor(colorID);
                        break;
                    case 1:
                        binding.WordleForthAttemptSecond.setBackgroundColor(colorID);
                        break;
                    case 2:
                        binding.WordleForthAttemptThird.setBackgroundColor(colorID);
                        break;
                    case 3:
                        binding.WordleForthAttemptForth.setBackgroundColor(colorID);
                        break;
                    case 4:
                        binding.WordleForthAttemptFifth.setBackgroundColor(colorID);
                        break;
                }
                break;
            case 4:
                switch (pos) {
                    case 0:
                        binding.WordleFifthAttemptFirst.setBackgroundColor(colorID);
                        break;
                    case 1:
                        binding.WordleFifthAttemptSecond.setBackgroundColor(colorID);
                        break;
                    case 2:
                        binding.WordleFifthAttemptThird.setBackgroundColor(colorID);
                        break;
                    case 3:
                        binding.WordleFifthAttemptForth.setBackgroundColor(colorID);
                        break;
                    case 4:
                        binding.WordleFifthAttemptFifth.setBackgroundColor(colorID);
                        break;
                }
                break;
        }
    }

    private void ClearColorLetters() {
        for (int i = 65; i < 91; i++) {
            SetColorLetter(getResources().getColor(R.color.soft_orange_500), i);
        }
    }

    private void SetColorLetter(int color, int currentAttemptLetter) {
        switch ((int) currentAttemptLetter) {
            case 65:
                binding.WordleBtLetterA.setBackgroundColor(color);
                break;
            case 66:
                binding.WordleBtLetterB.setBackgroundColor(color);
                break;
            case 67:
                binding.WordleBtLetterC.setBackgroundColor(color);
                break;
            case 68:
                binding.WordleBtLetterD.setBackgroundColor(color);
                break;
            case 69:
                binding.WordleBtLetterE.setBackgroundColor(color);
                break;
            case 70:
                binding.WordleBtLetterF.setBackgroundColor(color);
                break;
            case 71:
                binding.WordleBtLetterG.setBackgroundColor(color);
                break;
            case 72:
                binding.WordleBtLetterH.setBackgroundColor(color);
                break;
            case 73:
                binding.WordleBtLetterI.setBackgroundColor(color);
                break;
            case 74:
                binding.WordleBtLetterJ.setBackgroundColor(color);
                break;
            case 75:
                binding.WordleBtLetterK.setBackgroundColor(color);
                break;
            case 76:
                binding.WordleBtLetterL.setBackgroundColor(color);
                break;
            case 77:
                binding.WordleBtLetterM.setBackgroundColor(color);
                break;
            case 78:
                binding.WordleBtLetterN.setBackgroundColor(color);
                break;
            case 79:
                binding.WordleBtLetterO.setBackgroundColor(color);
                break;
            case 80:
                binding.WordleBtLetterP.setBackgroundColor(color);
                break;
            case 81:
                binding.WordleBtLetterQ.setBackgroundColor(color);
                break;
            case 82:
                binding.WordleBtLetterR.setBackgroundColor(color);
                break;
            case 83:
                binding.WordleBtLetterS.setBackgroundColor(color);
                break;
            case 84:
                binding.WordleBtLetterT.setBackgroundColor(color);
                break;
            case 85:
                binding.WordleBtLetterU.setBackgroundColor(color);
                break;
            case 86:
                binding.WordleBtLetterV.setBackgroundColor(color);
                break;
            case 87:
                binding.WordleBtLetterW.setBackgroundColor(color);
                break;
            case 88:
                binding.WordleBtLetterX.setBackgroundColor(color);
                break;
            case 89:
                binding.WordleBtLetterY.setBackgroundColor(color);
                break;
            case 90:
                binding.WordleBtLetterZ.setBackgroundColor(color);
                break;
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private void ClearAttempts() {
        binding.WordleFirstAttemptFirst.setBackground(getResources().getDrawable(R.drawable.wordle_border));
        binding.WordleFirstAttemptSecond.setBackground(getResources().getDrawable(R.drawable.wordle_border));
        binding.WordleFirstAttemptThird.setBackground(getResources().getDrawable(R.drawable.wordle_border));
        binding.WordleFirstAttemptForth.setBackground(getResources().getDrawable(R.drawable.wordle_border));
        binding.WordleFirstAttemptFifth.setBackground(getResources().getDrawable(R.drawable.wordle_border));
        binding.WordleFirstAttemptFirst.setText("");
        binding.WordleFirstAttemptSecond.setText("");
        binding.WordleFirstAttemptThird.setText("");
        binding.WordleFirstAttemptForth.setText("");
        binding.WordleFirstAttemptFifth.setText("");

        binding.WordleSecondAttemptFirst.setBackground(getResources().getDrawable(R.drawable.wordle_border));
        binding.WordleSecondAttemptSecond.setBackground(getResources().getDrawable(R.drawable.wordle_border));
        binding.WordleSecondAttemptThird.setBackground(getResources().getDrawable(R.drawable.wordle_border));
        binding.WordleSecondAttemptForth.setBackground(getResources().getDrawable(R.drawable.wordle_border));
        binding.WordleSecondAttemptFifth.setBackground(getResources().getDrawable(R.drawable.wordle_border));
        binding.WordleSecondAttemptFirst.setText("");
        binding.WordleSecondAttemptSecond.setText("");
        binding.WordleSecondAttemptThird.setText("");
        binding.WordleSecondAttemptForth.setText("");
        binding.WordleSecondAttemptFifth.setText("");

        binding.WordleThirdAttemptFirst.setBackground(getResources().getDrawable(R.drawable.wordle_border));
        binding.WordleThirdAttemptSecond.setBackground(getResources().getDrawable(R.drawable.wordle_border));
        binding.WordleThirdAttemptThird.setBackground(getResources().getDrawable(R.drawable.wordle_border));
        binding.WordleThirdAttemptForth.setBackground(getResources().getDrawable(R.drawable.wordle_border));
        binding.WordleThirdAttemptFifth.setBackground(getResources().getDrawable(R.drawable.wordle_border));
        binding.WordleThirdAttemptFirst.setText("");
        binding.WordleThirdAttemptSecond.setText("");
        binding.WordleThirdAttemptThird.setText("");
        binding.WordleThirdAttemptForth.setText("");
        binding.WordleThirdAttemptFifth.setText("");

        binding.WordleForthAttemptFirst.setBackground(getResources().getDrawable(R.drawable.wordle_border));
        binding.WordleForthAttemptSecond.setBackground(getResources().getDrawable(R.drawable.wordle_border));
        binding.WordleForthAttemptThird.setBackground(getResources().getDrawable(R.drawable.wordle_border));
        binding.WordleForthAttemptForth.setBackground(getResources().getDrawable(R.drawable.wordle_border));
        binding.WordleForthAttemptFifth.setBackground(getResources().getDrawable(R.drawable.wordle_border));
        binding.WordleForthAttemptFirst.setText("");
        binding.WordleForthAttemptSecond.setText("");
        binding.WordleForthAttemptThird.setText("");
        binding.WordleForthAttemptForth.setText("");
        binding.WordleForthAttemptFifth.setText("");

        binding.WordleFifthAttemptFirst.setBackground(getResources().getDrawable(R.drawable.wordle_border));
        binding.WordleFifthAttemptSecond.setBackground(getResources().getDrawable(R.drawable.wordle_border));
        binding.WordleFifthAttemptThird.setBackground(getResources().getDrawable(R.drawable.wordle_border));
        binding.WordleFifthAttemptForth.setBackground(getResources().getDrawable(R.drawable.wordle_border));
        binding.WordleFifthAttemptFifth.setBackground(getResources().getDrawable(R.drawable.wordle_border));
        binding.WordleFifthAttemptFirst.setText("");
        binding.WordleFifthAttemptSecond.setText("");
        binding.WordleFifthAttemptThird.setText("");
        binding.WordleFifthAttemptForth.setText("");
        binding.WordleFifthAttemptFifth.setText("");
    }

    public void KeyPressed(View view) {
        char letter = (String.valueOf(((Button) view).getText())).charAt(0);
        viewModel.KeyPressed(letter);
        WriteAttempt();
    }

    private void WriteAttempt() {
        switch (viewModel.GetAttempt()) {
            case 0:
                binding.WordleFirstAttemptFirst.setText(String.valueOf(viewModel.GetCurrentAttempt()[0]));
                binding.WordleFirstAttemptSecond.setText(String.valueOf(viewModel.GetCurrentAttempt()[1]));
                binding.WordleFirstAttemptThird.setText(String.valueOf(viewModel.GetCurrentAttempt()[2]));
                binding.WordleFirstAttemptForth.setText(String.valueOf(viewModel.GetCurrentAttempt()[3]));
                binding.WordleFirstAttemptFifth.setText(String.valueOf(viewModel.GetCurrentAttempt()[4]));
                break;
            case 1:
                binding.WordleSecondAttemptFirst.setText(String.valueOf(viewModel.GetCurrentAttempt()[0]));
                binding.WordleSecondAttemptSecond.setText(String.valueOf(viewModel.GetCurrentAttempt()[1]));
                binding.WordleSecondAttemptThird.setText(String.valueOf(viewModel.GetCurrentAttempt()[2]));
                binding.WordleSecondAttemptForth.setText(String.valueOf(viewModel.GetCurrentAttempt()[3]));
                binding.WordleSecondAttemptFifth.setText(String.valueOf(viewModel.GetCurrentAttempt()[4]));
                break;
            case 2:
                binding.WordleThirdAttemptFirst.setText(String.valueOf(viewModel.GetCurrentAttempt()[0]));
                binding.WordleThirdAttemptSecond.setText(String.valueOf(viewModel.GetCurrentAttempt()[1]));
                binding.WordleThirdAttemptThird.setText(String.valueOf(viewModel.GetCurrentAttempt()[2]));
                binding.WordleThirdAttemptForth.setText(String.valueOf(viewModel.GetCurrentAttempt()[3]));
                binding.WordleThirdAttemptFifth.setText(String.valueOf(viewModel.GetCurrentAttempt()[4]));
                break;
            case 3:
                binding.WordleForthAttemptFirst.setText(String.valueOf(viewModel.GetCurrentAttempt()[0]));
                binding.WordleForthAttemptSecond.setText(String.valueOf(viewModel.GetCurrentAttempt()[1]));
                binding.WordleForthAttemptThird.setText(String.valueOf(viewModel.GetCurrentAttempt()[2]));
                binding.WordleForthAttemptForth.setText(String.valueOf(viewModel.GetCurrentAttempt()[3]));
                binding.WordleForthAttemptFifth.setText(String.valueOf(viewModel.GetCurrentAttempt()[4]));
                break;
            case 4:
                binding.WordleFifthAttemptFirst.setText(String.valueOf(viewModel.GetCurrentAttempt()[0]));
                binding.WordleFifthAttemptSecond.setText(String.valueOf(viewModel.GetCurrentAttempt()[1]));
                binding.WordleFifthAttemptThird.setText(String.valueOf(viewModel.GetCurrentAttempt()[2]));
                binding.WordleFifthAttemptForth.setText(String.valueOf(viewModel.GetCurrentAttempt()[3]));
                binding.WordleFifthAttemptFifth.setText(String.valueOf(viewModel.GetCurrentAttempt()[4]));
                break;
        }
    }
}