package uk.co.rangersoftware.print;

import org.fusesource.jansi.Ansi;
import org.fusesource.jansi.AnsiConsole;

import uk.co.rangersoftware.media.SoundManager;
import uk.co.rangersoftware.media.SoundManagerImpl;

import static org.fusesource.jansi.Ansi.ansi;

public class ColourPrint {
    private final static int PRINT_DELAY = 10;

    public enum Foreground{
        WHITE,
        GREEN,
        YELLOW,
        RED,
        BLACK,
        CYAN,
        BLUE,
        MAGENTA
    }

    public ColourPrint(){
        AnsiConsole.systemInstall();
        System.out.println(ansi().eraseScreen(Ansi.Erase.ALL).cursor(0,0));
    }

    public void debug(String message){
        System.out.println(ansi().fg(enumToColour(Foreground.CYAN)).a(message).reset());
    }

    public void printError(String message, Foreground foreground){
        for(char c : message.toCharArray()){
            System.out.print(ansi().fg(enumToColour(foreground)).a(c).reset());
            System.out.flush();
            try {
                Thread.sleep(PRINT_DELAY);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println();
    }

    public void print(String message, Foreground foreground){
        for(char c : message.toCharArray()){
            System.out.print(ansi().fg(enumToColour(foreground)).a(c).reset());
            System.out.flush();
            try {
                Thread.sleep(PRINT_DELAY);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println();
    }

    public void print(String message){
        for(char c : message.toCharArray()){
            System.out.print(c);
            System.out.flush();
            try {
                Thread.sleep(PRINT_DELAY);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println();
    }

    public void cleanUp(){
        AnsiConsole.systemUninstall();
    }

    private Ansi.Color enumToColour(Foreground fg){
        Ansi.Color colour;
        switch(fg){
            case GREEN:
                colour = Ansi.Color.GREEN;
                break;
            case YELLOW:
                colour = Ansi.Color.YELLOW;
                break;
            case WHITE:
                colour = Ansi.Color.WHITE;
                break;
            case BLACK:
                colour = Ansi.Color.BLACK;
                break;
            case RED:
                colour = Ansi.Color.RED;
                break;
            case CYAN:
                colour = Ansi.Color.CYAN;
                break;
            case BLUE:
                colour = Ansi.Color.BLUE;
                break;
            case MAGENTA:
                colour = Ansi.Color.MAGENTA;
                break;
            default:
                colour = Ansi.Color.WHITE;
                break;
        }

        return colour;
    }
}
