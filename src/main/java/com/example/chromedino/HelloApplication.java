package com.example.chromedino;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import misc.Controls;
import misc.Animation;
import misc.DinoState;

import java.io.IOException;

public class HelloApplication extends Application {

    private GraphicsContext gc;
    private Land land;
    private Cactuses cactuses;
    private Clouds clouds;
    private Dino dino;
    private Controls controls;
    public static final int SCREEN_WIDTH = 1200;
    public static final int SCREEN_HEIGHT = 300;
    static final int GROUND_Y = 0;
    private double speed = 5.0;
    private int score;

    @Override
    public void start(Stage stage) throws IOException {
        Group root = new Group();
        Canvas canvas = new Canvas(SCREEN_WIDTH, SCREEN_HEIGHT); //canvas stage size
        root.getChildren().add(canvas);

        gc = canvas.getGraphicsContext2D();

        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setWidth(SCREEN_WIDTH);
        stage.setHeight(SCREEN_HEIGHT);
        stage.setFullScreen(false);
        stage.setResizable(false);
        stage.setTitle("Chrome Dino");

        stage.show(); //show stage

        controls = new Controls(scene);

        //Initialization
        land = new Land(GROUND_Y, speed,"/land.png");
        cactuses = new Cactuses();
        clouds = new Clouds(speed);
        dino = new Dino(controls, gc);
        //new GameLoop.start();
        new AnimationTimer(){
            @Override
            public void handle(long now){
                update();
                render();
            }
        }.start();
    }
    private void update(){
        land.update();
        cactuses.updatePosition();
        clouds.updatePosition();
        if(cactuses.spaceAvailable()){
            cactuses.createCactuses();
        }
        dino.updateMovement();
        cactuses.iscollision(dino.getHitBox());
    }

    private void render(){
        gc.setFill(Color.WHITE);
        gc.fillRect(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);
        land.render(gc);
        cactuses.draw(gc);
        clouds.draw(gc);
        dino.draw();
        cactuses.drawHitBox(gc);
        dino.drawHitBox(gc);
    }

    public static void main(String[] args) {
        launch(args);
    }
}