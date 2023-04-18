package com.example.asteroidsadvanced;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.input.KeyCode;
import javafx.scene.shape.Path;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Shape;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Ship extends Character{
    private boolean isInvincible = false;

    private boolean isHyperspaced = false;

    Timeline timeline = new Timeline();
    public Ship(int x, int y) {
        super(new Polygon(0,0,100,25,0,50,25,25), x, y);
    }

    public void setInvincible(boolean invincible) {
        this.isInvincible = invincible;
    }

    public boolean isInvincible() {
        return isInvincible;
    }


    public void Hyperspace(){

//        double angle = Math.toRadians(super.getCharacter().getRotate());
//        double X = Math.cos(angle)*15;
//        double Y = Math.sin(angle)*15;
//
//        super.getCharacter().setTranslateX(super.getCharacter().getTranslateX()+X);
//        super.getCharacter().setTranslateY(super.getCharacter().getTranslateY()+Y);
        boolean collision;
        Random rnd = new Random();
        this.setHyperspaced(true);
        super.getCharacter().setTranslateX(Math.random() * (1920 - 100  - this.getCharacter().getBoundsInParent().getWidth()));
        super.getCharacter().setTranslateY(Math.random() * (1080 - 200 -this.getCharacter().getBoundsInParent().getHeight()));
        super.setMovement(-super.getMovement().getX(), -super.getMovement().getY());
    }


    public boolean collide(Character other){
        Shape collisionArea = Shape.intersect(super.getCharacter(), other.getCharacter());
        return collisionArea.getBoundsInLocal().getWidth() != -1;
    }

    public boolean isHyperspaced() {
        return isHyperspaced;
    }

    public void setHyperspaced(boolean hyperspaced) {
        isHyperspaced = hyperspaced;
    }
}
