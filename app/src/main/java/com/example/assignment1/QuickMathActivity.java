package com.example.assignment1;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Random;

public class QuickMathActivity extends AppCompatActivity {

    private TextView tvQuestion, tvScore, tvTimer;
    private EditText etAnswer;
    private Button btnSubmit, btnBack;

    private int score = 0;
    private int correctAnswer;
    private Random random = new Random();
    private CountDownTimer timer;
    private boolean isGameActive = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quick_math);

        tvQuestion = findViewById(R.id.tvQuestion);
        tvScore = findViewById(R.id.tvScore);
        tvTimer = findViewById(R.id.tvTimer);
        etAnswer = findViewById(R.id.etAnswer);
        btnSubmit = findViewById(R.id.btnSubmit);
        btnBack = findViewById(R.id.btnBack);

        generateQuestion();
        startTimer();

        btnSubmit.setOnClickListener(v -> checkAnswer());
        btnBack.setOnClickListener(v -> finish());

        etAnswer.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                checkAnswer();
                return true;
            }
            return false;
        });
    }

    private void generateQuestion() {
        int a = random.nextInt(20) + 1;
        int b = random.nextInt(20) + 1;
        int op = random.nextInt(3); // 0: +, 1: -, 2: *

        switch (op) {
            case 0:
                correctAnswer = a + b;
                tvQuestion.setText(a + " + " + b + " = ?");
                break;
            case 1:
                correctAnswer = a - b;
                tvQuestion.setText(a + " - " + b + " = ?");
                break;
            case 2:
                a = random.nextInt(10) + 1;
                b = random.nextInt(10) + 1;
                correctAnswer = a * b;
                tvQuestion.setText(a + " × " + b + " = ?");
                break;
        }
        etAnswer.setText("");
    }

    private void checkAnswer() {
        if (!isGameActive) return;

        String input = etAnswer.getText().toString();
        if (input.isEmpty()) return;

        try {
            int userAnswer = Integer.parseInt(input);
            if (userAnswer == correctAnswer) {
                score++;
                tvScore.setText("Score: " + score);
                generateQuestion();
            } else {
                Toast.makeText(this, "Wrong!", Toast.LENGTH_SHORT).show();
                generateQuestion();
            }
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Invalid input", Toast.LENGTH_SHORT).show();
        }
    }

    private void startTimer() {
        timer = new CountDownTimer(30000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                tvTimer.setText("Time: " + (millisUntilFinished / 1000));
            }

            @Override
            public void onFinish() {
                isGameActive = false;
                tvTimer.setText("Time: 0");
                showGameOver();
            }
        }.start();
    }

    private void showGameOver() {
        new AlertDialog.Builder(this)
                .setTitle("Game Over")
                .setMessage("Final Score: " + score)
                .setPositiveButton("Play Again", (d, w) -> {
                    score = 0;
                    tvScore.setText("Score: 0");
                    isGameActive = true;
                    generateQuestion();
                    startTimer();
                })
                .setNegativeButton("Hub", (d, w) -> finish())
                .setCancelable(false)
                .show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (timer != null) timer.cancel();
    }
}
