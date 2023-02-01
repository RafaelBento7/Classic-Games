package com.classicgames.myapplication.ui.views.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.classicgames.myapplication.R;
import com.classicgames.myapplication.databinding.ActivityTicTacToeBinding;
import com.classicgames.myapplication.ui.viewmodels.TicTacToeViewModel;

public class TicTacToeActivity extends AppCompatActivity {

    private ActivityTicTacToeBinding binding;
    private TicTacToeViewModel viewModel;

    private int playerOnePoints = 0, playerTwoPoints = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new TicTacToeViewModel();
        binding = DataBindingUtil.setContentView(this, R.layout.activity_tic_tac_toe);
        binding.setActivity(this);

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

    public void playerMove(View view, int row, int col){
        if (!viewModel.canPlay(row, col)){    // Checks if player can play
            Toast.makeText(this, "You can not play there, you know it...", Toast.LENGTH_SHORT).show();
            return;
        }

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
        Toast.makeText(this, getResources().getString(R.string.tictactoe_draw), Toast.LENGTH_SHORT).show();
        initGame();
    }

    private void gameOver() {
        int currentPlayer = viewModel.getPlayerTurn();
        Toast.makeText(this, getResources().getString(R.string.tictactoe_win) + " "  + viewModel.getPlayerTurn() + "!", Toast.LENGTH_SHORT).show();
        if(currentPlayer == 1){
            Toast.makeText(this, getResources().getString(R.string.tictactoe_win)+" 1!", Toast.LENGTH_SHORT).show();
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