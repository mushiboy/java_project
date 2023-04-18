package com.example.game233;

import javafx.scene.shape.Polygon;

public class Bullets extends Character {
    public Bullets(int x, int y) {
        super(new Polygon(new double[]{5.0, -5.0, 5.0, 5.0, -5.0, 5.0, -5.0, -5.0}), x + 50, y + 30);
    }

    public void move() {
        super.getCharacter().setTranslateX(super.getCharacter().getTranslateX() + super.getMovement().getX());
        super.getCharacter().setTranslateY(super.getCharacter().getTranslateY() + super.getMovement().getY());
    }

    public void acc() {
        super.acc();
        double angle = Math.toRadians(super.getCharacter().getRotate());
        double X = Math.cos(angle) * 5.0;
        double Y = Math.sin(angle) * 5.0;
        super.setMovement(X, Y);
    }
}
