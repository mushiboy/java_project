package com.example.asteroidsadvanced;

import javafx.scene.shape.Polygon;
import javafx.scene.shape.Shape;
import java.util.Random;

// Asteroid class extends the Character class and represents an Asteroid object in the game
public class Asteroid extends Character {
    private double rotationalMovement; // Rotational movement value for the asteroid
    private int size; // Size of the asteroid
    int accelerationAmount; // Amount of acceleration for the asteroid

    // Constructor for Asteroid class that takes initial x, y positions and size
    public Asteroid(int x, int y, int z) {
        // Call the superclass constructor with the shape of the asteroid and initial positions
        super(new Polygon(25.0 * z, 0.0, 50.0 * z, 15.0 * z, 40.0 * z, 40.0 * z, 10.0 * z, 40.0 * z, 0.0, 15.0 * z), x, y);
        this.size = z;
        // Set the initial movement of the asteroid
        super.setMovement(0.1, 0.1);
        // Generate a random number for the initial rotation
        Random rnd = new Random();
        super.getCharacter().setRotate(rnd.nextInt(360));

        // Set accelerationAmount based on the size of the asteroid
        if (z == 3) {
            this.accelerationAmount = 1 + rnd.nextInt(10);
        }
        if (z == 2) {
            this.accelerationAmount = 1 + rnd.nextInt(20, 30);
        }
        if (z == 1) {
            this.accelerationAmount = 1 + rnd.nextInt(40, 70);
        }

        // Apply the acceleration
        for (int i = 0; i < this.accelerationAmount; i++) {
            acc();
        }

        // Set the rotational movement for the asteroid
        this.rotationalMovement = 0.5 - rnd.nextDouble();
    }

    // Override the move() method from the Character class to provide custom movement for the Asteroid
    @Override
    public void move() {
        super.move();
        // Update the asteroid's rotation based on its rotational movement value
        super.getCharacter().setRotate(super.getCharacter().getRotate() + this.rotationalMovement);
    }

    // Getter method for the Asteroid's size
    public int getSize() {
        return this.size;
    }

    // Method to check if the Asteroid collides with another Character object
    public boolean collide(Character other) {
        // Calculate the intersection area between the Asteroid and the other Character
        Shape collisionArea = Shape.intersect(super.getCharacter(), other.getCharacter());
        // Return true if the intersection width is not -1, which means there is a collision
        return collisionArea.getBoundsInLocal().getWidth() != -1;
    }
}