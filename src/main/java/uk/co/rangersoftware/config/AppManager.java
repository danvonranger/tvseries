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
        if (appIsRunning()) {
            logger.log("App is running, denying execution");
            return false;
        }
        Calendar calendar = Calendar.getInstance();
        if(calendar.get(Calendar.HOUR_OF_DAY) > 20) {
            logger.log("It's past the cutoff time, denying execution");
            return false;
        }

        File file = new File(LAST_DOWNLOAD_RUN_FILE_NAME);
        if (!file.exists()) {
            logger.log("Last download file not available, authorizing execution");
            createLockFile();
            persistCurrentTime();
            return true;
        }

        String lastRun = FileUtil.readContent(file);
        DateFormat formatter = new SimpleDateFormat(DATE_FORMAT);
        try {
            Date startDate = formatter.parse(lastRun);
            logger.log("Last run: " + startDate);
            Date endDate = new Date();
            long duration = endDate.getTime() - startDate.getTime();
            long diffInMinutes = TimeUnit.MINUTES.convert(duration, TimeUnit.MILLISECONDS);
            if (diffInMinutes < Constants.taskInterval) {
                logger.log("Not enought minutes (" + diffInMinutes +") since las run, denying execution");
                return false;
            }
            logger.log("Good to go, " + diffInMinutes  + " minutes since last run, authorizing execution");
            createLockFile();
            persistCurrentTime();
            return true;
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return false;
    }

    public static void stopRunningState() {
        File file = new File(LOCK_FILE_NAME);
        file.delete();
        persistCurrentTime();
    }

    private static void createLockFile() {
        File file = new File(LOCK_FILE_NAME);
        file.delete();
        FileUtil.appendToFile(file, "locked");
    }

    private static boolean appIsRunning() {
        File file = new File(LOCK_FILE_NAME);
        return file.exists();
    }

    private static void persistCurrentTime() {
        File file = new File(LAST_DOWNLOAD_RUN_FILE_NAME);
        file.delete();
        DateFormat formatter = new SimpleDateFormat(DATE_FORMAT);
        String data = formatter.format(new Date());
        FileUtil.appendToFile(file, data);
    }
}
