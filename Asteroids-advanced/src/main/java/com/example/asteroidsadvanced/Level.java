package com.example.asteroidsadvanced;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Level {
    private int levelNumber;

    private int numSize1Asteroids;
    private int numSize2Asteroids;
    private int numSize3Asteroids;
    private int numAliens;

    private List<Character> enemyList;

    private Random rnd = new Random();

    public static Level[] createLevels() {
        Level[] levels = new Level[14];
    
        levels[0] = new Level(1, 1, 1, 1, 0);
        levels[1] = new Level(2, 0, 0, 0, 1);
        levels[2] = new Level(3, 2, 1, 0, 0);
        levels[3] = new Level(4, 0, 2, 0, 0);
        levels[4] = new Level(5, 2, 3, 0, 0);
        levels[5] = new Level(6, 0, 4, 0, 1);
        levels[6] = new Level(7, 2, 2, 1, 0);
        levels[7] = new Level(8, 3, 3, 1, 0);
        levels[8] = new Level(9, 4, 3, 2, 0);
        levels[9] = new Level(10, 4, 2, 1, 2);

        return levels;
    }

    public Level(int levelNumber, int numSize1Asteroids, int numSize2Asteroids, int numSize3Asteroids, int numAliens) {
        this.levelNumber = levelNumber;
        this.numSize1Asteroids = numSize1Asteroids;
        this.numSize2Asteroids = numSize2Asteroids;
        this.numSize3Asteroids = numSize3Asteroids;
        this.numAliens = numAliens;

        this.enemyList = new ArrayList<>();

        for (int i = 0; i < numSize1Asteroids; i++) {
            Asteroid asteroid = new Asteroid(rnd.nextInt(1000), rnd.nextInt(1000), 1);
            this.enemyList.add(asteroid);
        }

        for (int i = 0; i < numSize2Asteroids; i++) {
            Asteroid asteroid = new Asteroid(rnd.nextInt(1000), rnd.nextInt(1000), 2);
            this.enemyList.add(asteroid);
        }

        for (int i = 0; i < numSize3Asteroids; i++) {
            Asteroid asteroid = new Asteroid(rnd.nextInt(1000), rnd.nextInt(1000), 3);
            this.enemyList.add(asteroid);
        }

        for (int i = 0; i < numAliens; i++) {
            Alien alien = new Alien(rnd.nextInt(1000), rnd.nextInt(1000));
            this.enemyList.add(alien);
        }
    }

    public List<Character> getEnemyList() {
        return enemyList;
    }

    public int getLevelNumber() {
        return levelNumber;
    }

    public void setLevelNumber(int levelNumber) {
        this.levelNumber = levelNumber;
    }

    public int getNumSize1Asteroids() {
        return numSize1Asteroids;
    }

    public void setNumSize1Asteroids(int numSize1Asteroids) {
        this.numSize1Asteroids = numSize1Asteroids;
    }

    public int getNumSize2Asteroids() {
        return numSize2Asteroids;
    }

    public void setNumSize2Asteroids(int numSize2Asteroids) {
        this.numSize2Asteroids = numSize2Asteroids;
    }

    public int getNumSize3Asteroids() {
        return numSize3Asteroids;
    }

    public void setNumSize3Asteroids(int numSize3Asteroids) {
        this.numSize3Asteroids = numSize3Asteroids;
    }

    public int getNumAliens() {
        return numAliens;
    }

    public void setNumAliens(int numAliens) {
        this.numAliens = numAliens;
    }
}