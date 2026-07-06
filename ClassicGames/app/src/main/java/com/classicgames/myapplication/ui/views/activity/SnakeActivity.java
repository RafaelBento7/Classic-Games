package com.classicgames.myapplication.ui.views.activity;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.content.res.AppCompatResources;

import com.classicgames.myapplication.R;
import com.classicgames.myapplication.data.models.SnakePiece;
import com.classicgames.myapplication.databinding.ActivitySnakeBinding;
import com.classicgames.myapplication.ui.viewmodels.SnakeViewModel;
import com.classicgames.myapplication.MyApplication;
import com.classicgames.myapplication.utils.ReviewHelper;
import com.classicgames.myapplication.utils.SoundManager;
import com.classicgames.myapplication.ui.dialog.CustomDialog;

import java.util.Timer;
import java.util.TimerTask;

public class SnakeActivity extends BaseActivity implements SurfaceHolder.Callback {

    public static final String MAP_SIZE = "snake_map_size";
    public static final String OBSTACLES_GAME = "snake_obstacles_game";

    private SnakeViewModel viewModel;
    private ActivitySnakeBinding binding;

    private SurfaceView surfaceView;
    private volatile SurfaceHolder surfaceHolder;
    private int mapSize, bodyRadiusSize, colsPerRow;
    private boolean obstaclesGame;
    private float clickX1 = 0,clickY1 = 0;
    private Timer timer;
    private Paint bodyColor = null, headColor = null, obstacleColor = null, obstacleBorder = null, eyeWhitePaint = null, eyePupilPaint = null;
    private Drawable pointApple = null, goldenApple = null;
    private final RectF drawRect = new RectF();
    private final Path spikePath = new Path();
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySnakeBinding.inflate(getLayoutInflater());
        binding.getRoot().setFitsSystemWindows(true);

        setSupportActionBar(binding.SnakeToolbar.getRoot());
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        binding.SnakeToolbar.ToolbarTitle.setText(R.string.snake_game);

        setContentView(binding.getRoot());
        getExtras();
        createPaints();

