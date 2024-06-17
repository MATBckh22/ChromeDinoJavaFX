package misc;

import com.example.chromedino.Birds;
import com.example.chromedino.Cactuses;
import com.example.chromedino.Clouds;
import com.example.chromedino.Dino;
import com.example.chromedino.Land;
import javafx.scene.input.MouseEvent;
import javafx.scene.Node;
import javafx.animation.AnimationTimer;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import misc.*;
import com.example.chromedino.Score;
import UserInterface.GameScreen;

import static com.example.chromedino.HelloApplication.SCREEN_WIDTH;
import static com.example.chromedino.HelloApplication.SCREEN_HEIGHT;

public class SkinManager {

    private Image[] skins;
    private double[] skinWidths;
    private double[] skinXPositions;
    public boolean skinselected = false;

    public SkinManager() {
        // Initialize skins
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

        skinWidths = new double[skins.length];
        skinXPositions = new double[skins.length];

        calculateSkinPositions();
    }

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
        double y = 150; // Change y as needed
        for (int i = 0; i < skins.length; i++) {
            gc.drawImage(skins[i], skinXPositions[i], y);
        }
    }

    public void handleMouseClick(MouseEvent event, Dino dino) {
        double clickX = event.getX();
        double clickY = event.getY();

        for (int i = 0; i < skins.length; i++) {
            double skinX = skinXPositions[i];
            double skinY = 150; // Assuming all skins are placed at y = 0
            System.out.println(clickX + " " + clickY);
            if (clickX >= skinX && clickX <= (skinX + skinWidths[i]) && clickY >= skinY && clickY <= (skinY + skins[i].getHeight()) && !skinselected) {
                setDinoImages(dino, i);
                System.out.println("Skin " + (i + 1) + " selected!");
                skinselected = true;
                break;
            }
        }
    }

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
        System.out.println(skinPrefix + "-jump.png");
        dino.setDinoRun1Image(skinPrefix + "-run-1.png");
        dino.setDinoRun2Image(skinPrefix + "-run-2.png");
        dino.setDinoDownRun1Image(skinPrefix + "-down-run-1.png");
        dino.setDinoDownRun2Image(skinPrefix + "-down-run-2.png");
        dino.setDinoDeadImage(skinPrefix + "-dead.png");
    }

    public boolean SkinSelected(){
        return skinselected;
    }
}