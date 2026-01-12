package com.example.thehumanlab;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.thehumanlab.databinding.ActivityReactionTimeBinding;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ReactionTime extends AppCompatActivity {

    private ActivityReactionTimeBinding binding;

    private static final int TOTAL_ROUNDS = 5;

    private int currentRound = 0;
    private boolean canTap = false;
    private long roundStartTime = 0L;
    private CountDownTimer roundTimer;
    private final List<Long> reactionTimes = new ArrayList<>();
    private static final long penalty_delay = 1500;
    private boolean penalty = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityReactionTimeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        EdgeToEdge.enable(this);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        binding.btnHome.setOnClickListener(v ->
                startActivity(new Intent(this, MainActivity.class)));

        binding.btnAction.setOnClickListener(v ->
                startActivity(new Intent(this, SelectGameActivity.class)));

        binding.layoutReactiontimeRound.setOnClickListener(v -> onScreenTapped());

        startRound();
    }
    private void startRound() {
        canTap = false;

        binding.layoutReactiontimeProgress.setVisibility(View.GONE);
        binding.layoutReactiontimeRound.setVisibility(View.VISIBLE);
        binding.layoutReactiontimeEndofround.setVisibility(View.GONE);
        binding.layoutReactiontimeResults.setVisibility(View.GONE);

        binding.layoutReactiontimeRound.setBackgroundColor(
                getColor(R.color.nm_background_blue)
        );
        binding.tvRound.setText("Czekaj...");

        startRandomTimer();
    }

    private void startRandomTimer() {
        if (roundTimer != null) roundTimer.cancel();

        long delay = 2000 + new Random().nextInt(3000);

        roundTimer = new CountDownTimer(delay, delay) {
            @Override public void onTick(long millisUntilFinished) {}

            @Override
            public void onFinish() {
                enableTap();
            }
        }.start();
    }

    private void enableTap() {
        canTap = true;
        roundStartTime = System.currentTimeMillis();

        binding.layoutReactiontimeRound.setBackgroundColor(
                getColor(R.color.nm_success_green)
        );
        binding.tvRound.setText("TERAZ!");
    }

    private void onScreenTapped() {
        if (!canTap) {
            applyPenalty();
            return;
        }

        long reactionTime = System.currentTimeMillis() - roundStartTime;
        reactionTimes.add(reactionTime);

        showRoundResult(reactionTime);
    }

    private void applyPenalty() {
        if(penalty) return;

        penalty=true;
        canTap=false;

        if (roundTimer != null) roundTimer.cancel();

        binding.layoutReactiontimeRound.setBackgroundColor(
                getColor(R.color.nm_fail_red)
        );
        binding.tvRound.setText("Za wcześnie!");

        binding.layoutReactiontimeRound.postDelayed(() -> {
            penalty = false;
            startRound();
        }, penalty_delay);
    }

    private void showRoundResult(long reactionTime) {
        canTap = false;
        currentRound++;

        binding.layoutReactiontimeRound.setVisibility(View.GONE);
        binding.layoutReactiontimeEndofround.setVisibility(View.VISIBLE);
        binding.tvRoundTime.setText(reactionTime + " ms");

        binding.layoutReactiontimeEndofround.postDelayed(() -> {
            if (currentRound < TOTAL_ROUNDS) {
                startRound();
            } else {
                showFinalResults();
            }
        }, 2000);
    }

    private void showFinalResults() {
        long sum = 0;
        for (long time : reactionTimes) sum += time;

        long average = sum / reactionTimes.size();

        HighScoreManager scoreManager = new HighScoreManager(this);
        scoreManager.saveReactionScore(average);

        binding.layoutReactiontimeEndofround.setVisibility(View.GONE);
        binding.layoutReactiontimeResults.setVisibility(View.VISIBLE);
        binding.tvReactiontime.setText(average + " ms");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (roundTimer != null) roundTimer.cancel();
    }
}