        // Predictive-back compatible replacement for the deprecated onBackPressed().
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                stopTimer();
                finish();
            }
        });

        ViewTreeObserver viewTreeObserver = binding.SnakeFrameLayoutGame.getViewTreeObserver();
        viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                float density = getResources().getDisplayMetrics().density;
                // The red frame is drawn as the background of a wrapper that hugs the
                // SurfaceView exactly, so it sits close to the play area no matter how much
                // slack the outer (constraint-stretched) frame has. A uniform padding keeps
                // the surface off the stroke with an equal gap on all four sides. (The border
                // is drawn in the UI layer, so it isn't affected by the surface's compositing.)
                int stroke = Math.round(3 * density);   // matches snake_map_border stroke
                int gap    = Math.round(2 * density);    // visible gap between border and surface
                int pad    = stroke + gap;

                int usableWidth  = binding.SnakeFrameLayoutGame.getWidth()  - 2 * pad;
                int usableHeight = binding.SnakeFrameLayoutGame.getHeight() - 2 * pad;

                bodyRadiusSize = usableWidth / (colsPerRow * 2);  // multiply per 2, BodySize
                int cell = bodyRadiusSize * 2;
                int widthSize = usableWidth - (usableWidth % cell);
                int heightSize = usableHeight - (usableHeight % cell);

                surfaceView = new SurfaceView(SnakeActivity.this);
                surfaceView.setLayoutParams(new FrameLayout.LayoutParams(widthSize, heightSize, Gravity.CENTER));

                FrameLayout border = new FrameLayout(SnakeActivity.this);
                border.setBackgroundResource(R.drawable.snake_map_border);
                border.setPadding(pad, pad, pad, pad);
                border.setLayoutParams(new FrameLayout.LayoutParams(widthSize + 2 * pad, heightSize + 2 * pad, Gravity.CENTER));
                border.addView(surfaceView);
                binding.SnakeFrameLayoutGame.addView(border);

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
        // Stop drawing once the surface is gone so the timer thread never
        // locks/draws on an invalid surface (would return a null Canvas).
        this.surfaceHolder = null;
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
            timer.schedule(new TimerTask() {
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
                        drawFrame();
                    }
                }
            },1000 - snakeSpeed,1000-snakeSpeed);
        });
    }

    /**
     * Locks the surface, draws a frame and posts it. Guards against a null/invalid
     * surface: {@link SurfaceHolder#lockCanvas()} returns {@code null} when the
     * surface has been destroyed or isn't ready, which previously caused an NPE
     * inside {@link #draw(Canvas)}.
     */
    private void drawFrame(){
        SurfaceHolder holder = surfaceHolder;
        if (holder == null || !holder.getSurface().isValid()) return;

        Canvas canvas = holder.lockCanvas();
        if (canvas == null) return;

        try {
            draw(canvas);
        } finally {
            try {
                holder.unlockCanvasAndPost(canvas);
            } catch (IllegalStateException ignored) {
                // Surface was destroyed between lock and post; nothing to draw.
            }
        }
    }

    private void draw(Canvas canvas){
        canvas.drawColor(Color.WHITE, PorterDuff.Mode.CLEAR);
        int r = bodyRadiusSize;
        float corner = r * 0.6f;

        // Point (green apple) and golden point (gold apple)
        drawApple(canvas, pointApple, viewModel.getPoint());
        if (viewModel.isGoldenPointActive())
            drawApple(canvas, goldenApple, viewModel.getGoldenPoint());

        // Snake body: rounded segments
        for (int i = 1; i < viewModel.getSnakeBodySize(); i++){
            SnakePiece piece = viewModel.getSnakeBodyPiece(i);
            drawRect.set(piece.getPositionX() - r, piece.getPositionY() - r, piece.getPositionX() + r, piece.getPositionY() + r);
            canvas.drawRoundRect(drawRect, corner, corner, bodyColor);
        }

        // Snake head: rounded segment with a pair of eyes facing the direction of travel
        SnakePiece head = viewModel.getSnakeHead();
        drawRect.set(head.getPositionX() - r, head.getPositionY() - r, head.getPositionX() + r, head.getPositionY() + r);
        canvas.drawRoundRect(drawRect, corner, corner, headColor);
        drawEyes(canvas, head, viewModel.getDirection());

        // Obstacles: red spikes
        if (viewModel.getObstacles() != null)
            for (SnakePiece obstacle : viewModel.getObstacles())
                drawSpike(canvas, obstacle.getPositionX(), obstacle.getPositionY());
    }

    /** Draws a red 8-point spike/star centred on the given point, sized to one tile. */
    private void drawSpike(Canvas canvas, float cx, float cy){
        int r = bodyRadiusSize;
        float outer = r * 0.95f, inner = r * 0.42f;
        int spikes = 8;
        spikePath.reset();
        for (int i = 0; i < spikes * 2; i++){
            float radius = (i % 2 == 0) ? outer : inner;
            double angle = Math.PI * i / spikes - Math.PI / 2;
            float x = cx + (float) (radius * Math.cos(angle));
            float y = cy + (float) (radius * Math.sin(angle));
            if (i == 0) spikePath.moveTo(x, y);
            else spikePath.lineTo(x, y);
        }
        spikePath.close();
        obstacleBorder.setStrokeWidth(Math.max(2f, r * 0.14f));
        canvas.drawPath(spikePath, obstacleColor);
        canvas.drawPath(spikePath, obstacleBorder);
    }

    private void drawApple(Canvas canvas, Drawable apple, SnakePiece piece){
        if (apple == null) return;
        int r = bodyRadiusSize;
        apple.setBounds(piece.getPositionX() - r, piece.getPositionY() - r, piece.getPositionX() + r, piece.getPositionY() + r);
        apple.draw(canvas);
    }

    private void drawEyes(Canvas canvas, SnakePiece head, char direction){
        int r = bodyRadiusSize;
        float cx = head.getPositionX(), cy = head.getPositionY();
        float along = r * 0.42f, apart = r * 0.42f;
        float eyeR = r * 0.30f, pupilR = r * 0.15f;
        float e1x, e1y, e2x, e2y;
        switch (direction){
            case 'L':
                e1x = cx - along; e1y = cy - apart; e2x = cx - along; e2y = cy + apart; break;
            case 'U':
                e1x = cx - apart; e1y = cy - along; e2x = cx + apart; e2y = cy - along; break;
            case 'D':
                e1x = cx - apart; e1y = cy + along; e2x = cx + apart; e2y = cy + along; break;
            default: // 'R'
                e1x = cx + along; e1y = cy - apart; e2x = cx + along; e2y = cy + apart; break;
        }
        canvas.drawCircle(e1x, e1y, eyeR, eyeWhitePaint);
        canvas.drawCircle(e2x, e2y, eyeR, eyeWhitePaint);
        canvas.drawCircle(e1x, e1y, pupilR, eyePupilPaint);
        canvas.drawCircle(e2x, e2y, pupilR, eyePupilPaint);
    }

    private void gameOver(){
        stopTimer();
        viewModel.gameOver();
        SoundManager.play(viewModel.isNewRecord() ? SoundManager.Sound.RECORD : SoundManager.Sound.GAME_OVER);
        MyApplication.getInstance().getRecords().recordSnakeGame(viewModel.getMapSize(), obstaclesGame, viewModel.getApplesEaten(), viewModel.getGoldenEaten(), viewModel.getScoreValue());

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
            if (viewModel.isNewRecord()) {
                dialog.setShareText(getResources().getString(R.string.share_record_points,
                        getResources().getString(R.string.snake_game), viewModel.getScoreValue(),
                        getResources().getString(R.string.store_link)));
                ReviewHelper.requestReview(this);
            }
            dialog.setOnShowListener(dialogInterface -> {
                dialog.getBtPositive().setText(getResources().getString(R.string.play_again));
                dialog.getBtNegative().setText(getResources().getString(R.string.back_menu));
            });
            dialog.show();
        });
    }

    private void createPaints(){
        bodyColor = new Paint();
        bodyColor.setColor(Color.rgb(76, 175, 80));     // green snake body
        bodyColor.setStyle(Paint.Style.FILL);
        bodyColor.setAntiAlias(true);

        headColor = new Paint();
        headColor.setColor(Color.rgb(46, 125, 50));     // darker-green snake head
        headColor.setStyle(Paint.Style.FILL);
        headColor.setAntiAlias(true);

        eyeWhitePaint = new Paint();
        eyeWhitePaint.setColor(Color.WHITE);
        eyeWhitePaint.setStyle(Paint.Style.FILL);
        eyeWhitePaint.setAntiAlias(true);

        eyePupilPaint = new Paint();
        eyePupilPaint.setColor(Color.rgb(33, 33, 33));
        eyePupilPaint.setStyle(Paint.Style.FILL);
        eyePupilPaint.setAntiAlias(true);

        obstacleColor = new Paint();
        obstacleColor.setColor(Color.rgb(229, 57, 53));  // red spike fill
        obstacleColor.setStyle(Paint.Style.FILL);
        obstacleColor.setAntiAlias(true);

        obstacleBorder = new Paint();
        obstacleBorder.setColor(Color.rgb(183, 28, 28)); // darker red spike edge
        obstacleBorder.setStyle(Paint.Style.STROKE);
        obstacleBorder.setStrokeJoin(Paint.Join.ROUND);
        obstacleBorder.setStrokeWidth(Math.max(2f, bodyRadiusSize * 0.14f));
        obstacleBorder.setAntiAlias(true);

        pointApple = AppCompatResources.getDrawable(this, R.drawable.snake_apple);
        goldenApple = AppCompatResources.getDrawable(this, R.drawable.snake_golden_apple);
    }

    private void stopTimer(){
        if (timer != null) {
            timer.cancel();
            timer.purge();
        }
    }

    @Override
    public void onStop(){
        super.onStop();
        stopTimer();
    }
}