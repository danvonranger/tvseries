package uk.co.rangersoftware.matchers;

import org.apache.commons.lang.StringEscapeUtils;
import uk.co.rangersoftware.util.TitleHelper;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TitleMatcher implements TitleMatch {
    private TitleHelper titleHelper;

    public TitleMatcher(){
        titleHelper = new TitleHelper();
    }

    public boolean seriesTitleMatchesCandidate(String title, String candidate) {
        String cleanCandidateTitle = titleHelper.prepareTitle(candidate);
        String cleanTitle = titleHelper.prepareTitle(title);
        String regExTitle = cleanTitle.replaceAll(" ", StringEscapeUtils.escapeJava("\\s"));
        String regex = String.format("^(%s\\s)(\\({0,1})(\\d{1,4}|S\\d{1,2}).*", regExTitle);

        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(cleanCandidateTitle);
        return matcher.find();
    }
}
