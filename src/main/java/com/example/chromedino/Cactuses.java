package com.example.chromedino;

import javafx.geometry.BoundingBox;
import javafx.geometry.Bounds;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import misc.EnemyManager;
import com.example.chromedino.GameStage;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import static com.example.chromedino.GameStage.SCREEN_WIDTH;

/**
 * Cactus class handles the spawning and hitbox drawing of cactus
 */

public class Cactuses {

    // Constructor
    private class Cactus {

        private Image cactusimage;
        private double x;
        private int y;

        private Cactus(Image cactusimage, double x, int y) {
            this.cactusimage = cactusimage;
            this.x = x;
            this.y = y;
        }
    }

    //9 images, therefore 9 cactuses
    private static final int CACTUS_AMOUNT = 9;
    //maximum group of cactuses to spawn as one
    private static final int MAX_CACTUS_GROUP = 3;
    //extra spaces to deduct from hitbox calculations
    private static final double HITBOX_X = 2.7;
    private static final int HITBOX_Y = 25;

    private EnemyManager eManager;
    private List<Cactus> cactuses;
    private Random random;
    private double speedX;

    public Cactuses(double speedX, EnemyManager eManager) {
        this.speedX = speedX;
        this.eManager = eManager;
        cactuses = new ArrayList<>();
        random = new Random();
    }

    //updates position for cactus
    public void updatePosition() {
        //System.out.println("Updating Position");
        for (Iterator<Cactus> i = cactuses.iterator(); i.hasNext(); ) {
            Cactus cactus = i.next();
            cactus.x -= speedX;
            if ((int) cactus.x + cactus.cactusimage.getWidth() < 0) i.remove();
        }
    }

    //check if there's any space to spawn a cactus
    public boolean spaceAvailable() {
        //System.out.println("Checking for space");
        if (cactuses.isEmpty()) {
            return true;
        }
        Cactus lastCactus = cactuses.get(cactuses.size() - 1);
        return GameStage.SCREEN_WIDTH - (
                lastCactus.x + lastCactus.cactusimage.getWidth())
                > eManager.getDistanceBetweenEnemies(); //distance between cactuses
    }

    //spawns cactus
    public boolean createCactuses() {
        //generate probability of cactuses spawning, handled by EnemyManager
        if (Math.random() * 100 < eManager.getCactusesPercentage()) {
            int numberofCactuses = (int) (Math.random() * MAX_CACTUS_GROUP + 1);
            for (int i = 0; i < numberofCactuses; i++) {
                Image cactusimage = new Image("cactus-" + (random.nextInt(CACTUS_AMOUNT) + 1) + ".png");
                int x = SCREEN_WIDTH;
                int y = (int) (250 - cactusimage.getHeight());
                //Image handling
                if (i > 0) {
                    Cactus lastCactus = cactuses.get(cactuses.size() - 1);
                    x = (int) (lastCactus.x + lastCactus.cactusimage.getWidth());
                }
                cactuses.add(new Cactus(cactusimage, x, y));
            }
            return true;
        }
        return false;
    }

    //detects collision by checking if an intersection between dino's hitbox and cactus occurs
    public boolean collisiondetector(Bounds dinoHitBox) {
        for (Cactus cactus : cactuses) {
            Bounds cactusHitBox = getHitBox(cactus);
            if (cactusHitBox.intersects(dinoHitBox)) {
                System.out.println("Cactuses Collision detected!");
                return true;
            }
        }
        return false;
    }

    private Bounds getHitBox(Cactus cactus) {
        //enable hitboxes in GameScene to see it
        return new BoundingBox(
                cactus.x + (cactus.cactusimage.getWidth() / HITBOX_X),
                cactus.y + (cactus.cactusimage.getHeight() / HITBOX_Y),
                cactus.cactusimage.getWidth() - (cactus.cactusimage.getWidth() / HITBOX_X) * 2,
                cactus.cactusimage.getHeight() - (cactus.cactusimage.getHeight() / HITBOX_Y)
        );
    }

    //clears cactuses
    public void clearCactuses() {
        cactuses.clear();
    }

    public void draw(GraphicsContext gc) {
        for (Cactus cactus : cactuses) {
            gc.drawImage(cactus.cactusimage, cactus.x, cactus.y);
        }
    }

    //draws hitboxes for debug mode
    public void drawHitBox(GraphicsContext gc) {
        gc.setStroke(Color.RED);
        gc.setLineWidth(1);
        for (Cactus cactus : cactuses) {
            Bounds cactusHitBox = getHitBox(cactus);
            gc.strokeRect(cactusHitBox.getMinX(),
                    cactusHitBox.getMinY(),
                    cactusHitBox.getWidth(),
                    cactusHitBox.getHeight());
        }
    }
}