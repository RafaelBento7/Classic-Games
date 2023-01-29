package com.classicgames.myapplication;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.classicgames.myapplication.ui.views.activity.MastermindGame;
import com.classicgames.myapplication.ui.views.activity.SnakeGame;
import com.classicgames.myapplication.ui.views.activity.TicTacToe;
import com.classicgames.myapplication.ui.views.activity.TrueColorGame;

public class MainActivity extends AppCompatActivity {

    ImageButton bSnakeGame,bTrueColorsGame,bMastermindGame,bTictactoeGame;
    Button bSnakeHelp, bTrueColorsHelp,bMastermindHelp,bTictacoeHelp;
    private static int snakeObstacles=0,snakeMapSize=1;

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
        EditText mapSizeET = new EditText(this);
        mapSizeET.setHint("Map size");
        mapSizeET.setInputType(InputType.TYPE_CLASS_NUMBER);
        AlertDialog.Builder builderMapLevel = new AlertDialog.Builder(MainActivity.this);
        builderMapLevel.setView(mapSizeET);
        builderMapLevel.setTitle(getResources().getString(R.string.map_size));
        builderMapLevel.setCancelable(false);
        builderMapLevel.setMessage(getResources().getString(R.string.snake_map_size_number));
        builderMapLevel.setPositiveButton(getResources().getString(R.string.next), (dialog, which) -> {
            String mapSizeValue = mapSizeET.getText().toString();
            if (mapSizeValue.equals("")) snakeMapSize = 1;
            else snakeMapSize = Integer.parseInt(mapSizeValue);
            if (snakeMapSize > 0 && snakeMapSize <= 3){
                EditText obstaclesET = new EditText(this);
                obstaclesET.setHint("Obstacles");
                obstaclesET.setInputType(InputType.TYPE_CLASS_NUMBER);
                AlertDialog.Builder builderObstacles = new AlertDialog.Builder(MainActivity.this);
                builderObstacles.setView(obstaclesET);
                builderObstacles.setTitle(getResources().getString(R.string.snake_obstacles));
                builderObstacles.setCancelable(false);
                if(snakeMapSize==1) builderObstacles.setMessage(getResources().getString(R.string.snake_obstacles_number)+" (0-20)");
                else if (snakeMapSize==2) builderObstacles.setMessage(getResources().getString(R.string.snake_obstacles_number)+" (0-40)");
                else builderObstacles.setMessage(getResources().getString(R.string.snake_obstacles_number)+" (0-75)");
                builderObstacles.setPositiveButton(getResources().getString(R.string.snake_create_obstacles), (Dialog, Which) -> {
                    String obstaclesValue = obstaclesET.getText().toString();
                    if (obstaclesValue.equals("")) snakeObstacles = 0;
                    else snakeObstacles = Integer.parseInt(obstaclesValue);
                    if (snakeObstacles >= 0 && snakeObstacles <= 20 && snakeMapSize == 1
                            || snakeObstacles >= 0 && snakeObstacles <= 50 && snakeMapSize == 2
                            || snakeObstacles >= 0 && snakeObstacles <= 75 && snakeMapSize == 3){
                        Intent intent = new Intent(MainActivity.this, SnakeGame.class);
                        startActivity(intent);
                    } else Toast.makeText(this, "Too many obstacles", Toast.LENGTH_SHORT).show();

                });
                builderObstacles.setNegativeButton(getResources().getString(R.string.cancel),(Dialog, Which) -> Toast.makeText(this, getResources().getString(R.string.choose_game), Toast.LENGTH_SHORT).show());

                builderObstacles.show();
            } else Toast.makeText(this, "Please choose 1, 2 or 3", Toast.LENGTH_SHORT).show();

        });
        builderMapLevel.setNegativeButton(getResources().getString(R.string.cancel),(dialog, which) -> Toast.makeText(this, getResources().getString(R.string.choose_game), Toast.LENGTH_SHORT).show());

        builderMapLevel.show();
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
        Intent intent = new Intent(MainActivity.this, TrueColorGame.class);
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
        Intent intent = new Intent(MainActivity.this, TicTacToe.class);
        startActivity(intent);
    }

    public static int getSnakeObstacles(){
        return snakeObstacles;
    }
    public static int getSnakeMapSize(){
        return snakeMapSize;
    }
}