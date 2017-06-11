package uk.co.rangersoftware.media;

public interface SoundManager
{
    void play(SoundManagerImpl.SoundType type);
    void stop();
    void sayIt(String message);
}