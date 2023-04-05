// Importing necessary libraries
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

// Class Definition
public class myGame extends Application {

    // Defining class variables
    public static int Width = 1980;
    public static int Height = 1080;
    public int points = 0;
    List<Bullets> bullets = new ArrayList<>();
    List<Bullets> bullets1 = new ArrayList<>();
    List<Asteroids> asteroids = new ArrayList<>();
    List<Alien> aliens = new ArrayList<>();
    public boolean surprise = true;
    Pane pane = new Pane();
    double Rotation = 0;

    // Start Method to Initialize the Game
    @Override
    public void start(Stage stage) throws Exception {
        // Creating Random Object
        Random rnd = new Random();

        // Creating Pane and Setting Preferences
        pane.setPrefSize(Width, Height);

        // Adding Ship
        Ship ship = new Ship(Width/2,Height/2);
        pane.getChildren().add(ship.getCharacter());

        // Adding Score Text
        Text text = new Text(10,20,"Points:"+points);
        pane.getChildren().add(text);

        // Adding Asteroids
        while(asteroids.size() < 10){
            Asteroids asteroid = new Asteroids(rnd.nextInt(1000), rnd.nextInt(1000), rnd.nextInt(1,4));
            asteroids.add(asteroid);
        }
        asteroids.forEach(asteroid -> {
            pane.getChildren().add(asteroid.getCharacter());
        });

        // Creating Scene and Setting Stage
        Scene scene = new Scene(pane);
        stage.setScene(scene);
        stage.show();

        // Key Pressed and Key Released Events
        Map<KeyCode, Boolean> pressedKey = new HashMap<>();
        scene.setOnKeyPressed(keyEvent -> {
            pressedKey.put(keyEvent.getCode(), Boolean.TRUE);
        });
        scene.setOnKeyReleased(keyEvent -> {
            pressedKey.put(keyEvent.getCode(),Boolean.FALSE);
        });

        // Animation Timer to Run the Game
        new AnimationTimer(){
            private long lastUpdate = 0;
            private long lastbullets = 0;

            public void handle(long now){
                // Hyperspace
                if(pressedKey.getOrDefault(KeyCode.A,false)){ship.Hyperspace();}
                // Turning Left
                if(pressedKey.getOrDefault(KeyCode.LEFT, false)){ship.turnLeft();}
                // Turning Right
                if(pressedKey.getOrDefault(KeyCode.RIGHT,false)){ship.turnRight();}
                // Accelerating
                if(pressedKey.getOrDefault(KeyCode.UP,false)){ship.acc();}
                // Firing Bullets
                if(pressedKey.getOrDefault(KeyCode.SPACE, false) && (now - lastUpdate >330_000_000)){
                    // create new bullet object
                    Bullets bullet = new Bullets((int)(ship.getCharacter().getTranslateX()),(int)(ship.getCharacter().getTranslateY()));
                    // set bullet rotation
                    bullet.getCharacter().setRotate(ship.getCharacter().getRotate());
                    // add bullet to the bullets list
                    bullets.add(bullet);
                    // move bullet forward
                    bullet.acc();
                    // add bullet to the pane
                    pane.getChildren().add(bullet.getCharacter());
                    // update timestamp
                    lastUpdate = now;
                }
                // Adding Aliens
                if( rnd.nextInt(100)<2 && aliens.size() == 0){
                    // create a new alien object at a random position
                    Alien alien = new Alien(1,rnd.nextInt(100,900));
                    // add alien to the aliens list
                    aliens.add(alien);
                    // add alien to the pane
                    pane.getChildren().add(alien.getCharacter());
                }
                // Alien Shooting Bullets
                if(aliens.size() == 1){
                    while(now - lastbullets > 440_000_000 && aliens.size()>0){
                        // create new bullet object for alien
                        Bullets bullet1 = new Bullets((int)(aliens.get(0).getCharacter().getTranslateX()),(int)(aliens.get(0).getCharacter().getTranslateY()));
                        // set bullet rotation
                        bullet1.getCharacter().setRotate(Rotation += 60);
                        // add bullet to the bullets1 list
                        bullets1.add(bullet1);
                        // move bullet forward
                        bullet1.acc();
                        // add bullet to the pane
                        pane.getChildren().add(bullet1.getCharacter());
                        // update timestamp
                        lastbullets = now;
                    }

                    // move the alien
                    aliens.get(0).move();
                }

                // move the player's ship
                ship.move();
                // move all the asteroids
                asteroids.forEach(asteroid -> {
                    asteroid.move();

                });
                // move all the bullets fired by the player's ship
                bullets.forEach(bullet -> {
                    bullet.move();
                });
                // move all the bullets fired by the aliens
                bullets1.forEach(bullet1 -> {
                    bullet1.move();
                });
                // check if any of the player's ship's bullets hit an asteroid
                bullets.forEach(bullet -> {
                    asteroids.forEach(asteroid -> {
                        if(asteroid.collide(bullet)){
                             // if the asteroid is at level 1, increase the points and remove the asteroid and bullet
                            if(asteroid.getLevel() == 1){points += 10;text.setText("Points:"+points);}
                            // if the asteroid is not at level 1, create two smaller asteroids of the next lower level, remove the original asteroid and bullet
                            if(asteroid.getLevel() != 1){
                                double X = asteroid.getCharacter().getTranslateX();
                                double Y = asteroid.getCharacter().getTranslateY();
                                int Z = asteroid.getLevel();
                                Downgrade((int)X+10,(int)Y+10,Z);
                                Downgrade((int)X-10,(int)Y-10,Z);
                            }
                            // Remove the asteroid and bullet from the game
                            pane.getChildren().remove(asteroid.getCharacter());
                            asteroids.remove(asteroid);
                            pane.getChildren().remove(bullet.getCharacter());
                            bullets.remove(bullet);
                        }
                    });
                });

                // check if any of the player's ship's bullets hit an alien ship
                bullets.forEach(bullet ->{
                    aliens.forEach(alien -> {
                        if(alien.collide(bullet)){

                            // Remove the alien ship and bullet from the game, and increase the player's points
                            pane.getChildren().remove(alien.getCharacter());
                            aliens.remove(alien);
                            pane.getChildren().remove(bullet.getCharacter());
                            bullets.remove(bullet);
                            bullets1.forEach(bullet1 -> {
                                pane.getChildren().remove(bullet1.getCharacter());
                            });
                            surprise = false;
                            bullets1.clear();
                            points += 100;
                            text.setText("Points:"+points);
                        }
                    });
                });
            }
        }.start();
    }

    // define Downgrade function to create two smaller asteroids from a larger one
    public void Downgrade(int x,int y,int z){
        Asteroids asteroid = new Asteroids(x,y,z-1);
        asteroids.add(asteroid);
        pane.getChildren().add(asteroid.getCharacter());

    }

    // main method to launch the game
    public static void main(String[] args){
        launch(args);
    }
}
