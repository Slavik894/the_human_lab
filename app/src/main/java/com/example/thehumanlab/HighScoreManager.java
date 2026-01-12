package com.example.thehumanlab;

import android.content.Context;
import android.content.SharedPreferences;

public class HighScoreManager {
    private static final String PREF_NAME = "HumanLabScores";
    private static final String KEY_REACTION = "BEST_REACTION_TIME";
    private static final String KEY_NUMBER = "BEST_NUMBER_MEMORY";
    private static final String KEY_PICTURE = "BEST_PICTURE_MEMORY";

    private final SharedPreferences preferences;

    public HighScoreManager(Context context) {
        preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }
    public void saveReactionScore(long time) {
        long currentBest = preferences.getLong(KEY_REACTION, 0);
        if (currentBest == 0 || time < currentBest) {
            preferences.edit().putLong(KEY_REACTION, time).apply();
        }
    }

    public long getReactionBest() {
        return preferences.getLong(KEY_REACTION, 0);
    }
    public void saveNumberScore(int round) {
        int currentBest = preferences.getInt(KEY_NUMBER, 0);
        if (round > currentBest) {
            preferences.edit().putInt(KEY_NUMBER, round).apply();
        }
    }
    public int getNumberBest() {
        return preferences.getInt(KEY_NUMBER, 0);
    }
    public void savePictureScore(int round) {
        int currentBest = preferences.getInt(KEY_PICTURE, 0);
        if (round > currentBest) {
            preferences.edit().putInt(KEY_PICTURE, round).apply();
        }
    }

    public int getPictureBest() {
        return preferences.getInt(KEY_PICTURE, 0);
    }
}