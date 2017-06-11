package uk.co.rangersoftware.log;

import java.text.SimpleDateFormat;

public class DateFormatter {
    private static SimpleDateFormat simpleDateFormat = null;
    public static SimpleDateFormat formatter(){
        if(simpleDateFormat == null) {
            simpleDateFormat = new SimpleDateFormat("hh:mm:ss");
        }
        return simpleDateFormat;
    }
}
