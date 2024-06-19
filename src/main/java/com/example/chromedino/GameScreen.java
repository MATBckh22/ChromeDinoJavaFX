package UserInterface;

import com.example.chromedino.Clouds;
import com.example.chromedino.Dino;
import com.example.chromedino.Land;
import javafx.scene.input.MouseEvent;
import misc.SkinManager;

import javafx.animation.AnimationTimer;
import javafx.scene.Scene;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import misc.*;
import com.example.chromedino.Score;
import misc.SoundManager;

import static com.example.chromedino.HelloApplication.SCREEN_WIDTH;
import static com.example.chromedino.HelloApplication.SCREEN_HEIGHT;

public class GameScreen extends Canvas {

    private static final int STARTING_SPEED = -5;
    private static final double DIFFICULTY_INC = -0.0002;
    private static final int LAND_SPEED = 15;

    public static final double GRAVITY = 1.3;
    public static final double SPEED_Y = -20;
    public static final int GROUND_Y = 0; //dskd: 280

    private final int FPS = 100;
    private final int NS_PER_FRAME = 1_000_000_000 / FPS;

    private double SpeedX = STARTING_SPEED;
    private GameState gameState = GameState.GAME_SKIN;
    private int introCountdown = 1000;
    private boolean introJump = true;
    private boolean showHitBoxes = false;
    private boolean collisions = true;

    private Controls controls;
    private Dino dino;
    private Land land;
    private Clouds clouds;
    private Score score;
    private GraphicsContext gc;
    private EnemyManager eManager;
    private ControlManager cManager;
    private SkinManager sManager;
    private SoundManager chooseSkinSound;
    private SoundManager inGameSound;
    private SoundManager gameOverSound;

    private boolean inGameSoundInitialized = false;

