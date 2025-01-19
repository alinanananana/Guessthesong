package com.example.guessthesong;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Random;

public class PlaySongActivity extends AppCompatActivity {
    private MediaPlayer mediaPlayer;
    private Handler handler;
    private Runnable countdownRunnable;
    private TextView player1BidTextView;
    private TextView player2BidTextView;
    private TextView turnTextView;
    private TextView playingTextView;
    private PulsatingCircleView pulsatingCircleView;
    private TextView countdownTextView;


    private int[] songResources = {
            R.raw.apple,
            R.raw.birds_of_a_feather,
            R.raw.bulletproof_heart,
            R.raw.enchanted,
            R.raw.good_luck_babe,
            R.raw.hard_times,
            R.raw.heather,
            R.raw.history_of_man,
            R.raw.hot_to_go,
            R.raw.ima,
            R.raw.lavish,
            R.raw.mantra,
            R.raw.mastermind,
            R.raw.not_today,
            R.raw.never_ending_song,
            R.raw.poison_poison,
            R.raw.snow_angel,
            R.raw.thats_so_true,
            R.raw.welcome_to_the_black_parade,
            R.raw.wendy,
            R.raw.what_was_i_made_for
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_song);

        // Инициализация TextView
        player1BidTextView = findViewById(R.id.player1BidTextView);
        player2BidTextView = findViewById(R.id.player2BidTextView);
        turnTextView = findViewById(R.id.turnTextView); // Текст, чей ход угадывать
        playingTextView = findViewById(R.id.playingTextView);
        pulsatingCircleView = findViewById(R.id.pulsatingCircleView);
        countdownTextView = findViewById(R.id.countdownTextView);

        // Получение данных из Intent
        int playTime = getIntent().getIntExtra("selectedBid", 5); // Ставка игрока
        int playerToPlay = getIntent().getIntExtra("playerToPlay", 1);
        int scorePlayer1 = getIntent().getIntExtra("scorePlayer1", 0);
        int scorePlayer2 = getIntent().getIntExtra("scorePlayer2", 0);
        int player1Bid = getIntent().getIntExtra("player1Bid", 0); // Ставка игрока 1
        int player2Bid = getIntent().getIntExtra("player2Bid", 0); // Ставка игрока 2

        player1BidTextView.setText("Ставка игрока 1:\n" + player1Bid);
        player2BidTextView.setText("Ставка игрока 2:\n" + player2Bid);
        turnTextView.setVisibility(View.INVISIBLE);
        setVisibilityPlaySongLayout(View.INVISIBLE);

        // Таймер задержки перед началом песни
        handler = new Handler();

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                player1BidTextView.setVisibility(View.INVISIBLE);
                player2BidTextView.setVisibility(View.INVISIBLE);
                turnTextView.setText(playerToPlay == 1 ? "Игрок 1,\nприготовьтесь!" : "Игрок 2,\nприготовьтесь!");  // Устанавливаем текст для отображения хода
                turnTextView.setVisibility(View.VISIBLE);
            }
        }, 2000);

        handler.postDelayed(() -> {
            // Скрыть текст с ходом игрока
            turnTextView.setVisibility(View.INVISIBLE);
            setVisibilityPlaySongLayout(View.VISIBLE);

            // Выбираем случайную песню
            int randomIndex = new Random().nextInt(songResources.length);
            int selectedSong = songResources[randomIndex];

            // Запуск воспроизведения песни
            mediaPlayer = MediaPlayer.create(this, selectedSong);
            mediaPlayer.start();

            // Настройка таймера обратного отсчета
            countdownRunnable = new Runnable() {
                int remainingTime = playTime;

                @Override
                public void run() {
                    if (remainingTime >= 0) {
                        countdownTextView.setText(String.valueOf(remainingTime)); // Обновляем цифры
                        remainingTime--;
                        handler.postDelayed(this, 1000);
                    } else {
                        // Останавливаем песню и переходим к следующему экрану
                        mediaPlayer.stop();
                        Intent intent = new Intent(PlaySongActivity.this, QuestionActivity.class);
                        intent.putExtra("scorePlayer1", scorePlayer1);
                        intent.putExtra("scorePlayer2", scorePlayer2);
                        intent.putExtra("playerToPlay", playerToPlay);
                        intent.putExtra("correctAnswer", randomIndex);
                        startActivity(intent);
                        finish();
                    }
                }
            };
            handler.post(countdownRunnable);

        }, 4000); // Задержка 3 секунды для отображения текста
    }

    private void setVisibilityPlaySongLayout(int visibility) {
        playingTextView.setVisibility(visibility);
        pulsatingCircleView.setVisibility(visibility);
        countdownTextView.setVisibility(visibility);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.release();
        }
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
        }
    }
}
