// This package declaration specifies the location of the class within the project
package com.example.asteroidsadvanced;

// Import the necessary JavaFX classes
import javafx.animation.Timeline;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Shape;

// Ship class extends the Character class and represents the player's spaceship
public class Ship extends Character {
    // Instance variables to store invincibility and hyperspace status
    private boolean isInvincible = false;
    private boolean isHyperspaced = false;

    // Create a Timeline instance to handle animations
    Timeline timeline = new Timeline();

    // Ship constructor with initial x and y position
    public Ship(int x, int y) {
        // Call the superclass constructor with the Polygon representing the ship's shape and its initial position
        super(new Polygon(0, 0, 50, 12.5, 0, 25, 12.5, 12.5), x, y);
    }

    // Setter for the invincibility status
    public void setInvincible(boolean invincible) {
        this.isInvincible = invincible;
    }

    // Getter for the invincibility status
    public boolean isInvincible() {
        return isInvincible;
    }

    // Method for initiating hyperspace jump
    public void Hyperspace() {
        // Set hyperspace status to true
        this.setHyperspaced(true);

        // Declare variables for new x and y position
        double newX;
        double newY;

        // Find new random position, ensuring it's not too close to the current position
        do {
            newX = Math.random() * (Controller.width - 100 - this.getCharacter().getBoundsInParent().getWidth()) + 50;
            newY = Math.random() * (Controller.height - 100 - this.getCharacter().getBoundsInParent().getHeight()) + 50;
        } while (Math.abs(newX - getCharacter().getTranslateX()) < 100 && Math.abs(newY - getCharacter().getTranslateY()) < 100);

        // Set the new position and reverse the ship's movement vector
        super.getCharacter().setTranslateX(newX);
        super.getCharacter().setTranslateY(newY);
        super.setMovement(-super.getMovement().getX(), -super.getMovement().getY());
    }

    // Method for checking if the ship collides with another Character object
    public boolean collide(Character other) {
        // Check for intersection between the ship's shape and the other Character's shape
        Shape collisionArea = Shape.intersect(super.getCharacter(), other.getCharacter());
        // Return true if there is an intersection
        return collisionArea.getBoundsInLocal().getWidth() != -1;
    }

    // Getter for the hyperspace status
    public boolean isHyperspaced() {
        return isHyperspaced;
    }

    // Setter for the hyperspace status
    public void setHyperspaced(boolean hyperspaced) {
        isHyperspaced = hyperspaced;
    }
}
