package com.classicgames.myapplication.ui.views.activity;

import android.content.res.AssetManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.core.content.ContextCompat;

import com.classicgames.myapplication.MyApplication;
import com.classicgames.myapplication.R;
import com.classicgames.myapplication.databinding.ActivityHangmanBinding;
import com.classicgames.myapplication.ui.dialog.CustomDialog;
import com.classicgames.myapplication.ui.viewmodels.HangmanViewModel;
import com.classicgames.myapplication.utils.FileHelper;
import com.classicgames.myapplication.utils.MessageBar;
import com.classicgames.myapplication.utils.ReviewHelper;
import com.classicgames.myapplication.utils.SoundManager;

import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;

public class HangmanActivity extends BaseActivity {

    /** One drawable per number of wrong guesses, 0 through HangmanModel.MAX_WRONG. */
    private static final int[] GALLOWS = {
            R.drawable.hangman_0, R.drawable.hangman_1, R.drawable.hangman_2, R.drawable.hangman_3,
            R.drawable.hangman_4, R.drawable.hangman_5, R.drawable.hangman_6,
    };

    private ActivityHangmanBinding binding;
    private HangmanViewModel viewModel;

    private FileHelper dictionary;

    private Button[] letterButtons;      // indexed by letter - 'A'

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHangmanBinding.inflate(getLayoutInflater());
        binding.getRoot().setFitsSystemWindows(true);
        setContentView(binding.getRoot());
        initViewArrays();

        setSupportActionBar(binding.HangmanToolbar.getRoot());
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        binding.HangmanToolbar.ToolbarTitle.setText(R.string.hangman);

        InitializeViewModel();

        binding.HangmanBtSkip.setOnClickListener(v -> {
            SoundManager.play(SoundManager.Sound.CLICK);
            Skip();
        });

        // Opens the words file
        try {
            AssetManager assetManager = getAssets();
            InputStream inputStream = assetManager.open("hangman_words_en.txt");
            dictionary = new FileHelper(inputStream);
        } catch (IOException e) {
            CustomDialog dialog = new CustomDialog(
                    this,
                    getResources().getString(R.string.hangman_file_error),
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
            viewModel.StartGame();
            SetNewAnswer();
        }
    }

    private void InitializeViewModel() {
        viewModel = new HangmanViewModel();

        viewModel.GetSeconds().observe(this, seconds -> {
            Integer minutes = viewModel.GetMinutes().getValue();
            binding.HangmanTvTimer.setText(String.format(Locale.getDefault(), "%d:%02d", minutes, seconds));
        });

        viewModel.GetPoints().observe(this, points -> binding.HangmanTvPoints.setText(getResources().getString(R.string.points, points)));
        viewModel.GetWrongGuesses().observe(this, wrong -> binding.HangmanIvGallows.setImageResource(GALLOWS[wrong]));
        viewModel.GetMaskedWord().observe(this, word -> binding.HangmanTvWord.setText(word));
    }

    /** Draws a fresh word and clears the keyboard colours. */
    private void SetNewAnswer() {
        if (dictionary == null) return;
        viewModel.SetNewAnswer(dictionary.GetWord());
        ResetKeyboard();
    }

    private void Skip() {
        if (viewModel.GetPoints().getValue() != null && viewModel.GetPoints().getValue() == 0) {
            MessageBar.show(this, R.string.hangman_cant_skip);
            return;
        }
        viewModel.SkipWord();
        SetNewAnswer();
    }

    public void KeyPressed(View view) {
        char letter = (String.valueOf(((Button) view).getText())).charAt(0);

        switch (viewModel.GuessLetter(letter)) {
            case ALREADY_GUESSED:
                return;
            case CORRECT:
                SoundManager.play(SoundManager.Sound.CLICK);
                MarkLetter(letter, R.color.hangman_correct);
                break;
            case WRONG:
                SoundManager.play(SoundManager.Sound.CLICK);
                MarkLetter(letter, R.color.hangman_wrong);
                break;
            case WORD_SOLVED:
                MarkLetter(letter, R.color.hangman_correct);
                Win();
                break;
            case GAME_OVER:
                MarkLetter(letter, R.color.hangman_wrong);
                GameOver();
                break;
        }
    }

