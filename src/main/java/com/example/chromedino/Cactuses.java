package com.example.chromedino;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import static com.example.chromedino.HelloApplication.GROUND_Y;
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
    private List<Cactus> cactuses;
    private Random random;
    private double speedX;

    public Cactuses(){
        cactuses = new ArrayList<>();
        random = new Random();
        speedX = 5.0;
    }
    public void updatePosition(){
        System.out.println("Updating Position");
        for(Iterator<Cactus> i = cactuses.iterator(); i.hasNext();){
            Cactus cactus = i.next();
            cactus.x -= speedX;
            if((int) cactus.x + cactus.cactusimage.getWidth() < 0) i.remove();
        }
    }

    public boolean spaceAvailable(){
        System.out.println("Checking for space");
        if(cactuses.isEmpty()) return true;
        Cactus lastCactus = cactuses.get(cactuses.size() - 1);
        return HelloApplication.SCREEN_WIDTH - (lastCactus.x + lastCactus.cactusimage.getWidth()) > 200; // distance between cactuses
    }

    public boolean createCactuses(){
        //generate probability of cactuses spawning to be less than 10% for each window
        if(Math.random() * 100 < 10){
            int numberofCactuses = (int) (Math.random() * MAX_CACTUS_GROUP + 1);
            for(int i = 0; i < numberofCactuses; i++){
                Image cactusImage = new Image("cactus-" + (random.nextInt(CACTUS_AMOUNT) + 1) + ".png");
                int x = SCREEN_WIDTH;
                int y = (int) (250 - cactusImage.getHeight());
                if (i > 0){
                    Cactus lastCactus = cactuses.get(cactuses.size() - 1);
                    x = (int) (lastCactus.x + lastCactus.cactusimage.getWidth());
                }
                cactuses.add(new Cactus(cactusImage, x, y));
            }
            return true;
        }
        return false;
    }

    public void clearCactuses(){
        cactuses.clear();
    }

    public void draw(GraphicsContext gc){
        for (Cactus cactus : cactuses){
            gc.drawImage(cactus.cactusimage, cactus.x, cactus.y);
        }
    }

}
