package com.example.game233;

import java.util.Random;
import javafx.animation.Timeline;
import javafx.scene.shape.Polygon;

public class Ship extends Character {
    Timeline timeline = new Timeline();

    public Ship(int x, int y) {
        super(new Polygon(new double[]{0.0, 0.0, 100.0, 25.0, 0.0, 50.0, 25.0, 25.0}), x, y);
    }

    public void Hyperspace() {
        Random rnd = new Random();
        double X = (double)rnd.nextInt(1000);
        super.getCharacter().setTranslateX((double)rnd.nextInt(1000));
        super.getCharacter().setTranslateY((double)rnd.nextInt(1000));
    }
}
