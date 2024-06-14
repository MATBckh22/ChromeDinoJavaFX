package com.example.chromedino;

import UserInterface.GameScreen;
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

import static com.example.chromedino.HelloApplication.SCREEN_WIDTH;
import static com.example.chromedino.HelloApplication.SCREEN_HEIGHT;

public class Score {
    // value by which score is increasing
    private static final double SCORE_INC = 0.1;
    // length of score on screen, max 99999 but i dont think that anyone will play that long so.....
    private static final int SCORE_LENGTH = 5;
    // width and height of single number on sprite
    private static final int NUMBER_WIDTH = 20;
    private static final int NUMBER_HEIGHT = 21;
    // here i calculate position of score on screen
    private static final int CURRENT_SCORE_X = SCREEN_WIDTH - (SCORE_LENGTH * NUMBER_WIDTH + SCREEN_WIDTH / 100);
    private static final int HI_SCORE_X = SCREEN_WIDTH - (SCORE_LENGTH * NUMBER_WIDTH + SCREEN_WIDTH / 100) * 2;
    private static final int HI_X = SCREEN_WIDTH - ((SCORE_LENGTH * NUMBER_WIDTH + SCREEN_WIDTH / 100) * 2 + NUMBER_WIDTH * 2 + SCREEN_WIDTH / 100);
    private static final int SCORE_Y = SCREEN_HEIGHT / 25;

    private GameScreen gameScreen;
    private String scoreFileName;
    private File scoreFile;
    private Image hi;
    private Image numbers;

    private double score;
    private int hiScore;

    public Score() {
        score = 0;
        scoreFileName = "pr.txt";
        scoreFile = new File("resources/" + scoreFileName);
        readScore();
        hi = new Image("hi.png");
        numbers = new Image("numbers.png");
    }

    //increment score
    public void scoreUp() {
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

    public void scoreReset(){
        if (score > hiScore){
            hiScore = (int) score;
        }
        score = 0;
    }

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
