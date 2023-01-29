package com.classicgames.myapplication.ui.views.activity;

import static java.lang.Math.abs;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.classicgames.myapplication.MainActivity;
import com.classicgames.myapplication.R;
import com.classicgames.myapplication.data.models.SnakePart;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class SnakeGame extends AppCompatActivity implements SurfaceHolder.Callback {
    private SurfaceView surfaceView;
    private TextView textViewPoints,textViewMaxPoints,textViewSpeed,textViewSnakeGoldenPoint;
    private SurfaceHolder surfaceHolder;
    private float clickX1=0,clickY1=0;
    private SharedPreferences load,save;
    private Timer timer;

    private final List<SnakePart> obstacles = new ArrayList<>();
    private String direction = "right";
    private int bodySize;
    private int snakeSpeed, goldenPointCounter, score=0,maxPoints,speedLevel=1;
    private final List<SnakePart> snakeBody = new ArrayList<>();
    private Canvas canvas = null;
    private Paint bodyColor=null,headColor=null,pointColor = null,goldenPointColor = null,obstacleColor=null;
    private int pointPosX,pointPosY,goldenPointPosX=-10,goldenPointPosY=-10;
    private boolean canMove=true,canPlay=true,goldenPointSpawn=true;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_snake_game);
        getComponents();
        bodySizeChooser();
        createPaints();
        loadRecord();
        surfaceView.getHolder().addCallback(this);
    }

    private void getComponents(){
        textViewPoints = findViewById(R.id.snakePoints);
        textViewMaxPoints = findViewById(R.id.snakeMaxPoints);
        textViewSpeed = findViewById(R.id.snakeCurrentSpeed);
        surfaceView = findViewById(R.id.snakeScreen);
        textViewSnakeGoldenPoint = findViewById(R.id.snakeGoldenPointTimer);
        save = getSharedPreferences("SnakeGame", Context.MODE_PRIVATE);
        load = getApplicationContext().getSharedPreferences("SnakeGame",Context.MODE_PRIVATE);
    }

    private void bodySizeChooser(){
        bodySize = MainActivity.getSnakeMapSize();
        if (bodySize==1) bodySize = 28;
        else if (bodySize==2) bodySize = 22;
        else bodySize = 16;
    }

    @Override
    public void surfaceCreated(@NonNull SurfaceHolder holder) {
        this.surfaceHolder = holder;
        if (!canPlay) return;
        createObstacles();
        start();
    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder holder) {

    }

    public boolean onTouchEvent(MotionEvent touchEvent){
        switch (touchEvent.getAction()){
            case MotionEvent.ACTION_DOWN:
                clickX1 = touchEvent.getX();
                clickY1 = touchEvent.getY();
                break;
            case MotionEvent.ACTION_UP:
                float clickX2 = touchEvent.getX();
                float clickY2 = touchEvent.getY();

                // Checks if movement was vertical or horizontal
                float x3 = abs(clickX1- clickX2);
                float y3 = abs(clickY1- clickY2);
                if (!canMove) return false;
                if (x3 > y3 && !direction.equals("right")&&!direction.equals("left")){   // horizontal
                    if (clickX1 < clickX2) direction = "right";
                    else direction = "left";
                    canMove = false;
                } else if(x3<y3 && !direction.equals("down")&&!direction.equals("up")){   // vertical
                    if (clickY1 < clickY2) direction = "down";
                    else direction = "up";
                    canMove=false;
                }
                break;
        }
        return false;
    }

    private void start(){
        score = 0;
        canMove = true;
        snakeSpeed=600;
        speedLevel=1;
        String points = getResources().getString(R.string.points) + " " + score;
        String currentSpeed = getResources().getString(R.string.snake_speed) + " 10%";
        textViewSnakeGoldenPoint.setText(getResources().getString(R.string.no_golden_point));
        textViewPoints.setText(points);
        textViewSpeed.setText(currentSpeed);
        direction = "right";
        snakeBody.clear();
        int defaultTaleSize = 3;
        int startPositionX = (bodySize)* defaultTaleSize;
        for (int i = 0; i< defaultTaleSize; i++){
            SnakePart snakePart = new SnakePart(startPositionX,bodySize);
            snakeBody.add(snakePart);
            startPositionX = startPositionX - (bodySize * 2);
        }
        generatePoint();
        goldenPointRemove();
        moveSnake();
    }

    private void loadRecord(){
        maxPoints = load.getInt("record",0);
        String Points = getResources().getString(R.string.max_points)+" "+maxPoints;
        textViewMaxPoints.setText(Points);
    }

    private void saveRecord(){
        runOnUiThread(()->{
            Toast.makeText(this, "NEW RECORD: "+score, Toast.LENGTH_SHORT).show();
            SharedPreferences.Editor editor = save.edit();
            editor.putInt("record",score);
            editor.apply();
            loadRecord();
        });
    }

    private void generatePoint() {
        int surfaceWidth = surfaceView.getWidth() - (bodySize*2);
        int surfaceHeight = surfaceView.getHeight() - (bodySize*2);
        int randomX = new Random().nextInt(surfaceWidth/bodySize);
        int randomY = new Random().nextInt(surfaceHeight/bodySize);
        if ((randomX%2)!=0){
            randomX++;
        }
        if ((randomY%2)!=0){
            randomY++;
        }
        pointPosX = (randomX*bodySize) + bodySize;
        pointPosY = (randomY*bodySize) + bodySize;

        boolean pointCollision = false;
        for (int i=1;i<snakeBody.size();i++){
            if(snakeBody.get(i).getPositionX() == pointPosX && snakeBody.get(i).getPositionY() == pointPosY){
                pointCollision = true;
                break;
            }
        }
        for(SnakePart obstacle: obstacles){
            if(obstacle.getPositionX() == pointPosX && obstacle.getPositionY()== pointPosY){
                pointCollision = true;
                break;
            }
        }
        if (pointCollision) generatePoint();
    }

    private void moveSnake() {
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                int headPosX = snakeBody.get(0).getPositionX();
                int headPosY = snakeBody.get(0).getPositionY();
                // if scores a point
                if (headPosX == pointPosX && headPosY == pointPosY){
                    growSnake();
                    generatePoint();
                    return;
                }

                if (!goldenPointSpawn) {    // if golden point is spawned
                    if (headPosX == goldenPointPosX && headPosY == goldenPointPosY) goldenPointCaught(); // if scores a golden point
                    else {
                        if (goldenPointCounter > 0){
                            goldenPointCounter--;
                            runOnUiThread(()->{
                                String goldenPoints = getResources().getString(R.string.golden_point_timer) + " " + goldenPointCounter;
                                textViewSnakeGoldenPoint.setText(goldenPoints);
                            });
                        }
                        else goldenPointRemove();
                    }
                }

                switch (direction){
                    case "right":
                        snakeBody.get(0).setPositionX(headPosX+(bodySize*2));
                        break;
                    case "left":
                        snakeBody.get(0).setPositionX(headPosX-(bodySize*2));
                        break;
                    case "up":
                        snakeBody.get(0).setPositionY(headPosY-(bodySize*2));
                        break;
                    case "down":
                        snakeBody.get(0).setPositionY(headPosY+(bodySize*2));
                        break;
                    default:
                        Toast.makeText(SnakeGame.this, getResources().getString(R.string.error), Toast.LENGTH_SHORT).show();
                        break;
                }

                if (checkGameOver(headPosX,headPosY)) gameOver();
                else{
                    if (goldenPointSpawn) goldenPoint();
                    canvas = surfaceHolder.lockCanvas();
                    draw(headPosX,headPosY);
                    surfaceHolder.unlockCanvasAndPost(canvas);
                }
                canMove = true;
            }
        },1000 - snakeSpeed,1000-snakeSpeed);
    }

    private void draw(int headPosX,int headPosY){
        canvas.drawColor(Color.WHITE, PorterDuff.Mode.CLEAR);
        // Point
        canvas.drawCircle(pointPosX,pointPosY,bodySize,pointColor);
        //Golden point
        if(goldenPointPosX != -10 && goldenPointPosY != -10) canvas.drawCircle(goldenPointPosX,goldenPointPosY,bodySize,goldenPointColor);
        //Snake head
        canvas.drawRect(snakeBody.get(0).getPositionX()-bodySize,snakeBody.get(0).getPositionY()-bodySize,snakeBody.get(0).getPositionX()+bodySize,snakeBody.get(0).getPositionY()+bodySize,headColor);
        //Snake Body
        for(int i = 1;i<snakeBody.size();i++){
            int getTempPosX = snakeBody.get(i).getPositionX();
            int getTempPosY = snakeBody.get(i).getPositionY();
            snakeBody.get(i).setPositionX(headPosX);
            snakeBody.get(i).setPositionY(headPosY);
            canvas.drawRect(snakeBody.get(i).getPositionX()-bodySize,snakeBody.get(i).getPositionY()-bodySize,snakeBody.get(i).getPositionX()+bodySize,snakeBody.get(i).getPositionY()+bodySize,bodyColor);
            headPosX = getTempPosX;
            headPosY = getTempPosY;
        }
        // Obstacles
        for (SnakePart obstacle: obstacles) {
            canvas.drawRect(obstacle.getPositionX()-bodySize,obstacle.getPositionY()-bodySize,obstacle.getPositionX()+bodySize,obstacle.getPositionY()+bodySize,obstacleColor);
        }
    }

    private void growSnake(){
        SnakePart snakePart = new SnakePart(0,0);
        snakeBody.add(snakePart);
        score++;
        runOnUiThread(() -> {
            String newPoint = getResources().getString(R.string.points)+" "+score;
            textViewPoints.setText(newPoint);
            if (score >= 5) increaseSpeed();
        });
    }

    private void increaseSpeed(){
        String  currentSpeed;
        if (score >= 5 && score < 10 && speedLevel==1){
            snakeSpeed = 650;
            currentSpeed = getResources().getString(R.string.snake_speed) + " 30%";
            textViewSpeed.setText(currentSpeed);
            speedLevel = 2;
            timer.purge();
            timer.cancel();
            moveSnake();
        } else if (score >= 10 && score < 25 && speedLevel==2) {
            snakeSpeed = 700;
            currentSpeed = getResources().getString(R.string.snake_speed) + " 50%";
            textViewSpeed.setText(currentSpeed);
            speedLevel=3;
            timer.purge();
            timer.cancel();
            moveSnake();
        } else if (score >= 25 && score < 50 && speedLevel == 3) {
            snakeSpeed = 800;
            currentSpeed = getResources().getString(R.string.snake_speed) + " 70%";
            textViewSpeed.setText(currentSpeed);
            speedLevel=4;
            timer.purge();
            timer.cancel();
            moveSnake();
        } else if (score >= 50 && score < 100 && speedLevel == 4) {
            snakeSpeed = 900;
            currentSpeed = getResources().getString(R.string.snake_speed) + " 85%";
            textViewSpeed.setText(currentSpeed);
            speedLevel = 5;
            timer.purge();
            timer.cancel();
            moveSnake();
        } else if (score >= 100 && speedLevel == 5){
            snakeSpeed = 950;
            currentSpeed = getResources().getString(R.string.snake_speed) + " 100%";
            textViewSpeed.setText(currentSpeed);
            speedLevel=6;
            timer.purge();
            timer.cancel();
            moveSnake();
        }
    }

    private boolean checkGameOver(int headPosX, int headPosY){
        //if snake touch the border
        boolean gameEnd = snakeBody.get(0).getPositionX() < 0 || snakeBody.get(0).getPositionY() < 0 ||
                snakeBody.get(0).getPositionX() >= surfaceView.getWidth() || snakeBody.get(0).getPositionY() >= surfaceView.getHeight();

        if (!gameEnd){
            for (SnakePart snakePart: snakeBody){  //if snake touches itself
                if(headPosX == snakePart.getPositionX() && headPosY == snakePart.getPositionY()){
                    gameEnd = true;
                    break;
                }
            }
        }
        if(!gameEnd){
            for(SnakePart obstacle:obstacles){  // if snake touch obstacle
                if(headPosX == obstacle.getPositionX()&&headPosY==obstacle.getPositionY()){
                    gameEnd = true;
                    break;
                }
            }
        }

        return gameEnd;
    }

    private void gameOver(){
        timer.purge();
        timer.cancel();
        canPlay = false;
        if (score > maxPoints) saveRecord();
        AlertDialog.Builder builder = new AlertDialog.Builder(SnakeGame.this);
        builder.setTitle(getResources().getString(R.string.game_over));
        builder.setCancelable(false);
        builder.setMessage(getResources().getString(R.string.game_over_points)+" "+score);
        builder.setNegativeButton(getResources().getString(R.string.back_menu),(dialog, which) -> finish());
        builder.setPositiveButton(getResources().getString(R.string.play_again), (dialog, which) -> {
            obstacles.clear();
            createObstacles();
            canPlay = true;
            start();
        });
        runOnUiThread(builder::show);
    }

    private void createPaints(){
        bodyColor = new Paint();
        bodyColor.setColor(Color.WHITE);
        bodyColor.setStyle(Paint.Style.FILL);
        bodyColor.setAntiAlias(true);

        headColor = new Paint();
        headColor.setColor(Color.rgb(192,192,192));
        headColor.setStyle(Paint.Style.FILL);
        headColor.setAntiAlias(true);

        pointColor = new Paint();
        pointColor.setColor(Color.GREEN);
        pointColor.setStyle(Paint.Style.FILL);
        pointColor.setAntiAlias(true);

        goldenPointColor = new Paint();
        goldenPointColor.setColor(Color.YELLOW);
        goldenPointColor.setStyle(Paint.Style.FILL);
        goldenPointColor.setAntiAlias(true);

        obstacleColor = new Paint();
        obstacleColor.setColor(Color.RED);
        obstacleColor.setStyle(Paint.Style.FILL);
        obstacleColor.setAntiAlias(true);
    }

    private void goldenPoint(){
        int random = new Random().nextInt(100);
        if (random > 98) {
            createGoldenPoint();
            goldenPointCounter = 25;
            runOnUiThread(()->{
                String goldenPoints = getResources().getString(R.string.golden_point_timer) + " " + goldenPointCounter;
                textViewSnakeGoldenPoint.setText(goldenPoints);
            });
            goldenPointSpawn = false;
        }
    }

    private void createGoldenPoint(){
        int surfaceWidth = surfaceView.getWidth() - (bodySize*2);
        int surfaceHeight = surfaceView.getHeight() - (bodySize*2);
        int randomX = new Random().nextInt(surfaceWidth/bodySize);
        int randomY = new Random().nextInt(surfaceHeight/bodySize);
        if ((randomX%2)!=0) randomX++;
        if ((randomY%2)!=0) randomY++;
        goldenPointPosX = (randomX*bodySize) + bodySize;
        goldenPointPosY = (randomY*bodySize) + bodySize;

        boolean pointCollision = false;
        for (SnakePart snakePart: snakeBody){   // Collision with snake
            if(snakePart.getPositionX() == goldenPointPosX && snakePart.getPositionY() == goldenPointPosY){
                pointCollision = true;
                break;
            }
        }
        if(!pointCollision) {
            for (SnakePart obstacle : obstacles) {   // Collision with obstacles
                if (obstacle.getPositionX() == goldenPointPosX && obstacle.getPositionY() == goldenPointPosY) {
                    pointCollision = true;
                    break;
                }
            }
        }
        if (pointCollision) createGoldenPoint();
    }

    private void goldenPointCaught(){
        score+=5;
        runOnUiThread(() -> {
            increaseSpeed();
            String newPoint = getResources().getString(R.string.points)+" "+score;
            textViewPoints.setText(newPoint);
        });
        goldenPointSpawn = true;
        goldenPointRemove();
    }

    private void goldenPointRemove(){
        goldenPointCounter = 25;
        goldenPointSpawn = true;
        goldenPointPosX = -10;
        goldenPointPosY = 10;
        runOnUiThread(() -> textViewSnakeGoldenPoint.setText(getResources().getString(R.string.no_golden_point)));
    }

    @Override
    public void onBackPressed(){
        timer.purge();
        timer.cancel();
        finish();
    }

    private void createObstacles(){
        int obstaclesNr = MainActivity.getSnakeObstacles();
        for (int i=0;i<obstaclesNr;i++){
            generateObstacle();
        }
    }

    private void generateObstacle(){
        int surfaceWidth = surfaceView.getWidth() - (bodySize*2);
        int surfaceHeight = surfaceView.getHeight() - (bodySize*2);
        int randomX = new Random().nextInt(surfaceWidth/bodySize);
        int randomY = new Random().nextInt(surfaceHeight/bodySize);
        if ((randomX%2)!=0) randomX++;
        if ((randomY%2)!=0) randomY++;
        int obstaclePosX = (randomX*bodySize) + bodySize;
        int obstaclePosY = (randomY*bodySize) + bodySize;

        boolean pointCollision = obstaclePosX == bodySize || obstaclePosY == bodySize; // Collision with edge
        if (!pointCollision) {
            for (SnakePart obstacle : obstacles) {  // Collision with other obstacles
                if (obstaclePosX == obstacle.getPositionX() && obstaclePosY == obstacle.getPositionY()) {
                    pointCollision = true;
                    break;
                }
            }
        }
        if (pointCollision) generateObstacle();
        else obstacles.add(new SnakePart(obstaclePosX,obstaclePosY));
    }
    @Override
    public void onStop(){
        super.onStop();
        timer.purge();
        timer.cancel();
    }
}