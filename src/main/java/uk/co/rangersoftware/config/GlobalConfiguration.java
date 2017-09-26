package uk.co.rangersoftware.config;

import uk.co.rangersoftware.io.FileUtil;
import uk.co.rangersoftware.model.Series;
import uk.co.rangersoftware.parsers.SiteParserFactory;
import uk.co.rangersoftware.util.Constants;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class GlobalConfiguration implements GlobalConfig {

    public GlobalConfiguration() {
        try {
            initialze();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public List<String> regularExpressionsForShows() throws FileNotFoundException {
        List<String> expressions = XmlParser.regularExpressionsForShowIdentification();
        return expressions;
    }

    public List<Series> loadSeries() throws FileNotFoundException {
        return XmlParser.loadSeries();
    }

    public List<String> userAgents() throws FileNotFoundException {
        List<String> agents = XmlParser.userAgents();
        return agents;
    }

    public String getMagnetApp() throws FileNotFoundException {
        return XmlParser.getMagnetApp();
    }

    public void updateSeriesData(Series series, boolean haveDownloads) {
        XmlParser.updateSeries(series, haveDownloads);
    }

    public List<String> downloadHistory(){
        File file = new File(Constants.DOWNLOAD_HISTORY_FILE_NAME);
        if(!file.exists()) return new ArrayList<String>();
        return FileUtil.readContentIntoList(file);
    }

    public void appendToFile(File file, String data) {
        FileUtil.appendToFile(file, data);
    }

    public void addToDownloadHistory(String data){
        File file = new File(Constants.DOWNLOAD_HISTORY_FILE_NAME);
        appendToFile(file, data);
    }

    public String getBaseUrl() {
        String url = "";
        try {
            url = XmlParser.getBaseUrl();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return url;
    }

    public List<SiteParserFactory.SiteType> siteTypes() {
        return XmlParser.getSiteParsers();
    }

    public String getSoundFile(String nodeName) throws FileNotFoundException {
        return XmlParser.nodeValue(nodeName);
    }

    public String configValue(String nodeName) throws FileNotFoundException {
        return XmlParser.nodeValue(nodeName);
    }

    public String attributeValue(String nodeName, String attributeName) throws FileNotFoundException {
        return XmlParser.attributeValue(nodeName, attributeName);
    }

    private void initialze() throws FileNotFoundException {
        Constants.idealSize = Integer.parseInt(configValue(Constants.IDEAL_SIZE));
        Constants.largestSize = Integer.parseInt(configValue(Constants.MAX_SIZE));
        Constants.isDryRun = Boolean.parseBoolean(configValue(Constants.DRY_RUN));
        Constants.verboseMode = Boolean.parseBoolean(configValue(Constants.DEBUG_MODE));

        Constants.connectionTimeoutInSeconds = Integer.parseInt(configValue(Constants.CONNECTION_TIMEOUT_IN_SECONDS));
        Constants.taskInterval = Integer.parseInt(configValue(Constants.TASK_INTERVAL_MINUTES));
        Constants.maxRetrieveAttemptsPerUserAgent = Integer.parseInt(configValue(Constants.MAX_USER_AGENT_RETRIES));
        Constants.staleRecycleIntervalInDays = Integer.parseInt(configValue(Constants.STALE_RECYCLE_INTERVAL_IN_DAYS));
        Constants.minimumInteralBetweenChecksInHours = Integer.parseInt(configValue(Constants.MINIMUM_INTERAL_BETWEEN_CHECKS_IN_HOURS));
        Constants.staleCandidatedTriggerInDays = Integer.parseInt(configValue(Constants.STALE_CANDIDATED_TRIGGER_IN_DAYS));
        Constants.INCLUDE_ALL_SERIES = Boolean.parseBoolean(configValue(Constants.UPDATE_ALL_SERIES));
    }
}
