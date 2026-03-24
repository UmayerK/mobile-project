package com.example.assignment1;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SnakeGameView extends View {

    public interface OnScoreChangeListener {
        void onScoreChanged(int score);
        void onGameOver(int score);
    }

    private enum Direction { UP, DOWN, LEFT, RIGHT }

    // Grid configuration
    private static final int GRID_SIZE = 20;
    private int cellSize;
    private int offsetX, offsetY;

    // Game state
    private List<Point> snake;
    private Point food;
    private Direction direction;
    private Direction nextDirection;
    private boolean isGameOver;
    private boolean isRunning;
    private int score;
    private final Random random = new Random();

    // Timing
    private final Handler handler = new Handler();
    private static final long TICK_MS = 150;

    // Drawing
    private Paint paintHead, paintBody, paintFood, paintGrid, paintGridLine;

    // Listener
    private OnScoreChangeListener scoreListener;

    // Gesture detection for swipe
    private GestureDetector gestureDetector;

    public SnakeGameView(Context context) {
        super(context);
        init(context);
    }

    public SnakeGameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public SnakeGameView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        paintHead = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintHead.setColor(ContextCompat.getColor(context, R.color.snake_head));

        paintBody = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintBody.setColor(ContextCompat.getColor(context, R.color.snake_body));

        paintFood = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintFood.setColor(ContextCompat.getColor(context, R.color.snake_food));

        paintGrid = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintGrid.setColor(ContextCompat.getColor(context, R.color.snake_bg));

        paintGridLine = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintGridLine.setColor(ContextCompat.getColor(context, R.color.snake_grid));
        paintGridLine.setStrokeWidth(1);

        gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                float dx = e2.getX() - e1.getX();
                float dy = e2.getY() - e1.getY();
                if (Math.abs(dx) > Math.abs(dy)) {
                    setDirection(dx > 0 ? Direction.RIGHT : Direction.LEFT);
                } else {
                    setDirection(dy > 0 ? Direction.DOWN : Direction.UP);
                }
                return true;
            }

            @Override
            public boolean onDown(MotionEvent e) {
                return true;
            }
        });

        resetGame();
    }

    public void setOnScoreChangeListener(OnScoreChangeListener listener) {
        this.scoreListener = listener;
    }

    public void setDirection(int dir) {
        // 0=UP, 1=DOWN, 2=LEFT, 3=RIGHT
        switch (dir) {
            case 0: setDirection(Direction.UP); break;
            case 1: setDirection(Direction.DOWN); break;
            case 2: setDirection(Direction.LEFT); break;
            case 3: setDirection(Direction.RIGHT); break;
        }
    }

    private void setDirection(Direction newDir) {
        // Prevent reversing
        if (direction == Direction.UP && newDir == Direction.DOWN) return;
        if (direction == Direction.DOWN && newDir == Direction.UP) return;
        if (direction == Direction.LEFT && newDir == Direction.RIGHT) return;
        if (direction == Direction.RIGHT && newDir == Direction.LEFT) return;
        nextDirection = newDir;
    }

    public void resetGame() {
        snake = new ArrayList<>();
        snake.add(new Point(GRID_SIZE / 2, GRID_SIZE / 2));
        snake.add(new Point(GRID_SIZE / 2 - 1, GRID_SIZE / 2));
        snake.add(new Point(GRID_SIZE / 2 - 2, GRID_SIZE / 2));
        direction = Direction.RIGHT;
        nextDirection = Direction.RIGHT;
        isGameOver = false;
        score = 0;
        spawnFood();
        if (scoreListener != null) scoreListener.onScoreChanged(0);
        startGame();
    }

    private void spawnFood() {
        Point p;
        do {
            p = new Point(random.nextInt(GRID_SIZE), random.nextInt(GRID_SIZE));
        } while (snake.contains(p));
        food = p;
    }

    public void startGame() {
        if (isRunning) return;
        isRunning = true;
        handler.post(gameLoop);
    }

    public void stopGame() {
        isRunning = false;
        handler.removeCallbacks(gameLoop);
    }

    private final Runnable gameLoop = new Runnable() {
        @Override
        public void run() {
            if (!isGameOver && isRunning) {
                update();
                invalidate();
                handler.postDelayed(this, TICK_MS);
            }
        }
    };

    private void update() {
        direction = nextDirection;

        Point head = snake.get(0);
        Point newHead = new Point(head.x, head.y);

        switch (direction) {
            case UP:    newHead.y--; break;
            case DOWN:  newHead.y++; break;
            case LEFT:  newHead.x--; break;
            case RIGHT: newHead.x++; break;
        }

        // Wall collision
        if (newHead.x < 0 || newHead.x >= GRID_SIZE || newHead.y < 0 || newHead.y >= GRID_SIZE) {
            gameOver();
            return;
        }

        // Self collision
        for (Point p : snake) {
            if (p.equals(newHead)) {
                gameOver();
                return;
            }
        }

        snake.add(0, newHead);

        // Food check
        if (newHead.equals(food)) {
            score++;
            if (scoreListener != null) scoreListener.onScoreChanged(score);
            spawnFood();
        } else {
            snake.remove(snake.size() - 1);
        }
    }

    private void gameOver() {
        isGameOver = true;
        isRunning = false;
        if (scoreListener != null) scoreListener.onGameOver(score);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        int size = Math.min(w, h);
        cellSize = size / GRID_SIZE;
        offsetX = (w - cellSize * GRID_SIZE) / 2;
        offsetY = (h - cellSize * GRID_SIZE) / 2;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // Background
        canvas.drawColor(paintGrid.getColor());

        // Grid lines
        for (int i = 0; i <= GRID_SIZE; i++) {
            canvas.drawLine(offsetX + i * cellSize, offsetY,
                    offsetX + i * cellSize, offsetY + GRID_SIZE * cellSize, paintGridLine);
            canvas.drawLine(offsetX, offsetY + i * cellSize,
                    offsetX + GRID_SIZE * cellSize, offsetY + i * cellSize, paintGridLine);
        }

        // Food (as a circle)
        float foodCx = offsetX + food.x * cellSize + cellSize / 2f;
        float foodCy = offsetY + food.y * cellSize + cellSize / 2f;
        canvas.drawCircle(foodCx, foodCy, cellSize / 2f - 2, paintFood);

        // Snake
        for (int i = 0; i < snake.size(); i++) {
            Point p = snake.get(i);
            Paint paint = (i == 0) ? paintHead : paintBody;
            float left = offsetX + p.x * cellSize + 1;
            float top = offsetY + p.y * cellSize + 1;
            float right = left + cellSize - 2;
            float bottom = top + cellSize - 2;
            canvas.drawRoundRect(left, top, right, bottom, 4, 4, paint);
        }

        // Game-over overlay text
        if (isGameOver) {
            Paint textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            textPaint.setColor(0xFFFFFFFF);
            textPaint.setTextSize(cellSize * 2f);
            textPaint.setTextAlign(Paint.Align.CENTER);
            canvas.drawText("GAME OVER", getWidth() / 2f, getHeight() / 2f, textPaint);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        gestureDetector.onTouchEvent(event);
        return true;
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        stopGame();
    }
}
