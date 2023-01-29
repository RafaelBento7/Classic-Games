package com.classicgames.myapplication.ui.views.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.classicgames.myapplication.R;
import com.classicgames.myapplication.data.models.MastermindAttemptModel;

import java.util.ArrayList;
import java.util.Random;

public class MastermindGame extends AppCompatActivity {

    private ArrayList<MastermindAttemptModel> mastermindAttempts;
    private TextView tvAttemptsLeft,tvTimer;
    private int[] solutionColors,colorsPicked;
    private int attempt = 0,colorPicked = 0;
    private Button btPurple, btGreen, btRed, btYellow, btBlue, btBrown, btOrange, btPink,btRestart;
    private int seconds=0,minutes =0;

    Handler timerHandler = new Handler();
    Runnable timerRunnable = new Runnable() {
        @Override
        public void run() {
            seconds ++;
            if (seconds==60){
                minutes++;
                seconds=0;
            }
            tvTimer.setText(String.format("%d:%02d", minutes, seconds));

            timerHandler.postDelayed(this, 1000);
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mastermind_game);
        initializeObjects();
        onClickListeners();
        initGame();
    }

    private void initializeObjects() {
        mastermindAttempts = new ArrayList<>();
        mastermindAttempts.add(new MastermindAttemptModel(findViewById(R.id.first_left_one), findViewById(R.id.first_left_two), findViewById(R.id.first_left_three), findViewById(R.id.first_left_four), findViewById(R.id.first_true_position), findViewById(R.id.first_wrong_position)));
        mastermindAttempts.add(new MastermindAttemptModel(findViewById(R.id.second_left_one), findViewById(R.id.second_left_two), findViewById(R.id.second_left_three), findViewById(R.id.second_left_four), findViewById(R.id.second_true_position), findViewById(R.id.second_wrong_position)));
        mastermindAttempts.add(new MastermindAttemptModel(findViewById(R.id.third_left_one), findViewById(R.id.third_left_two), findViewById(R.id.third_left_three), findViewById(R.id.third_left_four), findViewById(R.id.third_true_position), findViewById(R.id.third_wrong_position)));
        mastermindAttempts.add(new MastermindAttemptModel(findViewById(R.id.forth_left_one), findViewById(R.id.forth_left_two), findViewById(R.id.forth_left_three), findViewById(R.id.forth_left_four), findViewById(R.id.forth_true_position), findViewById(R.id.forth_wrong_position)));
        mastermindAttempts.add(new MastermindAttemptModel(findViewById(R.id.fifth_left_one), findViewById(R.id.fifth_left_two), findViewById(R.id.fifth_left_three), findViewById(R.id.fifth_left_four), findViewById(R.id.fifth_true_position), findViewById(R.id.fifth_wrong_position)));
        mastermindAttempts.add(new MastermindAttemptModel(findViewById(R.id.sixth_left_one), findViewById(R.id.sixth_left_two), findViewById(R.id.sixth_left_three), findViewById(R.id.sixth_left_four), findViewById(R.id.sixth_true_position), findViewById(R.id.sixth_wrong_position)));
        mastermindAttempts.add(new MastermindAttemptModel(findViewById(R.id.seventh_left_one), findViewById(R.id.seventh_left_two), findViewById(R.id.seventh_left_three), findViewById(R.id.seventh_left_four), findViewById(R.id.seventh_true_position), findViewById(R.id.seventh_wrong_position)));
        mastermindAttempts.add(new MastermindAttemptModel(findViewById(R.id.eight_left_one), findViewById(R.id.eight_left_two), findViewById(R.id.eight_left_three), findViewById(R.id.eight_left_four), findViewById(R.id.eight_true_position), findViewById(R.id.eight_wrong_position)));
        mastermindAttempts.add(new MastermindAttemptModel(findViewById(R.id.ninth_left_one), findViewById(R.id.ninth_left_two), findViewById(R.id.ninth_left_three), findViewById(R.id.ninth_left_four), findViewById(R.id.ninth_true_position), findViewById(R.id.ninth_wrong_position)));
        mastermindAttempts.add(new MastermindAttemptModel(findViewById(R.id.tenth_left_one), findViewById(R.id.tenth_left_two), findViewById(R.id.tenth_left_three), findViewById(R.id.tenth_left_four), findViewById(R.id.tenth_true_position), findViewById(R.id.tenth_wrong_position)));
        btPurple = findViewById(R.id.trueColorPurple);
        btGreen = findViewById(R.id.trueColorGreen);
        btRed = findViewById(R.id.trueColorRed);
        btYellow = findViewById(R.id.trueColorYellow);
        btBlue = findViewById(R.id.trueColorBlue);
        btBrown = findViewById(R.id.trueColorBrown);
        btOrange = findViewById(R.id.trueColorOrange);
        btPink = findViewById(R.id.trueColorPink);
        btRestart = findViewById(R.id.mastermindRestart);
        tvAttemptsLeft = findViewById(R.id.mastermindAttempts);
        tvTimer = findViewById(R.id.mastermindTimer);
        colorsPicked = new int[4];
    }

