package manager;

import javafx.application.Platform;
import java.io.File;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

/**
 * SoundManager class handles the basic media player functions, including play, stop, pause, and resume
 */

public class SoundManager {

    public static int WAITING_TIME = 0;

    private boolean playSound = false; // Flag to determine if the sound should be played
    private MediaPlayer mediaPlayer;

    // Constructor
    public SoundManager(String path) {
    	// Create a Media object from the file path which set to the sound file
    	Media sound = new Media(new File(path).toURI().toString());
    	this.mediaPlayer = new MediaPlayer(sound); // Initialize the MediaPlayer with the Media object
    	mediaPlayer.play(); // Play the sound
    	
    }

    // Play the sound on the JavaFX Application Thread
    public void playsound() {
        // Run the sound playing task on the JavaFX Application Thread
        Platform.runLater(() -> {
            if (playSound) {
                mediaPlayer.stop(); // Stop any currently playing media
                mediaPlayer.play(); // Play the new media
                playSound = false; // Reset the flag
            }
        });
    }

    // Sets playSound to true and calls play method
    public void triggerPlay() {
        playSound = true; // Set the flag to true indicating sound should be played
        playsound();
    }
    
    // Stops the currently playing sound
    public void stop() {
        Platform.runLater(() -> {
            if (mediaPlayer != null) {
                mediaPlayer.stop(); // Stop the media player
            }
        });
    }
    
    // Pauses the currently playing sound
    public void pause() {
    	Platform.runLater(() -> {
            if (mediaPlayer != null) {
                mediaPlayer.pause(); // Pause the media player
                playSound = true; // Ensure the sound can be resumed by setting the flag to true
            }
        });
    }
    
    // Resumes the paused sound
    public void resume() {
        Platform.runLater(() -> {
            if (mediaPlayer != null) {
                mediaPlayer.play(); // Resume the media player
            }
        });
    }
}
