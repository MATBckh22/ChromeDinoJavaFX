package misc;

import javafx.scene.image.Image;
import java.util.ArrayList;
import java.util.List;

/**
 * Animation class manages the animation of dino and birds by switching sprites when the current time has elapsed
 * it handles add, get, and clear sprites
 */

public class Animation {
    
	private List<Image> sprites; // List to store the frames of the animation
    private int currentSpriteIndex = 0; // Index of the current sprite being displayed
    private long lastUpdateTime; // Time when the last frame update occurred
    private int updateTimeMillis; // Time interval (in milliseconds) between frame updates
    
    // Constructor
    public Animation(int updateTimeMillis) {
        this.sprites = new ArrayList<>(); // Initialize the list of sprites
        this.currentSpriteIndex = 0; // Set the initial sprite index to 0
        this.lastUpdateTime = System.nanoTime(); // Store time in nanoseconds for higher precision
        this.updateTimeMillis = updateTimeMillis; // Set the time interval for updating frames
    }

    // Update the current sprite based on the elapsed time
    public void updateSprite() {
        long now = System.nanoTime(); // Get the current time in nanoseconds
        long elapsedTimeMillis = (now - lastUpdateTime) / 1_000_000; // Convert nanoseconds to milliseconds

        // Check if enough time has elapsed to update the sprite
        if (elapsedTimeMillis >= updateTimeMillis) {
        	// Move to the next sprite index, looping back to 0 if needed
            currentSpriteIndex = (currentSpriteIndex + 1) % sprites.size();
            lastUpdateTime = now; // Update the last update time to the current time
        }
    }
    
    // Add a sprite to the animation
    public void addSprite(Image sprite) {
        sprites.add(sprite); // Add sprite to the list of sprites
    }

    // Get the current sprite being displayed
    public Image getSprite() {
    	// Check if there are any sprites in the list
        if (!sprites.isEmpty()) { 
            return sprites.get(currentSpriteIndex); // Return the current sprite based on the index
        }
        return null; 
    }
    
    // Clear all sprites from the animation
    public void clearSprites() {
        sprites.clear(); // Clear the list of sprites
        currentSpriteIndex = 0; // Reset the sprite index to 0
    }
}
