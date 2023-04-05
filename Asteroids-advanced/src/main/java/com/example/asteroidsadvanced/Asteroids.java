// Define the package and imports
package com.example.asteroidsadvanced;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Shape;
import javafx.scene.transform.Rotate;
import java.util.Random;

// Define the Asteroids class as a subclass of the Character class
public class Asteroids extends Character{
    // Define instance variables
    private double rotationalMovement; // the amount of rotational movement that the asteroid should have
    private int level; // the level of the asteroid (1, 2, or 3)
    int accelerationAmount; // the amount of acceleration that the asteroid should have
    private Random rnd = new Random(); // a Random object used for generating random numbers

    // Define the constructor for the Asteroids class
    public Asteroids(int x, int y, int z) {
        // Call the superclass constructor with a new Polygon object scaled by the value of z
        super(new Polygon(25.0*z, 0.0, 50.0*z, 15.0*z, 40.0*z, 40.0*z, 10.0*z, 40.0*z, 0.0, 15.0*z), x, y);

        // Set the level instance variable
        this.level = z;

        // Set the movement of the asteroid to (0.1, 0.1)
        super.setMovement(0.1, 0.1);

        // Set the rotation of the asteroid to a random angle
        Random rnd = new Random();
        super.getCharacter().setRotate(rnd.nextInt(360));

        // Set the acceleration amount based on the level of the asteroid
        if (z == 3){
            this.accelerationAmount = 1 + rnd.nextInt(10);
        }
        if (z == 2){
            this.accelerationAmount = 1 + rnd.nextInt(20,30);
        }
        if (z == 1){
            this.accelerationAmount = 1 + rnd.nextInt(40,70);
        }

        // Apply acceleration to the asteroid a random number of times based on the acceleration amount
        for(int i = 0; i < this.accelerationAmount; i++){
            acc();
        }

        // Set the rotational movement of the asteroid to a random value between -0.5 and 0.5
        this.rotationalMovement = 0.5 - rnd.nextDouble();
    }

    // Override the move() method of the Character class
    @Override
    public void move() {
        // Call the move() method of the superclass to update the position of the asteroid
        super.move();

        // Add the rotational movement to the rotation of the asteroid
        super.getCharacter().setRotate(super.getCharacter().getRotate() + this.rotationalMovement);
    }

    // Define a method to get the level of the asteroid
    public int getLevel(){
        return this.level;
    }

    // Define a method to check if the asteroid has collided with a Bullets object
    public boolean collide(Bullets other){
        // Create a new Shape object that is the intersection of the asteroid and the bullets
        Shape collisionArea = Shape.intersect(super.getCharacter(), other.getCharacter());

        // Check if the width of the bounding box of the intersection is not -1
        return collisionArea.getBoundsInLocal().getWidth() != -1;
    }
}
