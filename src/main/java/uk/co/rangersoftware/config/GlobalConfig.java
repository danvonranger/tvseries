package uk.co.rangersoftware.config;

import uk.co.rangersoftware.model.Series;
import uk.co.rangersoftware.parsers.SiteParserFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;

public interface GlobalConfig {
    List<String> regularExpressionsForShows() throws FileNotFoundException;

    List<Series> loadSeries() throws FileNotFoundException;
    List<String> userAgents() throws FileNotFoundException;

    String getMagnetApp() throws FileNotFoundException;

    void updateSeriesData(Series series, boolean haveDownloads);

    List<String> downloadHistory();

    void appendToFile(File file, String data);

    void addToDownloadHistory(String data);

    String getBaseUrl();

    List<SiteParserFactory.SiteType> siteTypes();

    String getSoundFile(String nodeName) throws FileNotFoundException;

    String configValue(String nodeName) throws FileNotFoundException;

    String attributeValue(String nodeName, String attributeName) throws FileNotFoundException;
}
