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
        binding.tictactoePlayerOne.setText(pointsTextPlayerOne);
        String pointsTextPlayerTwo = getResources().getString(R.string.tictactoe_player_two)+" 0";
        binding.tictactoePlayerTwo.setText(pointsTextPlayerTwo);
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
        if (currentPlayer == 1) imageView.setImageResource(R.drawable.tictactoe_x);
        else imageView.setImageResource(R.drawable.tictactoe_o);

        viewModel.newPlayerMove(row, col);

        if (viewModel.checkWin()){
            gameOver();
            return;
        }

        if (viewModel.checkDraw()){
            draw();
            return;
        }

        if (currentPlayer == 1) binding.tictactoePlayerTurn.setText(getResources().getString(R.string.tictactoe_turn_player_two));
        else binding.tictactoePlayerTurn.setText(getResources().getString(R.string.tictactoe_turn_player_one));
        viewModel.changePlayerTurn();
    }

    private void clearImages(){
        binding.tictactoeOne.setImageResource(0);
        binding.tictactoeTwo.setImageResource(0);
        binding.tictactoeThree.setImageResource(0);
        binding.tictactoeFour.setImageResource(0);
        binding.tictactoeFive.setImageResource(0);
        binding.tictactoeSix.setImageResource(0);
        binding.tictactoeSeven.setImageResource(0);
        binding.tictactoeEight.setImageResource(0);
        binding.tictactoeNine.setImageResource(0);
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
            binding.tictactoePlayerOne.setText(pointsText);
        }
        else{

            playerTwoPoints++;
            String pointsText = getResources().getString(R.string.tictactoe_player_two)+" "+playerTwoPoints;
            binding.tictactoePlayerTwo.setText(pointsText);
        }
        initGame();
    }
}