package uk.co.rangersoftware.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class DurationTimer {
    private Date start;
    private Date stop;

    public void start(){
        start = new Date();
    }

    public String stop(){
        stop = new Date();
        return formattedDuration();
    }

    private  String formattedDuration() {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss.SSS", Locale.getDefault());
        sdf.setTimeZone(TimeZone.getTimeZone("GMT+0"));
        long diffInMillies = stop.getTime() - start.getTime();
        return sdf.format(new Date(diffInMillies - TimeZone.getDefault().getRawOffset()));
    }
}
