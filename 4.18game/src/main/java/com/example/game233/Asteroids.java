package com.example.game233;

import java.util.Random;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Shape;

public class Asteroids extends Character {
    private double rotationalMovement;
    private int level;
    int accelerationAmount;

    public Asteroids(int x, int y, int z) {
        super(new Polygon(new double[]{25.0 * (double)z, 0.0, 50.0 * (double)z, 15.0 * (double)z, 40.0 * (double)z, 40.0 * (double)z, 10.0 * (double)z, 40.0 * (double)z, 0.0, 15.0 * (double)z}), x, y);
        this.level = z;
        super.setMovement(0.1, 0.1);
        Random rnd = new Random();
        super.getCharacter().setRotate((double)rnd.nextInt(360));
        if (z == 3) {
            this.accelerationAmount = 1 + rnd.nextInt(10);
        }

        if (z == 2) {
            this.accelerationAmount = 1 + rnd.nextInt(20, 30);
        }

        if (z == 1) {
            this.accelerationAmount = 1 + rnd.nextInt(40, 70);
        }

        for(int i = 0; i < this.accelerationAmount; ++i) {
            this.acc();
        }

        this.rotationalMovement = 0.5 - rnd.nextDouble();
    }

    public void move() {
        super.move();
        super.getCharacter().setRotate(super.getCharacter().getRotate() + this.rotationalMovement);
    }

    public int getLevel() {
        return this.level;
    }

    public boolean collide(Bullets other) {
        Shape collisionArea = Shape.intersect(super.getCharacter(), other.getCharacter());
        return collisionArea.getBoundsInLocal().getWidth() != -1.0;
    }
}
