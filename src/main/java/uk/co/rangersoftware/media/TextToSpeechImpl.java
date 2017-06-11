package uk.co.rangersoftware.media;

import com.sun.speech.freetts.Voice;
import com.sun.speech.freetts.VoiceManager;

public class TextToSpeechImpl implements TextToSpeech {
    private final static String VOICE = "kevin16";
    private final VoiceManager voiceManager;
    private final Voice voice;

    public TextToSpeechImpl(){
        voiceManager = VoiceManager.getInstance();
        voice = voiceManager.getVoice(VOICE);
        voice.allocate();
    }

    public void sayIt(String text) {voice.speak(text);
    }
}
