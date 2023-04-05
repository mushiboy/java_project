package com.example.asteroidsadvanced;

// Import the required JavaFX classes
import javafx.geometry.Point2D;
import javafx.scene.shape.Polygon;

// Define a Character class
public class Character {

    // Define private instance variables
    private Point2D movement;
    private Polygon character;

    // Constructor that sets the shape and initial position of the character
    public Character(Polygon shape, int x, int y){
        this.character = shape;
        this.character.setTranslateX(x);
        this.character.setTranslateY(y);

        // Initialize the movement vector to zero
        this.movement = new Point2D(0,0);
    }

    // Getters for the character and movement vector
    public Polygon getCharacter(){
        return this.character;
    }
    public Point2D getMovement(){
        return this.movement;
    }

    // Setter for the movement vector
    public void setMovement(double x,double y){
        this.movement = this.movement.add(x,y);
    }

    // Method to turn the character to the right
    public void turnRight(){
        this.character.setRotate(this.character.getRotate() + 1);
    }

    // Method to turn the character to the left
    public void turnLeft(){
        this.character.setRotate(this.character.getRotate() - 1);
    }

    // Method to accelerate the character in the current direction
    public void acc(){

        // Calculate the current angle of the character in radians
        double angle = Math.toRadians(this.character.getRotate());

        // Calculate the X and Y components of the movement vector based on the angle and a fixed acceleration rate
        double X = Math.cos(angle) * 0.005;
        double Y = Math.sin(angle) * 0.005;

        // Update the movement vector with the new components
        this.movement = this.movement.add(X,Y);
    }

    // Method to move the character based on the current movement vector
    public void move(){

        // Update the position of the character based on the movement vector
        this.character.setTranslateX(this.character.getTranslateX()+ this.movement.getX());
        this.character.setTranslateY(this.character.getTranslateY()+ this.movement.getY());

        // Wrap the character around the screen if it goes off the edge
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
