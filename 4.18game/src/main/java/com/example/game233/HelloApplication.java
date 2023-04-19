package com.example.game233;

import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class HelloApplication extends Application {

    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("main.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1100.0, 700.0);
        stage.setTitle("Asteroids");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(new String[0]);
    }
}
