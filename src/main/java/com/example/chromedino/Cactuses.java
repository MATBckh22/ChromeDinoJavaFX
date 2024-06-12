package com.example.chromedino;

import javafx.geometry.BoundingBox;
import javafx.geometry.Bounds;
import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import static com.example.chromedino.HelloApplication.SCREEN_WIDTH;

public class Cactuses {

    //Constructor
    private class Cactus{

        private Image cactusimage;
        private double x;
        private int y;

        private Cactus(Image cactusimage, double x, int y){
            this.cactusimage = cactusimage;
            this.x = x;
            this.y = y;
        }
    }
    private static final int CACTUS_AMOUNT = 9;
    private static final int MAX_CACTUS_GROUP = 3;
    private static final double HITBOX_X = 2.7;
    private static final int HITBOX_Y = 25;
    private List<Cactus> cactuses;
    private Random random;
    private double speedX;

    public Cactuses(){
        cactuses = new ArrayList<>();
        random = new Random();
        speedX = 5.0;
    }
    public void updatePosition(){
        //System.out.println("Updating Position");
        for(Iterator<Cactus> i = cactuses.iterator(); i.hasNext();){
            Cactus cactus = i.next();
            cactus.x -= speedX;
            if((int) cactus.x + cactus.cactusimage.getWidth() < 0) i.remove();
        }
    }

    public boolean spaceAvailable(){
        //System.out.println("Checking for space");
        if(cactuses.isEmpty()) return true;
        Cactus lastCactus = cactuses.get(cactuses.size() - 1);
        return HelloApplication.SCREEN_WIDTH - (lastCactus.x + lastCactus.cactusimage.getWidth()) > 200; //distance between cactuses
    }

    public boolean createCactuses(){
        //generate probability of cactuses spawning to be less than 10% for each window
        if(Math.random() * 100 < 1){
            int numberofCactuses = (int) (Math.random() * MAX_CACTUS_GROUP + 1);
            for(int i = 0; i < numberofCactuses; i++){
                Image cactusimage = new Image("cactus-" + (random.nextInt(CACTUS_AMOUNT) + 1) + ".png");
                int x = SCREEN_WIDTH;
                int y = (int) (250 - cactusimage.getHeight());
                if (i > 0){
                    Cactus lastCactus = cactuses.get(cactuses.size() - 1);
                    x = (int) (lastCactus.x + lastCactus.cactusimage.getWidth());
                }
                cactuses.add(new Cactus(cactusimage, x, y));
            }
            return true;
        }
        return false;
    }

    public boolean iscollision(Bounds dinoHitBox){
        for(Cactus cactus : cactuses){
            Bounds cactusHitBox = getHitBox(cactus);
            if (cactusHitBox.intersects(dinoHitBox)){
                System.out.println("Collision detected!");
                return true;
            }
        }
        return false;
    }

    private Bounds getHitBox(Cactus cactus){
        // weird calculation by its working as needed
        // basically i make it thinner from left and right and shorter to match it perfectly
        // enable hitboxes in GameScreen to see it
        return new BoundingBox(
                cactus.x + (cactus.cactusimage.getWidth() / HITBOX_X),
                cactus.y + (cactus.cactusimage.getHeight() / HITBOX_Y),
                cactus.cactusimage.getWidth() - (cactus.cactusimage.getWidth() / HITBOX_X) * 2,
                cactus.cactusimage.getHeight() - (cactus.cactusimage.getHeight() / HITBOX_Y)
        );
    }
    public void clearCactuses(){
        cactuses.clear();
    }

    public void draw(GraphicsContext gc){
        for (Cactus cactus : cactuses){
            gc.drawImage(cactus.cactusimage, cactus.x, cactus.y);
        }
    }

    public void drawHitBox(GraphicsContext gc){
        gc.setStroke(Color.RED);
        gc.setLineWidth(1);
        for (Cactus cactus : cactuses) {
            Bounds cactusHitBox = getHitBox(cactus);
            gc.strokeRect(cactusHitBox.getMinX(), cactusHitBox.getMinY(), cactusHitBox.getWidth(), cactusHitBox.getHeight());
        }
    }
}
