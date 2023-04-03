package com.example.demo;

import javafx.geometry.Point2D;
import javafx.scene.shape.Polygon;

import java.util.Random;

public class Asteroid extends Character {

    private double rotationalMovement;
    public enum AsteroidSize {
        LARGE, MEDIUM, SMALL;
    }

    private AsteroidSize size;
    Random rnd = new Random();


    public Asteroid(int x, int y) {

        super(new PolygonFactory().createPolygon(), x, y);

        Random rnd = new Random();

        super.getCharacter().setRotate(rnd.nextInt(360));

        int accelerationAmount = 1 + rnd.nextInt(10);
        for (int i = 0; i < accelerationAmount; i++) {
            accelerate();
        }

        this.rotationalMovement = 0.5 - rnd.nextDouble();

    }

    public void setSize(AsteroidSize size) {
        this.size = size;
    }

    public void breakApart() {
        switch (size) {
            case LARGE:
                // Create two medium-sized asteroids with random directions and speeds
                for (int i = 0; i < 2; i++) {
                    Asteroid medium = new Asteroid(getX(), getY(), AsteroidSize.MEDIUM);
                    medium.setVelocity(rnd.nextInt(3) - 1, rnd.nextInt(3) - 1);
                    medium.accelerate();
                    getParent().addChild(medium);
                }
                break;
            case MEDIUM:
                // Create two small-sized asteroids with random directions and speeds
                for (int i = 0; i < 2; i++) {
                    Asteroid small = new Asteroid(getX(), getY(), AsteroidSize.SMALL);
                    small.setVelocity(rnd.nextInt(3) - 1, rnd.nextInt(3) - 1);
                    small.accelerate();
                    getParent().addChild(small);
                }
                break;
            case SMALL:
                // Do nothing
                break;
            default:
                break;
        }
    }
    

    public void update(long deltaTime) {
        super.update(deltaTime);
        super.getCharacter().setRotate(super.getCharacter().getRotate() + rotationalMovement);
    }

}

