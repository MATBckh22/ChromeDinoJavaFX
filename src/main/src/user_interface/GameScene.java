package user_interface;

import javafx.animation.AnimationTimer;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.Scene;
import manager.ControlsManager;
import manager.EnemyManager;
import manager.DifficultyManager;
import manager.SceneryManager;
import manager.SkinManager;
import manager.SoundManager;
import misc.*;

import static user_interface.GameStage.SCREEN_WIDTH;

import game_object.Clouds;
import game_object.Dino;
import game_object.Land;
import game_object.Mountains;
import game_object.Score;

import static user_interface.GameStage.SCREEN_HEIGHT;

/**
 * This is the main scene for the entire game to run on, it is the main body of the code structure
 * this class handles which method to call from other classes depending on game states
 */

public class GameScene extends Canvas {

    private static final int STARTING_SPEED = -5;
    private static final double DIFFICULTY_INC = -0.0002;
    //default land speed to be 15, can change depending on the difficulty
    public static int LAND_SPEED = 15;

    public static final int GROUND_Y = 0;

    private final int FPS = 100;

    private double SpeedX = STARTING_SPEED;
    //starts at skin selection screen
    private GameState gameState = GameState.GAME_SKIN;
    //countdown for the intro screen
    private int introCountdown = 1000;
    //to track the first jump of the game
    private boolean introJump = true;
    //debug
    private boolean showHitBoxes = false;
    private boolean collisions = true;

    private Dino dino;
    private Land land;
    private Land mountainLand;
    private Clouds clouds;
    private Score score;
    private GraphicsContext gc;
    private EnemyManager eManager;
    private ControlsManager cManager;
    private SkinManager sManager;
    private DifficultyManager dManager;
    private SoundManager chooseSkinSound;
    private SoundManager inGameSound;
    private SoundManager gameOverSound;
    private Mountains mountains;
    private SceneryManager sceManager;
    //exit button for zen mode
    private Image escZenButton = new Image("esc.png");

    private boolean inGameSoundInitialized = false;

