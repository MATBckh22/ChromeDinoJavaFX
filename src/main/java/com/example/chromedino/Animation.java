package misc;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.image.Image;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;

public class Animation {

    private List<Image> sprites;
    private int currentSpriteIndex = 0;
    private Timeline animationTimeline;

    public Animation(int updateTimeMillis) {
        sprites = new ArrayList<>();
        animationTimeline = new Timeline(new KeyFrame(Duration.millis(updateTimeMillis), e -> updateSprite()));
        animationTimeline.setCycleCount(Timeline.INDEFINITE);
    }

    // Starts the animation
    public void start() {
        animationTimeline.play();
    }

    // Stops the animation
    public void stop() {
        animationTimeline.stop();
    }

    public void updateSprite() {
        if (!sprites.isEmpty()) {
            currentSpriteIndex = (currentSpriteIndex + 1) % sprites.size();
        }
    }

    public void addSprite(Image sprite) {
        sprites.add(sprite);
    }

    public Image getSprite() {
        if (!sprites.isEmpty()) {
            return sprites.get(currentSpriteIndex);
        }
        return null;
    }

    public void clearSprites() {
        sprites.clear();
        currentSpriteIndex = 0;
    }
}