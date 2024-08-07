package misc;

import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

/**
 * Controls class handles the key pressed events to check whenever the appropriate action key is pressed
 */

public class Controls {

    private boolean isPressedUp = false; // Flag to check if the up key bind is pressed
    private boolean isPressedDown = false; // Flag to check if the down key bind is pressed
    private boolean isPaused = false; // Flag to check if the pause key bind is pressed

    // Constructor 
    public Controls(Scene scene) {
        // Add key press event handlers
    	scene.setOnKeyPressed(this::handleKeyPressed);
    	scene.setOnKeyReleased(this::handleKeyReleased);
    }

    // Handle key pressed events
    private void handleKeyPressed(KeyEvent event) {
        if (event.getCode() == KeyCode.UP || event.getCode() == KeyCode.W || event.getCode() == KeyCode.SPACE) {
            isPressedUp = true;
        } else if (event.getCode() == KeyCode.DOWN || event.getCode() == KeyCode.S) {
            isPressedDown = true;
        } else if (event.getCode() == KeyCode.ESCAPE) {
            isPaused = true;
        }
    }
    
    // Handle key released events
    private void handleKeyReleased(KeyEvent event) {
        if (event.getCode() == KeyCode.UP || event.getCode() == KeyCode.W || event.getCode() == KeyCode.SPACE) {
            isPressedUp = false;
        } else if (event.getCode() == KeyCode.DOWN || event.getCode() == KeyCode.S) {
            isPressedDown = false;
        } else if(event.getCode() == KeyCode.ESCAPE) {
            isPaused = false;
        }
    }
    
    // Check if the "up" action key is currently pressed
    public boolean isPressedUp() {
        return isPressedUp;
    }

    // Check if the "down" action key is currently pressed
    public boolean isPressedDown() {
        return isPressedDown;
    }
    
    // Check if the "pause" action key is currently pressed
    public boolean isPaused() {
    	return isPaused;
    }
}