    public GameScene(Scene scene, GraphicsContext gc) {
        super(scene.getWidth(), scene.getHeight());
        this.gc = gc;
        //initialization
        Controls controls = new Controls(scene);
        dino = new Dino(controls, gc);
        land = new Land(GROUND_Y, LAND_SPEED, "land.png");
        mountainLand = new Land(GROUND_Y + 10, LAND_SPEED, "background land.png");
        clouds = new Clouds(LAND_SPEED);
        score = new Score();
        cManager = new ControlsManager(controls, this);
        eManager = new EnemyManager(this);
        sManager = new SkinManager();
        dManager = new DifficultyManager();
        sceManager = new SceneryManager();
        chooseSkinSound = new SoundManager("resources/A Lonely Cherry Tree.mp3");
        gameOverSound = new SoundManager("resources/dead.wav");
        //mountain layers and their positions and speeds
        double[] speeds = {2, 5, 8, 10};
        String[] imagePaths = {"background layer 0.png", "background layer 1.png", "background layer 2.png", 
        		"background layer 3.png"};
        double[] startYs = {0, 10, 25, 25};
        //balloons
        String[] balloonImagePaths = {"hot air balloon blue lime.png", "hot air balloon green lime.png", 
        		"hot air balloon red yellow.png"};
        mountains = new Mountains(speeds, imagePaths, startYs, balloonImagePaths);
        //System.out.println(gameState);

        new AnimationTimer() {
            @Override
            public void handle(long now) {
                update();
                render();
            }
        }.start();

        //this is necessary to detect mouse events
        scene.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            sManager.handleMouseClick(event, dino);
        });
        scene.addEventHandler(MouseEvent.MOUSE_MOVED, event -> {
            sManager.handleMouseMoved(event);
        });

        scene.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            dManager.handleMouseClick(event);
        });
        scene.addEventHandler(MouseEvent.MOUSE_MOVED, event -> {
            dManager.handleMouseMoved(event);
        });

        scene.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            sceManager.handleMouseClick(event);
        });
        scene.addEventHandler(MouseEvent.MOUSE_MOVED, event -> {
            sceManager.handleMouseMoved(event);
        });

        scene.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            handleEscClick(event);
        });
        scene.addEventHandler(MouseEvent.MOUSE_MOVED, event -> {
            handleEscMoved(event);
        });
    }

    //MAIN UPDATE
    private void update() {
        //System.out.println(gameState);
        setFocusTraversable(true);
        cManager.update();
        switch(gameState) {
            case GAME_INTRO:
                chooseSkinSound.triggerPlay();
                dino.updateMovement();
                if(!introJump && dino.getDinoState() == DinoState.DINO_RUN) {
                    //if player selects "mountain" background
                    if(sceManager.getSelectedscenery().equals("Mountains")) {
                        mountainLand.update();
                        mountains.update();
                    }
                    else{
                        land.update();
                    }
                }
                clouds.updatePosition();
                introCountdown += SpeedX;
                //System.out.println(introCountdown);
                if (!inGameSoundInitialized) {
                	if (dManager.getSelectedDifficulty().equals("Zen")) {
                		inGameSound = new SoundManager("resources/nonamenosocks - fleeing the party early.mp3");
                	}
                	else {
                		inGameSound = new SoundManager("resources/Run As Fast As You Can.mp3");
                	}
                	inGameSound.triggerPlay();
            		inGameSoundInitialized = true;
                }
                if(introCountdown <= 0) {
                	gameState = GameState.GAME_IN_PROGRESS;
                }
                if(introJump){
                    dino.jump();
                    dino.setDinoState(DinoState.DINO_JUMP);
                    introJump = false;
                }
                break;
            case GAME_IN_PROGRESS:
                chooseSkinSound.stop();
                SpeedX += DIFFICULTY_INC;
                dino.updateMovement();
                land.update();
                clouds.updatePosition();
                mountainLand.update();
                mountains.update();
                eManager.updatePosition();
                mountains.update();
                if(collisions && eManager.collisionDetector(dino.getHitBox())) {
                    //ignores game over if collision is detected, moving to GAME_ZEN
                    if (dManager.getSelectedDifficulty().equals("Zen")) {
                        gameState = GameState.GAME_ZEN;
                        dino.dinoGameOver();
                    } else {
                        gameState = GameState.GAME_OVER;
                        dino.dinoGameOver();
                        score.writeScore();
                        inGameSound.stop();
                        inGameSoundInitialized = false;
                        gameOverSound.triggerPlay();
                    }
                }
                if(!dManager.getSelectedDifficulty().equals("Zen")) {
                	score.scoreUp();
                }
                break;
            default:
                break;
        }
    }

    //MAIN RENDER
    private void render() {
        gc.setFill(Color.WHITE);
        gc.fillRect(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);

        //System.out.println(dino.getDinoState());
        switch(gameState){
            case GAME_SKIN:
                SkinScreen(gc);
                break;
            case GAME_DIFFICULTY:
                difficultyScreen(gc);
                break;
            case GAME_SCENERY:
                sceneryScreen(gc);
                break;
            case GAME_START:
                startScreen(gc);
                break;
            case GAME_INTRO:
                introScreen(gc);
                break;
            case GAME_IN_PROGRESS:
                inProgressScreen(gc);
                break;
            case GAME_ZEN:
                System.out.println("zen");
                zenScreen(gc);
                break;
            case GAME_OVER:
                gameOverScreen(gc);
                break;
            case GAME_PAUSED:
                pausedScreen(gc);
                break;
            default:
                break;
        }
    }

    //getter method for speed
    public int getSpeed() {
        return LAND_SPEED;
    }

    //debug screen drawer
    private void drawDebugMenu() {
        eManager.drawHitbox(gc);
        dino.drawHitBox(gc);
    }

    //centering methods for image drawing
    private double centerX(Image image) {
        return (SCREEN_WIDTH - image.getWidth()) / 2;
    }

    private double centerY(Image image) {
        return (SCREEN_HEIGHT - image.getHeight()) / 2;
    }

    private void SkinScreen(GraphicsContext gc) {
        //clear canvas
        gc.setFill(Color.WHITE);
        gc.fillRect(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);
        
        //initializing images needed for skinScreen
        Image skinImage = new Image("choose.png");
        Image introEnhancedImage = new Image("enhanced.png");
        Image authorsImage = new Image("authors.png");
        Image javafx = new Image("javafx.png");
        Image cactus5 = new Image("cactus-5.png");
        Image cactus8 =  new Image("cactus-8.png");
        
        double centerX1 = (SCREEN_WIDTH - skinImage.getWidth()/2) / 2.0;
        double centerY1 = ((SCREEN_HEIGHT - skinImage.getHeight()/2) / 2.0) - 75;

        double centerX2 = centerX(introEnhancedImage);
        double centerY2 = centerY(introEnhancedImage) - 110;
        
        //draws skinScreen background
        gc.drawImage(skinImage, centerX1, centerY1, skinImage.getWidth()/2, skinImage.getHeight()/2);
        gc.drawImage(introEnhancedImage, centerX2, centerY2);
        gc.drawImage(authorsImage, 0, 5, authorsImage.getWidth()/3, authorsImage.getHeight()/3);
        gc.drawImage(javafx, 1050, 5, javafx.getWidth()/3, javafx.getHeight()/3);
        gc.drawImage(cactus8, 50 , 250 - cactus8.getHeight());
        gc.drawImage(cactus5, 1100, 250 - cactus5.getHeight());
        clouds.updatePosition();
        clouds.draw(gc);
        land.render(gc);
        sManager.drawSkins(gc);
        
        //when a player selects a skin, move to difficultyScreen
        if(sManager.SkinSelected()) gameState = GameState.GAME_DIFFICULTY;
    }

    private void difficultyScreen(GraphicsContext gc) {
        //clear canvas
        gc.setFill(Color.WHITE);
        gc.fillRect(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);
        
        land.render(gc);
        clouds.updatePosition();
        clouds.draw(gc);
        
        Image difficulty = new Image("difficulty.png");
        gc.drawImage(difficulty, centerX(difficulty), centerY(difficulty) - 75);
        dManager.drawDifficulties(gc);
        if (dManager.isDifficultySelected()) {
            //if a difficulty is selected, update all land speed and load backgrounds to move to sceneryScreen
            eManager.difficultyMode(dManager.getSelectedDifficulty());
            clouds = new Clouds(LAND_SPEED);
            land = new Land(GROUND_Y, LAND_SPEED, "land.png");
            mountainLand = new Land(GROUND_Y + 10, LAND_SPEED, "background land.png");
            eManager = new EnemyManager(this);
            gameState = GameState.GAME_SCENERY;
        }
    }

    private void sceneryScreen(GraphicsContext gc) {
        //clear canvas
        gc.setFill(Color.WHITE);
        gc.fillRect(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);
        sceManager.drawSceneries(gc);
        //previews background when mouse cursor hovers over the button
        switch(sceManager.hoveredSceneryIndex) {
            case 0:
                land.update();
                land.render(gc);
                clouds.updatePosition();
                clouds.draw(gc);
                break;
            case 1:
                mountains.update();
                mountainLand.update();
                mountains.render(gc);
                mountainLand.render(gc);
                break;
            default:
                System.out.println("not hovering");
        }
        sceManager.drawSceneries(gc);
        
        //move to start of the game when a background is chosen
        if(sceManager.isscenerySelected()) {
            sceManager.scenerySelected = false;
            gameState = GameState.GAME_START;
        }
    }

    private void startScreen(GraphicsContext gc) {
        //clear canvas
        gc.setFill(Color.WHITE);
        gc.fillRect(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);
        
        //draw background
        if(sceManager.getSelectedscenery().equals("Mountains")) {
            System.out.println("drawing");
            mountains.render(gc);
            mountainLand.render(gc);
            dino.draw();
        }
        else{
            //draw land
            land.render(gc);

            //reset and draw dino
            dino.draw();
            clouds.updatePosition();
            clouds.draw(gc);
        }
        
        //set alpha value based on introCountdown for the fading effect
        double alpha = introCountdown / 1000.0;
        gc.setGlobalAlpha(alpha);
        if(dManager.getSelectedDifficulty().equals("Zen")) {
            Image endlessImage = new Image("endless.png");

            double centerX = (SCREEN_WIDTH - endlessImage.getWidth() / 2) / 2.0;
            double centerY = (SCREEN_HEIGHT - endlessImage.getHeight() / 2) / 2.0;
            gc.drawImage(endlessImage, centerX, centerY - 25, endlessImage.getWidth()/2, 
            		endlessImage.getHeight()/2);
        }
        else {
            //load intro image
            Image introImage = new Image("intro-text.png");

            //draw intro image centered on screen
            double centerX = (SCREEN_WIDTH - introImage.getWidth()) / 2.0;
            double centerY = (SCREEN_HEIGHT - introImage.getHeight()) / 2.0;
            gc.drawImage(introImage, centerX, centerY);

            //reset alpha to 1.0 (fully opaque)
        }
        gc.setGlobalAlpha(1.0);
    }

    private void introScreen(GraphicsContext gc) {
        if(!sceManager.getSelectedscenery().equals("Mountains")) {
            clouds.draw(gc);
        }
        startScreen(gc);
    }

    //handles click exit zen mode button
    private void handleEscClick(MouseEvent event) {
        double clickEscX = event.getX();
        double clickEscY = event.getY();

        int escXPosition = 0;
        double escYPosition = 0;

        if (clickEscX >= escXPosition && clickEscX <= (escXPosition + escZenButton.getWidth() / 2) &&
                clickEscY >= escYPosition && clickEscY <= (escYPosition + escZenButton.getHeight() / 2)) {
            SpeedX = STARTING_SPEED;
            score.scoreReset();
            eManager.clearEnemy();
            dino.resetDino();
            clouds.clearClouds();
            land.resetLand();
            inGameSound.stop();
            sManager.skinselected = false;
            dManager.difficultySelected = false;
            sceManager.scenerySelected = false;
            inGameSoundInitialized = false;
            introCountdown = 1000;
            gameState = GameState.GAME_SKIN;
            chooseSkinSound.triggerPlay();
        }
    }

    //handles hover exit zen mode button
    private void handleEscMoved(MouseEvent event) {
        double mouseEscX = event.getX();
        double mouseEscY = event.getY();
        
        int escXPosition = 0;
        double escYPosition = 0;

        if (mouseEscX >= escXPosition && mouseEscX <= (escXPosition + escZenButton.getWidth() / 2) &&
                mouseEscY >= escYPosition && mouseEscY <= (escYPosition + escZenButton.getHeight() / 2)) {
            System.out.println("hovered");
            gc.setFill(Color.LIGHTGRAY); // Set the color for the hover effect
            gc.fillRect(escXPosition, escYPosition, escZenButton.getWidth() / 2, escZenButton.getHeight() / 2);
        }
    }

    private void inProgressScreen(GraphicsContext gc) {
        //draw background
        if(sceManager.getSelectedscenery().equals("Mountains")) {
            mountains.render(gc);
            mountainLand.render(gc);
            dino.draw();
        }
        else{
            //draw land
            land.render(gc);

            //reset and draw dino
            dino.draw();
            clouds.updatePosition();
            clouds.draw(gc);
        }
        //draw esc button if zen mode
        if(dManager.getSelectedDifficulty().equals("Zen")) {
            gc.drawImage(escZenButton, 0, 0, escZenButton.getWidth() / 2, escZenButton.getHeight() / 2);
        }
        eManager.draw(gc);
        if(!dManager.getSelectedDifficulty().equals("Zen")) score.draw(gc);
        if(showHitBoxes)
            drawDebugMenu();
    }

    private void zenScreen(GraphicsContext gc) {
        if(sceManager.getSelectedscenery().equals("Mountains")) {
            mountains.render(gc);
            mountainLand.render(gc);
            dino.draw();
        }
        else {
            //draw land
            land.render(gc);

            //reset and draw dino
            dino.draw();
            clouds.draw(gc);
        }
        eManager.draw(gc);
    }

    private void gameOverScreen(GraphicsContext gc) {
        if(sceManager.getSelectedscenery().equals("Mountains")) {
            mountains.render(gc);
            mountainLand.render(gc);
            dino.draw();
        }
        else {
            // Draw land
            land.render(gc);

            // Reset and draw dino
            dino.draw();
            clouds.updatePosition();
            clouds.draw(gc);
        }
        eManager.draw(gc);

        //load game over and replay images
        Image gameOverImage = new Image("game-over.png");
        Image replayImage = new Image("replay.png");

        //draw game over image centered above center
        double gameOverX = (SCREEN_WIDTH - gameOverImage.getWidth()) / 2.0;
        double gameOverY = SCREEN_HEIGHT / 2.0 - gameOverImage.getHeight() * 2;
        gc.drawImage(gameOverImage, gameOverX, gameOverY);

        //draw replay image centered below center
        double replayX = (SCREEN_WIDTH - replayImage.getWidth()) / 2.0;
        double replayY = SCREEN_HEIGHT / 2.0;
        gc.drawImage(replayImage, replayX, replayY);
    }

    private void pausedScreen(GraphicsContext gc) {
        if(sceManager.getSelectedscenery().equals("Mountains")) {
            mountains.render(gc);
            mountainLand.render(gc);
            dino.draw();
        }
        else{
            //draw land
            land.render(gc);

            //reset and draw dino
            dino.draw();
            clouds.draw(gc);
        }
        eManager.draw(gc);

        //load paused image
        Image pausedImage = new Image("paused.png");

        //draw paused image centered above center
        double pausedX = (SCREEN_WIDTH - pausedImage.getWidth()) / 2.0;
        double pausedY = SCREEN_HEIGHT / 2.0 - pausedImage.getHeight();
        gc.drawImage(pausedImage, pausedX, pausedY);

    }

    //handles up action
    public void pressUpAction() {
        //System.out.println("Pressed up!");
        if(gameState == GameState.GAME_IN_PROGRESS) {
            dino.jump();
            dino.setDinoState(DinoState.DINO_JUMP);
        }
        if(gameState == GameState.GAME_ZEN) {
            eManager.clearEnemy();
            dino.jump();
            dino.setDinoState(DinoState.DINO_JUMP);
            gameState = GameState.GAME_IN_PROGRESS;
        }
        releaseUpAction();
    }

    //handles up action
    public void releaseUpAction() {
        //when game starts, switch to intro scene
        if(gameState == gameState.GAME_START) gameState = gameState.GAME_INTRO;
        //when game ends, reset everything
        if(gameState == GameState.GAME_OVER){
            SpeedX = STARTING_SPEED;
            score.scoreReset();
            eManager.clearEnemy();
            dino.resetDino();
            clouds.clearClouds();
            land.resetLand();
            sManager.skinselected = false;
            dManager.difficultySelected = false;
            sceManager.scenerySelected = false;
            introCountdown = 1000;
            LAND_SPEED = 15;
            gameState = GameState.GAME_SKIN;
            chooseSkinSound.triggerPlay();
        }
    }

    //handles down action
    public void pressDownAction() {
        if(dino.getDinoState() != DinoState.DINO_JUMP && gameState == gameState.GAME_IN_PROGRESS) {
            dino.setDinoState(DinoState.DINO_DOWN_RUN);
        }
        releaseDownAction();
    }

    //handles down action
    public void releaseDownAction() {
        if(dino.getDinoState() != DinoState.DINO_JUMP && gameState == gameState.GAME_IN_PROGRESS){
            dino.setDinoState(DinoState.DINO_RUN);
        }
    }

    //debug
    public void pressDebugAction() {
        if(showHitBoxes == false)
            showHitBoxes = true;
        else
            showHitBoxes = false;
        if(collisions == true)
            collisions = false;
        else
            collisions = true;
    }

    //handles pause action
    public void pressPauseAction() {
        if(gameState == GameState.GAME_IN_PROGRESS) {
            gameState = GameState.GAME_PAUSED;
            inGameSound.pause();
        }
        else {
            gameState = GameState.GAME_IN_PROGRESS;
            inGameSound.resume();
        }
    }
}