    public GameScreen(Scene scene, GraphicsContext gc){
        super(scene.getWidth(), scene.getHeight());
        this.gc = gc;
        controls = new Controls(scene);
        dino = new Dino(controls, gc);
        land = new Land(GROUND_Y, LAND_SPEED, "land.png");
        clouds = new Clouds(LAND_SPEED);
        score = new Score();
        cManager = new ControlManager(controls, this);
        eManager = new EnemyManager();
        sManager = new SkinManager();
        chooseSkinSound = new SoundManager("src/main/resources/A Lonely Cherry Tree.mp3");
        gameOverSound = new SoundManager("src/main/resources/dead.wav");
        //System.out.println(gameState);

        new AnimationTimer() {
            @Override
            public void handle(long now) {
                update();
                render();
            }
        }.start();

        scene.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            sManager.handleMouseClick(event, dino);
        });
        scene.addEventHandler(MouseEvent.MOUSE_MOVED, event -> {
            sManager.handleMouseMoved(event);
        });
    }
    private void update(){
        //System.out.println(gameState);
        setFocusTraversable(true);
        cManager.update();
        switch(gameState){
            case GAME_INTRO:
                chooseSkinSound.triggerPlay();
                dino.updateMovement();
                if(!introJump && dino.getDinoState() == DinoState.DINO_RUN) land.update();
                clouds.updatePosition();
                introCountdown += SpeedX;
                //System.out.println(introCountdown);
                if (!inGameSoundInitialized){
                    inGameSound = new SoundManager("src/main/resources/Run As Fast As You Can.mp3");
                    inGameSound.triggerPlay();
                    inGameSoundInitialized = true;
                }
                if(introCountdown <= 0) gameState = GameState.GAME_IN_PROGRESS;
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
                eManager.updatePosition();
                if(collisions && eManager.isCollision(dino.getHitBox())) {
                    gameState = GameState.GAME_OVER;
                    dino.dinoGameOver();
                    score.writeScore();
                    inGameSound.stop();
                    inGameSoundInitialized = false;
                    gameOverSound.triggerPlay();
                }
                score.scoreUp();
                break;
            default:
                break;
        }
    }

    private void render(){
        gc.setFill(Color.WHITE);
        gc.fillRect(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);
        //System.out.println(dino.getDinoState());
        switch(gameState){
            case GAME_SKIN:
                SkinScreen(gc);
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

    private void drawDebugMenu(){
        eManager.drawHitbox(gc);
        dino.drawHitBox(gc);
    }

    private double centerX(Image image){
        return (SCREEN_WIDTH - image.getWidth()) / 2;
    }

    private double centerY(Image image){
        return (SCREEN_HEIGHT - image.getHeight()) / 2;
    }

    private void SkinScreen(GraphicsContext gc){
        gc.setFill(Color.WHITE);
        gc.fillRect(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);
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
        if(sManager.SkinSelected()) gameState = GameState.GAME_START;
    }
    private void startScreen(GraphicsContext gc) {
        //System.out.println("Start Screen");
        gc.setFill(Color.WHITE);
        gc.fillRect(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);
        // Draw land
        land.render(gc);

        // Reset and draw dino
        dino.draw();
        clouds.updatePosition();
        clouds.draw(gc);
        // Load intro image
        Image introImage = new Image("intro-text.png");

        // Set alpha composite based on introCountdown
        double alpha = introCountdown / 1000.0;
        gc.setGlobalAlpha(alpha);

        // Draw intro image centered on screen
        double centerX = (SCREEN_WIDTH - introImage.getWidth()) / 2.0;
        double centerY = (SCREEN_HEIGHT - introImage.getHeight()) / 2.0;
        gc.drawImage(introImage, centerX, centerY);

        // Reset alpha composite to 1.0 (fully opaque)
        gc.setGlobalAlpha(1.0);
    }

    private void introScreen(GraphicsContext gc) {
        clouds.draw(gc);
        startScreen(gc);
    }

    private void inProgressScreen(GraphicsContext gc) {
        clouds.draw(gc);
        land.render(gc);
        eManager.draw(gc);
        dino.draw();
        score.draw(gc);
        if(showHitBoxes)
            drawDebugMenu();
    }

    private void gameOverScreen(GraphicsContext gc) {
        clouds.draw(gc);
        dino.draw();
        eManager.draw(gc);
        land.render(gc);

        // Load game over and replay images
        Image gameOverImage = new Image("game-over.png");
        Image replayImage = new Image("replay.png");

        // Draw game over image centered above center
        double gameOverX = (SCREEN_WIDTH - gameOverImage.getWidth()) / 2.0;
        double gameOverY = SCREEN_HEIGHT / 2.0 - gameOverImage.getHeight() * 2;
        gc.drawImage(gameOverImage, gameOverX, gameOverY);

        // Draw replay image centered below center
        double replayX = (SCREEN_WIDTH - replayImage.getWidth()) / 2.0;
        double replayY = SCREEN_HEIGHT / 2.0;
        gc.drawImage(replayImage, replayX, replayY);
    }

    private void pausedScreen(GraphicsContext gc) {
        clouds.draw(gc);
        land.render(gc);
        dino.draw();
        eManager.draw(gc);

        // Load paused image
        Image pausedImage = new Image("paused.png");

        // Draw paused image centered above center
        double pausedX = (SCREEN_WIDTH - pausedImage.getWidth()) / 2.0;
        double pausedY = SCREEN_HEIGHT / 2.0 - pausedImage.getHeight();
        gc.drawImage(pausedImage, pausedX, pausedY);

    }

    public void pressUpAction(){
        //System.out.println("Pressed up!");
        if(gameState == GameState.GAME_IN_PROGRESS){
            dino.jump();
            dino.setDinoState(DinoState.DINO_JUMP);
        }
        releaseUpAction();
    }

    public void releaseUpAction(){
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
            gameState = GameState.GAME_SKIN;
        }
    }

    //set states, different to controls
    public void pressDownAction(){
        if(dino.getDinoState() != DinoState.DINO_JUMP && gameState == gameState.GAME_IN_PROGRESS){
            dino.setDinoState(DinoState.DINO_DOWN_RUN);
        }
        releaseDownAction();
    }

    //set states, different to controls
    public void releaseDownAction(){
        if(dino.getDinoState() != DinoState.DINO_JUMP && gameState == gameState.GAME_IN_PROGRESS){
            dino.setDinoState(DinoState.DINO_RUN);
        }
    }

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