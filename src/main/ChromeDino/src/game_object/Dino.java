package game_object;

import javafx.geometry.BoundingBox;
import javafx.geometry.Bounds;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

import manager.SoundManager;
import misc.Animation;
import misc.Controls;
import misc.DinoState;

/**
 * Dino class updates its movement and renders the dino
 * It includes a jump logic function
 * It handles the spawning and hit box drawing of dino
 * With private DinoState currentState, it sets the current state and return the image of the current state
 */

public class Dino {
    // Hit boxes for dino
    private static final int[] HITBOX_RUN = {19, 90, -32, -42};
    private static final int[] HITBOX_DOWN_RUN = {24, 55, 5, -25};
    public static final double X = 120; // Initial X position of dino
    private double y = 0; // Initial Y position of dino

    private static final double GROUND_DINO = 250; // Ground level of dino
    private static final double GROUND_Y = 280; // Y position of the ground
    private static final double SPEED_Y = -17; // Jumping Speed
    private static final double GRAVITY = 0.7;

    private Controls controls;
    private GraphicsContext gc;

    private Animation dinoRun;
    private Animation dinoDownRun;
    private SoundManager jumpSound;

    private double dinoY = GROUND_DINO; // Initial Y position of dino
    private double speedY = 0; // Initial dino speed
    private boolean isJumping = false; // Jumping state
    private DinoState currentState;
    
    private Image dinojump;
    private Image dinorun1;
    private Image dinorun2;
    private Image dinodownrun1;
    private Image dinodownrun2;
    private Image dinodead;

    public static final int SCREEN_WIDTH = 1200;
    public static final int SCREEN_HEIGHT = 300;

    // Constructor
    public Dino(Controls controls, GraphicsContext gc) {
    	this.controls = controls;
        this.gc = gc;

        // Initialize dinoRun animation
        dinoRun = new Animation(150);

        // Initialize dinoDownRun animation
        dinoDownRun = new Animation(150);

        //Initialize dino at jump state
        currentState = DinoState.DINO_JUMP;
        
        // Initialize jump sound
        jumpSound = new SoundManager("resources/jump.wav");
    }

    // Get the current state of the dino
    public DinoState getDinoState() {
        return currentState;
    }
    
    // Get the hit box for current state of dino
    public Bounds getHitBox() {
        double hitboxY;
        double width, height;
        switch (currentState) {
            case DINO_RUN: // Repeat the statement of DINO_DEAD
            case DINO_JUMP: // Repeat the statement of DINO_DEAD
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
                return null; // if break, can't return the BoundingBox below
        }
        return new BoundingBox(X - HITBOX_RUN[0], hitboxY, width, height);
    }
    
    // Update the movement of dino
    public void updateMovement() {
    	if (currentState == DinoState.DINO_DEAD) {
            return; // Do not update movement if the dino is dead
        }
    	
    	// Control jump if up key bind is pressed and dino is not jumping
        if (controls.isPressedUp() && !isJumping) {
        	jumpSound.triggerPlay();
            isJumping = true;
            currentState = DinoState.DINO_JUMP;
            speedY = SPEED_Y;
        }

        //Jump logic
        if (isJumping) {
            dinoY += speedY;
            speedY += GRAVITY;
            
            // If dino reaches the ground or down control is pressed, reset jump
            // Represent a quick fall move
            if (dinoY >= GROUND_Y || controls.isPressedDown()) {
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

    // Set the jump image for dino
    public void setDinoJumpImage(String DinoJump) {
        dinojump = new Image(DinoJump);
    }

    // Set the first run image for dino
    public void setDinoRun1Image(String DinoRun1) {
        dinorun1 = new Image(DinoRun1);
        dinoRun.clearSprites();
        dinoRun.addSprite(dinorun1);  // Add sprite to animation
    }

    // Set the second run image for dino
    public void setDinoRun2Image(String DinoRun2) {
        dinorun2 = new Image(DinoRun2);
        dinoRun.addSprite(dinorun2);  // Add sprite to animation
    }

    // Set the first down run image for dino
    public void setDinoDownRun1Image(String DinoDownRun1) {
        dinodownrun1 = new Image(DinoDownRun1);
        dinoDownRun.clearSprites();
        dinoDownRun.addSprite(dinodownrun1);  // Add sprite to animation
    }

    // Set the second down run image for dino    
    public void setDinoDownRun2Image(String DinoDownRun2) {
        dinodownrun2 = new Image(DinoDownRun2);
        dinoDownRun.addSprite(dinodownrun2);  // Add sprite to animation
    }

    // Set the dead image for dino  
    public void setDinoDeadImage(String DinoDead) {
        dinodead = new Image(DinoDead);
    }

    // Get the jump image of dino
    public Image getDinoJumpImage() {
        return dinojump;
    }

    // Get the first run image of dino
    public Image getDinoRun1Image() {
        return dinorun1;
    }

    // Get the second run image of dino
    public Image getDinoRun2Image() {
        return dinorun2;
    }

    // Get the first down run image of dino
    public Image getDinoDownRun1Image() {
        return dinodownrun1;
    }

    // Get the second down run image of dino
    public Image getDinoDownRun2Image() {
        return dinodownrun2;
    }

    // Get the dead image of dino
    public Image getDinoDeadImage() {
        return dinodead;
    }
    
    // Set current state of dino to dead
    public void setDead() {
        currentState = DinoState.DINO_DEAD;
    }
    
    // Draw the current image of dino based on current state
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
            gc.drawImage(currentDinoImage, 100, dinoY - currentDinoImage.getHeight());
        }
    }

    // Draw the hit box of dino
    public void drawHitBox(GraphicsContext gc) {
        gc.setStroke(Color.GREEN);
        gc.setLineWidth(1);
        gc.strokeRect(getHitBox().getMinX(), 
        		getHitBox().getMinY(), 
        		getHitBox().getWidth(), 
        		getHitBox().getHeight());
    }
    
    // Set current state of dino
    public void setDinoState(DinoState dinoState) {
        this.currentState = dinoState;
    }

    // Control the jump action of dino
    public void jump() {
        if(y == GROUND_Y - dinoRun.getSprite().getHeight()) {
            speedY = SPEED_Y;
            y += speedY;
        }
    }

    // Reset dino to initial state
    public void resetDino() {
        y = GROUND_Y - dinojump.getHeight();
        dinoY = GROUND_DINO;
        isJumping = false;
        currentState = DinoState.DINO_RUN;
    }

    // Handle the game over state for dino
    public void dinoGameOver() {
        if(y > GROUND_Y - dinodead.getHeight()) {
            y = GROUND_Y - dinodead.getHeight();
        }
        currentState = DinoState.DINO_DEAD;
    }
}
