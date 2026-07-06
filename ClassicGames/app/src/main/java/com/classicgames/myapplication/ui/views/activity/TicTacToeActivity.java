package com.classicgames.myapplication.ui.views.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.databinding.DataBindingUtil;

import com.classicgames.myapplication.MyApplication;
import com.classicgames.myapplication.R;
import com.classicgames.myapplication.databinding.ActivityTicTacToeBinding;
import com.classicgames.myapplication.ui.viewmodels.TicTacToeViewModel;
import com.classicgames.myapplication.utils.MessageBar;
import com.classicgames.myapplication.utils.SoundManager;

public class TicTacToeActivity extends BaseActivity {

    private ActivityTicTacToeBinding binding;
    private TicTacToeViewModel viewModel;

    private int playerOnePoints = 0, playerTwoPoints = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new TicTacToeViewModel();
        binding = DataBindingUtil.setContentView(this, R.layout.activity_tic_tac_toe);
        binding.getRoot().setFitsSystemWindows(true);
        binding.setActivity(this);

        setSupportActionBar(binding.TicTacToeToolbar.getRoot());
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        binding.TicTacToeToolbar.ToolbarTitle.setText(R.string.tic_tac_toe_game);

        String pointsTextPlayerOne = getResources().getString(R.string.tictactoe_player_one)+" 0";
        binding.TictactoeTvPlayerOne.setText(pointsTextPlayerOne);
        String pointsTextPlayerTwo = getResources().getString(R.string.tictactoe_player_two)+" 0";
        binding.TictactoeTvPlayerTwo.setText(pointsTextPlayerTwo);
        initGame();
    }

    public void initGame(){
        clearImages();
        viewModel.setNewGame();
    }

    /** Restart button tap: plays a click, then starts a fresh game. */
    public void restartGame(){
        SoundManager.play(SoundManager.Sound.CLICK);
        initGame();
    }

    public void playerMove(View view, int row, int col){
        if (!viewModel.canPlay(row, col)){    // Checks if player can play
            MessageBar.show(this, R.string.can_not_play_there);
            return;
        }

        SoundManager.play(SoundManager.Sound.CLICK);
        int currentPlayer = viewModel.getPlayerTurn();

        // Add X or O to ImageView
        ImageView imageView = (ImageView) view;
        if (currentPlayer == 1) imageView.setImageResource(R.drawable.tic_tac_toe_ic_x);
        else imageView.setImageResource(R.drawable.tic_tac_toe_ic_o);

        viewModel.newPlayerMove(row, col);

        if (viewModel.checkWin()){
            gameOver();
            return;
        }

        if (viewModel.checkDraw()){
            draw();
            return;
        }

        if (currentPlayer == 1) binding.TictactoeTvPlayerTurn.setText(getResources().getString(R.string.tictactoe_turn_player_two));
        else binding.TictactoeTvPlayerTurn.setText(getResources().getString(R.string.tictactoe_turn_player_one));
        viewModel.changePlayerTurn();
    }

    private void clearImages(){
        binding.TictactoeIv00.setImageResource(0);
        binding.TictactoeIv01.setImageResource(0);
        binding.TictactoeIv02.setImageResource(0);
        binding.TictactoeIv10.setImageResource(0);
        binding.TictactoeIv11.setImageResource(0);
        binding.TictactoeIv12.setImageResource(0);
        binding.TictactoeIv20.setImageResource(0);
        binding.TictactoeIv21.setImageResource(0);
        binding.TictactoeIv22.setImageResource(0);

    }

    private void draw() {
        SoundManager.play(SoundManager.Sound.DRAW);
        MyApplication.getInstance().getRecords().recordTicTacToeDraw();
        MessageBar.show(this, R.string.tictactoe_draw);
        viewModel.changePlayerTurn();
        initGame();
    }

    private void gameOver() {
        int currentPlayer = viewModel.getPlayerTurn();
        SoundManager.play(SoundManager.Sound.WIN);
        MyApplication.getInstance().getRecords().recordTicTacToeWin(currentPlayer);
        MessageBar.show(this, getResources().getString(R.string.tictactoe_win) + " " + currentPlayer + "!");
        if(currentPlayer == 1){
            playerOnePoints++;
            String pointsText = getResources().getString(R.string.tictactoe_player_one)+" "+playerOnePoints;
            binding.TictactoeTvPlayerOne.setText(pointsText);
        }
        else{

            playerTwoPoints++;
            String pointsText = getResources().getString(R.string.tictactoe_player_two)+" "+playerTwoPoints;
            binding.TictactoeTvPlayerTwo.setText(pointsText);
        }
        initGame();
    }
}