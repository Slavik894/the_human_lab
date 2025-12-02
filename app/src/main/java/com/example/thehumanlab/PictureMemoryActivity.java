package com.example.thehumanlab;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.thehumanlab.databinding.ActivityPictureMemoryBinding;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PictureMemoryActivity extends AppCompatActivity {

    private ActivityPictureMemoryBinding binding;
    private int currentRound = 1;
    private CountDownTimer timer;

    // Logika gry
    private List<Integer> cardIcons = new ArrayList<>();
    private ImageView firstCard = null;
    private ImageView secondCard = null;
    private int firstCardId = 0;
    private int secondCardId = 0;
    private boolean isBusy = false;
    private int pairsFound = 0;
    private int totalPairs = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPictureMemoryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setupListeners();
        startRound();
    }

    private void setupListeners() {
        binding.btnHome.setOnClickListener(v -> {
            if (timer != null) timer.cancel();
            finish();
        });

        binding.btnRetry.setOnClickListener(v -> {

            if (binding.tvResultMessage.getText().toString().equals("Sukces!")) {
                currentRound++;
            } else {
                currentRound = 1;
            }
            startRound();
        });
    }

    private void startRound() {

        binding.layoutGame.setVisibility(View.VISIBLE);
        binding.layoutResult.setVisibility(View.GONE);
        binding.gridCards.removeAllViews();
        binding.mainRoot.setBackgroundColor(ContextCompat.getColor(this, R.color.nm_background_blue));

        firstCard = null;
        isBusy = false;
        pairsFound = 0;

        int columns, rows, timeSeconds;

        if (currentRound == 1) {
            columns = 3; rows = 2; timeSeconds = 15;
        } else if (currentRound == 2) {
            columns = 3; rows = 4; timeSeconds = 30;
        } else {
            columns = 4; rows = 4; timeSeconds = 45;
        }

        totalPairs = (columns * rows) / 2;
        binding.gridCards.setColumnCount(columns);
        binding.gridCards.setRowCount(rows);


        generateCards(totalPairs);

        startTimer(timeSeconds * 1000L);
    }

    private void generateCards(int pairsCount) {
        cardIcons.clear();

        int[] images = {
                android.R.drawable.ic_menu_camera,
                android.R.drawable.ic_menu_call,
                android.R.drawable.ic_menu_compass,
                android.R.drawable.ic_menu_day,
                android.R.drawable.ic_menu_gallery,
                android.R.drawable.ic_menu_mapmode,
                android.R.drawable.ic_menu_my_calendar,
                android.R.drawable.ic_menu_share
        };

        for (int i = 0; i < pairsCount; i++) {
            cardIcons.add(images[i % images.length]);
            cardIcons.add(images[i % images.length]);
        }
        Collections.shuffle(cardIcons);

        for (int i = 0; i < cardIcons.size(); i++) {
            ImageView card = new ImageView(this);

            GridLayout.LayoutParams params = new GridLayout.LayoutParams();
            params.width = 180;
            params.height = 180;
            params.setMargins(10, 10, 10, 10);
            card.setLayoutParams(params);

            card.setBackgroundColor(Color.parseColor("#3700B3"));
            card.setTag(i);
            card.setPadding(30,30,30,30);

            final int position = i;
            card.setOnClickListener(v -> onCardClicked((ImageView) v, position));

            binding.gridCards.addView(card);
        }
    }

    private void startTimer(long duration) {
        if (timer != null) timer.cancel();
        timer = new CountDownTimer(duration, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                binding.tvTimer.setText((millisUntilFinished / 1000) + "s");
            }

            @Override
            public void onFinish() {
                endGame(false);
            }
        }.start();
    }

    private void onCardClicked(ImageView card, int position) {
        if (isBusy || card == firstCard || card.getDrawable() != null) return;

        int imageId = cardIcons.get(position);
        card.setImageResource(imageId);
        card.setBackgroundColor(Color.parseColor("#4FC3F7"));

        if (firstCard == null) {
            firstCard = card;
            firstCardId = imageId;
        } else {
            secondCard = card;
            secondCardId = imageId;
            isBusy = true;

            checkMatch();
        }
    }

    private void checkMatch() {
        if (firstCardId == secondCardId) {
            pairsFound++;
            firstCard = null;
            isBusy = false;

            if (pairsFound == totalPairs) {
                if (timer != null) timer.cancel();
                endGame(true);
            }
        } else {
            new Handler().postDelayed(() -> {
                firstCard.setImageDrawable(null);
                firstCard.setBackgroundColor(Color.parseColor("#3700B3"));
                secondCard.setImageDrawable(null);
                secondCard.setBackgroundColor(Color.parseColor("#3700B3"));
                firstCard = null;
                isBusy = false;
            }, 800);
        }
    }

    private void endGame(boolean success) {
        binding.layoutGame.setVisibility(View.GONE);
        binding.layoutResult.setVisibility(View.VISIBLE);

        if (success) {
            binding.mainRoot.setBackgroundColor(ContextCompat.getColor(this, R.color.nm_success_green));
            binding.tvResultMessage.setText("Sukces!");
            binding.tvScoreInfo.setText("Ukończono rundę " + currentRound);
            binding.btnRetry.setImageResource(R.drawable.next);
        } else {
            binding.mainRoot.setBackgroundColor(ContextCompat.getColor(this, R.color.nm_fail_red));
            binding.tvResultMessage.setText("Porażka");
            binding.tvScoreInfo.setText("Zabrakło czasu w rundzie " + currentRound);
            binding.btnRetry.setImageResource(R.drawable.lose);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (timer != null) timer.cancel();
    }
}