package manager;

import javafx.scene.input.MouseEvent;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

import static user_interface.GameStage.SCREEN_WIDTH;
import static user_interface.GameStage.SCREEN_HEIGHT;

/*
* Difficulty manager handles the difficultyScreen in GameScreen.
* When a player hovers over to one of the difficulty buttons, it will pop up its corresponding description.
* There are four difficulties in this game: Easy, Medium, Hard, and Zen.
*
* Easy: normal speed similar to the original game.
* Medium: higher speed on everything for more experienced players.
* Hard: hardest difficulty with the highest speed for the truly hardcore players.
* Zen: endless mode, score is turned off and the player can continue the game after the dino dies.
* An ESC button will appear in this mode should the player wants to quit.
*
* Once the player chooses a difficulty, EnemyManager will call the getter method and adjust difficulty parameters.
*/

public class DifficultyManager {

    private Image[] difficulties;
    private String[] difficultyNames = {"Easy", "Medium", "Hard", "Zen"};
    private Image[] difficultyDescriptions;
    private double[] difficultyHeights;
    private double[] difficultyYPositions;
    private int hoveredDifficultyIndex = -1;
    public boolean difficultySelected = false;
    private String selectedDifficulty = "";

    //constructor
    public DifficultyManager() {
        //initialize difficulty images
        difficulties = new Image[]{
                new Image("easy.png"),
                new Image("medium.png"),
                new Image("hard.png"),
                new Image("zen.png")
        };
        //initialize difficulty descriptions
        difficultyDescriptions = new Image[]{
                new Image("easy description.png"),
                new Image("medium description.png"),
                new Image("hard description.png"),
                new Image("zen description.png")
        };

        difficultyHeights = new double[difficulties.length];
        difficultyYPositions = new double[difficulties.length];

        calculateDifficultyPositions();
    }

    //calculates y positions for 4 buttons
    private void calculateDifficultyPositions() {
        double totalHeight = 0;
        for (Image difficulty : difficulties) {
            totalHeight += difficulty.getHeight() / 2;
        }

        double startY = (SCREEN_HEIGHT - totalHeight) / 2;
        double y = startY;
        for (int i = 0; i < difficulties.length; i++) {
            difficultyHeights[i] = difficulties[i].getHeight() / 2;
            difficultyYPositions[i] = y;
            y += difficultyHeights[i] + 5; // Add some spacing between difficulties
        }
    }

    public void drawDifficulties(GraphicsContext gc) {
        double x = (SCREEN_WIDTH - difficulties[0].getWidth() / 2) / 2 - 50;
        for (int i = 0; i < difficulties.length; i++) {
            if (i == hoveredDifficultyIndex) {
                gc.setFill(Color.LIGHTGRAY); //set light grey for the hover effect
                gc.fillRect(x, difficultyYPositions[i], difficulties[i].getWidth() / 2, difficultyHeights[i]);
                //draws description when the cursor hovers over the corresponding difficulty
                gc.drawImage(difficultyDescriptions[hoveredDifficultyIndex], x + 100, difficultyYPositions[i],
                        difficultyDescriptions[hoveredDifficultyIndex].getWidth() / 2, difficultyHeights[i]);
            }
            //draw all buttons
            gc.drawImage(difficulties[i], x, difficultyYPositions[i], 
            		difficulties[i].getWidth() / 2, difficultyHeights[i]);
        }
    }

    //handles when the button is right-clicked
    public void handleMouseClick(MouseEvent event) {
        double clickX = event.getX();
        double clickY = event.getY();

        for (int i = 0; i < difficulties.length; i++) {
            double difficultyX = (SCREEN_WIDTH - difficulties[i].getWidth() / 2) / 2 - 50;
            double difficultyY = difficultyYPositions[i];
            //calculates the click area of the button
            if (clickX >= difficultyX && clickX <= (difficultyX + difficulties[i].getWidth() / 2) && 
            		clickY >= difficultyY && clickY <= (difficultyY + difficultyHeights[i]) && 
            		!difficultySelected) {
                selectedDifficulty = difficultyNames[i];
                //System.out.println("Difficulty " + selectedDifficulty + " selected!");
                difficultySelected = true;
                break;
            }
        }
    }

    //handles when the cursor hovers over the button
    public void handleMouseMoved(MouseEvent event) {
        double mouseX = event.getX();
        double mouseY = event.getY();
        boolean hovered = false;

        for (int i = 0; i < difficulties.length; i++) {
            double difficultyX = (SCREEN_WIDTH - difficulties[i].getWidth() / 2) / 2 - 50;
            double difficultyY = difficultyYPositions[i];
            //calculates the hover area of the button
            if (mouseX >= difficultyX && mouseX <= (difficultyX + difficulties[i].getWidth() / 2) && 
            		mouseY >= difficultyY && mouseY <= (difficultyY + difficultyHeights[i])) {
                //System.out.println("hovered");
                hoveredDifficultyIndex = i;
                hovered = true;
                break;
            }
        }

        if (!hovered) {
            hoveredDifficultyIndex = -1;
        }
    }

    //boolean method to track if a difficulty is selected
    public boolean isDifficultySelected() {
        return difficultySelected;
    }

    //getter method for selected difficulty
    public String getSelectedDifficulty() {
        return selectedDifficulty;
    }
}
