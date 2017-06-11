package uk.co.rangersoftware.parsers;

import uk.co.rangersoftware.downloader.DownloadProgress;
import uk.co.rangersoftware.log.Log;
import uk.co.rangersoftware.matchers.MagnetMatch;
import uk.co.rangersoftware.matchers.TitleMatch;
import uk.co.rangersoftware.matchers.VersionMatch;
import uk.co.rangersoftware.media.SoundManager;
import uk.co.rangersoftware.util.Sizing;

public class SiteParserFactory implements SiteParser {
    private VersionMatch versionMatcher;
    private TitleMatch titleMatcher;
    private MagnetMatch magnetMatcher;
    private Log logger;
    private Sizing sizer;
    private DownloadProgress downloadProgress;
    private SoundManager soundManager;

    public SiteParserFactory(VersionMatch versionMatcher, TitleMatch titleMatcher, MagnetMatch magnetMatcher, Log logger, Sizing sizer, DownloadProgress downloadProgress, SoundManager soundManager) {
        this.versionMatcher = versionMatcher;
        this.titleMatcher = titleMatcher;
        this.magnetMatcher = magnetMatcher;
        this.logger = logger;
        this.sizer = sizer;
        this.downloadProgress = downloadProgress;
        this.soundManager = soundManager;
    }

    public enum SiteType{
        TPB
    }

    public Parser siteParser(SiteType type) throws Exception {
        Parser parser;
        switch(type){
            case TPB:
                parser = new ThePirateBaySiteParser(versionMatcher, titleMatcher, magnetMatcher, sizer, logger, downloadProgress, soundManager);
                break;
            default:
                throw new Exception("Invalid Site Parser: " + type);
        }
        return parser;
    }
}
