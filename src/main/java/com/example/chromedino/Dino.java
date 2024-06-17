package com.example.chromedino;

import javafx.geometry.BoundingBox;
import javafx.geometry.Bounds;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
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
    private static final double SPEED_Y = -20; // Initially -20 dskd: -12
    private static final double GRAVITY = 1.3; // Initially 1.3 dskd: 0.4

    private Controls controls;
    private GraphicsContext gc;

    private Animation dinoRun;
    private Animation dinoDownRun;

    private double dinoY = GROUND_DINO;
    private double speedY = 0;
    private boolean isJumping = false;
    private DinoState currentState;

    private Image dinojump;
    private Image dinorun1;
    private Image dinorun2;
    private Image dinodownrun1;
    private Image dinodownrun2;
    private Image dinodead;

    public static final int SCREEN_WIDTH = 1200;
    public static final int SCREEN_HEIGHT = 300;

    public Dino(Controls controls, GraphicsContext gc) {
        this.controls = controls;
        this.gc = gc;

        // Initialize dinoRun animation
        dinoRun = new Animation(5); // 150 milliseconds for each frame

        // Initialize dinoDownRun animation
        dinoDownRun = new Animation(5);

        //Initialize dino at jump state
        currentState = DinoState.DINO_JUMP;
    }

    public DinoState getDinoState(){
        return currentState;
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
        if (currentState == DinoState.DINO_DEAD) {
            return; // Do not update movement if the dino is dead
        }

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

    public void setDinoJumpImage(String DinoJump) {
        dinojump = new Image(DinoJump);
        System.out.println(DinoJump);
    }

    public void setDinoRun1Image(String DinoRun1) {
        dinorun1 = new Image(DinoRun1);
        dinoRun.clearSprites();
        dinoRun.addSprite(dinorun1);  // Add sprite to animation
    }

    public void setDinoRun2Image(String DinoRun2) {
        dinorun2 = new Image(DinoRun2);
        dinoRun.addSprite(dinorun2);  // Add sprite to animation
    }

    public void setDinoDownRun1Image(String DinoDownRun1) {
        dinodownrun1 = new Image(DinoDownRun1);
        dinoDownRun.clearSprites();
        dinoDownRun.addSprite(dinodownrun1);  // Add sprite to animation
    }

    public void setDinoDownRun2Image(String DinoDownRun2) {
        dinodownrun2 = new Image(DinoDownRun2);
        dinoDownRun.addSprite(dinodownrun2);  // Add sprite to animation
    }

    public void setDinoDeadImage(String DinoDead) {
        dinodead = new Image(DinoDead);
    }

    public Image getDinoJumpImage(){
        System.out.println(dinojump);
        return dinojump;
    }

    public Image getDinoRun1Image(){
        return dinorun1;
    }

    public Image getDinoRun2Image(){
        return dinorun2;
    }

    public Image getDinoDownRun1Image(){
        return dinodownrun1;
    }

    public Image getDinoDownRun2Image(){
        return dinodownrun2;
    }

    public Image getDinoDeadImage(){
        return dinodead;
    }

    public void setDead() {
        currentState = DinoState.DINO_DEAD;
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
                currentDinoImage = dinojump;
                break;
            case DINO_DEAD:
                currentDinoImage = dinodead;
                break;
            default:
                break;
        }
        System.out.println(currentDinoImage);
        if (currentDinoImage != null) {
            gc.drawImage(currentDinoImage, 100, dinoY - currentDinoImage.getHeight()); // Adjust position as needed
        }
    }

    public void drawHitBox(GraphicsContext gc) {
        gc.setStroke(Color.GREEN);
        gc.setLineWidth(1);
        gc.strokeRect(getHitBox().getMinX(),
                getHitBox().getMinY(), getHitBox().getWidth(), getHitBox().getHeight());
    }
    public void setDinoState(DinoState dinoState) {
        this.currentState = dinoState;
    }

    public void jump() {
        if(y == GROUND_Y - dinoRun.getSprite().getHeight()) {
            speedY = SPEED_Y;
            y += speedY;
        }
    }

    public void resetDino() {
        y = GROUND_Y - dinojump.getHeight();
        currentState = DinoState.DINO_RUN;
    }

    public void dinoGameOver() {
        if(y > GROUND_Y - dinodead.getHeight())
            y = GROUND_Y - dinodead.getHeight();
        currentState = DinoState.DINO_DEAD;
    }
}