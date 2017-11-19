package uk.co.rangersoftware.downloader;

import org.apache.commons.lang.StringUtils;
import uk.co.rangersoftware.config.GlobalConfig;
import uk.co.rangersoftware.log.Log;
import uk.co.rangersoftware.matchers.*;
import uk.co.rangersoftware.model.MagnetLink;
import uk.co.rangersoftware.model.Series;
import uk.co.rangersoftware.model.Show;
import uk.co.rangersoftware.parsers.Parser;
import uk.co.rangersoftware.parsers.SiteParser;
import uk.co.rangersoftware.parsers.SiteParserFactory;
import uk.co.rangersoftware.print.ColourPrint;
import uk.co.rangersoftware.process.MagnetTrigger;
import uk.co.rangersoftware.util.Constants;
import uk.co.rangersoftware.util.DownloadSizer;
import uk.co.rangersoftware.util.Sizing;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

public class DownloadManager {
    private Log logger;
    private VersionMatch versionMatcher;
    private TitleMatch titleMatcher;
    private MagnetMatch magnetMatcher;
    private Downloader downloader;
    private MagnetTrigger magnetTrigger;
    private Sizing sizer;
    private GlobalConfig globalConfig;

    public DownloadManager(Log logger, GlobalConfig config) {
        this.logger = logger;
        globalConfig = config;
    }

    public DownloadProgress start() throws Exception {
        DownloadProgress downloadProgress = new DownloadProgress();
        AvailabilityCheck check = new AvailabilityChecker(globalConfig);
        boolean siteAvailable = check.siteIsAvailable();
        if (!siteAvailable) {
            throw new Exception("VPN is not up, or site is unavailable. Aborting.");
        }
        initialize(globalConfig);
        List<Series> existingSeries = globalConfig.loadSeries();

        int seriesCount = existingSeries.size();
        int seriesProgressCounter = 0;
        SiteParser siteParser = new SiteParserFactory(versionMatcher, titleMatcher, magnetMatcher, logger, sizer, downloadProgress);
        List<SiteParserFactory.SiteType> siteTypes = globalConfig.siteTypes();
        Parser parser;

        for (SiteParserFactory.SiteType siteType : siteTypes) {
            parser = siteParser.siteParser(siteType);
            for (Series series : existingSeries) {
                boolean foundDownloads = false;
                seriesProgressCounter++;
                logger.log(String.format("%s/%s - %s", paddDownloadProgressCounter(seriesProgressCounter, seriesCount), seriesCount, series.getTitle()));
                int urlCount = 0;
                List<MagnetLink> magnetLinkCandidates = new ArrayList<MagnetLink>();
                for (String downloadUrl : series.getDownloadUrls()) {
                    urlCount++;
                    logger.debug("URl: "+ downloadUrl);
                    logger.debug(String.format("Attempt %s of %s for %s ", urlCount, series.getDownloadUrls().size(), series.getTitle()));
                    DownloadResult result = downloader.download(downloadUrl);
                    parser.candidates(result.rawData(), series, magnetLinkCandidates);
                }
                for (MagnetLink magnetLink : magnetLinkCandidates) {
                    foundDownloads = true;
                    triggerMagnetDowload(downloadProgress, globalConfig, series, magnetLink, magnetLinkCandidates.size());
                }
                globalConfig.updateSeriesData(series, foundDownloads);
            }
        }
        return downloadProgress;
    }

    private String paddDownloadProgressCounter(int seriesProgressCounter, int seriesCount){
        int padLength = ("" + seriesCount).length();
        return StringUtils.leftPad("" + seriesProgressCounter, padLength, " ");
    }

    private void triggerMagnetDowload(DownloadProgress downloadProgress, GlobalConfig configurationData, Series series, MagnetLink magnetLink, int count) {
        String progress = String.format("%s %s (%s MB)", series.getTitle(), magnetLink.getShow().toString(), magnetLink.getSizeInMB());
        logger.log("          Downloading: " + progress, ColourPrint.Foreground.GREEN);
        if(Constants.isDryRun) return;

        configurationData.addToDownloadHistory(magnetLink.getLink());
        downloadProgress.addDownload(progress);
        magnetTrigger.run(magnetLink.getLink());
    }

    private String constructDownloadSpeech(Show show, Series series){
        return String.format("season %s episode %s of %s", show.getSeason(), show.getEpisode(), series.getTitle());
    }

    private void initialize(GlobalConfig configurationData) throws FileNotFoundException {
        versionMatcher = new VersionMatcher(configurationData);
        titleMatcher = new TitleMatcher();
        magnetMatcher = new MagnetMatcher(configurationData);
        downloader = new DownloaderImpl(logger, configurationData);
        magnetTrigger = new MagnetTrigger(configurationData);
        sizer = new DownloadSizer();
    }
}
