package manager;

import game_object.Birds;
import game_object.Cactuses;
import javafx.geometry.Bounds;
import javafx.scene.canvas.GraphicsContext;
import misc.EnemyType;
import user_interface.GameScene;

/**
 * EnemyManager class manages the percentages of creating enemies and distance between them
 * Meanwhile, the class checks if enemies have collision with dino
 * It also clears the enemy
 * There is a class which allows players to set the difficulty
 */

public class EnemyManager {
	
	// Value by which chance of creating new enemy increasing
	private static final double PERCENTAGE_INC = 0.0001; // Increment percentage for enemy creation chance
	private static final double DISTANCE_DEC = -0.005; // Decrease distance between enemies over time
	private static int MINIMUM_DISTANCE = 250; // Minimum distance between enemies
	
	// Parameters like distance between enemies and chance of spawning an enemy
	private double distanceBetweenEnemies = 500; // Initial distance between enemies
	private double cactusesPercentage = 1; // Initial chance of creating a cactus
	private double birdsPercentage = 0.5; // Initial chance of creating a bird
	
	private Cactuses cactuses;
	private Birds birds;
	
    // Constructor 
	public EnemyManager(GameScene gameScene) {
		cactuses = new Cactuses(gameScene.getSpeed(), this);
		birds = new Birds(gameScene.getSpeed(), this);
	}
	
	// Get the distance between enemies
	public double getDistanceBetweenEnemies() {
		return distanceBetweenEnemies;
	}

    // Get the percentage chance of creating cactuses
	public double getCactusesPercentage() {
		return cactusesPercentage;
	}

	// Get the percentage chance of creating birds
	public double getBirdsPercentage() {
		return birdsPercentage;
	}

	// Update the position of enemies
	public void updatePosition() {
		cactusesPercentage += PERCENTAGE_INC; // Increment of creating cactuses
		birdsPercentage += PERCENTAGE_INC; // Increment of creating birds
		if (distanceBetweenEnemies > MINIMUM_DISTANCE) {
			distanceBetweenEnemies += DISTANCE_DEC; // Decrease the distance between enemies
		}
		cactuses.updatePosition(); // Update position of cactuses
		birds.updatePosition(); // Update position of birds
		if (cactuses.spaceAvailable() && birds.spaceAvailable()) {
			// "Randomly" choosing new enemy type 
			EnemyType newEnemyType = EnemyType.values()[(int) (Math.random() * EnemyType.values().length)];
			switch (newEnemyType) {
				case CACTUS:
					if (cactuses.createCactuses()) {
						break;
					}
				case BIRD:
					if (birds.createBird()) {
						break;
					}
				default:
					cactuses.createCactuses();
					break;
			}
		}
	}
	
	// Check if enemies collide with dino
	public boolean collisionDetector(Bounds hitBox) {
		return cactuses.collisiondetector(hitBox) || birds.collisiondetector(hitBox);
	}
	
	// Clear all the enemies
	public void clearEnemy() {
		cactuses.clearCactuses();
		birds.clearBirds();
	}
	
	// Draw all the enemies
	public void draw(GraphicsContext gc) {
		cactuses.draw(gc);
		birds.draw(gc);
	}
	
	// Draw the hit boxes for enemies
	public void drawHitbox(GraphicsContext gc) {
		cactuses.drawHitBox(gc);
		birds.drawHitBox(gc);
	}
	
	// Set the difficulty mode of the game while adjusting enemies parameters
	public void difficultyMode(String difficulty) {
        System.out.println(difficulty);
        switch (difficulty) {
            case "Easy":
                GameScene.LAND_SPEED = 15;
                MINIMUM_DISTANCE = 250;
                distanceBetweenEnemies = 500;
                cactusesPercentage = 1;
                birdsPercentage = 0.5;
                System.out.println("Easy");
                System.out.println(GameScene.LAND_SPEED);
                break;
            case "Medium":
            	GameScene.LAND_SPEED = 20;
                MINIMUM_DISTANCE = 400;
                distanceBetweenEnemies = 700;
                cactusesPercentage = 1;
                birdsPercentage = 0.5;
                System.out.println(GameScene.LAND_SPEED);
                break;
            case "Hard":
            	GameScene.LAND_SPEED = 30;
                MINIMUM_DISTANCE = 700;
                distanceBetweenEnemies = 1000;
                cactusesPercentage = 1;
                birdsPercentage = 0.5;
                System.out.println(GameScene.LAND_SPEED);
                break;
            default:
                System.out.println("String detect error!");
                break;
        }
    }
}
