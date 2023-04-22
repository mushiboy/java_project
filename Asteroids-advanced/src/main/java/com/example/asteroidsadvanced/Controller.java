package com.example.asteroidsadvanced;

import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.*;
import java.util.*;

// Controller class is responsible for managing the game state, user input, and UI updates
public class Controller {
    // Static variables to define the game window dimensions
    public static int width = 1000;
    public static int height = 700;
    // Instance variables for player points and lives
    public int points = 0;
    public int lives = 3;

    // Lists to store bullets and enemies
    List<Bullet> bullets = new ArrayList<>();
    List<Bullet> alien_bullets = new ArrayList<>();
    List<Character> enemies = new ArrayList<>();
    List<Asteroid> asteroidsToDowngrade = new ArrayList<>();
    private long alienSpawnTime;

    // JavaFX panes to display game elements and the end game screen
    Pane pane = new Pane();
    Pane end_pane = new Pane();
    double Rotation = 0;

    // Create the player's ship and a Timeline for animations
    static Ship ship = new Ship(width / 3, height / 3);
    static Timeline timeline = new Timeline();

    // Method to handle the start button click in the menu
    public void start(ActionEvent actionEvent) throws IOException {
        // Close the menu window
        Stage begin = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        begin.close();

        // Set the pane size and add the player's ship to it
        pane.setPrefSize(width, height);
        pane.getChildren().add(ship.getCharacter());

        // Display the points count on the screen
        Text text = new Text(10, 20, "Points:" + points);
        text.setFont(Font.font("Arial", 25));
        pane.getChildren().add(text);

        // Display the lives count on the screen
        Text livesText = new Text(10, 40, "Lives: " + lives);
        livesText.setFont(Font.font("Arial", 25));
        pane.getChildren().add(livesText);

        // Display the current level on the screen
        Text levelText = new Text(10, 60, "Level: 1"); // Start with level 1
        levelText.setFont(Font.font("Arial", 25));
        pane.getChildren().add(levelText);

        // Create an array of level configurations
        Level[] levels = Level.createLevels();
        // Set the initial list of enemies based on the first level
        enemies = levels[0].getEnemyList();

        // Add enemies to the pane
        enemies.forEach(enemy -> {
            pane.getChildren().add(enemy.getCharacter());
        });

        // Add invincibility effect to the ship for 5 seconds
        addInvincibility(5);

        // Create and set the scene for the game
        Scene scene = new Scene(pane);
        Scene endgame = new Scene(end_pane);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.show();

        // Use a HashMap to track key presses and releases for user input
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
                    if (now - alienSpawnTime > 5_000_000_000L && enemy.getSize() == 4) {
                        while (now - lastbullets > 1_000_000_000) {
                            Bullet bullet1 = new Bullet((int) (enemy.getCharacter().getTranslateX()), (int) (enemy.getCharacter().getTranslateY()));
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
                    if (enemy.collide(ship) && !ship.isInvincible()) {
                        lives--;
                        livesText.setText("Lives: " + lives);
                        if (lives > 0) {
                            // Reset the ship position and make it invincible for a short time
                            ship.getCharacter().setTranslateX(width / 3);
                            ship.getCharacter().setTranslateY(height / 3);
                            addInvincibility(5);
                        } else {
                            // End the game when the lives count reaches 0
                            pane.getChildren().remove(ship.getCharacter());
                            stage.setScene(endgame);
                            saveScore();
                            stop();
                            stage.close();
                            livesText.setText("Game Over");
                        }
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
                        lives--;
                        livesText.setText("Lives: " + lives);
                        pane.getChildren().remove(bullet.getCharacter());
                        alienBulletIterator.remove();
                        if (lives > 0) {
                            // Reset the ship position and make it invincible for a short time
                            ship.getCharacter().setTranslateX(width / 3);
                            ship.getCharacter().setTranslateY(height / 3);
                            addInvincibility(5);
                        } else {
                            // End the game when the lives count reaches 0
                            pane.getChildren().remove(ship.getCharacter());
                            stage.setScene(endgame);
                            saveScore();
                            stop();
                            stage.close();
                            livesText.setText("Game Over: " + points);
                        }
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
                                points += 30;
                                text.setText("Points:" + points);
                                double X = enemy.getCharacter().getTranslateX();
                                double Y = enemy.getCharacter().getTranslateY();
                                int Z = enemy.getSize();
                                asteroidsToDowngrade.add(new Asteroid((int)X+10, (int)Y+10, Z - 1));
                                asteroidsToDowngrade.add(new Asteroid((int)X-10, (int)Y-10, Z - 1));
                            }
                            if(enemy.getSize() == 4){
                                points += 100;
                                text.setText("Points:" + points);
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
                    levelText.setText("Level: " + (currentLevel + 1)); // +1 because the level is zero-indexed

                    if (currentLevel >= levels.length) {
                        // game over
                        livesText.setText("You Win!");
                        pane.getChildren().remove(ship.getCharacter());
                        stage.setScene(endgame);
                        saveScore();
                        stop();
                        stage.close();
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

                    addInvincibility(5);

                    enemies = levels[currentLevel].getEnemyList();
                    enemies.forEach(enemy -> {
                        pane.getChildren().add(enemy.getCharacter());
                    });

                    alienSpawnTime = System.nanoTime();
                };
            };
        }.start();
    }

    // Method to downgrade an asteroid and add it to the game
    public void Downgrade(int x, int y, int z) {
        Asteroid asteroid = new Asteroid(x, y, z);
        enemies.add(asteroid);
        pane.getChildren().add(asteroid.getCharacter());
    }

    // Method to add invincibility to the ship for a given duration (in seconds)
    public static void addInvincibility(int seconds) {
        ship.setInvincible(true);
        timeline.getKeyFrames().add(new KeyFrame(Duration.seconds(seconds), new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                ship.setInvincible(false); // Set the ship as not invincible after the duration
                timeline.stop();
            }
        }));
        timeline.play();
    }

    // Method to save the player's score and display the game over screen
    public void saveScore() {
        VBox root = new VBox();
        root.setStyle("-fx-background-color: black;");
        Stage stage = new Stage();
        Label promptLabel = new Label("                 GAME OVER \n Please enter your name: ");
        promptLabel.setFont(Font.font("Arial", 25));
        promptLabel.setTextFill(Color.WHITE);
        TextField name = new TextField();
        Button saveButton = new Button("Save");

        // Save button action event handler
        saveButton.setOnAction(event -> {
            String playerName = name.getText();
            try {
                BufferedWriter writer = new BufferedWriter(new FileWriter("./Asteroids-advanced/src/main/resources/highScores.txt", true));
                writer.write(playerName + ": " + points + "\n");
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                stage.close();
                Stage stage1 = new Stage();
                FXMLLoader fxmlLoader = new FXMLLoader(MyGame.class.getResource("menu.fxml"));
                Scene scene = null;
                try {
                    scene = new Scene(fxmlLoader.load(), 320, 440);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                stage1.setTitle("Asteroids");
                stage1.setScene(scene);
                stage1.show();
            }
        });

        // Add UI elements to the VBox and create a scene
        root.getChildren().addAll(promptLabel, name, saveButton);
        Scene scene = new Scene(root, 300, 200);
        stage.setScene(scene);
        stage.show();
    }

    // Method to clean the game state and reset the ship
    public void clean() {
        pane = new Pane();
        ship = new Ship(width / 3, height / 3);
    }
}