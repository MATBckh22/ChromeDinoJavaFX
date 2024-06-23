package com.example.chromedino;

import UserInterface.GameScene;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import misc.SoundManager;
import static com.example.chromedino.GameStage.SCREEN_WIDTH;
import static com.example.chromedino.GameStage.SCREEN_HEIGHT;

/**
 * handles the scoring system of the game and writes high scores into a txt file
 */

public class Score {
    //increment of score
    private static final double SCORE_INC = 0.1;
    private static final int SCORE_LENGTH = 5;
    private static final int NUMBER_WIDTH = 20;
    private static final int NUMBER_HEIGHT = 21;
    private static final int CURRENT_SCORE_X = SCREEN_WIDTH - (SCORE_LENGTH * NUMBER_WIDTH + SCREEN_WIDTH / 100);
    private static final int HI_SCORE_X = SCREEN_WIDTH - (SCORE_LENGTH * NUMBER_WIDTH + SCREEN_WIDTH / 100) * 2;
    private static final int HI_X = SCREEN_WIDTH - ((SCORE_LENGTH * NUMBER_WIDTH + SCREEN_WIDTH / 100) * 2 + NUMBER_WIDTH * 2 + SCREEN_WIDTH / 100);
    private static final int SCORE_Y = SCREEN_HEIGHT / 25;

    private GameScene GameScene;
    private String scoreFileName;
    private File scoreFile;
    private Image hi;
    private Image numbers;
    private SoundManager scoreUpSound;

    private double score;
    private int hiScore;

    public Score() {
        score = 0;
        scoreFileName = "pr.txt";
        scoreFile = new File(scoreFileName);
        readScore();
        hi = new Image("hi.png");
        numbers = new Image("numbers.png");
        scoreUpSound = new SoundManager("src/main/resources/scoreup.wav");
    }

    //increment score
    public void scoreUp() {
        if ((int) score != 0 && score % 100 <= 0.1) {
            scoreUpSound.triggerPlay();
        }
        score += SCORE_INC;
    }

    private Image cropImage(Image image, int number) {
        return new WritableImage(image.getPixelReader(), number * NUMBER_WIDTH, 0, NUMBER_WIDTH, NUMBER_HEIGHT);
    }

    private int[] scoreToArray(double ScoreType) {
        int[] scoreArray = new int[SCORE_LENGTH];
        int tempScore = (int) ScoreType;
        for (int i = 0; i < SCORE_LENGTH; i++) {
            int number = tempScore % 10;
            tempScore = (tempScore - number) / 10;
            scoreArray[i] = number;
        }
        return scoreArray;
    }

    //writing score method
    public void writeScore() {
        if (score > hiScore) {
            File file = scoreFile;
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(file, true))) {
                bw.write(String.format("result=%s,date=%s,player=%s\n", Integer.toString((int) score), new SimpleDateFormat("yyyyMMdd_HHmmss")
                        .format(Calendar.getInstance().getTime()), "Dino"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    //reading score method
    private void readScore() {
        if (scoreFile.exists()) {
            String line;
            try (BufferedReader br = new BufferedReader(new FileReader(scoreFile))) {
                while ((line = br.readLine()) != null) {
                    Matcher m = Pattern.compile("result=(\\d+),date=([\\d_]+),player=(\\w+)").matcher(line);
                    if (m.find()) {
                        if (Integer.parseInt(m.group(1)) > hiScore) {
                            hiScore = Integer.parseInt(m.group(1));
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            hiScore = (int) score;
        }
    }

    //reset high score
    public void scoreReset(){
        if (score > hiScore){
            hiScore = (int) score;
        }
        score = 0;
    }

    //renders the score by cropping the score image with equal lengths
    public void draw(GraphicsContext gc){
        int[] scoreArray = scoreToArray(score);
        for (int i = 0; i < SCORE_LENGTH; i++){
            gc.drawImage(cropImage(numbers, scoreArray[SCORE_LENGTH - i - 1]), CURRENT_SCORE_X + i * NUMBER_WIDTH, SCORE_Y);
        }
        if (hiScore > 0){
            int[] hiScoreArray = scoreToArray(hiScore);
            gc.setGlobalAlpha(0.5);
            for (int i = 0; i < SCORE_LENGTH; i++){
                gc.drawImage(cropImage(numbers, hiScoreArray[SCORE_LENGTH - i - 1]), HI_SCORE_X + i * NUMBER_WIDTH, SCORE_Y);
            }
            gc.drawImage(hi, HI_X, SCORE_Y);
            gc.setGlobalAlpha(1.0);
        }
    }
}