    private void Win() {
        SoundManager.play(SoundManager.Sound.WIN);
        MyApplication.getInstance().getRecords().recordHangmanWordSolved();
        MessageBar.show(this, getResources().getString(R.string.hangman_win, viewModel.GetAnswer()));
        SetNewAnswer();
    }

    private void GameOver() {
        viewModel.GameOver();
        SoundManager.play(viewModel.IsNewRecord() ? SoundManager.Sound.RECORD : SoundManager.Sound.GAME_OVER);
        MyApplication.getInstance().getRecords().recordHangmanGameOver(viewModel.GetWrongLetters(), viewModel.GetPerfectWords());
        binding.HangmanBtSkip.setEnabled(false);
        SetKeyboardEnabled(false);
        // The run is over, so show the word the player never got.
        binding.HangmanTvWord.setText(viewModel.GetAnswer());

        CustomDialog.DialogButtonClick gameOverDialog = new CustomDialog.DialogButtonClick() {
            @Override
            public void onPositiveButtonClicked() {
                binding.HangmanBtSkip.setEnabled(true);
                SetKeyboardEnabled(true);
                viewModel.StartGame();
                SetNewAnswer();
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
            message = getResources().getString(R.string.hangman_game_over_record, points, viewModel.GetTimer(), viewModel.GetAnswer());
            viewModel.SaveNewRecord();
        } else
            message = getResources().getString(R.string.hangman_game_over_no_record, points, viewModel.GetTimer(), viewModel.GetAnswer());

        CustomDialog dialog = new CustomDialog(this, message, getResources().getString(R.string.game_over_title), gameOverDialog);
        dialog.setCancelable(false);
        if (newRecord) {
            dialog.setShareText(getResources().getString(R.string.share_record_points,
                    getResources().getString(R.string.hangman_game), points,
                    getResources().getString(R.string.store_link)));
            ReviewHelper.requestReview(this);
        }
        dialog.setOnShowListener(dialogInterface -> {
            dialog.getBtPositive().setText(getResources().getString(R.string.play_again));
            dialog.getBtNegative().setText(getResources().getString(R.string.back_menu));
        });
        dialog.show();
    }

    /** Colours a used letter and takes it out of play. */
    private void MarkLetter(char letter, int colorRes) {
        int index = letter - 'A';
        if (index < 0 || index >= letterButtons.length) return;
        letterButtons[index].setBackgroundColor(ContextCompat.getColor(this, colorRes));
        letterButtons[index].setEnabled(false);
    }

    private void ResetKeyboard() {
        int normal = ContextCompat.getColor(this, R.color.soft_orange_500);
        for (Button button : letterButtons) {
            button.setBackgroundColor(normal);
            button.setEnabled(true);
        }
    }

    private void SetKeyboardEnabled(boolean enabled) {
        for (Button button : letterButtons) {
            button.setEnabled(enabled);
        }
    }

    /** Caches the A-Z keyboard buttons, indexed by letter - 'A'. */
    private void initViewArrays() {
        letterButtons = new Button[]{
                binding.HangmanBtLetterA, binding.HangmanBtLetterB, binding.HangmanBtLetterC, binding.HangmanBtLetterD,
                binding.HangmanBtLetterE, binding.HangmanBtLetterF, binding.HangmanBtLetterG, binding.HangmanBtLetterH,
                binding.HangmanBtLetterI, binding.HangmanBtLetterJ, binding.HangmanBtLetterK, binding.HangmanBtLetterL,
                binding.HangmanBtLetterM, binding.HangmanBtLetterN, binding.HangmanBtLetterO, binding.HangmanBtLetterP,
                binding.HangmanBtLetterQ, binding.HangmanBtLetterR, binding.HangmanBtLetterS, binding.HangmanBtLetterT,
                binding.HangmanBtLetterU, binding.HangmanBtLetterV, binding.HangmanBtLetterW, binding.HangmanBtLetterX,
                binding.HangmanBtLetterY, binding.HangmanBtLetterZ,
        };
    }
}
