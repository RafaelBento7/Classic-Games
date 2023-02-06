package com.classicgames.myapplication;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.classicgames.myapplication.ui.views.activity.MastermindGame;
import com.classicgames.myapplication.ui.views.activity.TicTacToeActivity;
import com.classicgames.myapplication.ui.views.activity.TrueColorsActivity;
import com.classicgames.myapplication.utils.CustomDialog;
import com.classicgames.myapplication.utils.SnakeMapSizeDialog;

public class MainActivity extends AppCompatActivity implements CustomDialog.DialogButtonClick {

    ImageButton bSnakeGame,bTrueColorsGame,bMastermindGame,bTictactoeGame;
    Button bSnakeHelp, bTrueColorsHelp,bMastermindHelp,bTictacoeHelp;
    private int snakeMapSize;
    private boolean snakeObstacles;
    private CustomDialog obstaclesDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initializeObjects();
        bSnakeGame.setOnClickListener(view-> snakeGame());
        bSnakeHelp.setOnClickListener(view -> snakeHelp());
        bTrueColorsGame.setOnClickListener(view -> trueColorsGame());
        bTrueColorsHelp.setOnClickListener(view -> trueColorsHelp());
        bMastermindGame.setOnClickListener(view -> mastermindGame());
        bMastermindHelp.setOnClickListener(view -> mastermindHelp());
        bTictactoeGame.setOnClickListener(view -> tictactoeGame());
    }

    private void initializeObjects(){
        bSnakeGame = findViewById(R.id.snakeGame);
        bSnakeHelp = findViewById(R.id.snakeHelp);
        bTrueColorsGame = findViewById(R.id.trueColorGame);
        bTrueColorsHelp = findViewById(R.id.trueColorHelp);
        bMastermindGame = findViewById(R.id.mastermindGame);
        bMastermindHelp = findViewById(R.id.mastermindHelp);
        bTictactoeGame = findViewById(R.id.tictactoeGame);
        bTictacoeHelp = findViewById(R.id.tictactoeHelp);
    }

    private void snakeGame(){
        snakeMapSize = 0;
        SnakeMapSizeDialog snakeMapSizeDialog = new SnakeMapSizeDialog(this);
        snakeMapSizeDialog.show();
        snakeMapSizeDialog.setOnDismissListener(dialog -> {
            snakeMapSize = snakeMapSizeDialog.getMapLevel();
            if (snakeMapSize != 0){
                obstaclesDialog = new CustomDialog(this, getString(R.string.snake_obstacles), this);
                obstaclesDialog.show();
            }
        });
    }

    private void snakeHelp(){
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle(getResources().getString(R.string.snake_game));
        builder.setMessage(
                getResources().getString(R.string.snake_rules)+"\n\n"+
                getResources().getString(R.string.speed)+"\n"+
                getResources().getString(R.string.snake_speed_level1)+"\n"+
                getResources().getString(R.string.snake_speed_level2)+"\n"+
                getResources().getString(R.string.snake_speed_level3)+"\n"+
                getResources().getString(R.string.snake_speed_level4)+"\n"+
                getResources().getString(R.string.snake_speed_level5)+"\n"+
                getResources().getString(R.string.snake_speed_level6)+"\n");
        builder.setPositiveButton(getResources().getString(R.string.lets_try), (dialog, which) -> {});
        builder.show();
    }

    private void trueColorsGame(){
        Intent intent = new Intent(MainActivity.this, TrueColorsActivity.class);
        startActivity(intent);
    }

    private void trueColorsHelp() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle(getResources().getString(R.string.true_colors_game));
        builder.setMessage(getResources().getString(R.string.true_colors_rules));
        builder.setPositiveButton(getResources().getString(R.string.lets_try), (dialog, which) -> {});
        builder.show();
    }

    private void mastermindHelp() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle(getResources().getString(R.string.mastermind_game));
        builder.setMessage(
                getResources().getString(R.string.mastermind_help)+"\n"+
                getResources().getString(R.string.mastermind_help2)+"\n"+
                getResources().getString(R.string.mastermind_help3)+"\n"+
                getResources().getString(R.string.mastermind_help4)+"\n"+
                getResources().getString(R.string.mastermind_help5)+"\n"+
                getResources().getString(R.string.mastermind_help6)+"\n");
        builder.setPositiveButton(getResources().getString(R.string.lets_try), (dialog, which) -> {});
        builder.show();
    }

    private void mastermindGame() {
        Intent intent = new Intent(MainActivity.this, MastermindGame.class);
        startActivity(intent);
    }

    private void tictactoeGame(){
        Intent intent = new Intent(MainActivity.this, TicTacToeActivity.class);
        startActivity(intent);
    }

    @Override
    public void onPositiveButtonClicked() {
        snakeObstacles = true;
        obstaclesDialog.dismiss();
        System.out.println("Com obstaculos");
    }

    @Override
    public void onNegativeButtonClicked() {
        snakeObstacles = false;
        obstaclesDialog.dismiss();
        System.out.println("Sem obstaculos");
    }
}