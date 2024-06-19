package misc;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.application.Platform;
import java.io.File;

public class SoundManager {

    public static int WAITING_TIME = 0;

    private boolean playSound = false;
    private String path;
    private MediaPlayer mediaPlayer;

    public SoundManager(String path) {
        this.path = path;
        Media sound = new Media(new File(path).toURI().toString());
        this.mediaPlayer = new MediaPlayer(sound);
        mediaPlayer.play();

    }

    public void playsound() {
        // Run the sound playing task on the JavaFX Application Thread
        Platform.runLater(() -> {
            if (playSound) {
                mediaPlayer.stop();  // Stop any currently playing media
                mediaPlayer.play();  // Play the new media
                playSound = false;
            }
        });
    }

    // Sets playSound to true and calls play method
    public void triggerPlay() {
        playSound = true;
        playsound();
    }

    // Stops the currently playing sound
    public void stop() {
        Platform.runLater(() -> {
            if (mediaPlayer != null) {
                mediaPlayer.stop();
            }
        });
    }

    public void pause() {
        Platform.runLater(() -> {
            if (mediaPlayer != null) {
                mediaPlayer.pause();
            }
        });
    }

    public void resume() {
        Platform.runLater(() -> {
            if (mediaPlayer != null) {
                mediaPlayer.play();
            }
        });
    }
}