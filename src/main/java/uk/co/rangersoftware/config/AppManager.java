package uk.co.rangersoftware.config;

import uk.co.rangersoftware.io.FileUtil;
import uk.co.rangersoftware.util.Constants;

import java.io.File;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import uk.co.rangersoftware.log.Log;

import static uk.co.rangersoftware.util.Constants.LAST_DOWNLOAD_RUN_FILE_NAME;
import static uk.co.rangersoftware.util.Constants.LOCK_FILE_NAME;

public class AppManager {
    private final static String DATE_FORMAT = "d-MMM-yyyy,HH:mm:ss aaa";

    public static boolean canRunApp(Log logger) {
        if(passedCutOffTime()) {
            logger.log("It's past the cutoff time, denying execution");
            return false;
        }

        if (lockFileExists()) {
            logger.log("App is running, denying execution");
            return false;
        }

        if(!sufficientTimeHasPassed()){
            logger.log("Not enough minutes since last run, denying execution");
            return false;
        }

        logger.log("All good, authorising execution");
        return true;
    }

    public static void deleteLockFile() {
        File file = new File(LOCK_FILE_NAME);
        file.delete();
    }

    public static void createLockFile() {
        deleteLockFile();
        File file = new File(LOCK_FILE_NAME);
        FileUtil.appendToFile(file, "locked");
    }

    public static void persistCurrentTime() {
        File file = new File(LAST_DOWNLOAD_RUN_FILE_NAME);
        file.delete();
        DateFormat formatter = new SimpleDateFormat(DATE_FORMAT);
        String data = formatter.format(new Date());
        FileUtil.appendToFile(file, data);
    }

    private static boolean passedCutOffTime() {
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.HOUR_OF_DAY) > 20;
    }

    private static boolean lockFileExists() {
        File file = new File(LOCK_FILE_NAME);
        return file.exists();
    }

    private static boolean sufficientTimeHasPassed() {
        File file = new File(LAST_DOWNLOAD_RUN_FILE_NAME);
        if (!file.exists()) {
            return true;
        }

        String lastRun = FileUtil.readContent(file);
        DateFormat formatter = new SimpleDateFormat(DATE_FORMAT);
        try {
            Date startDate = formatter.parse(lastRun);
            Date endDate = new Date();
            long duration = endDate.getTime() - startDate.getTime();
            long diffInMinutes = TimeUnit.MINUTES.convert(duration, TimeUnit.MILLISECONDS);
            return diffInMinutes >= Constants.taskInterval;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return  true;
    }
}
