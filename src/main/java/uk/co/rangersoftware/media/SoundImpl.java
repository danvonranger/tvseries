package uk.co.rangersoftware.media;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

import java.io.File;

public class SoundImpl implements Sound {
    private MediaPlayer player;

    public SoundImpl(String soundFilename){
        new javafx.embed.swing.JFXPanel();
        String soundTrack = soundFilename;
        String uriString = new File(soundTrack).toURI().toString();
        player = new MediaPlayer(new Media(uriString));
    }

    public void start() {
        player.seek(Duration.ZERO);
        player.play();
    }

    public void stop() {
        player.stop();
    }
}
