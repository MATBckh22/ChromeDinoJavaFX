package manager;

import javafx.scene.input.MouseEvent;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

import static user_interface.GameStage.SCREEN_WIDTH;
import static user_interface.GameStage.SCREEN_HEIGHT;

/*
* This class is to handle sceneryScreen in GameScreen.
* When the player's mouse cursor hovers over to the button,
* it will show a preview of the selected background.
* Once a background is chosen, scenerySelected becomes false and changes to the next screen.
* This class is scalable depending on the number of backgrounds/sceneries.
*/

public class SceneryManager {
    private Image[] sceneries;
    private String[] sceneryNames = {"Default", "Mountains"};
    private Image[] sceneryDescriptions;
    private double[] sceneryWidths;
    private double[] sceneryXPositions;
    public int hoveredSceneryIndex = -1;
    public boolean scenerySelected = false;
    private String selectedScenery = "";

    //constructor, initialize background buttons and their descriptions
    public SceneryManager() {
        // Initialize scenery images
        sceneries = new Image[]{
                new Image("default.png"),
                new Image("mountains.png")
        };
        sceneryDescriptions = new Image[]{
                new Image("original.png"),
                new Image("unique.png")
        };

        sceneryWidths = new double[sceneries.length];
        sceneryXPositions = new double[sceneries.length];

        calculateSceneryPositions();
    }

    //calculates x positions for 2 buttons
    private void calculateSceneryPositions() {
        double totalWidth = 0;
        for (Image scenery : sceneries) {
            totalWidth += scenery.getWidth() / 2;
        }

        double startX = (SCREEN_WIDTH - totalWidth) / 2;
        double x = startX;
        for (int i = 0; i < sceneries.length; i++) {
            sceneryWidths[i] = sceneries[i].getWidth() / 2;
            sceneryXPositions[i] = x;
            x += sceneryWidths[i] + 50; //horizontal spacing between backgrounds
        }
    }


    public void drawSceneries(GraphicsContext gc) {
        double y = (SCREEN_HEIGHT - sceneries[0].getHeight() / 2) / 2;
        for (int i = 0; i < sceneries.length; i++) {
            if (i == hoveredSceneryIndex) {
                gc.setFill(Color.TRANSPARENT); //Set transparent here for
                gc.fillRect(sceneryXPositions[i], y, sceneryWidths[i], sceneries[i].getHeight() / 2);
                //previews for different backgrounds
                gc.drawImage(sceneryDescriptions[hoveredSceneryIndex],
                        (SCREEN_WIDTH - sceneryDescriptions[hoveredSceneryIndex].getWidth() / 2) / 2,
                        20,
                        sceneryDescriptions[hoveredSceneryIndex].getWidth() / 2,
                        sceneryDescriptions[hoveredSceneryIndex].getHeight() / 2);
            }
            //draw buttons
            gc.drawImage(sceneries[i], sceneryXPositions[i], y, sceneryWidths[i], sceneries[i].getHeight() / 2);
        }
    }

    //handles when the button is right-clicked
    public void handleMouseClick(MouseEvent event) {
        double clickX = event.getX();
        double clickY = event.getY();

        for (int i = 0; i < sceneries.length; i++) {
            double sceneryX = sceneryXPositions[i];
            double sceneryY = (SCREEN_HEIGHT - sceneries[i].getHeight() / 2) / 2;
            //calculates the click area of the button
            if (clickX >= sceneryX && clickX <= (sceneryX + sceneryWidths[i]) && clickY >= sceneryY && 
            		clickY <= (sceneryY + sceneries[i].getHeight() / 2) && !scenerySelected) {
                selectedScenery = sceneryNames[i];
                //System.out.println("scenery " + selectedScenery + " selected!");
                scenerySelected = true;
                break;
            }
        }
    }

    //handles when the cursor hovers over the button
    public void handleMouseMoved(MouseEvent event) {
        double mouseX = event.getX();
        double mouseY = event.getY();
        boolean hovered = false;

        for (int i = 0; i < sceneries.length; i++) {
            double sceneryX = sceneryXPositions[i];
            double sceneryY = (SCREEN_HEIGHT - sceneries[i].getHeight() / 2) / 2;
            //calculates the hover area of the button
            if (mouseX >= sceneryX && mouseX <= (sceneryX + sceneryWidths[i]) && mouseY >= sceneryY && 
            		mouseY <= (sceneryY + sceneries[i].getHeight() / 2)) {
                hoveredSceneryIndex = i;
                //System.out.println("hovered");
                hovered = true;
                break;
            }
        }

        if (!hovered) {
            hoveredSceneryIndex = -1;
        }
    }

    //boolean method to track if a background is selected
    public boolean isscenerySelected() {
        return scenerySelected;
    }

    //getter method for selected background
    public String getSelectedscenery() {
        return selectedScenery;
    }
}
