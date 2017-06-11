package uk.co.rangersoftware.log;

import uk.co.rangersoftware.print.ColourPrint;
import uk.co.rangersoftware.util.Constants;

public class ConsoleLogger extends BaseLogger implements Log {
    private ColourPrint colourPrint;

    public ConsoleLogger(ColourPrint colourPrint) {
        this.colourPrint = colourPrint;
    }

    public void log(String message) {
        colourPrint.print(message);
    }

    public void debug(String message) {
        if(!Constants.verboseMode) return;
        colourPrint.debug("DEBUG: " + message);
    }

    public void log(String message, ColourPrint.Foreground foreground) {
        colourPrint.print(message, foreground);
    }

    public void logError(String message, ColourPrint.Foreground foreground) {
        colourPrint.printError(message, foreground);
    }

    public void cleanup() {
        colourPrint.cleanUp();
    }
}
