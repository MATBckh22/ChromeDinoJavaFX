package com.example.chromedino;

import UserInterface.GameScreen;
//import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
//import javafx.scene.image.Image;
//import javafx.scene.image.ImageView;
//import javafx.scene.paint.Color;
import javafx.stage.Stage;
import misc.Controls;
//import misc.Animation1;
//import misc.DinoState1;

import java.io.IOException;

//import com.example.chromedino.Land;
//import com.example.chromedino.Clouds;
//import com.example.chromedino.Cactuses;
//import com.example.chromedino.Birds;
import com.example.chromedino.Dino;
import misc.EnemyManager;
import misc.GameState;

public class HelloApplication extends Application {
    private GameScreen gameScreen;
    private GraphicsContext gc;
    //private Land land;
    //private Cactuses1 cactuses;
    //private Clouds clouds;
    //private Dino dino;
    //private Birds1 bird;
    //private Controls controls;
    //private EnemyManager enemyManager;
    public static final int SCREEN_WIDTH = 1200;
    public static final int SCREEN_HEIGHT = 300;
    //static final int GROUND_Y = 0;
    //private double speed = 12.0; //dskd: 5.0
    //private int score;

    @Override
    public void start(Stage stage) throws IOException {
        Group root = new Group();
        Canvas canvas = new Canvas(SCREEN_WIDTH, SCREEN_HEIGHT);
        gc = canvas.getGraphicsContext2D();
        root.getChildren().add(canvas);
        //gameScreen = new GameScreen(new Scene(root), gc);
        //root.getChildren().add(gameScreen);

        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setWidth(SCREEN_WIDTH);
        stage.setHeight(SCREEN_HEIGHT);
        stage.setResizable(false);
        stage.setTitle("Chrome Dino");

        new GameScreen(gc);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}