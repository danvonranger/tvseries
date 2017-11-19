package uk.co.rangersoftware;

import uk.co.rangersoftware.config.AppManager;
import uk.co.rangersoftware.config.GlobalConfig;
import uk.co.rangersoftware.config.GlobalConfiguration;
import uk.co.rangersoftware.downloader.DownloadManager;
import uk.co.rangersoftware.downloader.DownloadProgress;
import uk.co.rangersoftware.downloader.DownloaderImpl;
import uk.co.rangersoftware.exception.SiteNotAvailableException;
import uk.co.rangersoftware.log.ConsoleLogger;
import uk.co.rangersoftware.log.Log;
import uk.co.rangersoftware.plugins.autoremote.AutoRemoteManager;
import uk.co.rangersoftware.print.ColourPrint;
import uk.co.rangersoftware.util.DurationTimer;

import javax.mail.internet.AddressException;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

public class App {
    private static Log logger;

    public static void main(String[] args) throws FileNotFoundException {
        GlobalConfig globalConfig = new GlobalConfiguration();
        logger = new ConsoleLogger(new ColourPrint());
        DownloadProgress downloadProgress;
        DurationTimer timer = new DurationTimer();
        timer.start();
        try {
            if(AppManager.canRunApp(logger)) {
                AppManager.createLockFile();
                DownloadManager downloadManager = new DownloadManager(logger, globalConfig);
                downloadProgress = downloadManager.start();
                printDownloadProgress(downloadProgress, logger);
                AppManager.persistCurrentTime();
                AppManager.deleteLockFile();
                if(downloadProgress.getDownloadDetails().size() > 0){
                    AutoRemoteManager.sendNotification(downloadProgress, new DownloaderImpl(logger, globalConfig));
                }
            }
        } catch (FileNotFoundException ex) {
            AppManager.deleteLockFile();
            logger.logError("Aborting. " + ex.getMessage(), ColourPrint.Foreground.RED);
        } catch (SiteNotAvailableException siteEx) {
            AppManager.deleteLockFile();
            logger.logError("Aborting. " + siteEx.getMessage(), ColourPrint.Foreground.RED);
        } catch (Exception exception) {
            AppManager.deleteLockFile();
            logger.logError(exception.getMessage(), ColourPrint.Foreground.RED);
        } finally {
            logger.log(String.format("Search completed in %s", timer.stop()));
            logger.cleanup();
            logger.log("All done.");
            System.exit(0);
        }
    }

    private static void printDownloadProgress(DownloadProgress progress, Log logger) {
        for (String s : progress.getAdditionalDetails()) {
            logger.log(s, ColourPrint.Foreground.CYAN);
        }
        logger.log("");
        int downloadCount = progress.getDownloadDetails().size();
        if (downloadCount == 0) {
            logger.log("No new episodes found.", ColourPrint.Foreground.CYAN);
            return;
        }
        logger.log(String.format("Found %s new episodes:", downloadCount), ColourPrint.Foreground.CYAN);
        for (String s : progress.getDownloadDetails()) {
            logger.log(String.format("    %s", s), ColourPrint.Foreground.CYAN);
        }
    }
}
