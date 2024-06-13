package com.example.chromedino;

import static com.example.chromedino.HelloApplication.SCREEN_WIDTH;

import javafx.geometry.BoundingBox;
import javafx.geometry.Bounds;
import javafx.scene.paint.Color;
import misc.Animation;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Birds {
    // again this weird numbers to calculate hitboxes
    // this on is difference in two sprites of birds, with wings up and down so that bird is not jumping as crazy, it could be done easier but...
    private static final int HITBOX_MODELS_DIFF_IN_Y = -12;
    // numbers to calculate hitboxes of bird with wings pointing up and down
    private static final int[] HITBOX_WINGS_UP = {20, 4, -40, -20};
    private static final int[] HITBOX_WINGS_DOWN = {20, 4, -40, -28};
    // value to check current sprite
    private final int WINGS_DOWN_HEIGHT = (int) new Image("bird-fly-1.png").getHeight();

    private class Bird {

        private double x;
        private int y;
        private Animation birdFly;

        private Bird(double x, int y, Animation birdFly) {
            this.x = x;
            this.y = y;
            this.birdFly = birdFly;
        }

    }

    private List<Bird> birds;
    private double speedX;

    public Birds(double speedX) {
        this.speedX = speedX;
        birds = new ArrayList<Bird>();
    }

    public void updatePosition() {
        for (Iterator<Bird> i = birds.iterator(); i.hasNext();) {
            Bird bird = i.next();
            bird.x -= (speedX + speedX / 5);
            bird.birdFly.updateSprite();
        }
    }

    public boolean spaceAvailable() {
        for (Bird bird : birds) {
            Image sprite = bird.birdFly.getSprite();
            if (sprite != null) {
                if (SCREEN_WIDTH - (bird.x + sprite.getWidth()) < 500) // distanceBetweenEnemies
                {
                    return false;
                }
            }
        }
        return true;
    }

    public boolean createBird() {
        if (Math.random() * 100 < 5) {
            Animation birdFly = new Animation(5);
            birdFly.addSprite(new Image("bird-fly-1.png"));
            birdFly.addSprite(new Image("bird-fly-2.png"));

            birdFly.start(); // Start the animation

            birds.add(new Bird(SCREEN_WIDTH, (int) (Math.random() * (196)), birdFly));
            //Math.random() * (GROUND_Y - birdFly.getSprite().getHeight())), birdFly));
            return false;
        }
        return true;
    }

    public boolean iscollision(Bounds dinoHitBox) {
        for (Bird birds : birds) {
            Bounds birdsHitBox = getHitBox(birds);
            if (birdsHitBox.intersects(dinoHitBox)) {
                System.out.println("Birds Collision detected!");
                return true;
            }
        }
        return false;
    }
    private Bounds getHitBox(Bird bird) {
        double x = bird.x + HITBOX_WINGS_UP[0];
        double y = bird.birdFly.getSprite().getHeight() < WINGS_DOWN_HEIGHT ? bird.y + HITBOX_WINGS_UP[1] : bird.y + HITBOX_WINGS_DOWN[1];
        double width = bird.birdFly.getSprite().getWidth() + HITBOX_WINGS_UP[2];
        double height = bird.birdFly.getSprite().getHeight() < WINGS_DOWN_HEIGHT ? bird.birdFly.getSprite().getHeight() + HITBOX_WINGS_UP[3] : bird.birdFly.getSprite().getHeight() + HITBOX_WINGS_DOWN[3];

        return new BoundingBox(x, y, width, height);
    }
    public void clearBirds() {
        birds.clear();
    }

    public void draw(GraphicsContext gc) {
        for (Bird bird : birds) {
            Image sprite = bird.birdFly.getSprite();
            gc.drawImage(sprite, bird.x, bird.y);
        }
    }

    public void drawHitbox(GraphicsContext gc) {
        gc.setStroke(Color.RED);
        for (Iterator<Bird> i = birds.iterator(); i.hasNext();) {
            Bird bird = i.next();
            Bounds birdHitBox = getHitBox(bird);
            gc.strokeRect(birdHitBox.getMinX(), birdHitBox.getMinY(), birdHitBox.getWidth(), birdHitBox.getHeight());
        }
    }
}