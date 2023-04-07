package com.classicgames.myapplication.ui.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.classicgames.myapplication.data.models.SnakeModel;
import com.classicgames.myapplication.data.models.SnakePiece;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class SnakeViewModel extends ViewModel {

    private final SnakeModel game;

    private final MutableLiveData<Integer> score, record, currentSpeed, currentSpeedPercentage, goldenPointCounter;

    public SnakeViewModel(int mapSize, int bodyRadiusSize, int surfaceViewWidth, int surfaceViewHeight, boolean obstaclesGame){
        game = new SnakeModel(mapSize, bodyRadiusSize, surfaceViewWidth, surfaceViewHeight, obstaclesGame);

        score = new MutableLiveData<>();
        record = new MutableLiveData<>();
        currentSpeed = new MutableLiveData<>();
        currentSpeedPercentage = new MutableLiveData<>();
        goldenPointCounter = new MutableLiveData<>();

        record.postValue(game.getRecord());
        score.postValue(0);
    }

    public void startGame(){
        game.start();
        score.postValue(0);
        currentSpeed.postValue(game.getCurrentSpeed());
        currentSpeedPercentage.postValue(game.getCurrentSpeedPercentage());
        goldenPointCounter.postValue(game.getGoldenPointCounter());
    }

    public void moveSnake(){
        game.moveSnake();
    }

    public void changeDirection(float x1,float x2,float y1,float y2){
        game.changeDirection(x1, x2, y1, y2);
    }

    public void checkPoint(){
        if(game.pointCollision(game.getSnakeHead())){
            game.growSnake();
            score.postValue(game.getScore());
            game.generateNewPoint();
            if (game.shouldSpeedIncrease()){
                game.increaseSpeed();
                currentSpeed.postValue(game.getCurrentSpeed());
                currentSpeedPercentage.postValue(game.getCurrentSpeedPercentage());
            }
        }
    }

    public boolean isGoldenPointActive(){
        return game.isGoldenPointActive();
    }

    public void checkGoldenPoint(){
        if (game.goldenPointCollision(game.getSnakeHead())){
            game.goldenPointCaught();
            score.postValue(game.getScore());
            if (game.shouldSpeedIncrease()){
                game.increaseSpeed();
                currentSpeed.postValue(game.getCurrentSpeed());
                currentSpeedPercentage.postValue(game.getCurrentSpeedPercentage());
            }
        } else {
            game.loseGoldenPointPlay();
            goldenPointCounter.postValue(game.getGoldenPointCounter());
        }
    }

    public boolean isGameOver(){
        return game.isGameOver();
    }

    public void gameOver(){
        game.gameOver();
        record.postValue(game.getRecord());
    }

    public void trySpawnGoldenPoint(){
        game.trySpawnGoldenPoint();
        goldenPointCounter.postValue(game.getGoldenPointCounter());
    }

    public SnakePiece getSnakeBodyPiece(int i){
        return game.getSnakeBodyPiece(i);
    }

    public SnakePiece getSnakeHead(){
        return game.getSnakeHead();
    }

    public int getSnakeBodySize(){
        return game.getSnakeBodySize();
    }

    public SnakePiece getPoint(){
        return game.getPoint();
    }

    public SnakePiece getGoldenPoint(){
        return game.getGoldenPoint();
    }

    public ArrayList<SnakePiece> getObstacles(){
        return game.getObstacles();
    }

    public LiveData<Integer> getScore(){
        return this.score;
    }

    public LiveData<Integer> getRecord(){
        return this.record;
    }

    public LiveData<Integer> getCurrentSpeed(){
        return this.currentSpeed;
    }

    public LiveData<Integer> getCurrentSpeedPercentage(){
        return this.currentSpeedPercentage;
    }

    public LiveData<Integer> getGoldenPointCounter(){
        return this.goldenPointCounter;
    }
}
