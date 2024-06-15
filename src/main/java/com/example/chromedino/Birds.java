package com.example.chromedino;

import javafx.geometry.BoundingBox;
import javafx.geometry.Bounds;
import javafx.scene.paint.Color;
import misc.EnemyManager;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import misc.Animation;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static com.example.chromedino.HelloApplication.SCREEN_WIDTH;

public class Birds {

    //Constructor
    private class Bird {

        private Animation birdFly;
        private double x;
        private int y;

        private Bird(Animation birdFly, double x, int y) {
            this.birdFly = birdFly;
            this.x = x;
            this.y = y;
        }
    }

    // again this weird numbers to calculate hitboxes
    // this on is difference in two sprites of birds, with wings up and down so that bird is not jumping as crazy, it could be done easier but...
    private static final int HITBOX_MODELS_DIFF_IN_Y = -12;
    // numbers to calculate hitboxes of bird with wings pointing up and down
    private static final int[] HITBOX_WINGS_UP = {20, 4, -40, -20};
    private static final int[] HITBOX_WINGS_DOWN = {20, 4, -40, -28};
    // value to check current sprite
    private final int WINGS_DOWN_HEIGHT = (int) new Image("bird-fly-1.png").getHeight();

    private EnemyManager eManager;
    private List<Bird> birds;
    private double speedX;

    public Birds(double speedX, EnemyManager eManager) {
        this.speedX = speedX;
        this.eManager = eManager;
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
                if (SCREEN_WIDTH - (bird.x + sprite.getWidth()) < eManager.getDistanceBetweenEnemies()) {
                    return false;
                }
            }
        }
        return true;
    }

    public boolean createBird() {
        if (Math.random() * 100 < eManager.getBirdsPercentage()) {
            Animation birdFly = new Animation(5);
            birdFly.addSprite(new Image("bird-fly-1.png"));
            birdFly.addSprite(new Image("bird-fly-2.png"));

            birdFly.start(); // Start the animation

            birds.add(new Bird(birdFly, SCREEN_WIDTH, (int) (Math.random() * (196))));
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
        double y = bird.birdFly.getSprite().getHeight() < WINGS_DOWN_HEIGHT ?
                bird.y + HITBOX_WINGS_UP[1] : bird.y + HITBOX_WINGS_DOWN[1];
        double width = bird.birdFly.getSprite().getWidth() + HITBOX_WINGS_UP[2];
        double height = bird.birdFly.getSprite().getHeight() < WINGS_DOWN_HEIGHT ?
                bird.birdFly.getSprite().getHeight() + HITBOX_WINGS_UP[3] :
                bird.birdFly.getSprite().getHeight() + HITBOX_WINGS_DOWN[3];

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

    public void drawHitBox(GraphicsContext gc) {
        gc.setStroke(Color.RED);
        for (Iterator<Bird> i = birds.iterator(); i.hasNext();) {
            Bird bird = i.next();
            Bounds birdHitBox = getHitBox(bird);
            gc.strokeRect(birdHitBox.getMinX(),
                    birdHitBox.getMinY(),
                    birdHitBox.getWidth(),
                    birdHitBox.getHeight());
        }
    }
}