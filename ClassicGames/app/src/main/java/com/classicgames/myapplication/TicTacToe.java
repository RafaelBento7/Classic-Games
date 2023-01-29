package com.classicgames.myapplication;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class TicTacToe extends AppCompatActivity {

    private List<ImageView> imageViews;
    private boolean playerTurn = false;     // False = player 1, true = player 2
    private TextView tvPlayerTurn,tvPlayerOnePoints,tvPlayerTwoPoints;
    private int[][] grid;
    private int playerOnePoints=0,playerTwoPoints=0;
    private Button btRestart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tic_tac_toe);
        imageViews = new ArrayList<>();
        getComponents();
        grid = new int[3][3];
        onClickListeners();
        String pointsTextPlayerOne = getResources().getString(R.string.tictactoe_player_one)+" 0";
        tvPlayerOnePoints.setText(pointsTextPlayerOne);
        String pointsTextPlayerTwo = getResources().getString(R.string.tictactoe_player_two)+" 0";
        tvPlayerTwoPoints.setText(pointsTextPlayerTwo);
        startGame();
    }

    private void getComponents() {
        imageViews.add(findViewById(R.id.tictactoeOne));
        imageViews.add(findViewById(R.id.tictactoeTwo));
        imageViews.add(findViewById(R.id.tictactoeThree));
        imageViews.add(findViewById(R.id.tictactoeFour));
        imageViews.add(findViewById(R.id.tictactoeFive));
        imageViews.add(findViewById(R.id.tictactoeSix));
        imageViews.add(findViewById(R.id.tictactoeSeven));
        imageViews.add(findViewById(R.id.tictactoeEight));
        imageViews.add(findViewById(R.id.tictactoeNine));
        tvPlayerTurn = findViewById(R.id.tictactoePlayerTurn);
        btRestart = findViewById(R.id.btTictactoeRestart);
        tvPlayerOnePoints = findViewById(R.id.tictactoePlayerOne);
        tvPlayerTwoPoints = findViewById(R.id.tictactoePlayerTwo);
    }

    private void onClickListeners() {
        imageViews.get(0).setOnClickListener(view -> play(0,0));
        imageViews.get(1).setOnClickListener(view -> play(0,1));
        imageViews.get(2).setOnClickListener(view -> play(0,2));
        imageViews.get(3).setOnClickListener(view -> play(1,0));
        imageViews.get(4).setOnClickListener(view -> play(1,1));
        imageViews.get(5).setOnClickListener(view -> play(1,2));
        imageViews.get(6).setOnClickListener(view -> play(2,0));
        imageViews.get(7).setOnClickListener(view -> play(2,1));
        imageViews.get(8).setOnClickListener(view -> play(2,2));
        btRestart.setOnClickListener(view-> startGame());
    }

    private void startGame(){
        clearImages();
        for (int i=0;i<3;i++)
            for (int j = 0;j<3;j++)
                grid[i][j] = 0;
    }

    private void clearImages(){
        for (ImageView image:imageViews) {
            image.setImageResource(0);
        }
    }

    private void play(int x,int y){
        if (grid[x][y] != 0) return;
        if(!playerTurn) grid[x][y] = 3;
        else grid[x][y] = 4;
        switch (x){
            case 0:
                switch (y){
                    case 0:
                        if (playerTurn) imageViews.get(0).setImageResource(R.drawable.tictactoex);
                        else imageViews.get(0).setImageResource(R.drawable.tictactoeo);
                        break;
                    case 1:
                        if (playerTurn) imageViews.get(1).setImageResource(R.drawable.tictactoex);
                        else imageViews.get(1).setImageResource(R.drawable.tictactoeo);
                        break;
                    case 2:
                        if (playerTurn) imageViews.get(2).setImageResource(R.drawable.tictactoex);
                        else imageViews.get(2).setImageResource(R.drawable.tictactoeo);
                        break;
                }
                break;
            case 1:
                switch (y){
                    case 0:
                        if (playerTurn) imageViews.get(3).setImageResource(R.drawable.tictactoex);
                        else imageViews.get(3).setImageResource(R.drawable.tictactoeo);
                        break;
                    case 1:
                        if (playerTurn) imageViews.get(4).setImageResource(R.drawable.tictactoex);
                        else imageViews.get(4).setImageResource(R.drawable.tictactoeo);
                        break;
                    case 2:
                        if (playerTurn) imageViews.get(5).setImageResource(R.drawable.tictactoex);
                        else imageViews.get(5).setImageResource(R.drawable.tictactoeo);
                        break;
                }
                break;
            case 2:
                switch (y){
                    case 0:
                        if (playerTurn) imageViews.get(6).setImageResource(R.drawable.tictactoex);
                        else imageViews.get(6).setImageResource(R.drawable.tictactoeo);
                        break;
                    case 1:
                        if (playerTurn) imageViews.get(7).setImageResource(R.drawable.tictactoex);
                        else imageViews.get(7).setImageResource(R.drawable.tictactoeo);
                        break;
                    case 2:
                        if (playerTurn) imageViews.get(8).setImageResource(R.drawable.tictactoex);
                        else imageViews.get(8).setImageResource(R.drawable.tictactoeo);
                        break;
                }
                break;
        }
        if (checkWin())
            gameEnd();
        else {
            if (!playerTurn) tvPlayerTurn.setText(getResources().getString(R.string.tictactoe_turn_player_two));
            else tvPlayerTurn.setText(getResources().getString(R.string.tictactoe_turn_player_one));
            playerTurn = !playerTurn;
            //if draw
            if(grid[0][0] != 0 &&
                    grid[0][1] != 0 &&
                    grid[0][2] != 0 &&
                    grid[1][0] != 0 &&
                    grid[1][1] != 0 &&
                    grid[1][2] != 0 &&
                    grid[2][0] != 0 &&
                    grid[2][1] != 0 &&
                    grid[2][2] != 0) {
                draw();
                startGame();
            }
        }
    }

    private void draw() {
        Toast.makeText(this, getResources().getString(R.string.tictactoe_draw), Toast.LENGTH_SHORT).show();
        startGame();
    }

    private void gameEnd() {

        if(!playerTurn){
            Toast.makeText(this, getResources().getString(R.string.tictactoe_win)+" 1!", Toast.LENGTH_SHORT).show();
            playerOnePoints++;
            String pointsText = getResources().getString(R.string.tictactoe_player_one)+" "+playerOnePoints;
            tvPlayerOnePoints.setText(pointsText);
        }
        else{
            Toast.makeText(this, getResources().getString(R.string.tictactoe_win)+" 2!", Toast.LENGTH_SHORT).show();
            playerTwoPoints++;
            String pointsText = getResources().getString(R.string.tictactoe_player_two)+" "+playerTwoPoints;
            tvPlayerTwoPoints.setText(pointsText);
        }
        startGame();
    }

    private boolean checkWin() {
        //Horizontals
        if(grid[0][0]+grid[0][1]+grid[0][2]== 9 || grid[0][0]+grid[0][1]+grid[0][2]== 12) return true;
        else if (grid[1][0]+grid[1][1]+grid[1][2]== 9 || grid[1][0]+grid[1][1]+grid[1][2]== 12) return true;
        else if (grid[2][0]+grid[2][1]+grid[2][2]== 9 || grid[2][0]+grid[2][1]+grid[2][2]== 12) return true;
        //Verticals
        else if (grid[0][0]+grid[1][0]+grid[2][0]== 9 || grid[0][0]+grid[1][0]+grid[2][0]== 12) return true;
        else if (grid[0][1]+grid[1][1]+grid[2][1]== 9 || grid[0][1]+grid[1][1]+grid[2][1]== 12) return true;
        else if (grid[0][2]+grid[1][2]+grid[2][2]== 9 || grid[0][2]+grid[1][2]+grid[2][2]== 12) return true;
        // Diagonals
        else if (grid[0][0]+grid[1][1]+grid[2][2]== 9 || grid[0][0]+grid[1][1]+grid[2][2]== 12) return true;
        else return grid[2][0] + grid[1][1] + grid[0][2] == 9 || grid[2][0] + grid[1][1] + grid[0][2] == 12;
    }


}