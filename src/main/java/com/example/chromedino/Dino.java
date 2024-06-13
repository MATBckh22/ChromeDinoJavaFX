package com.example.chromedino;

import javafx.geometry.BoundingBox;
import javafx.geometry.Bounds;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import misc.Controls;
import misc.Animation;
import misc.DinoState;

public class Dino {
    //Hitboxes for dino
    private static final int[] HITBOX_RUN = {19, 90, -32, -42};
    private static final int[] HITBOX_DOWN_RUN = {24, 55, 5, -25};
    public static final double X = 120;
    private double y = 0;

    private static final double GROUND_DINO = 250;
    private static final double GROUND_Y = 280;
    private static final double SPEED_Y = -20;
    private static final double GRAVITY = 1.3;

    private Land land;
    private Controls controls;
    private GraphicsContext gc;

    private Image dinoJumpImage;
    private Image dinoDeadImage;
    private Animation dinoRun;
    private Animation dinoDownRun;

    private double dinoY = GROUND_DINO;
    private double speedY = 0;
    private boolean isJumping = false;
    private DinoState currentState = DinoState.DINO_RUN;

    public static final int SCREEN_WIDTH = 1200;
    public static final int SCREEN_HEIGHT = 300;

    public Dino(Controls controls, GraphicsContext gc) {
        this.controls = controls;
        this.gc = gc;

        // Initialize dinoRun animation
        dinoRun = new Animation(5); // 150 milliseconds for each frame
        dinoRun.addSprite(new Image("dino-run-1.png"));
        dinoRun.addSprite(new Image("dino-run-2.png"));
        dinoRun.start();

        // Initialize dinoDownRun animation
        dinoDownRun = new Animation(5);
        dinoDownRun.addSprite(new Image("dino-down-run-1.png"));
        dinoDownRun.addSprite(new Image("dino-down-run-2.png"));
        dinoDownRun.start();

        // Initialize dinoJump image
        dinoJumpImage = new Image("dino-jump.png");

        // Initialize dinoDeadImage image
        // Initialize dinoDeadImage image
        dinoDeadImage = new Image("dino-dead.png");
    }

    public Bounds getHitBox() {
        double hitboxY;
        double width, height;
        switch (currentState) {
            case DINO_RUN:
            case DINO_JUMP:
            case DINO_DEAD:
                hitboxY = dinoY - HITBOX_RUN[1];
                width = dinoRun.getSprite().getWidth();
                height = dinoRun.getSprite().getHeight();
                break;
            case DINO_DOWN_RUN:
                hitboxY = dinoY - HITBOX_DOWN_RUN[1];
                width = dinoDownRun.getSprite().getWidth() - HITBOX_DOWN_RUN[2];
                height = dinoDownRun.getSprite().getHeight() + HITBOX_DOWN_RUN[3];
                break;
            default:
                return null;
        }
        return new BoundingBox(X - HITBOX_RUN[0], hitboxY, width, height);
    }

    public void updateMovement() {
        if (controls.isPressedUp() && !isJumping) {
            isJumping = true;
            currentState = DinoState.DINO_JUMP;
            speedY = SPEED_Y;
        }

        //jump logic
        if (isJumping) {
            dinoY += speedY;
            speedY += GRAVITY;

            if (dinoY >= GROUND_Y) {
                dinoY = GROUND_DINO;
                speedY = 0;
                isJumping = false;
                currentState = DinoState.DINO_RUN;
            }
        } else if (controls.isPressedDown()) {
            currentState = DinoState.DINO_DOWN_RUN;
        } else {
            currentState = DinoState.DINO_RUN;
        }

        // Update the appropriate animation
        if (currentState == DinoState.DINO_RUN) {
            dinoRun.updateSprite();
        } else if (currentState == DinoState.DINO_DOWN_RUN) {
            dinoDownRun.updateSprite();
        }
        y = dinoY;
    }

    public void draw() {
        // Render dinosaur animation based on state
        Image currentDinoImage = null;
        switch (currentState) {
            case DINO_RUN:
                currentDinoImage = dinoRun.getSprite();
                break;
            case DINO_DOWN_RUN:
                currentDinoImage = dinoDownRun.getSprite();
                break;
            case DINO_JUMP:
                currentDinoImage = dinoJumpImage;
                break;
            default:
                break;
        }
        if (currentDinoImage != null) {
            gc.drawImage(currentDinoImage, 100, dinoY - currentDinoImage.getHeight()); // Adjust position as needed
        }
    }

    public void drawHitBox(GraphicsContext gc) {
        gc.setStroke(Color.GREEN);
        gc.setLineWidth(1);
        gc.strokeRect(getHitBox().getMinX(), getHitBox().getMinY(), getHitBox().getWidth(), getHitBox().getHeight());
    }
}