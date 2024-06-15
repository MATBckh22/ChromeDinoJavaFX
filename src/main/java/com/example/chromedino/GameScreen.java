package UserInterface;

import com.example.chromedino.Birds;
import com.example.chromedino.Cactuses;
import com.example.chromedino.Clouds;
import com.example.chromedino.Dino;
import com.example.chromedino.Land;

import javafx.animation.AnimationTimer;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import misc.Animation;
import misc.Controls;
import misc.DinoState;
import misc.GameState;
import com.example.chromedino.Score;
import misc.EnemyManager;
import com.example.chromedino.*;

import static com.example.chromedino.HelloApplication.SCREEN_WIDTH;
import static com.example.chromedino.HelloApplication.SCREEN_HEIGHT;

public class GameScreen extends Canvas {
    private AnimationTimer animationTimer;

    private static final int STARTING_SPEED = -5;
    private static final double DIFFICULTY_INC = -0.0002;

    public static final double GRAVITY = 1.3;
    public static final double SPEED_Y = -20;
    public static final int GROUND_Y = 0; //dskd: 280

    private final int FPS = 100;
    private final int NS_PER_FRAME = 1_000_000_000 / FPS;

    private double SpeedX = STARTING_SPEED;
    private GameState gameState = GameState.GAME_START;
    private int introCountdown = 1000;
    private boolean introJump = true;
    private boolean showHitBoxes = true;
    private boolean collisions = true;

    private Controls controls;
    private Dino dino;
    private Land land;
    private Clouds clouds;
    private Score score;
    private GraphicsContext gc;
    private EnemyManager eManager;

    public GameScreen(GraphicsContext gc){
        //super(scene.getWidth(), scene.getHeight());
        this.gc = gc;
        dino = new Dino(controls, gc);
        land = new Land(GROUND_Y, STARTING_SPEED, "land.png");
        clouds = new Clouds(STARTING_SPEED);
        score = new Score();
        System.out.println(gameState);
        setFocusTraversable(true);

        //key binds when start
        setOnKeyPressed(event -> {
            switch (event.getCode()) {
                case UP:
                    pressUpAction();
                    break;
                case DOWN:
                    pressDownAction();
                    break;
                case P:
                    pressPauseAction();
                    break;
                case D:
                    pressDebugAction();
                    break;
            }
        });

        setOnKeyReleased(event -> {
            switch (event.getCode()) {
                case UP:
                    releaseUpAction();
                    break;
                case DOWN:
                    releaseDownAction();
                    break;
            }
        });

        new AnimationTimer() {
            @Override
            public void handle(long now) {
                update();
                render();
            }
        }.start();
    }

    private void update(){
        switch(gameState){
            case GAME_INTRO:
                dino.updateMovement();
                if(!introJump && dino.getDinoState() == DinoState.DINO_RUN) land.update();
                clouds.updatePosition();
                introCountdown += SpeedX;
                if(introCountdown <= 0) gameState = GameState.GAME_IN_PROGRESS;
                if(introJump){
                    dino.jump();
                    dino.setDinoState(DinoState.DINO_JUMP);
                    introJump = false;
                }
                break;
            case GAME_IN_PROGRESS:
                SpeedX += DIFFICULTY_INC;
                dino.updateMovement();
                land.update();
                clouds.updatePosition();
                eManager.updatePosition();
                if(collisions && eManager.isCollision(dino.getHitBox())) {
                    gameState = GameState.GAME_OVER;
                    dino.dinoGameOver();
                    score.writeScore();
                    //gameOverSound.play();
                }
                score.scoreUp();
                break;
            default:
                break;
        }
    }

    private void render(){
        System.out.println(dino.getDinoState());
        switch(gameState){
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

    private void startScreen(GraphicsContext gc) {
        gc.setFill(Color.WHITE);
        gc.fillRect(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);
        // Draw land
        land.render(gc);

        // Draw dino
        dino.draw();

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
        // Draw in-progress screen elements (assuming inProgressScreen(g) handles this)
        inProgressScreen(gc);

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
        // Draw in-progress screen elements (assuming inProgressScreen(gc) handles this)
        inProgressScreen(gc);

        // Load paused image
        Image pausedImage = new Image("resources/paused.png");

        // Draw paused image centered above center
        double pausedX = (SCREEN_WIDTH - pausedImage.getWidth()) / 2.0;
        double pausedY = SCREEN_HEIGHT / 2.0 - pausedImage.getHeight();
        gc.drawImage(pausedImage, pausedX, pausedY);
    }

    public void pressUpAction(){
        if(gameState == GameState.GAME_IN_PROGRESS){
            dino.jump();
            dino.setDinoState(DinoState.DINO_JUMP);
        }
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
            gameState = GameState.GAME_IN_PROGRESS;
        }
    }

    //set states, different to controls
    public void pressDownAction(){
        if(dino.getDinoState() != DinoState.DINO_JUMP && gameState == gameState.GAME_IN_PROGRESS){
            dino.setDinoState(DinoState.DINO_DOWN_RUN);
        }
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
        if(gameState == GameState.GAME_IN_PROGRESS)
            gameState = GameState.GAME_PAUSED;
        else
            gameState = GameState.GAME_IN_PROGRESS;
    }
}
