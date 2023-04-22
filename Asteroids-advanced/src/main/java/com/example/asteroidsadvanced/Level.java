package com.example.asteroidsadvanced;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

// The Level class represents a game level with a specific number of different enemy types
public class Level {
    // Instance variables for the level number and the number of each enemy type
    private int levelNumber;

    private int numSize1Asteroids;
    private int numSize2Asteroids;
    private int numSize3Asteroids;
    private int numAliens;

    // A list to store all the enemy objects in the level
    private List<Character> enemyList;

    // Random number generator to generate enemy positions
    private Random rnd = new Random();

    // Static method to create an array of predefined levels
    public static Level[] createLevels() {
        Level[] levels = new Level[5];

        // Define the enemy composition for each level
        levels[0] = new Level(1, 1, 0, 0, 0);
        levels[1] = new Level(2, 2, 1, 0, 0);
        levels[2] = new Level(3, 2, 1, 1, 0);
        levels[3] = new Level(4, 2, 2, 2, 0);
        levels[4] = new Level(5, 2, 2, 2, 1);

        return levels;
    }

    // Constructor that initializes a level with the specified number of each enemy type
    public Level(int levelNumber, int numSize1Asteroids, int numSize2Asteroids, int numSize3Asteroids, int numAliens) {
        this.levelNumber = levelNumber;
        this.numSize1Asteroids = numSize1Asteroids;
        this.numSize2Asteroids = numSize2Asteroids;
        this.numSize3Asteroids = numSize3Asteroids;
        this.numAliens = numAliens;

        // Initialize the enemy list and add the enemies according to the specified numbers
        this.enemyList = new ArrayList<>();

        // Add size 1 asteroids
        for (int i = 0; i < numSize1Asteroids; i++) {
            Asteroid asteroid = new Asteroid(rnd.nextInt(Controller.width - 100), rnd.nextInt(Controller.height - 100), 1);
            this.enemyList.add(asteroid);
        }

        // Add size 2 asteroids
        for (int i = 0; i < numSize2Asteroids; i++) {
            Asteroid asteroid = new Asteroid(rnd.nextInt(Controller.width - 100), rnd.nextInt(Controller.height - 100), 2);
            this.enemyList.add(asteroid);
        }

        // Add size 3 asteroids
        for (int i = 0; i < numSize3Asteroids; i++) {
            Asteroid asteroid = new Asteroid(rnd.nextInt(Controller.width - 100), rnd.nextInt(Controller.height - 100), 3);
            this.enemyList.add(asteroid);
        }

        // Add aliens
        for (int i = 0; i < numAliens; i++) {
            Alien alien = new Alien(0, rnd.nextInt(Controller.height - 100));
            this.enemyList.add(alien);
        }
    }

    // Getter method for enemyList.
    public List<Character> getEnemyList() {
        return enemyList;
    }

    //Getter and setter methods for level number.
    public int getLevelNumber() {
        return levelNumber;
    }
    public void setLevelNumber(int levelNumber) {
        this.levelNumber = levelNumber;
    }

    // Getter and setter methods for size 1 asteroids.
    public int getNumSize1Asteroids() {
        return numSize1Asteroids;
    }
    public void setNumSize1Asteroids(int numSize1Asteroids) {
        this.numSize1Asteroids = numSize1Asteroids;
    }

    // Getter and setter methods for size 2 asteroids.
    public int getNumSize2Asteroids() {
        return numSize2Asteroids;
    }
    public void setNumSize2Asteroids(int numSize2Asteroids) {
        this.numSize2Asteroids = numSize2Asteroids;
    }

    // Getter and setter methods for size 3 asteroids.
    public int getNumSize3Asteroids() {
        return numSize3Asteroids;
    }
    public void setNumSize3Asteroids(int numSize3Asteroids) {
        this.numSize3Asteroids = numSize3Asteroids;
    }

    // Getter and setter methods for aliens.
    public int getNumAliens() {
        return numAliens;
    }
    public void setNumAliens(int numAliens) {
        this.numAliens = numAliens;
    }
}