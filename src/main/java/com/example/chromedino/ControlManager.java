package misc;

import misc.Controls;
import UserInterface.GameScreen;

public class ControlManager {

    private Controls controls;
    private GameScreen gameScreen;

    public ControlManager(Controls controls, GameScreen gameScreen) {
        this.controls = controls;
        this.gameScreen = gameScreen;
    }

    public void update() {
        if (controls.isPressedUp()) {
            System.out.println("State Updated");
            gameScreen.pressUpAction();
        }
        if (controls.isPressedDown()) {
            gameScreen.pressDownAction();
        }
        if (controls.isPaused()){
            gameScreen.pressPauseAction();
        }
    }
}