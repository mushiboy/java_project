package com.example.demo;

import java.util.Random;

public class Asteroid extends Character {

    private final AsteroidType type;
    private final double rotationalMovement;

    private final int size;


    public Asteroid(int x, int y, AsteroidType type) {

        super(new PolygonFactory().createPolygon(), x, y);
        this.type = type;

        Random rnd = new Random();
        super.getCharacter().setRotate(rnd.nextInt(360));

        int accelerationAmount = 1 + rnd.nextInt(10);
        for (int i = 0; i < accelerationAmount; i++) {
            accelerate();
        }

        this.rotationalMovement = 0.5 - rnd.nextDouble();
        switch (type) {
            case LARGE -> size = 50;
            case MEDIUM -> size = 30;
            case SMALL -> size = 20;
            default -> size = 0;
        }
    }

    public enum AsteroidType {
        LARGE, MEDIUM, SMALL;
        public AsteroidType nextSize() {
            return switch (this) {
                case LARGE -> MEDIUM;
                default -> SMALL;
            };
        }

    }

    public int getSize() {
        return size;
    }
    public Asteroid[] destroy() {
        if (type == AsteroidType.SMALL) {
            return new Asteroid[0];
        } else {
            Random rnd = new Random();
            Asteroid[] newAsteroids = new Asteroid[2];
            for (int i = 0; i < newAsteroids.length; i++) {
                newAsteroids[i] = new Asteroid((int) getCharacter().getTranslateX(), (int) getCharacter().getTranslateY(), type.nextSize());
                newAsteroids[i].setMovement(getMovement().normalize().multiply(2 + rnd.nextInt(6)));
            }
            return newAsteroids;
        }
    }



    public void move() {
        super.getCharacter().setRotate(super.getCharacter().getRotate() + rotationalMovement);
        super.move();
    }




}

