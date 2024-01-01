package com.classicgames.myapplication.ui.views.activity;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.classicgames.myapplication.R;
import com.classicgames.myapplication.data.models.SnakePiece;
import com.classicgames.myapplication.databinding.ActivitySnakeBinding;
import com.classicgames.myapplication.ui.viewmodels.SnakeViewModel;
import com.classicgames.myapplication.utils.BorderView;
import com.classicgames.myapplication.ui.dialog.CustomDialog;

import java.util.Timer;
import java.util.TimerTask;

public class SnakeActivity extends AppCompatActivity implements SurfaceHolder.Callback {

    public static final String MAP_SIZE = "snake_map_size";
    public static final String OBSTACLES_GAME = "snake_obstacles_game";

    private SnakeViewModel viewModel;
    private ActivitySnakeBinding binding;

    private SurfaceView surfaceView;
    private SurfaceHolder surfaceHolder;
    private int mapSize, bodyRadiusSize, colsPerRow;
    private boolean obstaclesGame;
    private float clickX1 = 0,clickY1 = 0;
    private Timer timer;
    private Canvas canvas = null;
    private Paint bodyColor = null, headColor = null, pointColor = null, goldenPointColor = null, obstacleColor = null;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySnakeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getExtras();
        createPaints();

        ViewTreeObserver viewTreeObserver = binding.SnakeFrameLayoutGame.getViewTreeObserver();
        viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                bodyRadiusSize = binding.SnakeFrameLayoutGame.getWidth() / (colsPerRow * 2);  // multiply per 2, BodySize
                int widthSize = binding.SnakeFrameLayoutGame.getWidth() - (binding.SnakeFrameLayoutGame.getWidth() % (bodyRadiusSize * 2));
                int heightSize = binding.SnakeFrameLayoutGame.getHeight() - (binding.SnakeFrameLayoutGame.getHeight() % (bodyRadiusSize * 2));

                surfaceView = new SurfaceView(SnakeActivity.this);
                FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(widthSize, heightSize);
                layoutParams.gravity = Gravity.CENTER;
                surfaceView.setLayoutParams(layoutParams);
                binding.SnakeFrameLayoutGame.addView(surfaceView);

                BorderView borderView = new BorderView(SnakeActivity.this);
                FrameLayout.LayoutParams borderLayoutParams = new FrameLayout.LayoutParams(widthSize, heightSize);
                borderLayoutParams.gravity = Gravity.CENTER;
                borderView.setLayoutParams(borderLayoutParams);
                binding.SnakeFrameLayoutGame.addView(borderView);

                surfaceView.getHolder().addCallback(SnakeActivity.this);

                binding.SnakeFrameLayoutGame.getViewTreeObserver().removeOnGlobalLayoutListener(this);

