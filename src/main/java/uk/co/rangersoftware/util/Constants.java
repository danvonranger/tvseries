package uk.co.rangersoftware.util;

public class Constants {
    public static final String downloadHistoryFileName = "F:\\java\\projects\\tvseries\\history.txt";
    public static final String tvShowsFileName = "F:\\java\\projects\\tvseries\\tv_shows.xml";
    public static final String downloadDataFileName = "F:\\java\\projects\\tvseries\\download.data";
    public static final String LAST_UPDATE_CHECK = "lastUpdateCheck";
    public static final String LAST_SERIES_DOWNLOADED = "lastSeriesDownloaded";
    public static final String SERIES = "series";
    public static final String SITE = "site";
    public static final String SEARCH_URL_MASK = "searchUrlMask";
    public static final String USER_AGENT = "userAgent";
    public static final String BASE_URL = "baseUrl";
    public static final String SEASON_REG_EXPRESSIONS = "seasonRegExpressions";
    public static final String MAGNETLINK_APP = "magnetlinkApp";
    public static final String EPISODES = "episodes";
    public static final String NAME = "name";
    public static final String FILTER_TERMS = "filterTerms";
    public static final String SOLO = "solo";
    public static final String MAX_SIZE = "maxSize";
    public static final String ID = "id";
    public static final String TPB = "TPB";
    public static final String IDEAL_SIZE = "idealSize";
    public static final String DRY_RUN = "dryRun";
    public static final String DEBUG_MODE = "debugMode";
    public static final String STALE_RECYCLE_INTERVAL_IN_DAYS = "staleRecycleIntervalInDays";
    public static final String MINIMUM_INTERAL_BETWEEN_CHECKS_IN_HOURS = "minimumInteralBetweenChecksInHours";
    public static final String STALE_CANDIDATED_TRIGGER_IN_DAYS = "staleCandidatedTriggerInDays";
    public static final String CONNECTION_TIMEOUT_IN_SECONDS = "connectionTimeoutInSeconds";
    public static final String MAX_USER_AGENT_RETRIES = "maxRetrieveAttemptsPerUserAgent";
    public static final String UPDATE_ALL_SERIES = "updateAllSeries";
    public static boolean INCLUDE_ALL_SERIES = false;
    public static boolean isDryRun = false;
    public static int largestSize = 0;
    public static int idealSize = 0;
    public static boolean verboseMode = false;
    public static long staleRecycleIntervalInDays = 1;
    public static int minimumInteralBetweenChecksInHours = 1;
    public static int staleCandidatedTriggerInDays = 1;
    public static int connectionTimeoutInSeconds = 1;
    public static int maxRetrieveAttemptsPerUserAgent = 0;
}
