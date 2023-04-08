package com.classicgames.myapplication.data.models;

import static java.lang.Math.abs;

import com.classicgames.myapplication.MyApplication;

import java.util.ArrayList;
import java.util.Random;

public class SnakeModel {

    private static final int DEFAULT_SNAKE_SIZE = 3;
    private final int SURFACE_VIEW_WIDTH, SURFACE_VIEW_HEIGHT, MAP_SIZE;

    private final ArrayList<SnakePiece> snakeBody, obstacles;
    private final SnakePiece point, goldenPoint;
    private int goldenPointCounter;

    private final boolean obstaclesGame;
    private int maxPoints, score;
    private int snakeSpeed, speedLevel;
    private final int bodyRadiusSize;
    private char direction;         // L = Left; R = Right; U = Up; D = Down
    private boolean canChangeDirection;     // Blocks the player from instantly changing directions twice

    public SnakeModel(int mapSize, int bodyRadiusSize, int surfaceViewWidth, int surfaceViewHeight, boolean obstaclesGame) {
        this.snakeBody = new ArrayList<>();
        this.obstacles = new ArrayList<>();
        point = new SnakePiece(-1,-1);
        goldenPoint = new SnakePiece(-1, -1);
        this.obstaclesGame = obstaclesGame;
        this.direction = 'R';
        this.bodyRadiusSize = bodyRadiusSize;
        this.SURFACE_VIEW_WIDTH = surfaceViewWidth;
        this.SURFACE_VIEW_HEIGHT = surfaceViewHeight;
        this.MAP_SIZE = mapSize;
        getMaxPoints();
    }

    private void getMaxPoints() {
        if (MAP_SIZE == 1)
            maxPoints = MyApplication.getInstance().getRecords().getSnakeSmallRecord();
        else if (MAP_SIZE == 2)
            maxPoints = MyApplication.getInstance().getRecords().getSnakeMediumRecord();
        else maxPoints = MyApplication.getInstance().getRecords().getSnakeBigRecord();
    }

    private void setNewRecord(int record){
        if (MAP_SIZE == 1)
            MyApplication.getInstance().getRecords().setSnakeSmallRecord(record);
        else if (MAP_SIZE == 2)
            MyApplication.getInstance().getRecords().setSnakeMediumRecord(record);
        else MyApplication.getInstance().getRecords().setSnakeBigRecord(record);
        maxPoints = record;
    }

    public void start() {
        score = 0;
        canChangeDirection = true;
        snakeSpeed = 600;
        speedLevel = 1;
        direction = 'R';
        snakeBody.clear();
        int startPosX = bodyRadiusSize * DEFAULT_SNAKE_SIZE;
        for (int i = 0; i < DEFAULT_SNAKE_SIZE; i++) {
            SnakePiece snakePiece = new SnakePiece(startPosX, bodyRadiusSize);
            snakeBody.add(snakePiece);
            startPosX -= bodyRadiusSize * 2;
        }
        generateNewPoint();
        obstacles.clear();
        if (obstaclesGame) createObstacles();
        disableGoldenPoint();
    }

    private void createObstacles() {
        int obstaclesNr;
        if (MAP_SIZE == 1) obstaclesNr = 15;
        else if (MAP_SIZE == 2) obstaclesNr = 30;
        else obstaclesNr = 50;
        for (int i = 0; i < obstaclesNr; i++){
            SnakePiece newObstacle = generateRandomSnakePiece();
            obstacles.add(newObstacle);
        }
    }

