package com.example.assignment1;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        CardView cardSnake = findViewById(R.id.cardSnake);
        CardView cardTicTacToe = findViewById(R.id.cardTicTacToe);
        CardView cardNumGuess = findViewById(R.id.cardNumGuess);
        CardView cardReflex = findViewById(R.id.cardReflex);

        cardSnake.setOnClickListener(v ->
                startActivity(new Intent(this, SnakeActivity.class)));

        cardTicTacToe.setOnClickListener(v ->
                startActivity(new Intent(this, TicTacToeActivity.class)));

        cardNumGuess.setOnClickListener(v ->
                startActivity(new Intent(this, NumberGuessActivity.class)));

        cardReflex.setOnClickListener(v ->
                startActivity(new Intent(this, ReflexActivity.class)));
    }
}
