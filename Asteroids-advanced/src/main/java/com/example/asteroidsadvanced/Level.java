package com.example.asteroidsadvanced;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Level {
    private int levelNumber; // The level number

    private int numSize1Asteroids; // The number of size 1 asteroids in the level
    private int numSize2Asteroids; // The number of size 2 asteroids in the level
    private int numSize3Asteroids; // The number of size 3 asteroids in the level
    private int numAliens; // The number of aliens in the level.

    private List<Asteroids> asteroidsList; // A list of asteroids in the level
    private List<Alien> alienList; // A list of aliens in the level

    private Random rnd = new Random(); // A random number generator for generating asteroid positions

    public static Level[] createLevels() {
        Level[] levels = new Level[3];
    
        // create Level 1 instance with 1 size 1 asteroid
        levels[0] = new Level(1, 1, 0, 0, 0);
    
        // create Level 2 instance with 2 size 1 asteroids
        levels[1] = new Level(2, 2, 0, 0, 0);
    
        // create Level 3 instance with 3 size 1 asteroids
        levels[2] = new Level(3, 3, 0, 0, 0);
    
        return levels;
    }

    // Constructor for creating a level
    public Level(int levelNumber, int numSize1Asteroids, int numSize2Asteroids, int numSize3Asteroids, int numAliens) {
        this.levelNumber = levelNumber;
        this.numSize1Asteroids = numSize1Asteroids;
        this.numSize2Asteroids = numSize2Asteroids;
        this.numSize3Asteroids = numSize3Asteroids;
        this.numAliens = numAliens;

        // Create two new ArrayList to store the asteroids and the aliens in the level
        this.asteroidsList = new ArrayList<>();
        this.alienList = new ArrayList<>();

        // Add the specified number of size 1 asteroids to the level
        for (int i = 0; i < numSize1Asteroids; i++) {
            // Create a new asteroid with a random position and size 1, and add it to the asteroids list
            Asteroids asteroid = new Asteroids(rnd.nextInt(1000), rnd.nextInt(1000), 1);
            this.asteroidsList.add(asteroid);
        }

        // Add the specified number of size 2 asteroids to the level
        for (int i = 0; i < numSize2Asteroids; i++) {
            // Create a new asteroid with a random position and size 2, and add it to the asteroids list
            Asteroids asteroid = new Asteroids(rnd.nextInt(1000), rnd.nextInt(1000), 2);
            this.asteroidsList.add(asteroid);
        }

        // Add the specified number of size 3 asteroids to the level
        for (int i = 0; i < numSize3Asteroids; i++) {
            // Create a new asteroid with a random position and size 3, and add it to the asteroids list
            Asteroids asteroid = new Asteroids(rnd.nextInt(1000), rnd.nextInt(1000), 3);
            this.asteroidsList.add(asteroid);
        }

        // Add the specified number of alien ships to the level
        for (int i = 0; i < numAliens; i++) {
            // Create a new alien with a random position and add it to the aliens list
            Alien alien = new Alien(rnd.nextInt(1000), rnd.nextInt(1000));
            this.alienList.add(alien);
        }
    }

    // Returns the list of asteroids in the current level
    public List<Asteroids> getCurrentLevelAsteroids() {
        return asteroidsList;
    }

    // Returns the list of aliens in the current level
    public List<Alien> getCurrentLevelAliens() {
        return alienList;
    }

    // Generates the list of asteroids for the next level
    public List<Asteroids> getNextLevelAsteroids() {
        // Calculate the number of asteroids for the next level based on the current level number and the number of asteroids in the current level
        int nextLevel = levelNumber + 1;
        int numSize1Asteroids = (int) (this.numSize1Asteroids * (1 + (nextLevel * 0.25)));
        int numSize2Asteroids = (int) (this.numSize2Asteroids * (1 + (nextLevel * 0.25)));
        int numSize3Asteroids = (int) (this.numSize3Asteroids * (1 + (nextLevel * 0.25)));

        // Create a new ArrayList to store the asteroids for the next level
        List<Asteroids> nextLevelAsteroidsList = new ArrayList<>();

        // Add the specified number of size 1 asteroids to the next level
        for (int i = 0; i < numSize1Asteroids; i++) {
            Asteroids asteroid = new Asteroids(rnd.nextInt(1000), rnd.nextInt(1000), 1);
            nextLevelAsteroidsList.add(asteroid);
        }

        // Add the specified number of size 2 asteroids to the next level
        for (int i = 0; i < numSize2Asteroids; i++) {
            Asteroids asteroid = new Asteroids(rnd.nextInt(1000), rnd.nextInt(1000), 2);
            nextLevelAsteroidsList.add(asteroid);
        }

        // Add the specified number of size 3 asteroids to the next level
        for (int i = 0; i < numSize3Asteroids; i++) {
            Asteroids asteroid = new Asteroids(rnd.nextInt(1000), rnd.nextInt(1000), 3);
            nextLevelAsteroidsList.add(asteroid);
        }

        return nextLevelAsteroidsList;
    }

    public List<Alien> getNextLevelAliens() {
        int nextLevel = levelNumber + 1;
        int numAliens = (int) (this.numAliens * (1 + (nextLevel * 0.25)));

        List<Alien> nextLevelAliensList = new ArrayList<>();

        // Add the specified number of alien ships to the level
        for (int i = 0; i < numAliens; i++) {
            Alien alien = new Alien(rnd.nextInt(1000), rnd.nextInt(1000));
            nextLevelAliensList.add(alien);
            }

        return nextLevelAliensList;
    }

    // This method returns the level number of the Level object it is called on.
    public int getLevelNumber() {
        return levelNumber;
    }

    // This method sets the level number of the Level object it is called on to the specified value.
    public void setLevelNumber(int levelNumber) {
        this.levelNumber = levelNumber;
    }

    // This method returns the number of size 1 asteroids in the Level object it is called on.
    public int getNumSize1Asteroids() {
        return numSize1Asteroids;
    }

    // This method sets the number of size 1 asteroids in the Level object it is called on to the specified value.
    public void setNumSize1Asteroids(int numSize1Asteroids) {
        this.numSize1Asteroids = numSize1Asteroids;
    }

    // This method returns the number of size 2 asteroids in the Level object it is called on.
    public int getNumSize2Asteroids() {
        return numSize2Asteroids;
    }

    // This method sets the number of size 2 asteroids in the Level object it is called on to the specified value.
    public void setNumSize2Asteroids(int numSize2Asteroids) {
        this.numSize2Asteroids = numSize2Asteroids;
    }

    // This method returns the number of size 3 asteroids in the Level object it is called on.
    public int getNumSize3Asteroids() {
        return numSize3Asteroids;
    }

    // This method sets the number of size 3 asteroids in the Level object it is called on to the specified value.
    public void setNumSize3Asteroids(int numSize3Asteroids) {
        this.numSize3Asteroids = numSize3Asteroids;
    }

    // This method returns the number of size 3 asteroids in the Level object it is called on.
    public int getNumAliens() {
        return numAliens;
        }
    
    // This method sets the number of size 3 asteroids in the Level object it is called on to the specified value.
    public void setNumAliens(int numAliens) {
        this.numAliens = numAliens;
        }
}