    private SnakePiece generateRandomSnakePiece() {
        int surfaceWidthSize = SURFACE_VIEW_WIDTH - (bodyRadiusSize * 2);       // Without last Position because random
        int surfaceHeightSize = SURFACE_VIEW_HEIGHT - (bodyRadiusSize * 2);     // can't be outside of surfaceView
        int randomX = new Random().nextInt(surfaceWidthSize / bodyRadiusSize);
        int randomY = new Random().nextInt(surfaceHeightSize / bodyRadiusSize);
        if ((randomX % 2) != 0) randomX++;
        if ((randomY % 2) != 0) randomY++;

        SnakePiece newPoint = new SnakePiece(0, 0);
        newPoint.setPositionX(randomX * bodyRadiusSize + bodyRadiusSize);
        newPoint.setPositionY(randomY * bodyRadiusSize + bodyRadiusSize);

        //Check collisions with Snake's body && Obstacles && Point
        boolean newPointCollision;

        // Check body
        newPointCollision = bodyCollision(newPoint);
        // Check obstacles
        if (!newPointCollision && obstaclesGame) newPointCollision = obstacleCollision(newPoint);
        // Check point
        if (!newPointCollision && point != null) newPointCollision = pointCollision(newPoint);
        // Check goldenPoint
        if (!newPointCollision && isGoldenPointActive())
            newPointCollision = goldenPointCollision(newPoint);

        if (newPointCollision) newPoint = generateRandomSnakePiece();

        return newPoint;
    }

    public void generateNewPoint(){
        SnakePiece newPoint = generateRandomSnakePiece();
        point.setPositionY(newPoint.getPositionY());
        point.setPositionX(newPoint.getPositionX());
    }

    public void generateNewGoldenPoint(){
        SnakePiece newGoldenPoint = generateRandomSnakePiece();
        goldenPoint.setPositionY(newGoldenPoint.getPositionY());
        goldenPoint.setPositionX(newGoldenPoint.getPositionX());
    }

    public boolean bodyCollision(SnakePiece piece) {
        if (!(snakeBody != null && snakeBody.size() > 0)) return false;
        for (SnakePiece snakeBodyPiece : snakeBody) {
            if (snakeBodyPiece.getPositionX() == piece.getPositionX() && snakeBodyPiece.getPositionY() == piece.getPositionY())
                return true;
        }
        return false;
    }

    public boolean obstacleCollision(SnakePiece piece) {
        if (!(obstacles != null && obstacles.size() > 0)) return false;
        for (SnakePiece obstacle : obstacles) {
            if (obstacle.getPositionX() == piece.getPositionX() && obstacle.getPositionY() == piece.getPositionY())
                return true;
        }
        return false;
    }

    public boolean pointCollision(SnakePiece piece) {
        return point.getPositionX() == piece.getPositionX() && point.getPositionY() == piece.getPositionY();
    }

    public boolean goldenPointCollision(SnakePiece piece) {
        return goldenPoint.getPositionX() == piece.getPositionX() && goldenPoint.getPositionY() == piece.getPositionY();
    }

    public void moveSnake() {
        int headPosX = snakeBody.get(0).getPositionX();
        int headPosY = snakeBody.get(0).getPositionY();

        switch (direction) {
            case 'R':
                snakeBody.get(0).setPositionX(headPosX + (bodyRadiusSize * 2));
                break;
            case 'L':
                snakeBody.get(0).setPositionX(headPosX - (bodyRadiusSize * 2));
                break;
            case 'U':
                snakeBody.get(0).setPositionY(headPosY - (bodyRadiusSize * 2));
                break;
            case 'D':
                snakeBody.get(0).setPositionY(headPosY + (bodyRadiusSize * 2));
                break;
        }

        SnakePiece temporaryPoint = new SnakePiece(snakeBody.get(0).getPositionX(), snakeBody.get(0).getPositionY());

        for (int i = 1; i < snakeBody.size(); i++) {
            temporaryPoint.setPositionX(snakeBody.get(i).getPositionX());
            temporaryPoint.setPositionY(snakeBody.get(i).getPositionY());
            snakeBody.get(i).setPositionX(headPosX);
            snakeBody.get(i).setPositionY(headPosY);
            headPosX = temporaryPoint.getPositionX();
            headPosY = temporaryPoint.getPositionY();
        }

        canChangeDirection = true;
    }

    public void growSnake(){
        SnakePiece snakePiece = new SnakePiece(0,0);
        snakeBody.add(snakePiece);
        score++;
    }

    public void trySpawnGoldenPoint(){
        int random = new Random().nextInt(100);
        if (random > 98) {
            generateNewGoldenPoint();
            goldenPointCounter = 25;
        }
    }

