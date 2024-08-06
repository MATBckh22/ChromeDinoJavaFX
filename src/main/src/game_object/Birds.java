package game_object;

import javafx.geometry.BoundingBox;
import javafx.geometry.Bounds;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import manager.EnemyManager;
import misc.Animation;

import static user_interface.GameStage.SCREEN_WIDTH;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Birds class handles the spawning and hit box drawing of birds
 */

public class Birds {
	
    private class Bird {
    	private Animation birdFly;
        private double x; // X position of bird
        private int y; // Y position of bird

        private Bird(Animation birdFly, double x, int y) {
        	this.birdFly = birdFly;
            this.x = x;
            this.y = y;
        }
    }

    // Hit box dimensions for bird with wings up and down
    private static final int[] HITBOX_WINGS_UP = {20, 4, -40, -20};
    private static final int[] HITBOX_WINGS_DOWN = {20, 4, -40, -28};
    // Height of bird sprite when wings down
    private final int WINGS_DOWN_HEIGHT = (int) new Image("bird-fly-1.png").getHeight();
    
	private EnemyManager eManager; // Enemy manager parameter
    private List<Bird> birds; // List of birds
    private double speedX; // Speed of bird

    // Constructor
    public Birds(double speedX, EnemyManager eManager) {
        this.speedX = speedX;
		this.eManager = eManager;
        birds = new ArrayList<Bird>();
    }

    // Update the position of birds
    public void updatePosition() {
        for (Iterator<Bird> i = birds.iterator(); i.hasNext();) {
        	Bird bird = i.next();
            bird.x -= (speedX + speedX / 5); // Move bird to the left, if plus, the bird will move to right
            bird.birdFly.updateSprite(); // Update bird animation
        }
    }

    // Check if the space is available to create a new bird
    public boolean spaceAvailable() {
        for (Bird bird : birds) {
            Image sprite = bird.birdFly.getSprite();
            if (sprite != null) {
                if (SCREEN_WIDTH - (bird.x + sprite.getWidth()) < eManager.getDistanceBetweenEnemies()) {
                    return false; // Means not enough space
                }
            }
        }
        return true; // Means enough space
    }

    // Create a new bird with a certain probability
    public boolean createBird() {
        if (Math.random() * 100 < eManager.getBirdsPercentage()) {
            Animation birdFly = new Animation(150); // Create bird animation
            birdFly.addSprite(new Image("bird-fly-1.png")); // Add first sprite
            birdFly.addSprite(new Image("bird-fly-2.png")); // Add second sprite
            
            birdFly.updateSprite(); // Start the animation
            
            // Add a new bird to the list at a certain height
            // 196 is the height we got through trial and error
            birds.add(new Bird(birdFly, SCREEN_WIDTH, (int) (Math.random() * (196))));
            return false;
        }
        return true;
    }
    
    // Check if bird collides with dino
    public boolean collisiondetector(Bounds dinoHitBox) {
        for (Bird birds : birds) {
            Bounds birdsHitBox = getHitBox(birds); // Get bird hit box
            if (birdsHitBox.intersects(dinoHitBox)) {
                return true; // Collision detected
            }
        }
        return false; // Collision not detected
    }
    
    // Get the hit box of bird
    private Bounds getHitBox(Bird bird) {
        double x = bird.x + HITBOX_WINGS_UP[0];
        double y = bird.birdFly.getSprite().getHeight() < WINGS_DOWN_HEIGHT ? 
        		bird.y + HITBOX_WINGS_UP[1] : bird.y + HITBOX_WINGS_DOWN[1];
        double width = bird.birdFly.getSprite().getWidth() + HITBOX_WINGS_UP[2];
        double height = bird.birdFly.getSprite().getHeight() < WINGS_DOWN_HEIGHT ? 
        		bird.birdFly.getSprite().getHeight() + HITBOX_WINGS_UP[3] : 
        			bird.birdFly.getSprite().getHeight() + HITBOX_WINGS_DOWN[3];

        return new BoundingBox(x, y, width, height);
    }
    
    // Clear all the birds from the list
    public void clearBirds() {
        birds.clear();
    }

    // Draw all the birds
    public void draw(GraphicsContext gc) {
        for (Bird bird : birds) {
            Image sprite = bird.birdFly.getSprite();
            gc.drawImage(sprite, bird.x, bird.y); // Draw bird at its current position
        }
    }
    
    // Draw the hit boxes for birds
    public void drawHitBox(GraphicsContext gc) {
        gc.setStroke(Color.RED); // Set the outline of hit box to red
        for (Iterator<Bird> i = birds.iterator(); i.hasNext();) {
            Bird bird = i.next();
            Bounds birdHitBox = getHitBox(bird); // Get bird hit box
            gc.strokeRect(birdHitBox.getMinX(), 
            		birdHitBox.getMinY(), 
            		birdHitBox.getWidth(), 
            		birdHitBox.getHeight());
        }
    }
}
