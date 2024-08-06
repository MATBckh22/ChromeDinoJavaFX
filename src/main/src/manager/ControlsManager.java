package manager;

import misc.Controls;
import user_interface.GameScene;

/**
 * ControlsManager class manages the triggering of appropriate actions
 */

public class ControlsManager {

    private Controls controls;
    private GameScene gameScene;

    // Constructor
    public ControlsManager(Controls controls, GameScene gameScene) {
        this.controls = controls;
        this.gameScene = gameScene;
    }

    // Update method to check controls and trigger appropriate actions during the game
    public void update() {
    	// Check if the up key bind is pressed
        if (controls.isPressedUp()) {
            gameScene.pressUpAction(); // Trigger the up action
        }
        // Check if the down key bind is pressed
        if (controls.isPressedDown()) {
        	gameScene.pressDownAction(); // Trigger the down action
        }
        // Check if the pause key bind is pressed
        if (controls.isPaused()){
        	gameScene.pressPauseAction(); // Trigger the pause action
        }
    }
}
