package uk.co.rangersoftware.media;

import org.apache.commons.lang.time.StopWatch;
import uk.co.rangersoftware.config.GlobalConfig;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;

public class SoundManagerImpl implements SoundManager {
    private GlobalConfig globalConfig;
    private Sound typeWriterSound;
    private Sound downloadSound;
    private Sound appEndSound;
    private Sound errorSound;
    private Map<SoundType, Sound> soundMap;
    private StopWatch timer = new StopWatch();
    private TextToSpeech textToSpeech;

    public SoundManagerImpl(GlobalConfig globalConfig) {
        this.globalConfig = globalConfig;
        initialize();
        textToSpeech = new TextToSpeechImpl();
    }

    public enum SoundType {
        TYPEWRITER,
        DOWNLOADING,
        APPLICATION_END,
        ERROR
    }

    public void play(SoundType type) {
        timer = new StopWatch();
        timer.start();
        Sound sound = soundPlayer(type);
        sound.start();
    }

    public void stop() {
        waitForMinDuration();
        timer.stop();
        for(Map.Entry<SoundType, Sound> entry : soundMap.entrySet()){
            entry.getValue().stop();
        }
    }

    public void sayIt(String message) {
        textToSpeech.sayIt(message);
    }

    private void waitForMinDuration(){
        while (timer.getTime() < 500){
            try {
                Thread.sleep(25);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private Sound soundPlayer(SoundType type) {
        return soundMap.containsKey(type) ?
                soundMap.get(type) :
                new NullSoundImpl();
    }

    private void initialize() {
        soundMap = new HashMap<SoundType, Sound>();
        try {
            typeWriterSound = new SoundImpl(globalConfig.getSoundFile("typeWriterSoundFile"));
            boolean isEnabled = Boolean.parseBoolean(globalConfig.attributeValue("typeWriterSoundFile", "enabled"));
            soundMap.put(SoundType.TYPEWRITER, isEnabled ? typeWriterSound : new NullSoundImpl());

            downloadSound = new SoundImpl(globalConfig.getSoundFile("downloadSoundFile"));
            isEnabled = Boolean.parseBoolean(globalConfig.attributeValue("downloadSoundFile", "enabled"));
            soundMap.put(SoundType.DOWNLOADING, isEnabled ? downloadSound : new NullSoundImpl());

            appEndSound = new SoundImpl(globalConfig.getSoundFile("applicationEnd"));
            isEnabled = Boolean.parseBoolean(globalConfig.attributeValue("applicationEnd", "enabled"));
            soundMap.put(SoundType.APPLICATION_END, isEnabled ? appEndSound : new NullSoundImpl());

            errorSound = new SoundImpl(globalConfig.getSoundFile("errorSoundFile"));
            isEnabled = Boolean.parseBoolean(globalConfig.attributeValue("errorSoundFile", "enabled"));
            soundMap.put(SoundType.ERROR, isEnabled ? errorSound : new NullSoundImpl());
        } catch (FileNotFoundException ex) {
        }
    }
}
