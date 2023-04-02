package com.asteroids;

import javafx.geometry.Point2D;
import javafx.scene.Node;

// Creating GameObject class
public class GameObject {
    
    // Initializing instance variables
    private Node view;
    private Point2D velocity = new Point2D(0, 0);
    private boolean alive = true;

    // Constructor
    public GameObject(Node view) {
        this.view = view;
    }

    // Getter for Node view
    public Node getView() {
        return view;
    }

    // Method to update object's position
    public void update() {
        view.setTranslateX(view.getTranslateX() + velocity.getX());
        view.setTranslateY(view.getTranslateY() + velocity.getY());
    }

    // Getter for object's rotation
    public double getRotate() {
        return view.getRotate();
    }

    // Method to rotate object right
    public void rotateRight () {
        view.setRotate(view.getRotate() + 5);
        setVelocity(new Point2D(Math.cos(Math.toRadians(getRotate())), Math.cos(Math.toRadians(getRotate()))));
    }

    // Method to rotate object left
    public void rotateLeft () {
        view.setRotate(view.getRotate() - 5);
        setVelocity(new Point2D(Math.cos(Math.toRadians(getRotate())), Math.cos(Math.toRadians(getRotate()))));
    }

    // Getter for object's alive status
    public boolean isAlive() {
        return alive;
    }

    // Getter for object's dead status
    public boolean isDead() {
        return !alive;
    }

    // Setter for object's alive status
    public void setAlive(boolean alive) {
        this.alive = alive;
    }

    // Setter for object's velocity
    public void setVelocity(Point2D velocity) {
        this.velocity = velocity;
    }

    // Getter for object's velocity
    public Point2D getVelocity() {
        return velocity;
    }

    // Method to check if objects are colliding
    public boolean isColliding(GameObject other) {
        return getView().getBoundsInParent().intersects(other.getView().getBoundsInParent());
    }
}
