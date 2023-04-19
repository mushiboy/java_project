package com.example.game233;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.util.Duration;

public class Controller implements gameListener {
    private gameListener gameListener;
    public static int Width = 800;
    public static int Height = 600;
    private int score = 0;
    private int lives = 3;
    public menuController menuContr = new menuController();

    @FXML
    private BorderPane menuContainer;
    @FXML
    private Pane savePane, newGamePane, gamePane;
    @FXML
    private ListView<String> scoreBoardList;
    @FXML
    private Text currentScore,livesLeft;
    Controller controller;
    @FXML
    private Button newGameButton;
    @FXML
    private Button saveButton;
    @FXML
    private Button dontSaveButton;
    @FXML
    private Text saveInfoText;
    @FXML
    private Text largeScoreEnd;
    @FXML
    private Text smallScoreEnd;
    @FXML
    private TextField playerName;


    List<Bullets> bullets = new ArrayList<>();
    List<Bullets> bullets1 = new ArrayList<>();
    List<Character> enemies = new ArrayList<>();
    private ScoreBoard scoreBoard;

    Ship ship;

    private boolean UPpressed = false;
    private boolean LEFTpressed = false;
    private boolean RIGHTpressed = false;
    private boolean SPACEpressed = false, SPACEreleased = true;
    private boolean Apressed = false;
    private boolean DOWNpressed = false;

    boolean collision;

    private long lastUpdate = 0;

    private int currentLevelIndex = 0;
    Level[] levels = Level.createLevels();

    static Timeline timeline = new Timeline();

    public void initialize() {
        scoreBoard = new ScoreBoard("saves", "score_saves");
        updateScoreBoard();

        newGamePane = getMenuPane("newGame.fxml");
        savePane = getMenuPane("gameOver.fxml");

        menuContr.init(this);
        changeMenu("newGame.fxml");
    }

    public void initGame(gameListener gameListener) {
        this.gameListener = gameListener;
        gameListener.scoreChanged(this.score);
        gameListener.livesLeftChanged(this.lives);
        scoreBoardList.requestFocus();

        enemies = levels[0].getEnemyList();

    }

    public void startGame() {
        this.initGame(this);

        ship = new Ship(Width / 2, Height / 2);
        this.gamePane.getChildren().add(this.ship.getCharacter());
        addInvincibility(5,ship);

        enemies.forEach(enemy -> {
            gamePane.getChildren().add(enemy.getCharacter());
        });

        startGameLoop();
    }

    private void startGameLoop() {
        new AnimationTimer() {
            @Override
            public void handle(long now) {
                // set the ship controlling action
                shipAction(now,ship);

                // move the ship on the gamePane
                updateCharacters();

                gameListener.gameOver();
                gamePane.getChildren().remove(ship.getCharacter());
//
//                checkCollisions();
//
//                updateLevels();
            }
        }.start();
    }

    @FXML
    private void setPressedKey(KeyEvent event) {
        switch (event.getCode()) {
            case UP -> UPpressed = true;
            case LEFT -> LEFTpressed = true;
            case RIGHT -> RIGHTpressed = true;
            case SPACE -> SPACEpressed = true;
            case DOWN -> DOWNpressed = true;
            case A -> Apressed = true;
            default -> {
            }
        }
    }

    @FXML
    private void setReleasedKey(KeyEvent event) {
        switch (event.getCode()) {
            case UP -> UPpressed = false;
            case LEFT -> LEFTpressed = false;
            case RIGHT -> RIGHTpressed = false;
            case DOWN -> DOWNpressed = false;
            case SPACE -> {
                SPACEpressed = false;
                SPACEreleased = true;
            }
            case A -> Apressed = false;
            default -> {
            }
        }
    }

