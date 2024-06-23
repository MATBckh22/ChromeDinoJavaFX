package com.example.chromedino;

import UserInterface.GameScene;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.stage.Stage;
import misc.Controls;
    
import java.io.IOException;

public class GameStage extends Application {
    private GameScene Gamescene;
    private Controls controls;
    private GraphicsContext gc;

    public static final int SCREEN_WIDTH = 1200;
    public static final int SCREEN_HEIGHT = 300; //270 if transparent window bar

    @Override
    public void start(Stage stage) throws IOException {
        Group root = new Group();
        Canvas canvas = new Canvas(SCREEN_WIDTH, SCREEN_HEIGHT);
        gc = canvas.getGraphicsContext2D();
        root.getChildren().add(canvas);

        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setWidth(SCREEN_WIDTH);
        stage.setHeight(SCREEN_HEIGHT);
        stage.setResizable(false);
        stage.setTitle("Chrome Dino");
        //stage.initStyle(StageStyle.TRANSPARENT);
        controls = new Controls(scene);

        Gamescene = new GameScene(scene, gc);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}