package com.example.chromedino;

import com.example.chromedino.GameStage;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

/**
 * Manages the animation of scrolling land, the land is moving relative to the dino
 * so dino stays static.
 */

public class Land {
    private double x = 0;
    private final double y;
    private double speed;
    private int landWidthScaled;
    private int landHeightScaled;
    private final Image land;

    public Land(double startY, double speed, String imagePath) {
        this.y = startY;
        this.speed = speed;

        //load image and check if it's null
        this.land = new Image(imagePath);
        if (this.land.isError()) {
            System.out.println("Error loading ground image.");
        } else {
            System.out.println("Ground image loaded successfully.");
            System.out.println("Ground image dimensions: " + land.getWidth() + "x" + land.getHeight());
        }

        //scale the land image by 2
        this.landWidthScaled = (int) land.getWidth() * 2;
        this.landHeightScaled = (int) land.getHeight() * 2;
    }

    //updates land
    public void update() {
        //SPEED_X to 2 decimal points
        x -= Math.round(speed * 100d) / 100d;

        //reset position if land goes out of the screen
        if (landWidthScaled <= Math.abs(x)) {
            x = 0;
        }
    }

    //redraws land for every updated position for the scrolling effect
    public void render(GraphicsContext gc) {
        gc.drawImage(land, x, y, landWidthScaled, landHeightScaled);
        // Drawing another land if image is ending
        if (landWidthScaled - GameStage.SCREEN_WIDTH <= Math.abs(x)) {
            gc.drawImage(land, landWidthScaled + x, y, landWidthScaled, landHeightScaled);
        }
    }

    public void updateSpeed(double newSpeed) {
        this.speed = newSpeed;
    }

    public void resetLand() {
        x = 0;
    }
}