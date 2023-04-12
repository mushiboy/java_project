package com.example.gamedemo;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

import java.util.regex.Pattern;

public class menuController {
    Controller controller;
    @FXML
    private Button newGameButton,saveButton,dontSaveButton;
    @FXML
    private TextField playerName;
    @FXML
    private Text saveInfoText, scoreTextSmall, scoreTextLarge;

    public void init(Controller controller){
        this.controller = controller;
        initKeyHandles();
    }


    private void initKeyHandles() {
        newGameButton.setOnAction(event -> {
            startGame();
        });
        saveButton.setOnAction(event -> {
            handleSave();
        });
        dontSaveButton.setOnAction(event -> {
            handleDontSave();
        });

    }

    @FXML
    private void startGame(){
        controller.changeMenu(" ");
        newGameButton.setDefaultButton(true);
        controller.startGame();
    }

    @FXML
    private void playerNameInputChanged() {
        int textInputLength = playerName.getText().trim().length();

        if (textInputLength == 0) {
            saveInfoText.setText("Enter playername to save score");
            saveInfoText.setFill(Color.WHITE);
            saveButton.setDisable(true);
        } else if (textInputLength > 14) {
            saveInfoText.setText("Name cannot exceed 14 characters");
            saveInfoText.setFill(Color.RED);
            saveButton.setDisable(true);
        } else if (!Pattern.matches("^[a-zA-Z0-9]*$",
                playerName.getText().trim())) {
            saveInfoText.setText("Playername cannot include special characters");
            saveInfoText.setFill(Color.RED);
            saveButton.setDisable(true);
        } else {
            saveInfoText.setText("Enter playername to save score");
            saveInfoText.setFill(Color.WHITE);
            saveButton.setDisable(false);
        }
    }

    @FXML
    private void  handleSave(){
        controller.addScore(playerName.getText().trim());
        handleDontSave();
    }
    @FXML
    private void handleDontSave() {
        controller.changeMenu("newGame.fxml");
    }


    public void gameOver(int score, int highscore) {
        controller.changeMenu("newGame.fxml");
        newGameButton.setDefaultButton(false);

        playerName.requestFocus();
        saveButton.setDefaultButton(true);
        dontSaveButton.setCancelButton(true);
        scoreTextLarge.setText(score > highscore ? "New Highscore!" : "Game over!");
        scoreTextSmall.setText("Score: " + score);
    }
}
