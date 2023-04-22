package com.example.asteroidsadvanced;

import javafx.scene.shape.Polygon;

// Bullet class that represents projectiles fired by the player's spaceship
public class Bullet extends Character {

    // Constructor method for the Bullet class, taking two integers as arguments for the initial position
    public Bullet(int x, int y) {
        // Create a new Polygon object with 4 points and pass it to the parent Character class constructor
        // along with the x and y arguments (offset by 50 and 30 respectively for proper positioning)
        super(new Polygon(5, -5, 5, 5, -5, 5, -5, -5), x + 50, y + 30);
    }

    // Move method for the Bullet class that updates the Bullet object's position based on its current movement values
    @Override
    public void move() {
        // Set the new x and y position of the Bullet object based on its current position and movement values
        super.getCharacter().setTranslateX(super.getCharacter().getTranslateX() + super.getMovement().getX());
        super.getCharacter().setTranslateY(super.getCharacter().getTranslateY() + super.getMovement().getY());
    }

    // Acceleration method for the Bullet class that modifies the movement values of the Bullet object
    @Override
    public void acc() {
        // Call the parent Character class acceleration method to increment the current movement value
        super.acc();

        // Calculate the new x and y movement values based on the current angle of the Bullet object
        double angle = Math.toRadians(super.getCharacter().getRotate());
        double X = Math.cos(angle) * 5;
        double Y = Math.sin(angle) * 5;

        // Set the new x and y movement values using the parent Character class setMovement method
        super.setMovement(X, Y);
    }
}
