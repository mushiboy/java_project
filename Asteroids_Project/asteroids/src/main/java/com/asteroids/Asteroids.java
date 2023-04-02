package com.asteroids;

import com.asteroids.GameObject;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.TriangleMesh;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Shape;
import javafx.geometry.Point2D;
import javafx.stage.Stage;
import javafx.animation.AnimationTimer;
import java.util.List;
import java.util.ArrayList;
import javafx.scene.input.KeyCode;


import java.io.IOException;

/**
 * JavaFX App
 */
public class Asteroids extends Application {

    // Initialize the root pane, and the lists of bullets and enemies
    private static Pane root;
    private List<GameObject> bullets = new ArrayList<>();
    private List<GameObject> enemies = new ArrayList<>();

    // Initialize the player object as a static variable
    private static GameObject player;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws IOException {
        // Create the scene and set the key event handlers for player movement and shooting
        stage.setScene(new Scene(createContent()));
        stage.getScene().setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.LEFT) {
                player.rotateLeft();
            } else if (e.getCode() == KeyCode.RIGHT) {
                player.rotateRight();
            } else if (e.getCode() == KeyCode.SPACE) {
                Bullet bullet = new Bullet();
                bullet.setVelocity(player.getVelocity().normalize().multiply(5));
                addBullet(bullet, player.getView().getTranslateX(), player.getView().getTranslateY());
            }
        });
        stage.show();
    }

    private static Parent createContent() throws IOException {
        // Initialize the root pane, create the player object and add it to the pane
        root = new Pane();
        root.setPrefSize(600, 600);

        Asteroids asteroids = new Asteroids();
        player = new Player();
        player.setVelocity(new Point2D(1, 0));
        asteroids.addGameObject(player, 300, 300);

        // Create and start the animation timer for updating the game state
        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long currentTime) {
                asteroids.onUpdate();
            }
        };

        timer.start();

        return root;
    }

    // Convenience method for adding game objects to the root pane
    private void addGameObject(GameObject object, double x, double y) {
        object.getView().setTranslateX(x);
        object.getView().setTranslateY(y);
        root.getChildren().add(object.getView());
    }

    // Convenience methods for adding bullets and enemies to their respective lists
    private void addBullet (GameObject bullet, double x, double y) {
        bullets.add(bullet);
        addGameObject(bullet, x, y);
    }

    private void addEnemy (GameObject enemy, double x, double y) {
        enemies.add(enemy);
        addGameObject(enemy, x, y);
    }

    // The main game loop that updates game state on every frame
    private void onUpdate() {
        // Check for collisions between bullets and enemies
        for (GameObject bullet : bullets) {
            for (GameObject enemy : enemies) {
                if (bullet.isColliding(enemy)) {
                    bullet.setAlive(false);
                    enemy.setAlive(false);

                    root.getChildren().removeAll(bullet.getView(), enemy.getView());
                }
            }
        }

        // Remove dead bullets and enemies from their lists
        bullets.removeIf(GameObject::isDead);
        enemies.removeIf(GameObject::isDead);

        // Update the player's position and rotation
        player.update();

        // Add a new enemy with a probability of 0.02 on each frame update
        if (Math.random() < 0.02) {
            addEnemy(new Roid(), Math.random() * root.getPrefWidth(), Math.random() * root.getPrefHeight());
        }
    }

    // Player class, which is a subclass of GameObject and represents the player object in the game
    private static class Player extends GameObject {
        Player () {
            super(new Rectangle(10, 10, Color.BLACK));
        }
    }

    // Roid class, which is a subclass of GameObject and represents a type of enemy object in the game
    private static class Roid extends GameObject {
        Roid () {
            super(new Circle(15, 15, 15, Color.BLACK));
        }
    }

    // Alien class, which is a subclass of GameObject and represents another type of enemy object in the game (currently unused)
    private static class Alien extends GameObject {
        Alien () {
            super(new Rectangle(20, 20, Color.BLACK));
        }
    }

    // Bullet class, which is a subclass of GameObject and represents the bullets fired by the player
    private static class Bullet extends GameObject {
        Bullet () {
            super(new Circle(5, 5, 5, Color.BLACK));
        }
    }
}