    private void onClickListeners(){
        btPurple.setOnClickListener(view -> {
            int color = giveColor(0);
            checkPlay(color);
        });
        btGreen.setOnClickListener(view -> {
            int color = giveColor(1);
            checkPlay(color);
        });
        btRed.setOnClickListener(view -> {
            int color = giveColor(2);
            checkPlay(color);
        });
        btYellow.setOnClickListener(view -> {
            int color = giveColor(3);
            checkPlay(color);
        });
        btBlue.setOnClickListener(view -> {
            int color = giveColor(4);
            checkPlay(color);
        });
        btBrown.setOnClickListener(view -> {
            int color = giveColor(5);
            checkPlay(color);
        });
        btOrange.setOnClickListener(view -> {
            int color = giveColor(6);
            checkPlay(color);
        });
        btPink.setOnClickListener(view -> {
            int color = giveColor(7);
            checkPlay(color);
        });
        btRestart.setOnClickListener(view-> {
            timerHandler.removeCallbacks(timerRunnable);
            initGame();
        });
    }

    private void initGame(){
        for (int i=1;i<mastermindAttempts.size();i++){
            mastermindAttempts.get(i).getFirst().setVisibility(View.GONE);
            mastermindAttempts.get(i).getSecond().setVisibility(View.GONE);
            mastermindAttempts.get(i).getThird().setVisibility(View.GONE);
            mastermindAttempts.get(i).getForth().setVisibility(View.GONE);
            mastermindAttempts.get(i).getTruePosition().setVisibility(View.GONE);
            mastermindAttempts.get(i).getWrongPosition().setVisibility(View.GONE);
        }
        mastermindAttempts.get(0).getFirst().setBackgroundTintList(ColorStateList.valueOf(Color.BLACK));
        mastermindAttempts.get(0).getSecond().setBackgroundTintList(ColorStateList.valueOf(Color.BLACK));
        mastermindAttempts.get(0).getThird().setBackgroundTintList(ColorStateList.valueOf(Color.BLACK));
        mastermindAttempts.get(0).getForth().setBackgroundTintList(ColorStateList.valueOf(Color.BLACK));
        mastermindAttempts.get(0).getTruePosition().setText("0");
        mastermindAttempts.get(0).getWrongPosition().setText("0");
        attempt = 0;
        colorPicked = 0;
        String attempts = getResources().getString(R.string.attempts_left)+" "+10;
        tvAttemptsLeft.setText(attempts);
        createSolution();
        seconds=0;
        minutes=0;
        timerHandler.postDelayed(timerRunnable, 0);
    }

    private void createSolution() {
        solutionColors = new int[4];
        for (int i=0;i<4;i++){
            Random randomColor = new Random();
            solutionColors[i] = giveColor(randomColor.nextInt(8));
            //Check if the color already exists
            for (int j=0; j<i;j++){
                boolean conflict = true;
                do {
                    if (solutionColors[j] == solutionColors[i]) solutionColors[i] = giveColor(randomColor.nextInt(8));
                    else conflict = false;
                } while (conflict);

            }
        }
    }

    private void showSolution(){
        TextView solutionOne = findViewById(R.id.solution_one);
        TextView solutionTwo = findViewById(R.id.solution_two);
        TextView solutionThree = findViewById(R.id.solution_three);
        TextView solutionFour = findViewById(R.id.solution_four);
        solutionOne.setBackgroundTintList(ColorStateList.valueOf(solutionColors[0]));
        solutionOne.setText("");
        solutionTwo.setBackgroundTintList(ColorStateList.valueOf(solutionColors[1]));
        solutionTwo.setText("");
        solutionThree.setBackgroundTintList(ColorStateList.valueOf(solutionColors[2]));
        solutionThree.setText("");
        solutionFour.setBackgroundTintList(ColorStateList.valueOf(solutionColors[3]));
        solutionFour.setText("");
    }

