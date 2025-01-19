package com.example.guessthesong;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageButton helpButton = findViewById(R.id.helpButton);
        helpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showRulesDialogFragment();
            }
        });

        // Логика для перехода на экран торга
        Button startGameButton = findViewById(R.id.startGameButton);
        startGameButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, BiddingActivity.class);
            startActivity(intent);
        });
    }

    // Диалоговое окно с правилами игры
    private void showRulesDialogFragment() {
        DialogFragment dialogFragment = new RulesDialogFragment();
        dialogFragment.show(getSupportFragmentManager(), "RulesDialogFragment");
    }
}
