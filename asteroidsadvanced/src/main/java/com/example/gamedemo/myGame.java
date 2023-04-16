package com.example.gamedemo;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;


public class myGame extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        stage.setTitle("Asteroids");
        stage.setScene(new Scene(FXMLLoader.load(Objects.requireNonNull(getClass().getResource("main.fxml")))));
        stage.show();
    }

    public static void main(String[] args) {
        Application.launch(args);
    }


}
