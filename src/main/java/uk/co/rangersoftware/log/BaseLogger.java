package uk.co.rangersoftware.log;

import java.text.SimpleDateFormat;
import java.util.Date;

public class BaseLogger {
    protected String formattedMessage(String message){
        SimpleDateFormat dt = DateFormatter.formatter();
        return String.format("%s - %s", dt.format(new Date()), message);
    }
}
