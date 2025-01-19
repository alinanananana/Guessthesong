package com.example.guessthesong;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputFilter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class BiddingActivity extends AppCompatActivity {
    private int player1Bid = -1; // Ставка игрока 1
    private int player2Bid = -1; // Ставка игрока 2
    private boolean isPlayer1Turn = true; // Флаг: сейчас ход игрока 1
    private TextView playerTurnTextView;
    private EditText bidInput;
    private Button submitBidButton;
    int scorePlayer1 = 0;
    int scorePlayer2 = 0;

    private ImageView playerAvatar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bidding);

        playerTurnTextView = findViewById(R.id.playerTurnTextView);
        playerAvatar = findViewById(R.id.playerAvatar);

        bidInput = findViewById(R.id.bidInput);
        bidInput.setFilters(new InputFilter[]{
                new InputFilterMinMax(1, 20)
        });
        submitBidButton = findViewById(R.id.submitBidButton);

        updateUI(); // Настроить интерфейс для первого игрока

        // Получаем очки игроков из Intent
        scorePlayer1 = getIntent().getIntExtra("scorePlayer1", 0);
        scorePlayer2 = getIntent().getIntExtra("scorePlayer2", 0);

        submitBidButton.setOnClickListener(v -> {
            // Получаем введенную ставку
            String bidString = bidInput.getText().toString();
            if (bidString.isEmpty()) {
                Toast.makeText(this, "Введите количество секунд!", Toast.LENGTH_SHORT).show();
                return;
            }

            int bid = Integer.parseInt(bidString);

            // Сохраняем ставку текущего игрока
            if (isPlayer1Turn) {
                player1Bid = bid;
                isPlayer1Turn = false; // Передаем ход второму игроку
                bidInput.setText(""); // Очищаем поле ввода для второго игрока
                updateUI();
            } else {
                player2Bid = bid;
                if (player1Bid == player2Bid) {
                    Toast.makeText(this, "Ставки должны отличаться", Toast.LENGTH_SHORT).show();
                    return;
                }
                revealBidsAndProceed(); // Показываем ставки и продолжаем игру
            }
        });
    }

    private void updateUI() {
        if (isPlayer1Turn) {
            playerTurnTextView.setText("Ход Игрока 1: сделайте ставку!");
            playerAvatar.setImageResource(R.drawable.player1);
            submitBidButton.setBackgroundColor(Color.parseColor("#F69FA5"));
        } else {
            playerTurnTextView.setText("Ход Игрока 2: сделайте ставку!");
            playerAvatar.setImageResource(R.drawable.player2);
            submitBidButton.setBackgroundColor(Color.parseColor("#FFCC42"));
        }
    }

    private void revealBidsAndProceed() {

        // Передаем минимальную ставку в следующую активность
        int selectedBid = Math.min(player1Bid, player2Bid);
        Intent intent = new Intent(BiddingActivity.this, PlaySongActivity.class);
        intent.putExtra("selectedBid", selectedBid);
        intent.putExtra("player1Bid", player1Bid);
        intent.putExtra("player2Bid", player2Bid);
        intent.putExtra("playerToPlay", player1Bid < player2Bid ? 1 : 2); // Кто будет угадывать
        intent.putExtra("scorePlayer1", scorePlayer1);
        intent.putExtra("scorePlayer2", scorePlayer2);
        startActivity(intent);
    }

}


