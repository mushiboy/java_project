// Import necessary classes
package com.example.asteroidsadvanced;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.PathTransition;
import javafx.animation.Timeline;
import javafx.scene.shape.*;
import javafx.util.Duration;

// Create a class Alien that extends from Character
public class Alien extends Character{

    // Create a new timeline
    Timeline timeline = new Timeline();
    int X = 0;
    int Y = 0;

    // Constructor that takes x and y coordinates as parameters
    public Alien(int x, int y) {
    
        // Call the constructor of the Character class
        super(new Polygon( 130, 50, 110, 80, 0, 80, -20, 50, 20, 50, 30, 30, 80, 30, 90,50), x, y);
    
        // Set the movement of the alien
        super.setMovement(0.1,0.1);
        this.X = x;
        this.Y = y;
    }

    // Override the move method of the Character class
    @Override
    public void move() {
    
        // Add keyframes to the timeline to move the alien on a path
        timeline.getKeyFrames().add(new KeyFrame(Duration.seconds(1), new KeyValue(super.getCharacter().translateXProperty(), (150+X)), new KeyValue(super.getCharacter().translateYProperty(), (100+Y))));
        timeline.getKeyFrames().add(new KeyFrame(Duration.seconds(2), new KeyValue(super.getCharacter().translateXProperty(), (450+X)), new KeyValue(super.getCharacter().translateYProperty(), (100+Y))));
        timeline.getKeyFrames().add(new KeyFrame(Duration.seconds(3), new KeyValue(super.getCharacter().translateXProperty(), (600+X)), new KeyValue(super.getCharacter().translateYProperty(), Y)));
        timeline.getKeyFrames().add(new KeyFrame(Duration.seconds(4), new KeyValue(super.getCharacter().translateXProperty(), (850+X)), new KeyValue(super.getCharacter().translateYProperty(), Y)));
        timeline.getKeyFrames().add(new KeyFrame(Duration.seconds(5), new KeyValue(super.getCharacter().translateXProperty(), (1150+X)), new KeyValue(super.getCharacter().translateYProperty(), (100+Y))));
        timeline.getKeyFrames().add(new KeyFrame(Duration.seconds(6), new KeyValue(super.getCharacter().translateXProperty(), (1450+X)), new KeyValue(super.getCharacter().translateYProperty(), (100+Y))));
        timeline.getKeyFrames().add(new KeyFrame(Duration.seconds(7), new KeyValue(super.getCharacter().translateXProperty(), (1600+X)), new KeyValue(super.getCharacter().translateYProperty(), Y)));
        timeline.getKeyFrames().add(new KeyFrame(Duration.seconds(8), new KeyValue(super.getCharacter().translateXProperty(), (1800+X)), new KeyValue(super.getCharacter().translateYProperty(), Y)));
    
        // Set the timeline to auto-reverse
        timeline.setAutoReverse(true);
    
        // Set the cycle count of the timeline to indefinite
        timeline.setCycleCount(Timeline.INDEFINITE);
    
        // Play the timeline
        timeline.play();
    }

    // Method to check collision with bullets
    public boolean collide(Bullets other){
    
        // Get the shape of the intersection of the alien and the bullets
        Shape collisionArea = Shape.intersect(super.getCharacter(), other.getCharacter());
    
        // Return true if the width of the bounds of the collision area is not -1
        return collisionArea.getBoundsInLocal().getWidth() != -1;
    }
}
