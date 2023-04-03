package com.example.asteroidsadvanced;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Polygon;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.security.Key;
import java.util.*;

public class myGame extends Application {
    public static int Width = 1980;
    public static int Height = 1080;
    public int points = 0;
    List<Bullets> bullets = new ArrayList<>();
    List<Bullets> bullets1 = new ArrayList<>();
    public boolean surprise = true;
    Pane pane = new Pane();
    double Rotation = 0;
    @Override
    public void start(Stage stage) throws Exception {
        Random rnd = new Random();
        pane.setPrefSize(Width, Height);
        Ship ship = new Ship(Width/2,Height/2);
        pane.getChildren().add(ship.getCharacter());
        Text text = new Text(10,20,"Points:"+points);

        Scene scene = new Scene(pane);
        stage.setScene(scene);
        stage.show();

        Map<KeyCode, Boolean> pressedKey = new HashMap<>();
        scene.setOnKeyPressed(keyEvent -> {
            pressedKey.put(keyEvent.getCode(), Boolean.TRUE);
        });
        scene.setOnKeyReleased(keyEvent -> {
            pressedKey.put(keyEvent.getCode(),Boolean.FALSE);
        });

        new AnimationTimer(){
            private long lastUpdate = 0;

            public void handle(long now){
                if(pressedKey.getOrDefault(KeyCode.A,false)){ship.Hyperspace();}
                if(pressedKey.getOrDefault(KeyCode.LEFT, false)){ship.turnLeft();}
                if(pressedKey.getOrDefault(KeyCode.RIGHT,false)){ship.turnRight();}
                if(pressedKey.getOrDefault(KeyCode.UP,false)){ship.acc();}
                if(pressedKey.getOrDefault(KeyCode.SPACE, false) && (now - lastUpdate >330_000_000)){
                    Bullets bullet = new Bullets((int)(ship.getCharacter().getTranslateX()),(int)(ship.getCharacter().getTranslateY()));
                    bullet.getCharacter().setRotate(ship.getCharacter().getRotate());
                    bullets.add(bullet);
                    bullet.acc();
                    pane.getChildren().add(bullet.getCharacter());
                    lastUpdate = now;
                }



                ship.move();

                bullets.forEach(bullet -> {
                    bullet.move();
                });
                bullets1.forEach(bullet1 -> {
                    bullet1.move();
                });

            }
        }.start();
    }


    public static void main(String[] args){
        launch(args);
    }
}
