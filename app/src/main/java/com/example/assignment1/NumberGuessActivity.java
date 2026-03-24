package com.example.assignment1;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Random;

public class NumberGuessActivity extends AppCompatActivity {

    private int secretNumber;
    private int attempts;
    private EditText etGuess;
    private TextView tvFeedback, tvAttempts;
    private Button btnGuess;
    private final Random random = new Random();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_number_guess);

        etGuess = findViewById(R.id.etGuess);
        tvFeedback = findViewById(R.id.tvFeedback);
        tvAttempts = findViewById(R.id.tvAttempts);
        btnGuess = findViewById(R.id.btnGuess);
        Button btnNewGame = findViewById(R.id.btnNewGame);
        Button btnBack = findViewById(R.id.btnGuessBack);

        btnGuess.setOnClickListener(v -> makeGuess());
        btnNewGame.setOnClickListener(v -> newGame());
        btnBack.setOnClickListener(v -> finish());

        newGame();
    }

    private void newGame() {
        secretNumber = random.nextInt(100) + 1; // 1-100
        attempts = 0;
        tvFeedback.setText("");
        tvAttempts.setText(String.format(getString(R.string.attempts_label), 0));
        etGuess.setText("");
        etGuess.setEnabled(true);
        btnGuess.setEnabled(true);
    }

    private void makeGuess() {
        String input = etGuess.getText().toString().trim();
        if (input.isEmpty()) {
            Toast.makeText(this, R.string.invalid_input, Toast.LENGTH_SHORT).show();
            return;
        }

        int guess;
        try {
            guess = Integer.parseInt(input);
        } catch (NumberFormatException e) {
            Toast.makeText(this, R.string.invalid_input, Toast.LENGTH_SHORT).show();
            return;
        }

        if (guess < 1 || guess > 100) {
            Toast.makeText(this, R.string.invalid_input, Toast.LENGTH_SHORT).show();
            return;
        }

        attempts++;
        tvAttempts.setText(String.format(getString(R.string.attempts_label), attempts));

        if (guess < secretNumber) {
            tvFeedback.setText(R.string.too_low);
        } else if (guess > secretNumber) {
            tvFeedback.setText(R.string.too_high);
        } else {
            // Correct!
            tvFeedback.setText(String.format(getString(R.string.correct_guess), attempts));
            etGuess.setEnabled(false);
            btnGuess.setEnabled(false);
        }

        etGuess.setText("");
    }
}
