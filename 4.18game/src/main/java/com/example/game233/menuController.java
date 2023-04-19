package com.example.game233;

import java.util.regex.Pattern;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

public class menuController {
    private gameListener gameListener;
    Controller controller;
    @FXML
    private BorderPane menuContainer;
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


    public void init(Controller controller) {
        this.controller = controller;
        newGamePane = controller.getMenuPane("newGame.fxml");
        initKeyHandles();
    }

    private void initKeyHandles() {
        newGameButton.setOnAction((event) -> {
            startGame();
        });
        saveButton.setOnAction((event) -> {
            handleSave();
        });
        dontSaveButton.setOnAction((event) -> {
            handleDontSave();
        });
    }

    @FXML
    private void startGame() {
        controller.changeMenu(" ");
        newGameButton.setDefaultButton(true);
        controller.startGame();
    }

    @FXML
    private void handleSave() {
        controller.addScore(this.playerName.getText().trim());
        handleDontSave();
    }

    @FXML
    private void handleDontSave() {
        savePane.setVisible(false);
        newGamePane.setVisible(true);
        newGameButton.setDefaultButton(true);

    }

    @FXML
    private void playerNameInputChanged() {
        int textInputLength = this.playerName.getText().trim().length();
        if (textInputLength == 0) {
            saveInfoText.setText("Enter playername to save score");
            saveInfoText.setFill(Color.WHITE);
            saveButton.setDisable(true);
        } else if (textInputLength > 14) {
            saveInfoText.setText("Name cannot exceed 14 characters");
            saveInfoText.setFill(Color.RED);
            saveButton.setDisable(true);
        } else if (!Pattern.matches("^[a-zA-Z0-9]*$", this.playerName.getText().trim())) {
            saveInfoText.setText("Playername cannot include special characters");
            saveInfoText.setFill(Color.RED);
            saveButton.setDisable(true);
        } else {
            saveInfoText.setText("Enter playername to save score");
            saveInfoText.setFill(Color.WHITE);
            saveButton.setDisable(false);
        }

    }

    public void gameOver(int score, int highscore) {
        controller.changeMenu("gameOver.fxml");
        newGameButton.setDefaultButton(false);
        playerName.requestFocus();
        saveButton.setDefaultButton(true);
        dontSaveButton.setCancelButton(true);
        largeScoreEnd.setText(score > highscore ? "New Highscore!" : "Game over!");
        smallScoreEnd.setText("Score: " + score);
    }
}
