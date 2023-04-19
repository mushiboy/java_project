package com.example.game233;

public interface gameListener {
    void livesLeftChanged(int liveLeft);

    void gameOver();

    void scoreChanged(int newScore);
}
