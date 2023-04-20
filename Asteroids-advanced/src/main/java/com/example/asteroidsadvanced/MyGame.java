package com.example.asteroidsadvanced;

import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import java.util.*;

public class MyGame extends Application {
    public static int Width = 1980;
    public static int Height = 1080;
    public int points = 0;

    List<Bullet> bullets = new ArrayList<>();
    List<Bullet> alien_bullets = new ArrayList<>();
    List<Character> enemies = new ArrayList<>();
    List<Asteroid> asteroidsToDowngrade = new ArrayList<>();

    static Pane pane = new Pane();
    Pane end_pane = new Pane();
    double Rotation = 0;

    static Ship ship = new Ship(Width/3,Height/3);
    static Timeline timeline = new Timeline();

    @Override
    public void start(Stage stage) throws Exception {
        Random rnd = new Random();
        pane.setPrefSize(Width, Height);

        pane.getChildren().add(ship.getCharacter());
        Text text = new Text(10,20,"Points:"+points);
        pane.getChildren().add(text);
        
        // create levels array
        Level[] levels = Level.createLevels();
        enemies = levels[0].getEnemyList();

        enemies.forEach(enemy -> {
            pane.getChildren().add(enemy.getCharacter());
        });
        //Adding invincibility to ship
        addInvincibility(5);

        Scene scene = new Scene(pane);
        Scene endgame = new Scene(end_pane);
        stage.setScene(scene);
        stage.show();

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
            private int currentLevel = 0;

            public void handle(long now){
                if(pressedKey.getOrDefault(KeyCode.A,false)){
                    pressedKey.remove(KeyCode.A);
                    boolean collision;
                    timeline.stop();

                    do {
                        collision=false;
                        ship.Hyperspace();
                        for (Character enemy : enemies) {
                            if (enemy.collide(ship)) {
                                collision=true;
                                break;
                            }
                        }
                        for (Bullet bullet : alien_bullets) {
                            if (ship.collide(bullet)) {
                                collision=true;
                                break;
                            }
                        }
                    } while(collision);
                    addInvincibility(5);
                }
                if(pressedKey.getOrDefault(KeyCode.LEFT, false)){ship.turnLeft();}
                if(pressedKey.getOrDefault(KeyCode.RIGHT,false)){ship.turnRight();}
                if(pressedKey.getOrDefault(KeyCode.UP,false)){
                    ship.setHyperspaced(false);
                    ship.acc();
                }
                if(pressedKey.getOrDefault(KeyCode.SPACE, false) && (now - lastUpdate >330_000_000)){
                    Bullet bullet = new Bullet((int)(ship.getCharacter().getTranslateX()),(int)(ship.getCharacter().getTranslateY()));
                    bullet.getCharacter().setRotate(ship.getCharacter().getRotate());
                    bullets.add(bullet);
                    bullet.acc();
                    pane.getChildren().add(bullet.getCharacter());
                    lastUpdate = now;
                }

                enemies.forEach(enemy -> {
                    if(enemy.getSize() == 4) {
                        while(now - lastbullets > 1000000000) {
                            Bullet bullet1 = new Bullet((int)(enemy.getCharacter().getTranslateX()), (int)(enemy.getCharacter().getTranslateY()));
                            var diff_y = ship.getCharacter().getTranslateX() - enemy.getCharacter().getTranslateX();
                            var diff_x = ship.getCharacter().getTranslateY() - enemy.getCharacter().getTranslateY();
                            var angle = Math.toDegrees(Math.atan2(diff_x, diff_y));
                            bullet1.getCharacter().setRotate(angle);
                            alien_bullets.add(bullet1);
                            bullet1.acc();
                            pane.getChildren().add(bullet1.getCharacter());
                            lastbullets = now;
                        }
                    }
                });

                if (!ship.isHyperspaced()) {
                    ship.move();
                }

                enemies.forEach(enemy -> {
                    enemy.move();
                });

                enemies.forEach(enemy -> {
                    if(enemy.collide(ship)  && !ship.isInvincible()){
                        pane.getChildren().remove(ship.getCharacter());
                        stage.setScene(endgame);
                        text.setText("Game Over");
                    }
                });

                bullets.forEach(bullet -> {
                    bullet.move();
                });
                
                alien_bullets.forEach(bullet1 -> {
                    bullet1.move();
                });

                Iterator<Bullet> alienBulletIterator = alien_bullets.iterator();
                while (alienBulletIterator.hasNext()) {
                    Bullet bullet = alienBulletIterator.next();
                    if (ship.collide(bullet) && !ship.isInvincible()) {
                        pane.getChildren().remove(ship.getCharacter());
                        pane.getChildren().remove(bullet.getCharacter());
                        alienBulletIterator.remove();
                        text.setText("Game Over:" + points);
                    }
                }

                Iterator<Bullet> bulletIterator = bullets.iterator();

                while (bulletIterator.hasNext()) {
                    Bullet bullet = bulletIterator.next();
                    Iterator<Character> enemyIterator = enemies.iterator();
                    while (enemyIterator.hasNext()) {
                        Character enemy = enemyIterator.next();
                        if (enemy.collide(bullet)) {
                            if(enemy.getSize() == 1){points += 10;text.setText("Points:"+points);}
                            if(enemy.getSize() == 2 || enemy.getSize() == 3){
                                double X = enemy.getCharacter().getTranslateX();
                                double Y = enemy.getCharacter().getTranslateY();
                                int Z = enemy.getSize();
                                asteroidsToDowngrade.add(new Asteroid((int)X+10, (int)Y+10, Z - 1));
                                asteroidsToDowngrade.add(new Asteroid((int)X-10, (int)Y-10, Z - 1));
                            }
                            if(enemy.getSize() == 4){
                                points += 100;
                                text.setText("Points:"+points);
                                alien_bullets.forEach(bullet1 -> {
                                    pane.getChildren().remove(bullet1.getCharacter());
                                });
                                alien_bullets.clear();
                                }
                            pane.getChildren().remove(bullet.getCharacter());
                            bulletIterator.remove(); // Remove bullet using iterator
                            pane.getChildren().remove(enemy.getCharacter());
                            enemyIterator.remove(); // Remove enemy using iterator
                        }
                    };
                };

                for (Asteroid asteroid : asteroidsToDowngrade) {
                    Downgrade((int) asteroid.getCharacter().getTranslateX(), (int) asteroid.getCharacter().getTranslateY(), asteroid.getSize());
                }
                asteroidsToDowngrade.clear();

                if (enemies.isEmpty()) {
                    currentLevel++;
                    if (currentLevel >= levels.length) {
                        // game over
                        pane.getChildren().remove(ship.getCharacter());
                        stage.setScene(endgame);
                        text.setText("You Win!");
                        return;
                    }
                    bullets.forEach(bullet -> {
                        pane.getChildren().remove(bullet.getCharacter());
                    });
                    bullets.clear();
                    
                    alien_bullets.forEach(bullet -> {
                        pane.getChildren().remove(bullet.getCharacter());
                    });
                    alien_bullets.clear();

                    enemies.clear();
                    enemies = levels[currentLevel].getEnemyList();
                    enemies.forEach(enemy -> {
                        pane.getChildren().add(enemy.getCharacter());
                    });
                };
            };
        }.start();
    }

    public void Downgrade(int x,int y,int z){
        Asteroid asteroid = new Asteroid(x,y,z);
        enemies.add(asteroid);
        pane.getChildren().add(asteroid.getCharacter());
    }
    public static void addInvincibility(int seconds) {
        ship.setInvincible(true);
        Circle circle = new Circle(60);
        timeline.getKeyFrames().add(new KeyFrame(Duration.seconds(seconds), new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                ship.setInvincible(false); // Set the ship as not invincible after the duration
                timeline.stop();
            }
        }));
        timeline.play();
    }
    public static void main(String[] args){
        launch(args);
    }
}