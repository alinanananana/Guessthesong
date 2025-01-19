package com.example.guessthesong;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class QuestionActivity extends AppCompatActivity {
    private String[] songTitles = {
            "apple",
            "birds of a feather",
            "bulletproof heart",
            "enchanted",
            "good luck, babe!",
            "hard times",
            "heather",
            "history of man",
            "hot to go!",
            "ima",
            "lavish",
            "mantra",
            "mastermind",
            "not today",
            "never ending song",
            "poison poison",
            "snow angel",
            "that's so true",
            "welcome to the black parade",
            "wendy",
            "what was i made for"
    };

    private int scorePlayer1;
    private int scorePlayer2;
    private int playerToPlay;


    private TextView currentPlayerTextView;
    private TextView scorePlayer1TextView;
    private TextView scorePlayer2TextView;
    private TextView questionText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);

        // Получение данных из предыдущей активности
        scorePlayer1 = getIntent().getIntExtra("scorePlayer1", 0);
        scorePlayer2 = getIntent().getIntExtra("scorePlayer2", 0);
        playerToPlay = getIntent().getIntExtra("playerToPlay", -1);
        int correctAnswerIndex = getIntent().getIntExtra("correctAnswer", 0);

        // Инициализация элементов интерфейса
        questionText = findViewById(R.id.questionText);
        currentPlayerTextView = findViewById(R.id.playerTurnTextView);
        scorePlayer1TextView = findViewById(R.id.scorePlayer1TextView);
        scorePlayer2TextView = findViewById(R.id.scorePlayer2TextView);
        // Устанавливаем вопрос и баллы
        questionText.setText("Как называется эта песня?");

        updateScores();
        updateCurrentPlayerText();

        // Установка текста кнопок с ответами
        Button[] answerButtons = {
                findViewById(R.id.answerButton1),
                findViewById(R.id.answerButton2),
                findViewById(R.id.answerButton3),
                findViewById(R.id.answerButton4)
        };

        // Устанавливаем начальный цвет кнопок
        for (Button button : answerButtons) {
            button.setBackgroundColor(Color.parseColor("#FF6750A3"));
        }

        // Выбираем случайную кнопку для правильного ответа
        int correctButtonIndex = new Random().nextInt(answerButtons.length);
        answerButtons[correctButtonIndex].setText(songTitles[correctAnswerIndex]);

        // Заполняем остальные кнопки случайными неправильными ответами
        List<String> wrongAnswers = new ArrayList<>(Arrays.asList(songTitles));
        wrongAnswers.remove(correctAnswerIndex);

        for (int i = 0; i < answerButtons.length; i++) {
            if (i != correctButtonIndex) {
                int randomWrongIndex = new Random().nextInt(wrongAnswers.size());
                answerButtons[i].setText(wrongAnswers.get(randomWrongIndex));
                wrongAnswers.remove(randomWrongIndex);
            }
        }

        // Обработка нажатия на кнопки ответов
        for (int i = 0; i < answerButtons.length; i++) {
            int finalI = i;
            answerButtons[i].setOnClickListener(v -> {
                boolean isCorrect = finalI == correctButtonIndex;

                // Меняем цвет кнопки
                if (isCorrect) {
                    answerButtons[finalI].setBackgroundColor(Color.parseColor("#FF91CA4F"));
                } else {
                    answerButtons[finalI].setBackgroundColor(Color.parseColor("#FFBF3636"));
                    answerButtons[correctButtonIndex].setBackgroundColor(Color.parseColor("#FF91CA4F"));
                }

                // Обновляем очки игрока
                if (playerToPlay == 1) {
                    scorePlayer1 += isCorrect ? 1 : -1;
                } else {
                    scorePlayer2 += isCorrect ? 1 : -1;
                }

                updateScores();


                // Задержка перед следующей активностью
                new Handler().postDelayed(() -> {
                    if (scorePlayer1 >= 3) {
                        endGame("Игрок 1", R.drawable.player1);
                    } else if (scorePlayer2 >= 3) {
                        endGame("Игрок 2", R.drawable.player2);
                    } else {
                        goToBiddingActivity();
                    }
                }, 1500);
            });
        }
    }

    // Обновление текста текущего игрока
    private void updateCurrentPlayerText() {
        if (playerToPlay == 1) {
            currentPlayerTextView.setText("Ход Игрока 1");
            currentPlayerTextView.setBackgroundColor(Color.parseColor("#F69FA5"));
        } else {
            currentPlayerTextView.setText("Ход Игрока 2");
            currentPlayerTextView.setBackgroundColor(Color.parseColor("#FFCC42"));
        }
    }

    // Обновление очков на экране
    private void updateScores() {
        scorePlayer1TextView.setText("Игрок 1: " + scorePlayer1 + " " + getCorrectWordForm(scorePlayer1));
        scorePlayer2TextView.setText("Игрок 2: " + scorePlayer2 + " " + getCorrectWordForm(scorePlayer2));
    }


    // Завершение игры
    private void endGame(String winner, int imageId) {
        Intent intent = new Intent(QuestionActivity.this, WinnerActivity.class);
        intent.putExtra("winner", winner);
        intent.putExtra("imageId", imageId);
        startActivity(intent);
        finish();
    }

    // Переход к активности ставок
    private void goToBiddingActivity() {
        Intent intent = new Intent(QuestionActivity.this, BiddingActivity.class);
        intent.putExtra("scorePlayer1", scorePlayer1);
        intent.putExtra("scorePlayer2", scorePlayer2);
        intent.putExtra("playerToPlay", playerToPlay != 1); // Смена хода
        startActivity(intent);
        finish();
    }

    private String getCorrectWordForm(int score) {
        int absScore = Math.abs(score); // Берём абсолютное значение для анализа
        int lastDigit = absScore % 10;
        int lastTwoDigits = absScore % 100;

        if (lastTwoDigits >= 11 && lastTwoDigits <= 19) {
            return "очков";
        }

        switch (lastDigit) {
            case 1:
                return "очко";
            case 2:
            case 3:
            case 4:
                return "очка";
            default:
                return "очков";
        }
    }

}

