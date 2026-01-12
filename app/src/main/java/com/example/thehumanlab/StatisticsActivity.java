package com.example.thehumanlab;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.thehumanlab.databinding.ActivityStatisticsBinding;

public class StatisticsActivity extends AppCompatActivity {

    private ActivityStatisticsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityStatisticsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        loadScores();
    }

    private void loadScores() {
        HighScoreManager manager = new HighScoreManager(this);
        long reactionTime = manager.getReactionBest();
        if (reactionTime == 0) {
            binding.tvScoreReaction.setText("-");
        } else {
            binding.tvScoreReaction.setText(reactionTime + " ms");
        }

        int numberRounds = manager.getNumberBest();
        binding.tvScoreNumber.setText(String.valueOf(numberRounds));

        int pictureRounds = manager.getPictureBest();
        binding.tvScorePicture.setText(String.valueOf(pictureRounds));
    }
}