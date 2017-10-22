package uk.co.rangersoftware;

import uk.co.rangersoftware.config.AppManager;
import uk.co.rangersoftware.config.GlobalConfig;
import uk.co.rangersoftware.config.GlobalConfiguration;
import uk.co.rangersoftware.downloader.DownloadManager;
import uk.co.rangersoftware.downloader.DownloadProgress;
import uk.co.rangersoftware.exception.SiteNotAvailableException;
import uk.co.rangersoftware.io.FileUtil;
import uk.co.rangersoftware.log.ConsoleLogger;
import uk.co.rangersoftware.log.Log;
import uk.co.rangersoftware.media.SoundManager;
import uk.co.rangersoftware.media.SoundManagerImpl;
import uk.co.rangersoftware.print.ColourPrint;
import uk.co.rangersoftware.util.DurationTimer;

import java.io.File;
import java.io.FileNotFoundException;

import static uk.co.rangersoftware.util.Constants.TV_SHOWS_FILE_NAME;

public class App {
    private static Log logger;

    public static void main(String[] args) throws FileNotFoundException {
        GlobalConfig globalConfig = new GlobalConfiguration();
        SoundManager soundManager = new SoundManagerImpl(globalConfig);
        logger = new ConsoleLogger(new ColourPrint(soundManager));
        DownloadProgress downloadProgress;
        DurationTimer timer = new DurationTimer();
        timer.start();


        try {
            if(AppManager.canRunApp(logger)) {
                AppManager.createLockFile();
                DownloadManager downloadManager = new DownloadManager(logger, globalConfig, soundManager);
                downloadProgress = downloadManager.start();
                printDownloadProgress(downloadProgress, logger, soundManager);
                AppManager.persistCurrentTime();
                AppManager.deleteLockFile();
                if(downloadProgress.getDownloadDetails().size() > 0){
                }
            }
        } catch (FileNotFoundException ex) {
            AppManager.deleteLockFile();
            //soundManager.sayIt("Aborting downlaod. " + ex.getMessage() );
            logger.logError("Aborting. " + ex.getMessage(), ColourPrint.Foreground.RED);
        } catch (SiteNotAvailableException siteEx) {
            AppManager.deleteLockFile();
            //soundManager.sayIt("Aborting download. " + siteEx.getMessage());
            logger.logError("Aborting. " + siteEx.getMessage(), ColourPrint.Foreground.RED);
        } catch (Exception exception) {
            AppManager.deleteLockFile();
            //soundManager.sayIt("Aborting download. " + exception.getMessage());
            logger.logError(exception.getMessage(), ColourPrint.Foreground.RED);
        } finally {
            logger.log(String.format("Search completed in %s", timer.stop()));
            logger.cleanup();
            //soundManager.play(SoundManagerImpl.SoundType.APPLICATION_END);
            logger.log("All done.");
            System.exit(0);
        }
    }

    private static void printDownloadProgress(DownloadProgress progress, Log logger, SoundManager soundManager) {
        for (String s : progress.getAdditionalDetails()) {
            logger.log(s, ColourPrint.Foreground.CYAN);
        }
        logger.log("");
        int downloadCount = progress.getDownloadDetails().size();
        //soundManager.sayIt("Number of new episodes.");
        //soundManager.sayIt("" + downloadCount);
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
