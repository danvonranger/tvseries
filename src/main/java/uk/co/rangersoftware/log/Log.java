package uk.co.rangersoftware.log;

import uk.co.rangersoftware.print.ColourPrint;

public interface Log {
    void log(String message);
    void debug(String message);
    void log(String message, ColourPrint.Foreground foreground);
    void logError(String message, ColourPrint.Foreground foreground);
    void cleanup();
}
