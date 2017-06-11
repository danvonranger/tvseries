package uk.co.rangersoftware.config;

import org.apache.commons.lang.StringUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import uk.co.rangersoftware.model.Series;
import uk.co.rangersoftware.parsers.SiteParserFactory;
import uk.co.rangersoftware.util.Constants;
import uk.co.rangersoftware.util.DateHelper;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.FileNotFoundException;
import java.security.InvalidParameterException;
import java.util.*;

import static uk.co.rangersoftware.util.DateHelper.differenceInDays;

public class XmlParser {
    private static Document configDoc = null;
    private static String magnetApp = null;
    private static Comparator<String> cmp = null;
    private static List<String> showRegExList = null;
    private static List<String> userAgentList = null;

    private static void loadConfigDoc() throws FileNotFoundException {
        if (configDoc != null) return;

        File fXmlFile = new File(Constants.TV_SHOWS_FILE_NAME);
        if (!fXmlFile.exists()) {
            throw new FileNotFoundException("Failed to find configuration file: " + Constants.TV_SHOWS_FILE_NAME);
        }
        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            configDoc = dBuilder.parse(fXmlFile);
            configDoc.getDocumentElement().normalize();
        } catch (Exception ex) {
            throw new InvalidParameterException("Failed whilst loading configuration data. " + ex.getMessage());
        }
    }

    public static void updateSeries(Series series, boolean haveDownloads) {
        try {
            loadConfigDoc();
            NodeList list = configDoc.getElementsByTagName(Constants.SERIES);
            boolean foundIt = false;
            if (list.getLength() == 0)
                throw new Exception("ABORTING. The in memory tv show file is blank! Cancelled save.");
            for (int index = 0; index < list.getLength(); index++) {
                Node node = list.item(index);
                Element el = (Element) node;
                String seriesTitle = el.getAttribute(Constants.NAME);
                Collections.sort(series.getHistory(), getComparator());
                if (seriesTitle.equalsIgnoreCase(series.getTitle())) {
                    setDates(el, haveDownloads);
                    el.getElementsByTagName(Constants.EPISODES).item(0).setTextContent(StringUtils.join(series.getHistory(), ","));
                    foundIt = true;
                    break;
                }
            }
            if (!foundIt) return;

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(configDoc);
            StreamResult result = new StreamResult(new File(Constants.TV_SHOWS_FILE_NAME));
            transformer.transform(source, result);
        } catch (Exception ex) {

        }
    }

    private static void setDates(Element el, boolean hasDownload) {
        el.getElementsByTagName(Constants.LAST_UPDATE_CHECK).item(0).setTextContent(DateHelper.dateToString(new Date()));
        if (!hasDownload) return;
        el.getElementsByTagName(Constants.LAST_SERIES_DOWNLOADED).item(0).setTextContent(DateHelper.dateToString(new Date()));
    }

    private static Comparator<String> getComparator() {
        if (cmp == null) {
            cmp = new Comparator<String>() {
                public int compare(String o1, String o2) {
                    String o1S = o1.split("-")[0];
                    String o1E = o1.split("-")[1];
                    String o2S = o2.split("-")[0];
                    String o2E = o2.split("-")[1];
                    int s1 = Integer.parseInt(o1S);
                    int s2 = Integer.parseInt(o2S);
                    int e1 = 0;
                    int e2 = 0;
                    if (s1 == s2) {
                        e1 = Integer.parseInt(o1E);
                        e2 = Integer.parseInt(o2E);
                        return e1 - e2;
                    }

                    return s1 - s2;
                }
            };
        }
        return cmp;
    }

    public static String getMagnetApp() throws FileNotFoundException {
        if (magnetApp != null) return magnetApp;
        loadConfigDoc();
        magnetApp = configDoc.getElementsByTagName(Constants.MAGNETLINK_APP).item(0).getTextContent();
        return magnetApp;
    }

    public static List<String> regularExpressionsForShowIdentification() throws FileNotFoundException {
        loadConfigDoc();
        if (showRegExList != null) return showRegExList;

        showRegExList = new ArrayList<String>();
        String cstOfExpressions = configDoc.getElementsByTagName(Constants.SEASON_REG_EXPRESSIONS).item(0).getFirstChild().getNodeValue();
        for (String s : cstOfExpressions.split(";")) {
            showRegExList.add(s);
        }
        return showRegExList;
    }

    private static List<String> getUploaders() {
        List<String> data = new ArrayList<String>();
        String csv = configDoc.getElementsByTagName(Constants.FILTER_TERMS).item(0).getTextContent();
        for (String s : csv.split(",")) {
            if (data.contains(s)) continue;
            data.add(s);
        }
        return data;
    }

    public static String getBaseUrl() throws FileNotFoundException {
        loadConfigDoc();
        return configDoc.getElementsByTagName(Constants.BASE_URL).item(0).getTextContent();
    }

    private static void populateDownloadUrls(List<Series> series) throws FileNotFoundException {
        NodeList list = configDoc.getElementsByTagName(Constants.SEARCH_URL_MASK);
        String baseUrl = getBaseUrl();
        List<String> uploaders = getUploaders();

        for (Series ser : series) {
            for (int index = 0; index < list.getLength(); index++) {
                Node node = list.item(index);
                Element el = (Element) node;
                String urlMask = el.getTextContent();
                for (String uploader : uploaders) {
                    String downloadUrl = urlMask.replace("{baseUrl}", baseUrl);
                    String title = ser.getTitle().replaceAll(" ", "%20");
                    downloadUrl = downloadUrl.replace("{encodedSeriesName}", title);
                    downloadUrl = downloadUrl.replace("{uploader}", uploader);
                    downloadUrl = downloadUrl.replaceAll(" ", "%20");
                    ser.addDownloadUrl(downloadUrl);
                }
            }
        }
    }

    public static List<Series> loadSeries() throws FileNotFoundException {
        loadConfigDoc();
        List<Series> seriesList = new ArrayList<Series>();
        List<Series> soloSeriesList = new ArrayList<Series>();

        NodeList list = configDoc.getElementsByTagName(Constants.SERIES);
        for (int index = 0; index < list.getLength(); index++) {
            Node node = list.item(index);
            Element el = (Element) node;
            String seriesTitle = el.getAttribute(Constants.NAME);
            if (!consideredActive(el)) continue;
            boolean isSolo = el.hasAttribute(Constants.SOLO);
            String seriesEpisodes = el.getElementsByTagName(Constants.EPISODES).item(0).getTextContent();
            Series series = new Series();
            series.setTitle(seriesTitle);
            series.initializeHistory(seriesEpisodes);
            if (isSolo) soloSeriesList.add(series);
            seriesList.add(series);
        }
        if (!soloSeriesList.isEmpty()) {
            seriesList = soloSeriesList;
        }
        populateDownloadUrls(seriesList);
        return seriesList;
    }

    private static boolean consideredActive(Element el) {
        // Is this a forced update of all series?
        if(Constants.INCLUDE_ALL_SERIES) return true;
        String lastUpdateCheckAsString = el.getElementsByTagName(Constants.LAST_UPDATE_CHECK).item(0).getTextContent();
        if (lastUpdateCheckAsString.length() == 0) return true;
        String lastSeriesDownloadedAsString = el.getElementsByTagName(Constants.LAST_SERIES_DOWNLOADED).item(0).getTextContent();
        if (lastSeriesDownloadedAsString.length() == 0) return true;
        Date lastUpdateCheck = DateHelper.stringToDate(lastUpdateCheckAsString);
        Date lastSeriesDownloaded = DateHelper.stringToDate(lastSeriesDownloadedAsString);
        // has it been less that x hours since the last update? If so, ignore
        long hoursSinceLastUpdateCheck = DateHelper.differenceInHours(new Date(), lastUpdateCheck);
        if (hoursSinceLastUpdateCheck < Constants.minimumInteralBetweenChecksInHours) return false;
        // was the last series download less than 8 days, if more than move on to next check
        long daysSinceLasSeriesDownloaded = differenceInDays(new Date(), lastSeriesDownloaded);
        if (daysSinceLasSeriesDownloaded <= Constants.staleCandidatedTriggerInDays) return true;
        // lastly, if the number of days since a download is mod 14 then we try agains, else we ignore it
        return daysSinceLasSeriesDownloaded % Constants.staleRecycleIntervalInDays == 0;
    }

    public static String nodeValue(String nodeName) throws FileNotFoundException {
        loadConfigDoc();
        return configDoc.getElementsByTagName(nodeName).item(0).getTextContent();
    }

    public static List<SiteParserFactory.SiteType> getSiteParsers() {
        List<SiteParserFactory.SiteType> siteTypes = new ArrayList<SiteParserFactory.SiteType>();

        NodeList list = configDoc.getElementsByTagName(Constants.SITE);
        for (int index = 0; index < list.getLength(); index++) {
            Node node = list.item(index);
            Element el = (Element) node;
            String siteId = el.getAttribute(Constants.ID);
            SiteParserFactory.SiteType siteType = fromString(siteId);
            if (siteId == null) continue;
            if (siteTypes.contains(siteType)) continue;
            siteTypes.add(siteType);
        }

        return siteTypes;
    }

    private static SiteParserFactory.SiteType fromString(String id) {
        SiteParserFactory.SiteType type = null;
        if (id.equalsIgnoreCase(Constants.TPB)) {
            type = SiteParserFactory.SiteType.TPB;
        }

        return type;
    }

    public static String attributeValue(String nodeName, String attributeName) throws FileNotFoundException {
        loadConfigDoc();
        Node node = configDoc.getElementsByTagName(nodeName).item(0);
        Element el = (Element) node;
        return el.hasAttribute(attributeName) ? el.getAttribute(attributeName) : "";
    }

    public static List<String> userAgents() throws FileNotFoundException {
        loadConfigDoc();
        if (userAgentList != null) return userAgentList;

        userAgentList = new ArrayList<String>();
        NodeList list = configDoc.getElementsByTagName(Constants.USER_AGENT);
        for (int index = 0; index < list.getLength(); index++) {
            Node node = list.item(index);
            Element el = (Element) node;
            boolean isEnabled = Boolean.parseBoolean(el.getAttribute("enabled"));
            if(!isEnabled) continue;
            userAgentList.add(el.getTextContent());
        }

        return userAgentList;
    }
}
