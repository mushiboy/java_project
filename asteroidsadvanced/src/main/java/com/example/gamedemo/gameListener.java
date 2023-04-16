package com.example.gamedemo;

public interface gameListener {

    public void livesLeftChanged(int livedLeft);

    public void gameOver();

    public void scoreChanged(int newScore);

}
