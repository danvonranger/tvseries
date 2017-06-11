package uk.co.rangersoftware.matchers;

import uk.co.rangersoftware.config.GlobalConfig;
import uk.co.rangersoftware.model.Show;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class VersionMatcher implements VersionMatch{
    private GlobalConfig globalConfig;
    private List<String> seasonRegExpressions = null;

    public VersionMatcher(GlobalConfig globalConfig) {
        this.globalConfig = globalConfig;
    }

    private void loadRegularExpression() throws FileNotFoundException {
        if(seasonRegExpressions != null) return;
        seasonRegExpressions = globalConfig.regularExpressionsForShows();
    }

    public Show mapToShow(String input) throws FileNotFoundException {
        loadRegularExpression();
        Show show = new Show();

        for (String regex : seasonRegExpressions) {
            Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
            Matcher matcher = pattern.matcher(input);
            if (matcher.find() && matcher.group().length() > 0 && matcher.groupCount() == 2) {
                int seasonNumber = Integer.parseInt(matcher.group(1));
                int episodeNumber = Integer.parseInt(matcher.group(2));
                show.setSeason(seasonNumber);
                show.setEpisode(episodeNumber);
                break;
            }
        }

        return show;
    }
}