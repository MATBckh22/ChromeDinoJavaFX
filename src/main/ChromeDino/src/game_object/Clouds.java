package game_object;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import static user_interface.GameStage.*;

/**
 * Clouds class manages the spawning and rendering of clouds
 */

public class Clouds {

    //Constructor
    private class Cloud {
        private Image cloudimage;
        private double x;
        private int y;

        private Cloud(Image cloudimage, double x, int y) {
            this.cloudimage = cloudimage;
            this.x = x;
            this.y = y;
        }
    }

    private static final int CLOUDS_AMOUNT = 5;
    //0.5 percentage for a cloud to spawn
    private static final double CLOUD_PERCENTAGE = 0.5;
    private Set<Cloud> clouds;
    private int cloudwidthscaled;
    private int cloudheightscaled;
    private double speedX;

    public Clouds(double speedX) {
        this.speedX = speedX;
        clouds = new HashSet<>();
        cloudwidthscaled = (int) (new Image("cloud.png").getWidth() * 2);
        cloudheightscaled = (int) (new Image("cloud.png").getHeight() * 2);
    }

    //updates cloud position, basically redrawing every update
    public void updatePosition() {
        isOutofScreen();
        createClouds();
    }

    //check if a cloud is out of screen
    public void isOutofScreen() {
        for (Iterator<Cloud> i = clouds.iterator(); i.hasNext(); ) {
            Cloud cloud = i.next();
            cloud.x -= speedX / 7;
            if (cloud.x + cloudwidthscaled < 0) {
            	i.remove();
            }
        }
    }

    //creates clouds of random y positions
    private void createClouds() {
        if (clouds.size() < CLOUDS_AMOUNT) {
            for (Cloud cloud : clouds) {
                if (cloud.x >= SCREEN_WIDTH - cloudwidthscaled) {
                	return;
                }
            }
            if (Math.random() * 100 < CLOUD_PERCENTAGE) {
                clouds.add(new Cloud(new Image("cloud.png"), SCREEN_WIDTH, 
                		(int) (Math.random() * SCREEN_HEIGHT / 2)));
            }
        }
    }

    public void clearClouds() {
        clouds.clear();
    }

    public void draw(GraphicsContext gc) {
        for (Cloud cloud : clouds) {
            gc.drawImage(cloud.cloudimage, 
            		cloud.x, cloud.y, 
            		cloud.cloudimage.getWidth(), 
            		cloud.cloudimage.getHeight());
        }
    }
}
