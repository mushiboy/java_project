package com.example.gamedemo;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Objects;


public class myGame extends Application {
    public static int Width = 1100;
    public static int Height = 680;

    @Override
    public void start(Stage stage) throws Exception {
        stage.setTitle("Asteroids");
        stage.setScene(new Scene(FXMLLoader.load(Objects.requireNonNull(getClass().getResource("main.fxml")))));
        stage.show();
    }


}