    public void shipAction(long now,Ship ship){
        if (UPpressed)
            ship.setHyperspaced(false);ship.acc();
        if (LEFTpressed)
            ship.turnLeft();
        if (RIGHTpressed)
            ship.turnRight();
        if (SPACEpressed && SPACEreleased && (now - lastUpdate > 330_000_000)) {
            Bullets bullet = new Bullets((int) (ship.getCharacter().getTranslateX()), (int) (ship.getCharacter().getTranslateY()));
            bullet.getCharacter().setRotate(ship.getCharacter().getRotate());
            bullets.add(bullet);
            bullet.acc();
            gamePane.getChildren().add(bullet.getCharacter());
            lastUpdate = now;
            SPACEreleased = false;
        }
        if (Apressed){
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
                for (Bullets bullet : bullets1) {
                    if (ship.collide(bullet)) {
                        collision=true;
                        break;
                    }
                }
            } while(collision);
            addInvincibility(5,ship);
        }
    }

    private void updateCharacters() {
        if (!ship.isHyperspaced()) {
            ship.move();
        }

        enemies.forEach(Character::move);

        bullets.forEach(bullet -> {
            bullet.move();
        });

        bullets1.forEach(bullet1 -> {
            bullet1.move();
        });
    }

    private void checkCollisions() {
        checkShipCollisions();

        bullets.forEach(bullet -> {
            enemies.forEach(enemy -> {
                if(enemy.collide(bullet)){
                    if(enemy.getSize() == 1){incrementScore(10);}
                    if(enemy.getSize() == 2 || enemy.getSize() == 3){
                        double X = enemy.getCharacter().getTranslateX();
                        double Y = enemy.getCharacter().getTranslateY();
                        int Z = enemy.getSize();
                        Downgrade((int)X+10,(int)Y+10,Z);
                        Downgrade((int)X-10,(int)Y-10,Z);
                    }
                    if(enemy.getSize() == 4){
                        incrementScore(100);
                        bullets1.clear();
                        bullets1.forEach(bullet1 -> {
                            gamePane.getChildren().remove(bullet1.getCharacter());
                        });
                    }
                    gamePane.getChildren().remove(enemy.getCharacter());
                    enemies.remove(enemy);
                    gamePane.getChildren().remove(bullet.getCharacter());
                    bullets.remove(bullet);
                }
            });
        });

        bullets1.forEach(bullet ->{
            if(ship.collide(bullet) && !ship.isInvincible()){
                gameListener.gameOver();
                gamePane.getChildren().remove(ship.getCharacter());
                gamePane.getChildren().remove(bullet.getCharacter());
                bullets.remove(bullet);
                bullets1.forEach(bullet1 -> {
                    gamePane.getChildren().remove(bullet1.getCharacter());
                });

            }
        });
    }
    private void checkShipCollisions() {
        enemies.forEach(enemy -> {
            if (enemy.collide(ship)) {
                if(lives > 0){
                    ship = new Ship(Width / 2, Height / 2);
                    gamePane.getChildren().add(ship.getCharacter());
                    gameListener.livesLeftChanged(lives -= 1);
                }
                if(lives == 0){
                    gameListener.gameOver();
                    gamePane.getChildren().remove(ship.getCharacter());
                }
            }
        });
    }

    public void Downgrade(int x,int y,int z){
        Asteroids asteroid = new Asteroids(x,y,z-1);
        enemies.add(asteroid);
        gamePane.getChildren().add(asteroid.getCharacter());
    }

    private void updateLevels() {
        if (enemies.isEmpty()) {
            currentLevelIndex++;
            if (currentLevelIndex >= levels.length) {
                gamePane.getChildren().remove(ship.getCharacter());
                gameListener.gameOver();
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

    public static void addInvincibility(int seconds,Ship ship) {
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

    public void changeMenu(String s) {
        switch (s) {
            case "newGame.fxml" -> menuContainer.setCenter(newGamePane);
            case "gameOver.fxml" -> menuContainer.setCenter(savePane);
            default -> this.menuContainer.setCenter((Node) null);
        }

    }

    @Override
    public void livesLeftChanged(int liveLeft) {
        livesLeft.setText(liveLeft + " lives left");
    }
    @Override
    public void scoreChanged(int newScore) {
        currentScore.setText("Score: " + newScore);
    }

    private void incrementScore(int score) {
        gameListener.scoreChanged(this.score += score);
    }

    public void addScore(String playerName) {
        scoreBoard.addScore(playerName, this.score);
        updateScoreBoard();
    }
    @Override
    public void gameOver() {
        UPpressed = false;
        LEFTpressed = false;
        RIGHTpressed = false;

        menuContr.gameOver(this.score, this.scoreBoard.getHighScore());

    }


    private void updateScoreBoard() {
        scoreBoardList.setItems(scoreBoard.getScores().stream()
                .limit(18)
                .map(element -> scoreBoard.getScores().indexOf(element) + 1 + ". " + element.getKey() + ": "
                        + element.getValue())
                .collect(Collectors.toCollection(FXCollections::observableArrayList)));
    }



}
