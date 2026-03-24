package com.example.assignment1;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class SnakeActivity extends AppCompatActivity {

    private SnakeGameView snakeView;
    private TextView tvScore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_snake);

        snakeView = findViewById(R.id.snakeGameView);
        tvScore = findViewById(R.id.tvScore);
        Button btnRestart = findViewById(R.id.btnRestart);
        Button btnBack = findViewById(R.id.btnSnakeBack);

        // D-pad
        Button btnUp = findViewById(R.id.btnUp);
        Button btnDown = findViewById(R.id.btnDown);
        Button btnLeft = findViewById(R.id.btnLeft);
        Button btnRight = findViewById(R.id.btnRight);

        snakeView.setOnScoreChangeListener(new SnakeGameView.OnScoreChangeListener() {
            @Override
            public void onScoreChanged(int score) {
                tvScore.setText(String.format(getString(R.string.score_label), score));
            }

            @Override
            public void onGameOver(int score) {
                runOnUiThread(() -> {
                    new AlertDialog.Builder(SnakeActivity.this)
                            .setTitle("Game Over")
                            .setMessage(String.format(getString(R.string.game_over), score))
                            .setPositiveButton(R.string.play_again, (d, w) -> snakeView.resetGame())
                            .setNegativeButton(R.string.back, (d, w) -> finish())
                            .setCancelable(false)
                            .show();
                });
            }
        });

        btnRestart.setOnClickListener(v -> snakeView.resetGame());
        btnBack.setOnClickListener(v -> finish());

        // D-pad controls: 0=UP, 1=DOWN, 2=LEFT, 3=RIGHT
        btnUp.setOnClickListener(v -> snakeView.setDirection(0));
        btnDown.setOnClickListener(v -> snakeView.setDirection(1));
        btnLeft.setOnClickListener(v -> snakeView.setDirection(2));
        btnRight.setOnClickListener(v -> snakeView.setDirection(3));
    }

    @Override
    protected void onPause() {
        super.onPause();
        snakeView.stopGame();
    }

    @Override
    protected void onResume() {
        super.onResume();
        snakeView.startGame();
    }
}