    private int giveColor(int color) {
        switch (color){
            case 0:
                color = getResources().getColor(R.color.purple);
                break;
            case 1:
                color = getResources().getColor(R.color.green);
                break;
            case 2:
                color = getResources().getColor(R.color.red);
                break;
            case 3:
                color = getResources().getColor(R.color.yellow);
                break;
            case 4:
                color = getResources().getColor(R.color.blue);
                break;
            case 5:
                color = getResources().getColor(R.color.brown);
                break;
            case 6:
                color = getResources().getColor(R.color.orange);
                break;
            case 7:
                color = getResources().getColor(R.color.pink);
                break;
        }
        return color;
    }

    private void checkPlay(int color){
        switch (colorPicked){
            case 0:
                mastermindAttempts.get(attempt).getFirst().setVisibility(View.VISIBLE);
                mastermindAttempts.get(attempt).getFirst().setBackgroundTintList(ColorStateList.valueOf(color));
                colorsPicked[0] = color;
                break;
            case 1:
                mastermindAttempts.get(attempt).getSecond().setVisibility(View.VISIBLE);
                mastermindAttempts.get(attempt).getSecond().setBackgroundTintList(ColorStateList.valueOf(color));
                colorsPicked[1] = color;
                break;
            case 2:
                mastermindAttempts.get(attempt).getThird().setVisibility(View.VISIBLE);
                mastermindAttempts.get(attempt).getThird().setBackgroundTintList(ColorStateList.valueOf(color));
                colorsPicked[2] = color;
                break;
            case 3:
                mastermindAttempts.get(attempt).getForth().setVisibility(View.VISIBLE);
                mastermindAttempts.get(attempt).getForth().setBackgroundTintList(ColorStateList.valueOf(color));
                colorsPicked[3] = color;
                break;
        }
        colorPicked++;
        if (colorPicked==4) checkSolution();
    }

    private void checkSolution() {
        int truePosition =0, wrongPosition =0;
        for(int i=0;i<4;i++){
            for(int j=0;j<4;j++){
                if (solutionColors[i] == colorsPicked[j] && i==j){
                    truePosition++;
                    break;
                }
                else if(solutionColors[i] == colorsPicked[j]) wrongPosition++;
            }
        }
        mastermindAttempts.get(attempt).getTruePosition().setVisibility(View.VISIBLE);
        mastermindAttempts.get(attempt).getTruePosition().setText(String.valueOf(truePosition));
        mastermindAttempts.get(attempt).getWrongPosition().setVisibility(View.VISIBLE);
        mastermindAttempts.get(attempt).getWrongPosition().setText(String.valueOf(wrongPosition));
        if (truePosition==4){
            win();
            return;
        }
        String attempts = getResources().getString(R.string.attempts_left)+" "+(9-attempt);
        tvAttemptsLeft.setText(attempts);
        if (attempt == 9){
            gameOver();
            return;
        }
        colorPicked=0;
        attempt++;
    }

    private void gameOver() {
        Toast.makeText(this, getResources().getString(R.string.game_over), Toast.LENGTH_SHORT).show();
        showSolution();
        seconds=0;
        minutes=0;
        timerHandler.removeCallbacks(timerRunnable);
    }

    private void win() {
        Toast.makeText(this, "WIN", Toast.LENGTH_SHORT).show();
        showSolution();
        timerHandler.removeCallbacks(timerRunnable);
        saveRecord();
    }

    private void saveRecord(){
        SharedPreferences load = getApplicationContext().getSharedPreferences("mastermind",Context.MODE_PRIVATE);
        int score = 10-attempt;
        int maxPoints = load.getInt("record",0);
        if (maxPoints> score) return;
        SharedPreferences save = getSharedPreferences("mastermind", Context.MODE_PRIVATE);
        Toast.makeText(this, "NEW RECORD: "+score+" ATTEMPTS LEFT", Toast.LENGTH_SHORT).show();
        SharedPreferences.Editor editor = save.edit();
        String timer = tvTimer.getText().toString();
        editor.putString("timer",timer);
        editor.putInt("record",score);
        editor.apply();
    }

}