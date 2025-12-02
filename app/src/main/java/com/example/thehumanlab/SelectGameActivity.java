package com.example.thehumanlab;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.thehumanlab.databinding.ActivityMainBinding;
import com.example.thehumanlab.databinding.ActivitySelectGameBinding;

public class SelectGameActivity extends AppCompatActivity {

    private ActivitySelectGameBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySelectGameBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        EdgeToEdge.enable(this);
        binding.numbersGameBtn.setOnClickListener(view -> {
            startActivity(new Intent(this, NumberMemoryActivity.class));
        });
        binding.cardsGameBtn.setOnClickListener(view -> {
            startActivity(new Intent(this, PictureMemoryActivity.class));
        });

    }
}