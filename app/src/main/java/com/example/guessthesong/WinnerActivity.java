package com.example.guessthesong;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.airbnb.lottie.LottieAnimationView;

public class WinnerActivity extends AppCompatActivity {
    Button restartBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_winner);

        restartBtn = findViewById(R.id.restartGameButton);

        String winner = getIntent().getStringExtra("winner");
        TextView winnerTextView = findViewById(R.id.winnerTextView);
        winnerTextView.setText(winner + " победил!");

        int imageId = getIntent().getIntExtra("imageId",-1);
        ImageView winnerAvatarImageView = findViewById(R.id.winnerAvatarImageView);
        winnerAvatarImageView.setImageResource(imageId);

        // Настройка анимации хлопушки
        LottieAnimationView confettiAnimationView = findViewById(R.id.confettiAnimationView);
        confettiAnimationView.setVisibility(View.VISIBLE);
        confettiAnimationView.playAnimation();

        restartBtn.setOnClickListener(v -> goToMainActivity());
    }
    private void goToMainActivity(){
        Intent intent = new Intent(WinnerActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
