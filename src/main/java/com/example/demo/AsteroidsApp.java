package com.example.demo;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.geometry.Point2D;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class AsteroidsApp extends Application {

    public static int WIDTH = 800;
    public static int HEIGHT = 600;

    public static AtomicInteger points;
    public static void main(String[] args) {
        launch(args);
    }


    @Override
    public void start(Stage stage) {
        Pane pane = new Pane();
        pane.setPrefSize(WIDTH, HEIGHT);


        Text pointsText = new Text("Points: 0");
        pointsText.setX(10);
        pointsText.setY(20);
        points = new AtomicInteger();
        pane.getChildren().add(pointsText);


        Ship ship = new Ship(WIDTH / 2, HEIGHT / 2);
        List<Asteroid> asteroids = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            Random rnd = new Random();
            int typeIndex = rnd.nextInt(3);

            Asteroid.AsteroidType type;
            switch (typeIndex) {
                case 0:
                    type = Asteroid.AsteroidType.LARGE;
                    break;
                case 1:
                    type = Asteroid.AsteroidType.MEDIUM;
                    break;
                default:
                    type = Asteroid.AsteroidType.SMALL;
                    break;
            }
            Asteroid asteroid = new Asteroid(rnd.nextInt(WIDTH / 3), rnd.nextInt(HEIGHT), type);
            asteroids.add(asteroid);
        }


        pane.getChildren().add(ship.getCharacter());
        asteroids.forEach(asteroid -> pane.getChildren().add(asteroid.getCharacter()));



        Scene scene = new Scene(pane);

        Map<KeyCode, Boolean> pressedKeys = new HashMap<>();
        scene.setOnKeyPressed(event -> pressedKeys.put(event.getCode(), Boolean.TRUE));
        scene.setOnKeyReleased(event -> pressedKeys.put(event.getCode(), Boolean.FALSE));


        List<Bullet> bullets = new ArrayList<>();


        new AnimationTimer() {
            Point2D movement = new Point2D(1, 0);
            @Override
            public void handle(long now) {
                if (pressedKeys.getOrDefault(KeyCode.LEFT, false)) {
                    ship.turnLeft();
                }

                if (pressedKeys.getOrDefault(KeyCode.RIGHT, false)) {
                    ship.turnRight();
                }

                if (pressedKeys.getOrDefault(KeyCode.UP, false)) {
                    ship.accelerate();
                }

                ship.move();
                asteroids.forEach(Asteroid::move);

                asteroids.forEach(asteroid -> {
                    if (ship.collide(asteroid)) {
                        stop();
                    }
                });



                if (pressedKeys.getOrDefault(KeyCode.SPACE, false) && bullets.size() < 3) {
                    // we shoot
                    Bullet bullet = new Bullet((int) ship.getCharacter().getTranslateX(), (int) ship.getCharacter().getTranslateY());
                    bullet.getCharacter().setRotate(ship.getCharacter().getRotate());
                    bullets.add(bullet);

                    bullet.accelerate();
                    bullet.setMovement(bullet.getMovement().normalize().multiply(3));

                    pane.getChildren().add(bullet.getCharacter());
                }

                bullets.forEach(bullet -> {
                    asteroids.forEach(asteroid -> {
                        if(bullet.collide(asteroid)) {
                            bullet.setAlive(false);
                            Asteroid[] newAsteroids = asteroid.destroy();
                            for (Asteroid newAsteroid : newAsteroids) {
                                asteroids.add(newAsteroid);
                            }
                        }
                    });

                    if(bullet.isAlive()) {
                        pointsText.setText("Points: " + points.addAndGet(1000));
                    }
                });

                bullets.stream()
                        .filter(Character::isAlive)
                        .forEach(bullet -> pane.getChildren().remove(bullet.getCharacter()));
                bullets.removeAll(bullets.stream()
                        .filter(bullet -> bullet.isAlive())
                        .toList());

                asteroids.stream()
                        .filter(asteroid -> asteroid.isAlive())
                        .forEach(asteroid -> pane.getChildren().remove(asteroid.getCharacter()));
                asteroids.removeAll(asteroids.stream()
                        .filter(asteroid -> asteroid.isAlive())
                        .toList());

                ship.move();
                asteroids.forEach(asteroid -> asteroid.move());
                bullets.forEach(bullet -> bullet.move());

            }

        }.start();


        stage.setTitle("Asteroids!");
        stage.setScene(scene);
        stage.show();
    }


}


