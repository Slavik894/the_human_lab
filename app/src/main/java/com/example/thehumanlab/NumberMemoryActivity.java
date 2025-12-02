package com.example.thehumanlab;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.thehumanlab.databinding.ActivityNumberMemoryBinding;

import java.util.Random;

public class NumberMemoryActivity extends AppCompatActivity {

    private ActivityNumberMemoryBinding binding;
    private int currentRound = 1;
    private String currentGeneratedNumber = "";
    private CountDownTimer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityNumberMemoryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        EdgeToEdge.enable(this);
        ViewCompat.setOnApplyWindowInsetsListener(binding.mainRoot, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        setupListeners();
        startRound();
    }

    private void setupListeners() {
        binding.btnSubmitAnswer.setOnClickListener(v -> checkAnswer());

        binding.btnHome.setOnClickListener(v -> {
            if (timer != null) timer.cancel();
//            Intent intent = new Intent(NumberMemoryActivity.this, MainActivity.class);
//            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
//            startActivity(intent);
            finish();
        });

    }

    private void startRound() {
        binding.layoutMemorize.setVisibility(View.VISIBLE);
        binding.layoutInput.setVisibility(View.GONE);
        binding.layoutResult.setVisibility(View.GONE);

        binding.mainRoot.setBackgroundColor(ContextCompat.getColor(this, R.color.nm_background_blue));

        currentGeneratedNumber = generateRandomNumber(currentRound);
        binding.tvNumberToMemorize.setText(currentGeneratedNumber);

        binding.etNumberInput.setText("");

        startMemorizeTimer();
    }

    private void startMemorizeTimer() {
        if (timer != null) timer.cancel();

        timer = new CountDownTimer(6000, 10) {
            @Override
            public void onTick(long millisUntilFinished) {
                int progress = (int) (millisUntilFinished * 100 / 6000);
                binding.progressBarMemorize.setProgress(progress);
            }

            @Override
            public void onFinish() {
                showInputScreen();
            }
        }.start();
    }

    private void showInputScreen() {
        binding.layoutMemorize.setVisibility(View.GONE);
        binding.layoutInput.setVisibility(View.VISIBLE);

        startInputTimer();
    }

    private void startInputTimer() {
        if (timer != null) timer.cancel();

        timer = new CountDownTimer(10000, 10) {
            @Override
            public void onTick(long millisUntilFinished) {
                int progress = (int) (millisUntilFinished * 100 / 10000);
                binding.progressBarInput.setProgress(progress);
            }

            @Override
            public void onFinish() {

                checkAnswer();
            }
        }.start();
    }

    private void checkAnswer() {
        if (timer != null) timer.cancel();

        String userAnswer = binding.etNumberInput.getText().toString();
        boolean isCorrect = userAnswer.equals(currentGeneratedNumber);

        showResultScreen(isCorrect, userAnswer);
    }

    private void showResultScreen(boolean isCorrect, String userAnswer) {
        binding.layoutInput.setVisibility(View.GONE);
        binding.layoutResult.setVisibility(View.VISIBLE);

        binding.tvRoundInfo.setText("Runda " + currentRound);
        binding.tvResultCorrectNumber.setText(currentGeneratedNumber);
        binding.tvResultUserNumber.setText(userAnswer.isEmpty() ? "-" : userAnswer);

        if (isCorrect) {

            binding.mainRoot.setBackgroundColor(ContextCompat.getColor(this, R.color.nm_success_green));

            binding.btnAction.setImageResource(R.drawable.next);
            binding.btnAction.setOnClickListener(v -> {
                currentRound++;
                startRound();
            });

        } else {
            binding.mainRoot.setBackgroundColor(ContextCompat.getColor(this, R.color.nm_fail_red));

            binding.btnAction.setImageResource(R.drawable.lose);
            binding.btnAction.setOnClickListener(v -> {
                currentRound = 1;
                startRound();
            });
        }
    }

    private String generateRandomNumber(int length) {
        Random random = new Random();
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < length; i++) {
            sb.append(random.nextInt(10));
        }
        return sb.toString();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (timer != null) timer.cancel();
    }
}