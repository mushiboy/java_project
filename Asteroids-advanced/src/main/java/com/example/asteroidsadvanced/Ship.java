package com.example.asteroidsadvanced;

import javafx.animation.Timeline;
import javafx.scene.shape.Polygon;

import java.util.Random;

public class Ship extends Character {

    Timeline timeline = new Timeline();

    // constructor for the Ship class, initializes a Polygon shape for the Ship
    public Ship(int x, int y) {
        super(new Polygon(0, 0, 100, 25, 0, 50, 25, 25), x, y);
    }

    // Hyperspace method for the Ship class, randomly moves the Ship to a new location
    public void Hyperspace(){

        // Unused code to move the ship by an angle and distance
//        double angle = Math.toRadians(super.getCharacter().getRotate());
//        double X = Math.cos(angle)*15;
//        double Y = Math.sin(angle)*15;
//
//        super.getCharacter().setTranslateX(super.getCharacter().getTranslateX()+X);
//        super.getCharacter().setTranslateY(super.getCharacter().getTranslateY()+Y);

        // Generate random X and Y coordinates within 1000 units
        Random rnd = new Random();
        double X = rnd.nextInt(1000);
        super.getCharacter().setTranslateX(rnd.nextInt(1000));
        super.getCharacter().setTranslateY(rnd.nextInt(1000));
    }
}
