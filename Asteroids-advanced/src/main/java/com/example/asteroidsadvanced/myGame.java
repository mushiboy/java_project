package com.example.asteroidsadvanced;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Polygon;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.security.Key;
import java.util.*;

public class myGame extends Application {
    public static int Width = 1980;
    public static int Height = 1080;
    public int points = 0;
    List<Bullets> bullets = new ArrayList<>();
    List<Bullets> bullets1 = new ArrayList<>();
    List<Character> enemies = new ArrayList<>();
    private Level[] levels; // declare array to store levels
    private int currentLevel;
    public boolean surprise = true;
    Pane pane = new Pane();
    Pane end_pane = new Pane();
    double Rotation = 0;

    @Override
    public void start(Stage stage) throws Exception {
        Random rnd = new Random();
        pane.setPrefSize(Width, Height);
        Ship ship = new Ship(Width/2,Height/2);
        pane.getChildren().add(ship.getCharacter());
        Text text = new Text(10,20,"Points:"+points);
        pane.getChildren().add(text);

        Scene scene = new Scene(pane);
        Scene endgame = new Scene(end_pane);
        stage.setScene(scene);
        stage.show();

        // create levels array
        Level[] levels = Level.createLevels();
        enemies = levels[0].getEnemyList();
        currentLevel = levels[0].getLevelNumber();


        Map<KeyCode, Boolean> pressedKey = new HashMap<>();
        scene.setOnKeyPressed(keyEvent -> {
            pressedKey.put(keyEvent.getCode(), Boolean.TRUE);
        });
        scene.setOnKeyReleased(keyEvent -> {
            pressedKey.put(keyEvent.getCode(),Boolean.FALSE);
        });

        new AnimationTimer(){
            private long lastUpdate = 0;
            private long lastbullets = 0;

            public void handle(long now){
                if(pressedKey.getOrDefault(KeyCode.A,false)){ship.Hyperspace();}
                if(pressedKey.getOrDefault(KeyCode.LEFT, false)){ship.turnLeft();}
                if(pressedKey.getOrDefault(KeyCode.RIGHT,false)){ship.turnRight();}
                if(pressedKey.getOrDefault(KeyCode.UP,false)){ship.acc();}
                if(pressedKey.getOrDefault(KeyCode.SPACE, false) && (now - lastUpdate >330_000_000)){
                    Bullets bullet = new Bullets((int)(ship.getCharacter().getTranslateX()),(int)(ship.getCharacter().getTranslateY()));
                    bullet.getCharacter().setRotate(ship.getCharacter().getRotate());
                    bullets.add(bullet);
                    bullet.acc();
                    pane.getChildren().add(bullet.getCharacter());
                    lastUpdate = now;
                }

                if(enemies.size() == 0){
                    currentLevel++;
                    if(currentLevel < levels.length){
                        enemies = levels[currentLevel].getEnemyList();
                    } else {
                        pane.getChildren().remove(ship.getCharacter());
                        stage.setScene(endgame);
                        text.setText("Game Over");
                        return;
                    }
                } else {
                    ship.move();
                    enemies.forEach(asteroid -> {
                        asteroid.move();
                    });
                
                    enemies.forEach(asteroid -> {
                        if(asteroid.collide(ship)){
                            pane.getChildren().remove(ship.getCharacter());
                            stage.setScene(endgame);
                            text.setText("Game Over");
                        }
                    });
                    enemies.forEach(alien -> {
                        if(alien.collide(ship)){
                            pane.getChildren().remove(ship.getCharacter());
                            text.setText("Game Over");
                        }
                    });
                
                    bullets.forEach(bullet -> {
                        bullet.move();
                    });
                    bullets1.forEach(bullet1 -> {
                        bullet1.move();
                    });
                    bullets.forEach(bullet -> {
                        enemies.forEach(enemy -> {
                            if(enemy.collide(bullet)){
                                if(enemy instanceof Asteroids) {
                                    Asteroids asteroid = (Asteroids) enemy;
                                    if(asteroid.getSize() == 1) {
                                        points += 10;
                                        text.setText("Points:"+points);
                                    } else {
                                        double X = asteroid.getCharacter().getTranslateX();
                                        double Y = asteroid.getCharacter().getTranslateY();
                                        int Z = asteroid.getSize();
                                        Downgrade((int)X+10,(int)Y+10,Z);
                                        Downgrade((int)X-10,(int)Y-10,Z);
                                    }
                                    pane.getChildren().remove(asteroid.getCharacter());
                                    enemies.remove(asteroid);
                                }
                                pane.getChildren().remove(bullet.getCharacter());
                                bullets.remove(bullet);
                            }
                        });
                    });
                
                    bullets.forEach(bullet ->{
                        enemies.forEach(alien -> {
                            if(alien.collide(bullet)){
                                pane.getChildren().remove(alien.getCharacter());
                                enemies.remove(alien);
                                pane.getChildren().remove(bullet.getCharacter());
                                bullets.remove(bullet);
                                bullets1.forEach(bullet1 -> {
                                    pane.getChildren().remove(bullet1.getCharacter());
                                });
                                surprise = false;
                                bullets1.clear();
                                points += 100;
                                text.setText("Points:"+points);
                            }
                        });
                    });
                }
            }
        }.start();
    }
    

    public void Downgrade(int x,int y,int z){
        Asteroids asteroid = new Asteroids(x,y,z-1);
        enemies.add(asteroid);
        pane.getChildren().add(asteroid.getCharacter());
    }

    public static void main(String[] args){
        launch(args);
    }
}