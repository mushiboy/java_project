package com.example.asteroidsadvanced;

import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Begin {
    Controller newGame;

    public void startGame(ActionEvent actionEvent) throws IOException {
        newGame = new Controller();
        newGame.clean();
        newGame.start(actionEvent);
    }

    public void display(){
        VBox root = new VBox();
//        root.setStyle("-fx-background-color: black;");

        StringBuilder content = new StringBuilder();
        try{
            BufferedReader reader = new BufferedReader(new FileReader("src/main/resources/highScores.txt"));
            String line;

            while((line = reader.readLine()) != null){
                content.append(line).append("\n");
            }
            reader.close();
        } catch (IOException e){
            e.printStackTrace();
        }
        // Create the high scores label and set its font and color
        Label highScoresLabel = new Label(content.toString());
        highScoresLabel.setFont(Font.font("Brush Script MT", 20));
        highScoresLabel.setTextFill(Color.BLACK);

        // Wrap the high scores label in a scroll pane
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(highScoresLabel);
        scrollPane.setFitToWidth(true);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);

        // Add the scroll pane to the pane and the pane to the root container
        root.getChildren().add(scrollPane);


        // Create the scene with the root container and set its size
        Scene scene = new Scene(root, 500, 300);

        // Create the stage and set its scene
        Stage stage = new Stage();
        stage.setScene(scene);

        // Show the stage
        stage.show();
    }

    public void Introduction(){
        VBox root = new VBox();
        root.setStyle("-fx-background-color: black;");
        Pane pane = new Pane();
        StringBuilder content = new StringBuilder();
        try{
            BufferedReader reader = new BufferedReader(new FileReader("src/main/resources/introduction.txt"));
            String line;

            while((line = reader.readLine()) != null){
                int i = 0;
                while(i<line.length()){
                    content.append(line.charAt(i));
                    i++;
                }
                content.append("\n");
            }
            reader.close();
        } catch (IOException e){
            e.printStackTrace();
        }
        Label label = new Label(content.toString());
        label.setFont(Font.font("Brush Script MT", 20));
        label.setTextFill(Color.WHITE);
        pane.getChildren().add(label);
        root.getChildren().add(pane);
        Scene scene = new Scene(root, 800, 600);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.show();
    }
}
