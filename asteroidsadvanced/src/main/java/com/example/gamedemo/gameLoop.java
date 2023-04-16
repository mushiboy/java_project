package com.example.gamedemo;

import javafx.fxml.FXML;
import javafx.scene.layout.Pane;

import java.util.ArrayList;
import java.util.List;

public class gameLoop {
    private gameListener gameListener1;
    @FXML
    Pane gamePane;
    private int score = 0, lives = 3;
    public static int Width = 800;
    public static int Height = 600;
    List<Bullets> bullets = new ArrayList<>();
    List<Bullets> bullets1 = new ArrayList<>();
    List<Asteroids> asteroids = new ArrayList<>();
    List<Alien> aliens = new ArrayList<>();
    boolean surprise = true;

    Ship ship = new Ship(Width / 2, Height / 2);

    public void initGame(gameListener gameListener1) {
        this.gameListener1 = gameListener1;

        gameListener1.scoreChanged(score);
        gameListener1.livesLeftChanged(lives);

    }

    public void Loop(long now) {

        bullets.forEach(bullet -> {
            asteroids.forEach(asteroid -> {
                if (asteroid.collide(bullet)) {
                    if (asteroid.getLevel() == 1) {
                        incrementScore(10);
                    }
                    if (asteroid.getLevel() != 1) {
                        double X = asteroid.getCharacter().getTranslateX();
                        double Y = asteroid.getCharacter().getTranslateY();
                        int Z = asteroid.getLevel();
                        Downgrade((int) X + 10, (int) Y + 10, Z);
                        Downgrade((int) X - 10, (int) Y - 10, Z);
                    }
                    gamePane.getChildren().remove(asteroid.getCharacter());
                    asteroids.remove(asteroid);
                    gamePane.getChildren().remove(bullet.getCharacter());
                    bullets.remove(bullet);
                }
            });
        });

        bullets.forEach(bullet -> {
            aliens.forEach(alien -> {
                if (alien.collide(bullet)) {
                    gamePane.getChildren().remove(alien.getCharacter());
                    aliens.remove(alien);
                    gamePane.getChildren().remove(bullet.getCharacter());
                    bullets.remove(bullet);
                    bullets1.forEach(bullet1 -> {
                        gamePane.getChildren().remove(bullet1.getCharacter());
                    });
                    surprise = false;
                    bullets1.clear();
                    incrementScore(100);
                }
            });
        });
    }

    public int getScore() {
        return score;
    }

//    public int getLives() {
//        return lives;
//    }
    public boolean isGameOver() {
        return lives == 0;
    }


    public void Downgrade(int x,int y,int z){
        Asteroids asteroid = new Asteroids(x,y,z-1);
        asteroids.add(asteroid);
        gamePane.getChildren().add(asteroid.getCharacter());

    }

    private void incrementScore(int score) {
        gameListener1.scoreChanged(this.score += score);
    }





}