    private void disableGoldenPoint() {
        goldenPointCounter = 25;
        goldenPoint.setPositionX(-1);
        goldenPoint.setPositionY(-1);
    }

    public void goldenPointCaught(){
        score += 5;
        disableGoldenPoint();
    }

    public void loseGoldenPointPlay(){
        goldenPointCounter--;
        if (goldenPointCounter <= 0) disableGoldenPoint();
    }

    public boolean isGoldenPointActive() {
        if (goldenPoint == null) return false;
        return !(goldenPoint.getPositionX() == -1 || goldenPoint.getPositionY() == -1);
    }

    public void changeDirection(float x1, float x2, float y1, float y2) {
        if (!canChangeDirection) return;

        // Checks if movement was vertical or horizontal
        float horizontal = abs(x1 - x2);
        float vertical = abs(y1 - y2);
        if (horizontal > vertical && direction != 'R' && direction != 'L') {   // horizontal
            if (x1 < x2) direction = 'R';
            else direction = 'L';
            canChangeDirection = false;
        } else if (horizontal < vertical && direction != 'D' && direction != 'U') {   // vertical
            if (y1 < y2) direction = 'D';
            else direction = 'U';
            canChangeDirection = false;
        }
    }

    public boolean shouldSpeedIncrease(){
        return score >= 5 && score < 10 && speedLevel == 1 ||
                score >= 10 && score < 25 && speedLevel == 2 ||
                score >= 25 && score < 50 && speedLevel == 3 ||
                score >= 50 && score < 100 && speedLevel == 4 ||
                score >= 100 && speedLevel == 5;
    }

    public void increaseSpeed(){
        speedLevel++;
        switch (speedLevel){
            case 2:
                snakeSpeed = 650;
                break;
            case 3:
                snakeSpeed = 700;
                break;
            case 4:
                snakeSpeed = 800;
                break;
            case 5:
                snakeSpeed = 900;
                break;
            case 6:
                snakeSpeed = 950;
                break;
        }
    }

    public boolean isGameOver() {
        boolean gameOver;
        gameOver = obstacleCollision(snakeBody.get(0));
        if (!gameOver) {
            for (int i = 1; i < snakeBody.size(); i++) { // Check body collision
                if (snakeBody.get(0).getPositionX() == snakeBody.get(i).getPositionX() && snakeBody.get(0).getPositionY() == snakeBody.get(i).getPositionY())
                    return true;
            }

            gameOver = snakeBody.get(0).getPositionX() < 0 ||
                    snakeBody.get(0).getPositionY() < 0 ||
                    snakeBody.get(0).getPositionX() >= SURFACE_VIEW_WIDTH ||
                    snakeBody.get(0).getPositionY() >= SURFACE_VIEW_HEIGHT;
        }
        return gameOver;
    }

    public void gameOver() {
        if (maxPoints < score) setNewRecord(score);
    }

    public int getRecord(){
        return this.maxPoints;
    }

    public int getScore(){
        return this.score;
    }

    public int getGoldenPointCounter(){
        return this.goldenPointCounter;
    }

    public int getCurrentSpeedPercentage() {
        int speedPercentage = 0;
        switch (speedLevel){
            case 1:
                speedPercentage = 10;
                break;
            case 2:
                speedPercentage = 30;
                break;
            case 3:
                speedPercentage = 50;
                break;
            case 4:
                speedPercentage = 70;
                break;
            case 5:
                speedPercentage = 85;
                break;
            case 6:
                speedPercentage = 100;
                break;
        }

        return speedPercentage;
    }

    public int getCurrentSpeed() {
        return this.snakeSpeed;
    }

    public SnakePiece getSnakeHead(){
        return this.snakeBody.get(0);
    }

    public SnakePiece getSnakeBodyPiece(int i) {
        return this.snakeBody.get(i);
    }

    public int getSnakeBodySize() {
        return this.snakeBody.size();
    }

    public SnakePiece getPoint() {
        return this.point;
    }

    public SnakePiece getGoldenPoint() {
        return this.goldenPoint;
    }

    public ArrayList<SnakePiece> getObstacles() {
        return this.obstacles;
    }
}