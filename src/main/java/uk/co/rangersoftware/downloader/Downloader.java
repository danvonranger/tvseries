package uk.co.rangersoftware.downloader;

import java.io.FileNotFoundException;

public interface Downloader {
    DownloadResult download(String url, boolean useNewMethod) throws FileNotFoundException;
}
