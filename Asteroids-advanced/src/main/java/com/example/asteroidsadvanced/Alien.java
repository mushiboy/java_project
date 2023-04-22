package com.example.asteroidsadvanced;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.shape.*;
import javafx.util.Duration;

public class Alien extends Character {
    Timeline timeline = new Timeline();
    int X = 0;
    int Y = 0;
    private int size = 4;

    public Alien(int x, int y) {
        super(new Polygon(130, 50, 110, 80, 0, 80, -20, 50, 20, 50, 30, 30, 80, 30, 90, 50), x, y);
        super.setMovement(0.1, 0.1);
        this.X = x;
        this.Y = y;
    }

    @Override
    public void move() {
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
        timeline.setAutoReverse(true);
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    public int getSize() {
        return this.size;
    }
}
