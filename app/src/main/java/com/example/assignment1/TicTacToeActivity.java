package com.example.assignment1;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

public class TicTacToeActivity extends AppCompatActivity {

    private Button[][] buttons = new Button[3][3];
    private boolean isXTurn = true;
    private String[][] board = new String[3][3];
    private int moveCount = 0;
    private TextView tvStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tic_tac_toe);

        tvStatus = findViewById(R.id.tvTttStatus);
        Button btnReset = findViewById(R.id.btnTttReset);
        Button btnBack = findViewById(R.id.btnTttBack);

        // Initialize 3x3 buttons
        int[][] btnIds = {
                {R.id.btn00, R.id.btn01, R.id.btn02},
                {R.id.btn10, R.id.btn11, R.id.btn12},
                {R.id.btn20, R.id.btn21, R.id.btn22}
        };

        for (int r = 0; r < 3; r++) {
            for (int c = 0; c < 3; c++) {
                final int row = r, col = c;
                buttons[r][c] = findViewById(btnIds[r][c]);
                buttons[r][c].setOnClickListener(v -> onCellClick(row, col));
                board[r][c] = "";
            }
        }

        btnReset.setOnClickListener(v -> resetBoard());
        btnBack.setOnClickListener(v -> finish());

        updateStatus();
    }

    private void onCellClick(int row, int col) {
        if (!board[row][col].isEmpty()) return;

        String mark = isXTurn ? "X" : "O";
        board[row][col] = mark;
        buttons[row][col].setText(mark);
        buttons[row][col].setTextColor(ContextCompat.getColor(this,
                isXTurn ? R.color.ttt_x : R.color.ttt_o));
        moveCount++;

        if (checkWin(mark)) {
            showResult(String.format(getString(R.string.player_wins), mark));
            disableBoard();
            return;
        }

        if (moveCount == 9) {
            showResult(getString(R.string.draw_game));
            return;
        }

        isXTurn = !isXTurn;
        updateStatus();
    }

    private boolean checkWin(String mark) {
        // Rows
        for (int r = 0; r < 3; r++) {
            if (board[r][0].equals(mark) && board[r][1].equals(mark) && board[r][2].equals(mark))
                return true;
        }
        // Columns
        for (int c = 0; c < 3; c++) {
            if (board[0][c].equals(mark) && board[1][c].equals(mark) && board[2][c].equals(mark))
                return true;
        }
        // Diagonals
        if (board[0][0].equals(mark) && board[1][1].equals(mark) && board[2][2].equals(mark))
            return true;
        if (board[0][2].equals(mark) && board[1][1].equals(mark) && board[2][0].equals(mark))
            return true;

        return false;
    }

    private void showResult(String message) {
        tvStatus.setText(message);
        new AlertDialog.Builder(this)
                .setTitle("Game Over")
                .setMessage(message)
                .setPositiveButton(R.string.play_again, (d, w) -> resetBoard())
                .setNegativeButton(R.string.back, (d, w) -> finish())
                .setCancelable(false)
                .show();
    }

    private void disableBoard() {
        for (int r = 0; r < 3; r++)
            for (int c = 0; c < 3; c++)
                buttons[r][c].setEnabled(false);
    }

    private void resetBoard() {
        isXTurn = true;
        moveCount = 0;
        for (int r = 0; r < 3; r++) {
            for (int c = 0; c < 3; c++) {
                board[r][c] = "";
                buttons[r][c].setText("");
                buttons[r][c].setEnabled(true);
            }
        }
        updateStatus();
    }

    private void updateStatus() {
        tvStatus.setText(String.format(getString(R.string.player_turn), isXTurn ? "X" : "O"));
    }
}
