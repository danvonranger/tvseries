package uk.co.rangersoftware.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class DateHelper {
    public static Date stringToDate(String dateAsString) {
        Date date;
        try {
            date = dateFormatter().parse(dateAsString);
        } catch (ParseException e) {
            e.printStackTrace();
            date = new Date();
        }
        return date;
    }

    public static String dateToString(Date date) {
        return dateFormatter().format(date);
    }

    public static long differenceInDays(Date sooner, Date later) {
        long diff = sooner.getTime() - later.getTime();
        return TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
    }

    public static long differenceInHours(Date sooner, Date later) {
        long diff = sooner.getTime() - later.getTime();
        return TimeUnit.HOURS.convert(diff, TimeUnit.MILLISECONDS);
    }

    private static DateFormat dateFormatter() {
        return new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    }
}