                initializeViewModel(widthSize, heightSize);
            }
        });
    }

    private void initializeViewModel(int surfaceWidthSize, int SurfaceHeightSize) {
        viewModel = new SnakeViewModel(mapSize, bodyRadiusSize, surfaceWidthSize, SurfaceHeightSize, obstaclesGame);

        viewModel.getScore().observe(this, score -> binding.SnakeTvPoints.setText(getResources().getString(R.string.points, score)));
        viewModel.getRecord().observe(this, record -> binding.SnakeTvMaxPoints.setText(getResources().getString(R.string.max_points, record)));
        viewModel.getGoldenPointCounter().observe(this, goldenPointCounter -> {
            if (viewModel.isGoldenPointActive())
                binding.SnakeTvGoldenPointTimer.setText(getResources().getString(R.string.golden_point_timer, goldenPointCounter));
            else binding.SnakeTvGoldenPointTimer.setText(getResources().getString(R.string.no_golden_point));
        });
        viewModel.getCurrentSpeed().observe(this, currentSpeed -> {
            stopTimer();
            startTimer(currentSpeed);
        });
        viewModel.getCurrentSpeedPercentage().observe(this, currentSpeedPercentage ->
                binding.SnakeTvCurrentSpeed.setText(getResources().getString(R.string.snake_speed, currentSpeedPercentage)));
    }

    private void getExtras(){
        mapSize = getIntent().getIntExtra(MAP_SIZE,-1);
        if (mapSize == 1) {
            colsPerRow = 10;
        }
        else if (mapSize == 2) colsPerRow = 15;
        else colsPerRow = 20;

        obstaclesGame = getIntent().getBooleanExtra(OBSTACLES_GAME, false);
    }

    @Override
    public void surfaceCreated(@NonNull SurfaceHolder holder) {
        this.surfaceHolder = holder;
        viewModel.startGame();
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
                viewModel.changeDirection(clickX1, clickX2, clickY1, clickY2);
                break;
        }
        return false;
    }

    private void startTimer(int snakeSpeed) {
        runOnUiThread(() -> {
            timer = new Timer();
            timer.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    viewModel.checkPoint();
                    if (viewModel.isGoldenPointActive())
                        viewModel.checkGoldenPoint();

                    viewModel.moveSnake();

                    if (viewModel.isGameOver()){
                        gameOver();
                    } else {
                        if (!viewModel.isGoldenPointActive()) viewModel.trySpawnGoldenPoint();
                        canvas = surfaceHolder.lockCanvas();
                        draw();
                        surfaceHolder.unlockCanvasAndPost(canvas);
                    }
                }
            },1000 - snakeSpeed,1000-snakeSpeed);
        });
    }

    private void draw(){
        canvas.drawColor(Color.WHITE, PorterDuff.Mode.CLEAR);
        // Point
        canvas.drawCircle(
                viewModel.getPoint().getPositionX(),
                viewModel.getPoint().getPositionY(),
                bodyRadiusSize,
                pointColor
        );
        //Golden point
        if (viewModel.isGoldenPointActive())
            canvas.drawCircle(
                    viewModel.getGoldenPoint().getPositionX(),
                    viewModel.getGoldenPoint().getPositionY(),
                    bodyRadiusSize,
                    goldenPointColor
            );
        //Snake head
        canvas.drawRect(
                viewModel.getSnakeHead().getPositionX() - bodyRadiusSize,
                viewModel.getSnakeHead().getPositionY() - bodyRadiusSize,
                viewModel.getSnakeHead().getPositionX() + bodyRadiusSize,
                viewModel.getSnakeHead().getPositionY() + bodyRadiusSize,
                headColor
        );
        //Snake Body
        for (int i = 1; i < viewModel.getSnakeBodySize(); i++){
            canvas.drawRect(
                    viewModel.getSnakeBodyPiece(i).getPositionX() - bodyRadiusSize,
                    viewModel.getSnakeBodyPiece(i).getPositionY() - bodyRadiusSize,
                    viewModel.getSnakeBodyPiece(i).getPositionX() + bodyRadiusSize,
                    viewModel.getSnakeBodyPiece(i).getPositionY() + bodyRadiusSize,
                    bodyColor);
        }
        // Obstacles
        if (viewModel.getObstacles() != null)
            for (SnakePiece obstacle: viewModel.getObstacles()) {
                canvas.drawRect(
                        obstacle.getPositionX() - bodyRadiusSize,
                        obstacle.getPositionY() - bodyRadiusSize,
                        obstacle.getPositionX() + bodyRadiusSize,
                        obstacle.getPositionY() + bodyRadiusSize,
                        obstacleColor
                );
            }
    }

    private void gameOver(){
        stopTimer();
        viewModel.gameOver();

        runOnUiThread(() -> {
            CustomDialog.DialogButtonClick gameOverDialog = new CustomDialog.DialogButtonClick() {
                @Override
                public void onPositiveButtonClicked() {
                    viewModel.startGame();
                }

                @Override
                public void onNegativeButtonClicked() {
                    finish();
                }
            };
            CustomDialog dialog = new CustomDialog(this,
                    getResources().getString(R.string.game_over_points) + " " + viewModel.getScore().getValue(),
                    getResources().getString(R.string.game_over_title),
                    gameOverDialog);
            dialog.setCancelable(false);
            dialog.setOnShowListener(dialogInterface -> {
                dialog.getBtPositive().setText(getResources().getString(R.string.play_again));
                dialog.getBtNegative().setText(getResources().getString(R.string.back_menu));
            });
            dialog.show();
        });
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

    private void stopTimer(){
        if (timer != null) {
            timer.cancel();
            timer.purge();
        }
    }

    @Override
    public void onBackPressed(){
        stopTimer();
        finish();
    }

    @Override
    public void onStop(){
        super.onStop();
        stopTimer();
    }
}