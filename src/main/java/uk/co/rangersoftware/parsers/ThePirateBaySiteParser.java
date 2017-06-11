package uk.co.rangersoftware.parsers;

import org.apache.commons.lang.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import uk.co.rangersoftware.downloader.DownloadProgress;
import uk.co.rangersoftware.log.Log;
import uk.co.rangersoftware.matchers.MagnetMatch;
import uk.co.rangersoftware.matchers.TitleMatch;
import uk.co.rangersoftware.matchers.VersionMatch;
import uk.co.rangersoftware.media.SoundManager;
import uk.co.rangersoftware.model.DownloadSize;
import uk.co.rangersoftware.model.MagnetLink;
import uk.co.rangersoftware.model.Series;
import uk.co.rangersoftware.model.Show;
import uk.co.rangersoftware.util.Constants;
import uk.co.rangersoftware.util.Sizing;

import java.util.ArrayList;
import java.util.List;

public class ThePirateBaySiteParser implements Parser {
    private VersionMatch versionMatcher;
    private TitleMatch titleMatcher;
    private MagnetMatch magnetLinkMatcher;
    private Sizing sizer;
    private Log logger;
    private DownloadProgress downloadProgress;
    private SoundManager soundManager;

    public ThePirateBaySiteParser(VersionMatch versionMatcher, TitleMatch titleMatcher, MagnetMatch magnetLinkMatcher, Sizing sizer, Log logger, DownloadProgress downloadProgress, SoundManager soundManager) {
        this.versionMatcher = versionMatcher;
        this.titleMatcher = titleMatcher;
        this.magnetLinkMatcher = magnetLinkMatcher;
        this.sizer = sizer;
        this.logger = logger;
        this.downloadProgress = downloadProgress;
        this.soundManager = soundManager;
    }

    public List<MagnetLink> candidates(String rawHtmlData, Series series, List<MagnetLink> candidates) {
        Document doc = Jsoup.parse(rawHtmlData);
        Elements rowsWithoutAttributes = doc.select("td:not([^])");
        for(Element tableRow : rowsWithoutAttributes){
            try {
                String candidateTitle = tableRow.select("div > a").first().text();
                if(!titleMatcher.seriesTitleMatchesCandidate(series.getTitle(), candidateTitle)) continue;
                String magnetLink = tableRow.parent().select("td > a").first().attr("href");
                if(magnetLinkMatcher.hasBeenDownloaded(magnetLink)) continue;
                Show show = versionMatcher.mapToShow(candidateTitle);
                logger.debug("Looking at " + show.toString());
                if(!show.isValid()) continue;
                if(series.showHasBeenDownloaded(show)) continue;
                String sizeData = tableRow.parent().select("td > font").first().text();
                DownloadSize downloadSize = sizer.downloadSize(sizeData, Constants.largestSize);
                if(!downloadSize.isValid()){
                    String ignoreMessage = invalidSizeMessage(series.getTitle(), show, downloadSize);
                    logger.debug(ignoreMessage);
                    downloadProgress.addInformationMessage(ignoreMessage);
                    continue;
                }
                MagnetLink magnet = new MagnetLink(magnetLink, candidateTitle, downloadSize.getSizeInMB());
                magnet.setShow(show);
                addOrReplaceCandidate(candidates, magnet);
            }catch (Exception ex){
                continue;
            }
        }
        addToHistory(series, candidates);

        return candidates;
    }

    private void addToHistory(Series sereis, List<MagnetLink> candidates){
        if(Constants.isDryRun) return;
        for(MagnetLink m: candidates){
            sereis.addToHistory(m.getShow());
        }
    }

    private void addOrReplaceCandidate(List<MagnetLink> candidates, MagnetLink candidateMagnetLink){
        MagnetLink existingMagnetLink = null;
        for(MagnetLink m : candidates){
            if(magnetLinksReferToSameEpisode(m, candidateMagnetLink)){
                existingMagnetLink = m;
                break;
            }
        }

        if(existingMagnetLink == null){
            candidates.add(candidateMagnetLink);
            return;
        }

        if(existingMagnetLink.getSizeInMB() >= Constants.idealSize){
            return;
        }

        if(candidateMagnetLink.getSizeInMB() > existingMagnetLink.getSizeInMB()){
            candidates.remove(existingMagnetLink);
            candidates.add(candidateMagnetLink);
        }
    }

    private boolean magnetLinksReferToSameEpisode(MagnetLink m1, MagnetLink m2){
        return m1.getShow().getSeason() == m2.getShow().getSeason() &&
                m1.getShow().getEpisode() == m2.getShow().getEpisode();
    }

    private String invalidSizeMessage(String title, Show show, DownloadSize downloadSize){
        return String.format("Skipped %s S%sE%s, %s",
                title,
                StringUtils.leftPad("" + show.getSeason(), 2, "0"),
                StringUtils.leftPad("" + show.getEpisode(), 2, "0"),
                downloadSize.getFailureMessage());
    }
}
