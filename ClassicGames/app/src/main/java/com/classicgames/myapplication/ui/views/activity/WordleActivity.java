package com.classicgames.myapplication.ui.views.activity;


import android.content.res.AssetManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.classicgames.myapplication.MyApplication;
import com.classicgames.myapplication.R;
import com.classicgames.myapplication.data.models.WordleModel;
import com.classicgames.myapplication.databinding.ActivityWordleBinding;
import com.classicgames.myapplication.ui.viewmodels.WordleViewModel;
import com.classicgames.myapplication.ui.dialog.CustomDialog;
import com.classicgames.myapplication.utils.FileHelper;
import com.classicgames.myapplication.utils.MessageBar;
import com.classicgames.myapplication.utils.ReviewHelper;
import com.classicgames.myapplication.utils.SoundManager;

import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;

public class WordleActivity extends BaseActivity {

    private ActivityWordleBinding binding;
    private WordleViewModel viewModel;

    private FileHelper dictionary;

    private TextView[][] attemptCells;   // [attempt 0-4][position 0-4]
    private Button[] letterButtons;      // indexed by letter - 'A'

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityWordleBinding.inflate(getLayoutInflater());
        binding.getRoot().setFitsSystemWindows(true);
        setContentView(binding.getRoot());
        initViewArrays();

        setSupportActionBar(binding.WordleToolbar.getRoot());
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        binding.WordleToolbar.ToolbarTitle.setText(R.string.wordle);

        InitializeViewModel();
        binding.WordleBtCheck.setOnClickListener(v -> {
            SoundManager.play(SoundManager.Sound.CLICK);
            if (viewModel.GetCurrentAttemptPos() > 4) {
                if (viewModel.IsWin()) Win();
                else if (viewModel.IsGameOver()) GameOver();
                else {
                    ChangeColorStates();
                    viewModel.SetNextAttempt();
                    WriteAttempt();
                }
            } else {
                MessageBar.show(this, R.string.wordle_check_error);
            }
        });

        binding.WordleBtSkip.setOnClickListener(v -> {
            SoundManager.play(SoundManager.Sound.CLICK);
            Skip();
        });

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
            MessageBar.show(this, R.string.wordle_cant_skip);
            return;
        }
        viewModel.SkipRound();
        ClearAttempts();
        ClearColorLetters();
        SetNewAnswer();
    }

    private void Win() {
        SoundManager.play(SoundManager.Sound.WIN);
        MyApplication.getInstance().getRecords().recordWordleWin();
        ClearAttempts();
        MessageBar.show(this, getResources().getString(R.string.wordle_win, viewModel.GetAnswer()));
        viewModel.WinRound();
        ClearColorLetters();
        SetNewAnswer();
    }

    private void GameOver() {
        viewModel.GameOver();
        SoundManager.play(viewModel.IsNewRecord() ? SoundManager.Sound.RECORD : SoundManager.Sound.GAME_OVER);
        MyApplication.getInstance().getRecords().recordWordleGameOver();
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
        boolean newRecord = viewModel.IsNewRecord();
        if (newRecord) {
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
        if (newRecord) {
            dialog.setShareText(getResources().getString(R.string.share_record_points,
                    getResources().getString(R.string.wordle_game), points,
                    getResources().getString(R.string.store_link)));
            ReviewHelper.requestReview(this);
        }
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
                colorID = ContextCompat.getColor(this,R.color.wordle_wrong_letter);

            else if (currentAttemptStates[i] == WordleModel.LETTER_STATE.WRONG_POSITION)
                colorID = ContextCompat.getColor(this,R.color.wordle_wrong_position);

            else if (currentAttemptStates[i] == WordleModel.LETTER_STATE.CORRECT)
                colorID = ContextCompat.getColor(this,R.color.wordle_correct);
            else colorID = ContextCompat.getColor(this,R.color.soft_orange_500);


            SetColorLetter(colorID, (int) currentAttemptLetters[i]);
            SetRectangleColor(viewModel.GetAttempt(), colorID, i);
        }
    }

    private void SetRectangleColor(int attempt, int colorID, int pos) {
        attemptCells[attempt][pos].setBackgroundColor(colorID);
    }

    private void ClearColorLetters() {
        for (int i = 65; i < 91; i++) {
            SetColorLetter(ContextCompat.getColor(this,R.color.soft_orange_500), i);
        }
    }

    private void SetColorLetter(int color, int currentAttemptLetter) {
        int index = currentAttemptLetter - 'A';
        if (index >= 0 && index < letterButtons.length) {
            letterButtons[index].setBackgroundColor(color);
        }
    }

    private void ClearAttempts() {
        for (TextView[] row : attemptCells) {
            for (TextView cell : row) {
                cell.setBackground(ContextCompat.getDrawable(this, R.drawable.wordle_border));
                cell.setText("");
            }
        }
    }

    public void KeyPressed(View view) {
        SoundManager.play(SoundManager.Sound.CLICK);
        char letter = (String.valueOf(((Button) view).getText())).charAt(0);
        viewModel.KeyPressed(letter);
        WriteAttempt();
    }

    private void WriteAttempt() {
        int attempt = viewModel.GetAttempt();
        char[] current = viewModel.GetCurrentAttempt();
        for (int i = 0; i < attemptCells[attempt].length; i++) {
            attemptCells[attempt][i].setText(String.valueOf(current[i]));
        }
    }

    /** Caches the 5x5 grid of attempt cells and the A-Z keyboard buttons */
    private void initViewArrays() {
        attemptCells = new TextView[][]{
                {binding.WordleFirstAttemptFirst, binding.WordleFirstAttemptSecond, binding.WordleFirstAttemptThird, binding.WordleFirstAttemptForth, binding.WordleFirstAttemptFifth},
                {binding.WordleSecondAttemptFirst, binding.WordleSecondAttemptSecond, binding.WordleSecondAttemptThird, binding.WordleSecondAttemptForth, binding.WordleSecondAttemptFifth},
                {binding.WordleThirdAttemptFirst, binding.WordleThirdAttemptSecond, binding.WordleThirdAttemptThird, binding.WordleThirdAttemptForth, binding.WordleThirdAttemptFifth},
                {binding.WordleForthAttemptFirst, binding.WordleForthAttemptSecond, binding.WordleForthAttemptThird, binding.WordleForthAttemptForth, binding.WordleForthAttemptFifth},
                {binding.WordleFifthAttemptFirst, binding.WordleFifthAttemptSecond, binding.WordleFifthAttemptThird, binding.WordleFifthAttemptForth, binding.WordleFifthAttemptFifth},
        };
        letterButtons = new Button[]{
                binding.WordleBtLetterA, binding.WordleBtLetterB, binding.WordleBtLetterC, binding.WordleBtLetterD,
                binding.WordleBtLetterE, binding.WordleBtLetterF, binding.WordleBtLetterG, binding.WordleBtLetterH,
                binding.WordleBtLetterI, binding.WordleBtLetterJ, binding.WordleBtLetterK, binding.WordleBtLetterL,
                binding.WordleBtLetterM, binding.WordleBtLetterN, binding.WordleBtLetterO, binding.WordleBtLetterP,
                binding.WordleBtLetterQ, binding.WordleBtLetterR, binding.WordleBtLetterS, binding.WordleBtLetterT,
                binding.WordleBtLetterU, binding.WordleBtLetterV, binding.WordleBtLetterW, binding.WordleBtLetterX,
                binding.WordleBtLetterY, binding.WordleBtLetterZ,
        };
    }
}