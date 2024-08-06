package manager;

import javafx.scene.input.MouseEvent;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

import static user_interface.GameStage.SCREEN_WIDTH;

import game_object.Dino;

/*
* SkinManager handles the skinScreen in GameScreen, which is the first screen of the game.
* This class loads all the skins into the game and prompts the player to select a skin.
* Each skin name will show up when the player's cursor hovers onto the corresponding skin.
* Once a skin is selected, skinselected becomes false and goes to the next screen.
*/

public class SkinManager {

    private Image[] skins;
    private Image[] skinNames;
    private double[] skinWidths;
    private double[] skinXPositions;
    public boolean skinselected = false;
    private int hoveredSkinIndex = -1;

    public SkinManager() {
        //Initialize skins
        skins = new Image[]{
                new Image("dino-jump.png"),
                new Image("dino-work-jump.png"),
                new Image("dino-viking-jump.png"),
                new Image("dino-gentleman-jump.png"),
                new Image("dino-miss-jump.png"),
                new Image("dino-girl-jump.png"),
                new Image("dino-granny-jump.png"),
                new Image("dino-green-jump.png")
        };
        //Initialize skin names
        skinNames = new Image[]{
                new Image("default.png"),
                new Image("work.png"),
                new Image("viking.png"),
                new Image("gentleman.png"),
                new Image("missdino.png"),
                new Image("girly.png"),
                new Image("granny.png"),
                new Image("green.png"),
        };

        skinWidths = new double[skins.length];
        skinXPositions = new double[skins.length];

        calculateSkinPositions();
    }

    //calculates x positions for 8 buttons
    private void calculateSkinPositions() {
        double totalWidth = 0;
        for (Image skin : skins) {
            totalWidth += skin.getWidth();
        }

        double startX = (SCREEN_WIDTH - totalWidth) / 2;
        double x = startX;
        for (int i = 0; i < skins.length; i++) {
            skinWidths[i] = skins[i].getWidth();
            skinXPositions[i] = x;
            x += skinWidths[i];
        }
    }

    public void drawSkins(GraphicsContext gc) {
        double y = 150;
        for (int i = 0; i < skins.length; i++) {
            if (i == hoveredSkinIndex) {
                gc.setFill(Color.LIGHTGRAY); //set light grey for the hover effect
                gc.fillRect(skinXPositions[i], y, skinWidths[i], skins[i].getHeight());
                gc.drawImage(skinNames[i], skinXPositions[i], y - 25, 100, 20);
            }
            //draws all skins
            gc.drawImage(skins[i], skinXPositions[i], y);
        }
    }

    //handles when the button is right-clicked
    public void handleMouseClick(MouseEvent event, Dino dino) {
        double clickX = event.getX();
        double clickY = event.getY();

        for (int i = 0; i < skins.length; i++) {
            double skinX = skinXPositions[i];
            double skinY = 150; // Assuming all skins are placed at y = 0
            //System.out.println(clickX + " " + clickY);
            //calculates the click area of the button
            if (clickX >= skinX && clickX <= (skinX + skinWidths[i]) && clickY >= skinY && 
            		clickY <= (skinY + skins[i].getHeight()) && !skinselected) {
                setDinoImages(dino, i);
                //System.out.println("Skin " + (i + 1) + " selected!");
                skinselected = true;
                break;
            }
        }
    }

    //handles when the cursor hovers over the button
    public void handleMouseMoved(MouseEvent event) {
        double mouseX = event.getX();
        double mouseY = event.getY();
        boolean hovered = false;

        for (int i = 0; i < skins.length; i++) {
            double skinX = skinXPositions[i];
            double skinY = 150;
            //calculates the hover area of the button
            if (mouseX >= skinX && mouseX <= (skinX + skinWidths[i]) && mouseY >= skinY && 
            		mouseY <= (skinY + skins[i].getHeight())) {
                //System.out.println("mouse hovered at " + mouseX + " " + mouseY);
                hoveredSkinIndex = i;
                hovered = true;
                break;
            }
        }

        if (!hovered) {
            hoveredSkinIndex = -1;
        }
    }

    //when the player selects a skin, all of its corresponding dino states are passed to dino
    private void setDinoImages(Dino dino, int skinIndex) {
        String skinPrefix = "";
        switch (skinIndex) {
            case 0: skinPrefix = "dino"; break;
            case 1: skinPrefix = "dino-work"; break;
            case 2: skinPrefix = "dino-viking"; break;
            case 3: skinPrefix = "dino-gentleman"; break;
            case 4: skinPrefix = "dino-miss"; break;
            case 5: skinPrefix = "dino-girl"; break;
            case 6: skinPrefix = "dino-granny"; break;
            case 7: skinPrefix = "dino-green"; break;
            default: break;
        }

        dino.setDinoJumpImage(skinPrefix + "-jump.png");
        dino.setDinoRun1Image(skinPrefix + "-run-1.png");
        dino.setDinoRun2Image(skinPrefix + "-run-2.png");
        dino.setDinoDownRun1Image(skinPrefix + "-down-run-1.png");
        dino.setDinoDownRun2Image(skinPrefix + "-down-run-2.png");
        dino.setDinoDeadImage(skinPrefix + "-dead.png");
    }

    //boolean method to track if a skin is selected
    public boolean SkinSelected() {
        return skinselected;
    }
}
