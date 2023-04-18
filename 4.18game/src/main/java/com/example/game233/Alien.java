package com.example.game233;


import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Shape;
import javafx.util.Duration;

public class Alien extends Character {
    Timeline timeline = new Timeline();
    int X = 0;
    int Y = 0;

    public Alien(int x, int y) {
        super(new Polygon(new double[]{130.0, 50.0, 110.0, 80.0, 0.0, 80.0, -20.0, 50.0, 20.0, 50.0, 30.0, 30.0, 80.0, 30.0, 90.0, 50.0}), x, y);
        super.setMovement(0.1, 0.1);
        this.X = x;
        this.Y = y;
    }

    public void move() {
        this.timeline.getKeyFrames().add(new KeyFrame(Duration.seconds(1.0), new KeyValue[]{new KeyValue(super.getCharacter().translateXProperty(), 150 + this.X), new KeyValue(super.getCharacter().translateYProperty(), 100 + this.Y)}));
        this.timeline.getKeyFrames().add(new KeyFrame(Duration.seconds(2.0), new KeyValue[]{new KeyValue(super.getCharacter().translateXProperty(), 450 + this.X), new KeyValue(super.getCharacter().translateYProperty(), 100 + this.Y)}));
        this.timeline.getKeyFrames().add(new KeyFrame(Duration.seconds(3.0), new KeyValue[]{new KeyValue(super.getCharacter().translateXProperty(), 600 + this.X), new KeyValue(super.getCharacter().translateYProperty(), this.Y)}));
        this.timeline.getKeyFrames().add(new KeyFrame(Duration.seconds(4.0), new KeyValue[]{new KeyValue(super.getCharacter().translateXProperty(), 850 + this.X), new KeyValue(super.getCharacter().translateYProperty(), this.Y)}));
        this.timeline.getKeyFrames().add(new KeyFrame(Duration.seconds(5.0), new KeyValue[]{new KeyValue(super.getCharacter().translateXProperty(), 1150 + this.X), new KeyValue(super.getCharacter().translateYProperty(), 100 + this.Y)}));
        this.timeline.getKeyFrames().add(new KeyFrame(Duration.seconds(6.0), new KeyValue[]{new KeyValue(super.getCharacter().translateXProperty(), 1450 + this.X), new KeyValue(super.getCharacter().translateYProperty(), 100 + this.Y)}));
        this.timeline.getKeyFrames().add(new KeyFrame(Duration.seconds(7.0), new KeyValue[]{new KeyValue(super.getCharacter().translateXProperty(), 1600 + this.X), new KeyValue(super.getCharacter().translateYProperty(), this.Y)}));
        this.timeline.getKeyFrames().add(new KeyFrame(Duration.seconds(8.0), new KeyValue[]{new KeyValue(super.getCharacter().translateXProperty(), 1800 + this.X), new KeyValue(super.getCharacter().translateYProperty(), this.Y)}));
        this.timeline.setAutoReverse(true);
        this.timeline.setCycleCount(-1);
        this.timeline.play();
    }

    public boolean collide(Bullets other) {
        Shape collisionArea = Shape.intersect(super.getCharacter(), other.getCharacter());
        return collisionArea.getBoundsInLocal().getWidth() != -1.0;
    }
}
