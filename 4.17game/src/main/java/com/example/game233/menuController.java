package com.example.game233;

import java.util.regex.Pattern;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

public class menuController {
    private gameListener gameListener;
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
    @FXML
    private Pane newGamePane;
    @FXML
    private Pane savePane;

    public menuController() {
    }

    public void init(Controller controller) {
        this.controller = controller;
        this.initKeyHandles();
    }

    private void initKeyHandles() {
        this.newGameButton.setOnAction((event) -> {
            this.startGame();
        });
        this.saveButton.setOnAction((event) -> {
            this.handleSave();
        });
        this.dontSaveButton.setOnAction((event) -> {
            this.handleDontSave();
        });
    }

    @FXML
    private void startGame() {
        this.controller.changeMenu(" ");
        this.newGameButton.setDefaultButton(true);
        this.controller.startGame();
    }

    @FXML
    private void handleSave() {
        this.controller.addScore(this.playerName.getText().trim());
        this.handleDontSave();
    }

    @FXML
    private void handleDontSave() {
        this.controller.changeMenu("newGame.fxml");
        this.newGameButton.setDefaultButton(true);
    }

    @FXML
    private void playerNameInputChanged() {
        int textInputLength = this.playerName.getText().trim().length();
        if (textInputLength == 0) {
            this.saveInfoText.setText("Enter playername to save score");
            this.saveInfoText.setFill(Color.WHITE);
            this.saveButton.setDisable(true);
        } else if (textInputLength > 14) {
            this.saveInfoText.setText("Name cannot exceed 14 characters");
            this.saveInfoText.setFill(Color.RED);
            this.saveButton.setDisable(true);
        } else if (!Pattern.matches("^[a-zA-Z0-9]*$", this.playerName.getText().trim())) {
            this.saveInfoText.setText("Playername cannot include special characters");
            this.saveInfoText.setFill(Color.RED);
            this.saveButton.setDisable(true);
        } else {
            this.saveInfoText.setText("Enter playername to save score");
            this.saveInfoText.setFill(Color.WHITE);
            this.saveButton.setDisable(false);
        }

    }

    public void gameOver(int score, int highscore) {
        this.controller.changeMenu("gameOver.fxml");
        this.newGameButton.setDefaultButton(false);
        this.playerName.requestFocus();
        this.saveButton.setDefaultButton(true);
        this.dontSaveButton.setCancelButton(true);
        this.largeScoreEnd.setText(score > highscore ? "New Highscore!" : "Game over!");
        this.smallScoreEnd.setText("Score: " + score);
    }
}
