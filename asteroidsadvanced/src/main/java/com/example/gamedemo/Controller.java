package com.example.gamedemo;

import javafx.animation.Animation;
import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ListView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.util.Duration;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class Controller implements gameListener{
    private final menuController menuContr = new menuController();
    public static int Width = 800;
    public static int Height = 600;

    private boolean UPpressed = false, LEFTpressed = false,
            RIGHTpressed = false, Apressed = false, SPACEpressed = false, SPACEreleased = true;

    public int score = 0;
    public boolean surprise = true;

    @FXML
    Pane gamePane;
    @FXML
    private BorderPane menuContainer;
    @FXML
    private Pane newGamePane, savePane;
    @FXML
    private ScoreBoard scoreBoard;
    @FXML
    private ListView<String> scoreBoardList;
    @FXML
    private Text currentScore, livesLeft;


    private Timer timer = new Timer();
    private gameLoop game;
    List<Bullets> bullets = new ArrayList<>();
    List<Bullets> bullets1 = new ArrayList<>();
    List<Alien> aliens = new ArrayList<>();
    private List<Character> character = new ArrayList<>();
    List<Asteroids> asteroids = new ArrayList<>();
    double Rotation = 0;
    private long lastBulletTime = System.nanoTime();


    public void initialize() {
        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
        executor.scheduleAtFixedRate(this::startGame, 0, 16, TimeUnit.MILLISECONDS);

        scoreBoard = new ScoreBoard("saves", "score_saves");

        // loads scoreboard from file and updates view
        updateScoreBoard();

        gamePane.setStyle("-fx-background-color: black;");
        gamePane.setPrefSize(Width, Height);


        // initalize menu view
        newGamePane = getMenuPane("/newGame.fxml");
        savePane = getMenuPane("/gameOver.fxml");
        menuContr.init(this);

        // Propt user with welcome screen
        changeMenu("newGame.fxml");

    }


    private class Timer extends AnimationTimer {
        @Override
        public void handle(long now) {
            gamePane.setStyle("-fx-background-color: black;");
            game.Loop(now);
            renderChar();
            shipAction(game.ship);
    };
    }

    public void renderChar(){
        spawnAsteroids();
        spawnAlien();
        moveGameObjects();
    }

    private void spawnAsteroids() {
        Random rnd = new Random();

        while (character instanceof Asteroids && character.size() < 10) {
            Asteroids asteroid = new Asteroids(rnd.nextInt(1000), rnd.nextInt(1000), rnd.nextInt(1, 4));
            character.add(asteroid);
        }

        character.forEach(asteroid -> {
            gamePane.getChildren().add(asteroid.getCharacter());
        });
    }

    private void spawnAlien() {
        Random rnd = new Random();

        if (rnd.nextInt(100) < 2 && aliens.size() == 0) {
            Alien alien = new Alien(1, rnd.nextInt(100, 900));
            aliens.add(alien);
            gamePane.getChildren().add(alien.getCharacter());
        }
    }

    private void moveGameObjects() {
        long now = System.nanoTime();

        if (aliens.size() == 1) {
            if (now - lastBulletTime > 440_000_000 && aliens.size() > 0) {
                spawnAlienBullet();
                lastBulletTime = now;
            }
            aliens.get(0).move();
        }
        game.ship.move();
        asteroids.forEach(Asteroids::move);
        bullets.forEach(Bullets::move);
        bullets1.forEach(Bullets::move);
    }

    private void spawnAlienBullet() {
        Bullets bullet1 = new Bullets((int) (aliens.get(0).getCharacter().getTranslateX()), (int) (aliens.get(0).getCharacter().getTranslateY()));
        bullet1.getCharacter().setRotate(Rotation += 60);
        bullets1.add(bullet1);
        bullet1.acc();
        gamePane.getChildren().add(bullet1.getCharacter());
    }

    private void shipAction(Ship ship) {
        gamePane.setOnKeyPressed(this::handleKeyPressed);
        gamePane.setOnKeyReleased(this::handleKeyReleased);

        Duration frameTime = Duration.millis(1000 / 60); // 60 FPS
        KeyFrame keyFrame = new KeyFrame(frameTime, e -> updateShip(ship));
        Timeline timeline = new Timeline(keyFrame);
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }

    private void updateShip(Ship ship) {
        if (UPpressed) {
            ship.acc();
        }
        if (LEFTpressed) {
            ship.turnLeft();
        }
        if (RIGHTpressed) {
            ship.turnRight();
        }
        if (Apressed) {
            ship.Hyperspace();
        }
//        if (SPACEpressed && SPACEreleased ) {
//            gameLoop.ship.shoot();
//            SPACEreleased = false;
//        }
    }

    @FXML
    private void handleKeyPressed(KeyEvent event) {
        switch (event.getCode()) {
            case UP:
                UPpressed = true;
                break;
            case LEFT:
                LEFTpressed = true;
                break;
            case RIGHT:
                RIGHTpressed = true;
                break;
            case A:
                Apressed = true;
                break;
            case SPACE:
                SPACEpressed = true;
            default:
                break;
        }
    }
    @FXML
    private void handleKeyReleased(KeyEvent event) {
        switch (event.getCode()) {
            case UP:
                UPpressed = false;
                break;
            case LEFT:
                LEFTpressed = false;
                break;
            case RIGHT:
                RIGHTpressed = false;
                break;
            case A:
                Apressed = false;
                break;
            case SPACE: {
                SPACEpressed = false;
                SPACEreleased = true;
            }
            default:
                break;
        }
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
        scoreBoard.addScore(playerName,score);
        updateScoreBoard();
    }

    @Override
    public void livesLeftChanged(int livedLeft) {
        this.livesLeft.setText(livesLeft + " lives left");
    }
    @Override
    public void scoreChanged(int newScore) {
        currentScore.setText("Score: " + newScore);
    }

    @Override
    public void gameOver() {
        UPpressed = false;
        LEFTpressed = false;
        RIGHTpressed = false;

        menuContr.gameOver(game.getScore(), scoreBoard.getHighScore());
    }


    public void startGame() {
        game.initGame(this);
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

//    @Override
//    public void gameOver() {
//        menuContr.gameOver(gameLoop.getScore(), scoreBoard.getHighScore());
//    }

}
