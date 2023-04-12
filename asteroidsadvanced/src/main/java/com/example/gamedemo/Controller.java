package com.example.gamedemo;

import javafx.animation.AnimationTimer;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ListView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class Controller {
    private menuController menuContr = new menuController();
    public static int Width = 800;
    public static int Height = 600;

    private ScoreBoard scoreBoard;
    public int points = 0;
    public boolean surprise = true;
    @FXML
    private ListView<String> scoreBoardList;
    Pane pane = new Pane();
    @FXML
    private BorderPane menuContainer;
    private Pane newGamePane, savePane;
    List<Bullets> bullets = new ArrayList<>();
    List<Bullets> bullets1 = new ArrayList<>();
    List<Asteroids> asteroids = new ArrayList<>();
    List<Alien> aliens = new ArrayList<>();
    double Rotation = 0;
    private AnimationTimer timer;

    public void initialize() {
        scoreBoard = new ScoreBoard("saves", "score_saves");

        // loads scoreboard from file and updates view
        updateScoreBoard();


        // renders all the objects on screen
        pane.setStyle("-fx-background-color: black;");
        pane.setPrefSize(Width, Height);
        Ship ship = new Ship(Width / 2, Height / 2);
        Text text = new Text(10, 20, "Points:" + points);
        pane.getChildren().add(ship.getCharacter());
        pane.getChildren().add(text);

        Random rnd = new Random();

        while (asteroids.size() < 10) {
            Asteroids asteroid = new Asteroids(rnd.nextInt(1000), rnd.nextInt(1000), rnd.nextInt(1, 4));
            asteroids.add(asteroid);
        }
        asteroids.forEach(asteroid -> {
            pane.getChildren().add(asteroid.getCharacter());
        });

        Map<KeyCode, Boolean> pressedKey = new HashMap<>();
        pane.setOnKeyPressed(keyEvent -> {
            pressedKey.put(keyEvent.getCode(), Boolean.TRUE);
        });
        pane.setOnKeyReleased(keyEvent -> {
            pressedKey.put(keyEvent.getCode(), Boolean.FALSE);
        });


        newGamePane = getMenuPane("newGame.fxml");
        menuContr.init(this);

        changeMenu("newGame.fxml");

        timer = new AnimationTimer() {

            private long lastUpdate = 0;
            private long lastbullets = 0;
            @Override

            public void handle(long now) {
                pane.setStyle("-fx-background-color: black;");

                // controls spaceship actions
                if (pressedKey.getOrDefault(KeyCode.A, false)) {
                    ship.Hyperspace();
                }
                if (pressedKey.getOrDefault(KeyCode.LEFT, false)) {
                    ship.turnLeft();
                }
                if (pressedKey.getOrDefault(KeyCode.RIGHT, false)) {
                    ship.turnRight();
                }
                if (pressedKey.getOrDefault(KeyCode.UP, false)) {
                    ship.acc();
                }
                if (pressedKey.getOrDefault(KeyCode.SPACE, false) && (now - lastUpdate > 330_000_000)) {
                    Bullets bullet = new Bullets((int) (ship.getCharacter().getTranslateX()), (int) (ship.getCharacter().getTranslateY()));
                    bullet.getCharacter().setRotate(ship.getCharacter().getRotate());
                    bullets.add(bullet);
                    bullet.acc();
                    pane.getChildren().add(bullet.getCharacter());
                    lastUpdate = now;
                }

                // spawning and firing of bullets from an alien enemy in the game
                if (rnd.nextInt(100) < 2 && aliens.size() == 0) {
                    Alien alien = new Alien(1, rnd.nextInt(100, 900));
                    aliens.add(alien);
                    pane.getChildren().add(alien.getCharacter());
                }
                if (aliens.size() == 1) {
                    while (now - lastbullets > 440_000_000 && aliens.size() > 0) {
                        Bullets bullet1 = new Bullets((int) (aliens.get(0).getCharacter().getTranslateX()), (int) (aliens.get(0).getCharacter().getTranslateY()));
                        bullet1.getCharacter().setRotate(Rotation += 60);
                        bullets1.add(bullet1);
                        bullet1.acc();
                        pane.getChildren().add(bullet1.getCharacter());
                        lastbullets = now;
                    }
                    aliens.get(0).move();
                }
                ship.move();
                asteroids.forEach(asteroid -> {
                    asteroid.move();

                });
                bullets.forEach(bullet -> {
                    bullet.move();
                });
                bullets1.forEach(bullet1 -> {
                    bullet1.move();
                });
                bullets.forEach(bullet -> {
                    asteroids.forEach(asteroid -> {
                        if (asteroid.collide(bullet)) {
                            if (asteroid.getLevel() == 1) {
                                points += 10;
                                text.setText("Points:" + points);
                            }
                            if (asteroid.getLevel() != 1) {
                                double X = asteroid.getCharacter().getTranslateX();
                                double Y = asteroid.getCharacter().getTranslateY();
                                int Z = asteroid.getLevel();
                                Downgrade((int) X + 10, (int) Y + 10, Z);
                                Downgrade((int) X - 10, (int) Y - 10, Z);
                            }
                            pane.getChildren().remove(asteroid.getCharacter());
                            asteroids.remove(asteroid);
                            pane.getChildren().remove(bullet.getCharacter());
                            bullets.remove(bullet);
                        }
                    });
                });
                bullets.forEach(bullet -> {
                    aliens.forEach(alien -> {
                        if (alien.collide(bullet)) {
                            pane.getChildren().remove(alien.getCharacter());
                            aliens.remove(alien);
                            pane.getChildren().remove(bullet.getCharacter());
                            bullets.remove(bullet);
                            bullets1.forEach(bullet1 -> {
                                pane.getChildren().remove(bullet1.getCharacter());
                            });
                            surprise = false;
                            bullets1.clear();
                            points += 100;
                            text.setText("Points:" + points);
                        }
                    });
                });
            }
        };
    };


    public void Downgrade(int x,int y,int z){
        Asteroids asteroid = new Asteroids(x,y,z-1);
        asteroids.add(asteroid);
        pane.getChildren().add(asteroid.getCharacter());

    }

    public void changeMenu(String s) {
        switch (s) {
            case "newGame.fxml" -> menuContainer.setCenter(newGamePane);
            case "gameOver.fxml" -> menuContainer.setCenter(savePane);
            default -> menuContainer.setCenter(null);
        }
    }
    private void updateScoreBoard() {
        scoreBoardList.setItems(scoreBoard.getScores().stream()
                .limit(18)
                .map(element -> scoreBoard.getScores().indexOf(element) + 1 + ". " + element.getKey() + ": "
                        + element.getValue())
                .collect(Collectors.toCollection(FXCollections::observableArrayList)));
    }

    public void addScore(String playerName) {
        scoreBoard.addScore(playerName,points);
        updateScoreBoard();
    }

    public void startGame() {
        scoreBoardList.requestFocus();
        timer.start();

    }

    public Pane getMenuPane(String s) {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(s));
        fxmlLoader.setController(menuContr);
        try {
            return fxmlLoader.load();
        } catch (IOException e) {
            System.out.println(e.toString());
            return null;
        }
    }

}
