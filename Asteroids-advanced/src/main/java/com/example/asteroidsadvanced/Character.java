package com.example.asteroidsadvanced;

import javafx.geometry.Point2D;
import javafx.scene.shape.Polygon;

public class Character {
    private Point2D movement;
    private Polygon character;

    public Character(Polygon shape, int x, int y){
        this.character = shape;
        this.character.setTranslateX(x);
        this.character.setTranslateY(y);

        this.movement = new Point2D(0,0);
    }

    public Polygon getCharacter(){
        return this.character;
    }
    public Point2D getMovement(){
        return this.movement;
    }
    public void setMovement(double x,double y){
        this.movement = this.movement.add(x,y);
    }

    public void turnRight(){
        this.character.setRotate(this.character.getRotate() + 1);
    }

    public void turnLeft(){
        this.character.setRotate(this.character.getRotate() - 1);
    }

    public void acc(){
        double angle = Math.toRadians(this.character.getRotate());

        double X = Math.cos(angle) * 0.005;
        double Y = Math.sin(angle) * 0.005;
        this.movement = this.movement.add(X,Y);
    }

    public void move(){
        this.character.setTranslateX(this.character.getTranslateX()+ this.movement.getX());
        this.character.setTranslateY(this.character.getTranslateY()+ this.movement.getY());

        if (this.character.getTranslateX() < 0){
            this.character.setTranslateX(this.character.getTranslateX() + myGame.Width);
        }
        if (this.character.getTranslateX() > myGame.Width){
            this.character.setTranslateX(this.character.getTranslateX() % myGame.Width);
        }
        if (this.character.getTranslateY() < 0){
            this.character.setTranslateY(this.character.getTranslateY() + myGame.Height);
        }
        if (this.character.getTranslateY() > myGame.Height){
            this.character.setTranslateY(this.character.getTranslateY() % myGame.Height);
        }

    }

}
