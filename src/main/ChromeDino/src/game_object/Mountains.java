package game_object;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static user_interface.GameStage.SCREEN_WIDTH;

/**
* Mountains class draws the "Mountains" background.
* There are 4 layers of images to form different mountains, going at different speeds to mimic realistic visual effects.
* Balloons of 3 variations will randomly spawn with sizes of 1-2 scaled randomly, and random speeds of 1-3.
*/

public class Mountains {

    //class for mountains, labeled as Layer since there's 4 layers
    private static class Layer {
        private double x = 0;
        private final double y;
        private final double speed;
        private final int widthScaled;
        private final int heightScaled;
        private final Image image;

        //constructor for Layer
        public Layer(double y, double speed, String imagePath) {
            this.y = y;
            this.speed = speed;
            this.image = new Image(imagePath);
            this.widthScaled = (int) image.getWidth();
            this.heightScaled = (int) image.getHeight();
        }

        //updates position for each layer, when the layer moves out of screen, it will spawn a new one
        public void update() {
            x -= Math.round(speed * 100d) / 100d;
            if (widthScaled <= Math.abs(x)) {
                x = 0;
            }
        }

        //renders mountain layers
        public void render(GraphicsContext gc) {
            gc.drawImage(image, x, y, widthScaled, heightScaled);
            if (widthScaled - SCREEN_WIDTH <= Math.abs(x)) {
                gc.drawImage(image, widthScaled + x, y, widthScaled, heightScaled);
            }
        }
    }

    //class for balloons
    private static class Balloon {
        private double x;
        private double y;
        private final double speed;
        private double scale;
        private final Image image;

        public Balloon(double x, double y, double speed, double scale, Image image) {
            this.x = x;
            this.y = y;
            this.speed = speed;
            this.scale = scale;
            this.image = image;
        }

        //updates position, if balloon moves out of screen it will generate a new one
        public void update() {
            x -= speed;
            if (x + image.getWidth() * scale < 0) {
                x = SCREEN_WIDTH;
                y = new Random().nextDouble() * SCREEN_WIDTH / 2 - 50; //Random y position
                scale = 1 + new Random().nextDouble() * 1; //Random scale between 1 and 2
            }
        }

        //renders balloon
        public void render(GraphicsContext gc) {
            gc.drawImage(image, x, y, image.getWidth() * scale, image.getHeight() * scale);
        }
    }

    private final Layer[] layers;
    private final List<Balloon> balloons = new ArrayList<>();

    public Mountains(double[] speeds, String[] imagePaths, double[] startYs, String[] balloonImagePaths) {
        //exception handling when the parameter lengths aren't the same
        if (speeds.length != imagePaths.length || imagePaths.length != startYs.length) {
            throw new IllegalArgumentException("Array lengths must match");
        }
        //Initialize mountain layers
        layers = new Layer[speeds.length];
        for (int i = 0; i < layers.length; i++) {
            layers[i] = new Layer(startYs[i], speeds[i], imagePaths[i]);
        }

        //initialize hot air balloons
        Random rand = new Random();
        for (int i = 0; i < 3; i++) {
            double x = rand.nextDouble() * SCREEN_WIDTH;
            double y = rand.nextDouble() * SCREEN_WIDTH / 2;
            double speed = 1 + rand.nextDouble() * 2; //Random speed between 1 and 3
            double scale = 1 + rand.nextDouble() * 1; //Random scale between 1 and 2
            String balloonImagePath = balloonImagePaths[rand.nextInt(balloonImagePaths.length)];
            //spawn new balloon
            balloons.add(new Balloon(x, y, speed, scale, new Image(balloonImagePath)));
        }
    }

    //update mountain positions then balloons
    public void update() {
        for (Layer layer : layers) {
            layer.update();
        }
        for (Balloon balloon : balloons) {
            balloon.update();
        }
    }

    //render mountain positions then balloons
    public void render(GraphicsContext gc) {
        for (Layer layer : layers) {
            layer.render(gc);
        }
        for (Balloon balloon : balloons) {
            balloon.render(gc);
        }
    }
}
