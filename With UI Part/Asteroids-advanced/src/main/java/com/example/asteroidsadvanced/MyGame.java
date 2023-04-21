package com.example.asteroidsadvanced;

import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import java.util.*;

public class MyGame extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        //FXML
        FXMLLoader fxmlLoader = new FXMLLoader(MyGame.class.getResource("menu.fxml"));
        Scene scene = new Scene(fxmlLoader.load(),320,440);
        stage.setTitle("Asteroids");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args){
        launch(args);
    }
}