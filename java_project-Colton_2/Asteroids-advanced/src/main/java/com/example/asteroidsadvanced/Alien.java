package com.example.asteroidsadvanced;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.shape.*;
import javafx.util.Duration;

// Alien class extends the Character class and represents an Alien character in the game
public class Alien extends Character {
    // Create a Timeline object to handle animation for the Alien
    Timeline timeline = new Timeline();
    int X = 0; // Initial X position
    int Y = 0; // Initial Y position
    private int size = 4; // Size of the Alien

    // Constructor for Alien class that takes initial x and y positions
    public Alien(int x, int y) {
        // Call the superclass constructor with the shape of the alien and initial positions
        super(new Polygon(130, 50, 110, 80, 0, 80, -20, 50, 20, 50, 30, 30, 80, 30, 90, 50), x, y);
        // Set the initial movement of the alien
        super.setMovement(0.1, 0.1);
        this.X = x;
        this.Y = y;
    }

    // Override the move() method from the Character class to provide custom animation for the Alien
    @Override
    public void move() {
        // Create KeyFrames for the Timeline object to define the alien's movement pattern
        timeline.getKeyFrames().addAll(
                new KeyFrame(Duration.seconds(1), new KeyValue(super.getCharacter().translateXProperty(), (Controller.width * 0.1 + X)), new KeyValue(super.getCharacter().translateYProperty(), (100 + Y))),
                new KeyFrame(Duration.seconds(2), new KeyValue(super.getCharacter().translateXProperty(), (Controller.width * 0.3 + X)), new KeyValue(super.getCharacter().translateYProperty(), (100 + Y))),
                new KeyFrame(Duration.seconds(3), new KeyValue(super.getCharacter().translateXProperty(), (Controller.width * 0.4 + X)), new KeyValue(super.getCharacter().translateYProperty(), Y)),
                new KeyFrame(Duration.seconds(4), new KeyValue(super.getCharacter().translateXProperty(), (Controller.width * 0.6 + X)), new KeyValue(super.getCharacter().translateYProperty(), Y)),
                new KeyFrame(Duration.seconds(5), new KeyValue(super.getCharacter().translateXProperty(), (Controller.width * 0.8 + X)), new KeyValue(super.getCharacter().translateYProperty(), (100 + Y))),
                new KeyFrame(Duration.seconds(6), new KeyValue(super.getCharacter().translateXProperty(), (Controller.width * 0.9 + X)), new KeyValue(super.getCharacter().translateYProperty(), (100 + Y))),
                new KeyFrame(Duration.seconds(7), new KeyValue(super.getCharacter().translateXProperty(), (Controller.width * 0.95 + X)), new KeyValue(super.getCharacter().translateYProperty(), Y)),
                new KeyFrame(Duration.seconds(8), new KeyValue(super.getCharacter().translateXProperty(), (Controller.width * 1.0 + X)), new KeyValue(super.getCharacter().translateYProperty(), Y))
        );
        // Set the Timeline to auto-reverse, which means it will play in reverse after completing the forward animation
        timeline.setAutoReverse(true);
        // Set the Timeline to repeat indefinitely
        timeline.setCycleCount(Timeline.INDEFINITE);
        // Start the Timeline animation
        timeline.play();
    }

    // Getter method for the Alien's size
    public int getSize() {
        return this.size;
    }
}