package com.example.chromedino;

import com.example.chromedino.HelloApplication;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

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

        // Load image and check if it's null
        this.land = new Image(imagePath);
        if (this.land.isError()) {
            System.out.println("Error loading ground image.");
        } else {
            System.out.println("Ground image loaded successfully.");
            System.out.println("Ground image dimensions: " + land.getWidth() + "x" + land.getHeight());
        }

        // Scale the land image
        this.landWidthScaled = (int) land.getWidth() * 2;
        this.landHeightScaled = (int) land.getHeight() * 2;
    }

    public void update() {
        // + SPEED_X to 2 decimal points
        x -= Math.round(speed * 100d) / 100d;

        // Reset position if land goes out of the screen
        if (landWidthScaled <= Math.abs(x)) {
            x = 0;
        }
    }

    public void render(GraphicsContext gc) {
        gc.drawImage(land, x, y, landWidthScaled, landHeightScaled);
        // Drawing another land if image is ending
        if (landWidthScaled - HelloApplication.SCREEN_WIDTH <= Math.abs(x)) {
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