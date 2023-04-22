// This package declaration specifies the location of the class within the project
package com.example.asteroidsadvanced;

// Import the necessary JavaFX classes
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

// MyGame class extends the JavaFX Application class and serves as the main class for the game
public class MyGame extends Application {
    // The start method is called when the JavaFX application is launched
    @Override
    public void start(Stage stage) throws Exception {
        // Load the menu FXML file using the FXMLLoader
        FXMLLoader fxmlLoader = new FXMLLoader(MyGame.class.getResource("menu.fxml"));

        // Create a new Scene with the loaded FXML and set its dimensions
        Scene scene = new Scene(fxmlLoader.load(), 640, 440);

        // Set the title for the stage (window)
        stage.setTitle("Asteroids");

        // Set the scene on the stage
        stage.setScene(scene);

        // Show the stage (make it visible)
        stage.show();
    }

    // The main method serves as the entry point for the application
    public static void main(String[] args) {

        // Launch the JavaFX application
        launch(args);
    }
}
