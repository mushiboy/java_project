package com.example.asteroidsadvanced;

import javafx.animation.Timeline;
import javafx.scene.shape.Polygon;

public class Ship extends Character{
    Timeline timeline = new Timeline();
    public Ship(int x, int y) {
        super(new Polygon(0,0,100,25,0,50,25,25), x, y);
    }

    public void Hyperspace(){
        double angle = Math.toRadians(super.getCharacter().getRotate());
        double X = Math.cos(angle)*15;
        double Y = Math.sin(angle)*15;
        super.getCharacter().setTranslateX(super.getCharacter().getTranslateX()+X);
        super.getCharacter().setTranslateY(super.getCharacter().getTranslateY()+Y);
    }


}
