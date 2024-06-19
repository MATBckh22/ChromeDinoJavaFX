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
    private long lastUpdateTime;
    private int updateTimeMillis;

    public Animation(int updateTimeMillis) {
        //sprites = new ArrayList<>();
        //animationTimeline = new Timeline(new KeyFrame(Duration.millis(updateTimeMillis), e -> updateSprite()));
        //animationTimeline.setCycleCount(Timeline.INDEFINITE);
        this.sprites = new ArrayList<>();
        this.currentSpriteIndex = 0;
        this.lastUpdateTime = System.nanoTime(); // Store time in nanoseconds for higher precision
        this.updateTimeMillis = updateTimeMillis;
    }

    public void updateSprite() {
        long now = System.nanoTime();
        long elapsedTimeMillis = (now - lastUpdateTime) / 1_000_000; // Convert nanoseconds to milliseconds
        System.out.println(elapsedTimeMillis);

        if (elapsedTimeMillis >= updateTimeMillis) {
            currentSpriteIndex = (currentSpriteIndex + 1) % sprites.size();
            lastUpdateTime = now;
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