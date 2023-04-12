package com.example.gamedemo;

import javafx.scene.image.Image;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Shape;

import java.util.Random;

public class Asteroids extends Character{
    private double rotationalMovement;
    private int level;
    int accelerationAmount;
    private Random rnd = new Random();
    private Image image;

    public Asteroids(int x, int y, int z) {
        super(new Polygon(25.0*z, 0.0, 50.0*z, 15.0*z, 40.0*z, 40.0*z, 10.0*z, 40.0*z, 0.0, 15.0*z), x, y);
        this.level = z;
        super.setMovement(0.1,0.1);
        Random rnd = new Random();
        super.getCharacter().setRotate(rnd.nextInt(360));
        if (z == 3){this.accelerationAmount = 1 + rnd.nextInt(10);}
        if (z == 2){this.accelerationAmount = 1 + rnd.nextInt(20,30);}
        if (z == 1){this.accelerationAmount = 1 + rnd.nextInt(40,70);}
        for(int i = 0; i < this.accelerationAmount; i++){ acc();}

        this.rotationalMovement = 0.5 - rnd.nextDouble();
    }

    @Override
    public void move() {
        super.move();
        super.getCharacter().setRotate(super.getCharacter().getRotate() + this.rotationalMovement);
    }

    public int getLevel(){
        return this.level;
    }

    public boolean collide(Bullets other){
        Shape collisionArea = Shape.intersect(super.getCharacter(), other.getCharacter());
        return collisionArea.getBoundsInLocal().getWidth() != -1;
    }


}
