package uk.co.rangersoftware.plugins.autoremote;

import uk.co.rangersoftware.downloader.DownloadProgress;
import uk.co.rangersoftware.downloader.DownloadResult;
import uk.co.rangersoftware.downloader.Downloader;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class AutoRemoteManager {
    public static void sendNotification(DownloadProgress progress, Downloader downloader) {
        List<String> episodes = new ArrayList<String>();

        for (String download : progress.getDownloadDetails()) {
            episodes.add(download);
        }

        String url = AutoRemote.notifcationUrl(episodes);
        try {
            DownloadResult result = downloader.download(url, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void sendNotificationTest(List<String> downloads, Downloader downloader) {
        String url = AutoRemote.notifcationUrl(downloads);
        try {
            DownloadResult result = downloader.download(url, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
