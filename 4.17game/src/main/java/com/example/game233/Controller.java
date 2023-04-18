package com.example.game233;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Timer;
import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.ListView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Polygon;
import javafx.scene.text.Text;

public class Controller implements gameListener {
    private gameListener gameListener;
    public static int Width = 800;
    public static int Height = 600;
    private int score = 0;
    private int lives = 3;
    private final menuController menuContr = new menuController();
    private Map<KeyCode, Boolean> pressedKey = new HashMap();

    @FXML
    private BorderPane menuContainer;
    @FXML
    private Pane savePane, newGamePane, gamePane;
    @FXML
    private ListView<String> scoreBoardList;
    @FXML
    private Text currentScore,livesLeft;


    List<Bullets> bullets = new ArrayList();
    List<Bullets> bullets1 = new ArrayList();
    List<Character> enemies = new ArrayList<>();
    private ScoreBoard scoreBoard;
    double Rotation = 0.0;
    public int points = 0;

    Ship ship;
    Text text;


    private long lastUpdate = 0;
    private long lastbullets = 0;
    private int currentLevelIndex = 0;
    Level[] levels = Level.createLevels();

    public void initialize() {
        this.scoreBoard = new ScoreBoard("saves", "score_saves");
        this.updateScoreBoard();

        this.newGamePane = this.getMenuPane("newGame.fxml");
        this.savePane = this.getMenuPane("gameOver.fxml");

        this.menuContr.init(this);
        this.changeMenu("newGame.fxml");
    }

    public void initGame(gameListener gameListener) {
        this.gameListener = gameListener;
        gameListener.scoreChanged(this.score);
        gameListener.livesLeftChanged(this.lives);
        this.scoreBoardList.requestFocus();

        ship = new Ship(Width / 2, Height / 2);
        text = new Text(10, 20, "Points:" + points);

        this.gamePane.getChildren().add(this.ship.getCharacter());
        this.gamePane.getChildren().add(this.text);

        enemies = levels[0].getEnemyList();

        enemies.forEach(enemy -> {
            gamePane.getChildren().add(enemy.getCharacter());
        });

    }

    public void startGame() {
        this.initGame(this);
        setupKeyHandlers(gamePane);
        startGameLoop(gamePane);
    }


    private void setupKeyHandlers(Pane gamePane) {

        gamePane.setOnKeyPressed(keyEvent -> {
            pressedKey.put(keyEvent.getCode(), Boolean.TRUE);
        });
        gamePane.setOnKeyReleased(keyEvent -> {
            pressedKey.put(keyEvent.getCode(), Boolean.FALSE);
        });
    }



    private void startGameLoop(Pane gamePane) {
        new AnimationTimer() {
            @Override
            public void handle(long now) {
                handlePlayerInput(now);

                ship.move();
                enemies.forEach(enemy -> {
                    enemy.move();
                });

                checkCollisions();

                updateBullets(now);
                checkBulletEnemyCollisions();
                updateLevels();
            }
        }.start();
    }

    private void handlePlayerInput(long now) {
        if (pressedKey.getOrDefault(KeyCode.A, false)) {
            pressedKey.remove(KeyCode.A);
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
            gamePane.getChildren().add(bullet.getCharacter());
            lastUpdate = now;
        }
    }

    private void checkCollisions() {
        enemies.forEach(enemy -> {
            if (enemy.collide(ship)) {
                gamePane.getChildren().remove(ship.getCharacter());
                gameListener.gameOver();
            }
        });
    }

    private void updateBullets(long now) {
        bullets.forEach(Bullets::move);
        bullets1.forEach(Bullets::move);
    }

    private void checkBulletEnemyCollisions() {
        bullets.forEach(bullet -> {
            enemies.forEach(enemy -> {
                if (enemy.collide(bullet)) {
                    handleEnemyHit(enemy, bullet);
                }
            });
        });
    }

    private void updateLevels() {
        if (enemies.isEmpty()) {
            currentLevelIndex++;
            if (currentLevelIndex >= levels.length) {
                gamePane.getChildren().remove(ship.getCharacter());
                gameListener.gameOver();
                text.setText("You Win!");
                return;
            }

            bullets.forEach(bullet -> {
                gamePane.getChildren().remove(bullet.getCharacter());
            });
            bullets.clear();

            bullets1.forEach(bullet -> {
                gamePane.getChildren().remove(bullet.getCharacter());
            });
            bullets1.clear();

            enemies.clear();
            enemies = levels[currentLevelIndex].getEnemyList();
            enemies.forEach(enemy -> {
                gamePane.getChildren().add(enemy.getCharacter());
            });
        }
    }

    private void Downgrade(Polygon x, int y, int z) {
        Character newEnemy1 = new Character(x, y, z - 1);
        Character newEnemy2 = new Character(x, y, z - 1);

        gamePane.getChildren().add(newEnemy1.getCharacter());
        gamePane.getChildren().add(newEnemy2.getCharacter());

        enemies.add(newEnemy1);
        enemies.add(newEnemy2);
    }

    private void handleEnemyHit(Character enemy, Bullets bullet) {
        if (enemy.getSize() == 1) {
            points += 10;
            text.setText("Points:" + points);
        }
        if (enemy.getSize() == 2 || enemy.getSize() == 3) {
            double X = enemy.getCharacter().getTranslateX();
            double Y = enemy.getCharacter().getTranslateY();
            int Z = enemy.getSize();
//            Downgrade(X + 10, Y + 10, Z);
//            Downgrade(X - 10, Y - 10, Z);
        }
        if (enemy.getSize() == 4) {
            points += 100;
            text.setText("Points:" + points);
            bullets1.clear();
            bullets1.forEach(bullet1 -> {
                gamePane.getChildren().remove(bullet1.getCharacter());
            });
        }
        gamePane.getChildren().remove(enemy.getCharacter());

    }


    private Pane getMenuPane(String s) {
        FXMLLoader fxmlLoader = new FXMLLoader(this.getClass().getResource(s));
        fxmlLoader.setController(this.menuContr);

        try {
            return (Pane)fxmlLoader.load();
        } catch (IOException var4) {
            System.out.println(var4.toString());
            return null;
        }
    }

    public void changeMenu(String s) {
        switch (s) {
            case "newGame.fxml":
                this.menuContainer.setCenter(this.newGamePane);
                break;
            case "gameOver.fxml":
                this.menuContainer.setCenter(this.savePane);
                break;
            default:
                this.menuContainer.setCenter((Node)null);
        }

    }

    public void livesLeftChanged(int livedLeft) {
    }

    public void scoreChanged(int newScore) {
    }

    public void addScore(String playerName) {
        this.scoreBoard.addScore(playerName, this.points);
        this.updateScoreBoard();
    }

    public void gameOver() {
        this.setReleasedKey();
        this.menuContr.gameOver(this.points, this.scoreBoard.getHighScore());
    }

    @FXML
    private void setPressedKey() {
        this.gamePane.setOnKeyPressed((keyEvent) -> {
            this.pressedKey.put(keyEvent.getCode(), Boolean.TRUE);
        });
    }

    @FXML
    private void setReleasedKey() {
        this.gamePane.setOnKeyReleased((keyEvent) -> {
            this.pressedKey.put(keyEvent.getCode(), Boolean.FALSE);
        });
    }

    private void updateScoreBoard() {
    }



}
