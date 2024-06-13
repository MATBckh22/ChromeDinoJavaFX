package misc;

import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class Controls {

    private boolean isPressedUp = false;
    private boolean isPressedDown = false;

    public Controls(Scene scene) {
        scene.setOnKeyPressed(this::handleKeyPressed);
        scene.setOnKeyReleased(this::handleKeyReleased);
    }

    private void handleKeyPressed(KeyEvent event) {
        if (event.getCode() == KeyCode.UP || event.getCode() == KeyCode.W || event.getCode() == KeyCode.SPACE) {
            isPressedUp = true;
        } else if (event.getCode() == KeyCode.DOWN || event.getCode() == KeyCode.S) {
            isPressedDown = true;
        }
    }

    private void handleKeyReleased(KeyEvent event) {
        if (event.getCode() == KeyCode.UP || event.getCode() == KeyCode.W || event.getCode() == KeyCode.SPACE) {
            isPressedUp = false;
        } else if (event.getCode() == KeyCode.DOWN || event.getCode() == KeyCode.S) {
            isPressedDown = false;
        }
    }

    public boolean isPressedUp() {
        return isPressedUp;
    }

    public boolean isPressedDown() {
        return isPressedDown;
    }
}