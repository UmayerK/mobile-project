package com.example.assignment1;

import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import java.util.Random;

public class ReflexActivity extends AppCompatActivity {

    private enum State { IDLE, WAITING, GO }

    private State state = State.IDLE;
    private long goTimestamp;
    private long bestTime = Long.MAX_VALUE;
    private final Random random = new Random();
    private final Handler handler = new Handler();
    private Runnable goRunnable;

    private Button btnTapTarget;
    private TextView tvResult, tvBestTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reflex);

        btnTapTarget = findViewById(R.id.btnTapTarget);
        tvResult = findViewById(R.id.tvReflexResult);
        tvBestTime = findViewById(R.id.tvBestTime);
        Button btnTryAgain = findViewById(R.id.btnTryAgain);
        Button btnBack = findViewById(R.id.btnReflexBack);

        btnTapTarget.setOnClickListener(v -> onTap());
        btnTryAgain.setOnClickListener(v -> startRound());
        btnBack.setOnClickListener(v -> finish());

        // Start the first round
        startRound();
    }

    private void startRound() {
        state = State.WAITING;
        tvResult.setText("");
        btnTapTarget.setText(getString(R.string.tap_here));
        btnTapTarget.setBackgroundTintList(
                ContextCompat.getColorStateList(this, R.color.reflex_waiting));

        // Random delay between 1 and 5 seconds
        long delay = 1000 + random.nextInt(4000);
        goRunnable = () -> {
            state = State.GO;
            goTimestamp = System.currentTimeMillis();
            btnTapTarget.setText(getString(R.string.tap_now));
            btnTapTarget.setBackgroundTintList(
                    ContextCompat.getColorStateList(this, R.color.reflex_go));
        };
        handler.postDelayed(goRunnable, delay);
    }

    private void onTap() {
        switch (state) {
            case IDLE:
                startRound();
                break;

            case WAITING:
                // Tapped too early
                handler.removeCallbacks(goRunnable);
                state = State.IDLE;
                tvResult.setText(getString(R.string.too_early));
                btnTapTarget.setText(getString(R.string.tap_here));
                btnTapTarget.setBackgroundTintList(
                        ContextCompat.getColorStateList(this, R.color.reflex_idle));
                break;

            case GO:
                long reactionTime = System.currentTimeMillis() - goTimestamp;
                state = State.IDLE;
                tvResult.setText(String.format(getString(R.string.reaction_time), reactionTime));

                if (reactionTime < bestTime) {
                    bestTime = reactionTime;
                }
                tvBestTime.setText(String.format(getString(R.string.best_time), bestTime));

                btnTapTarget.setText(getString(R.string.tap_here));
                btnTapTarget.setBackgroundTintList(
                        ContextCompat.getColorStateList(this, R.color.reflex_idle));
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (goRunnable != null) {
            handler.removeCallbacks(goRunnable);
        }
    }
}
