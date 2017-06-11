package uk.co.rangersoftware.matchers;

import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class TitleMatcherTest {
    private TitleMatch classUnderTest;

    @Before
    public void before(){
        classUnderTest = new TitleMatcher();
    }

    @Test
    public void matchWithSeasonAndEpisode(){
        String title = "Agent X";
        String candidate = "Agent.X.S12E12.Blah.Blah";
        boolean matches = classUnderTest.seriesTitleMatchesCandidate(title, candidate);
        assertThat(matches).isEqualTo(true);
    }

    @Test
    public void matchWithParenthesis(){
        String title = "Agent X";
        String candidate = "(Agent).[X].(2015).S12E12.Blah.Blah";
        boolean matches = classUnderTest.seriesTitleMatchesCandidate(title, candidate);
        assertThat(matches).isEqualTo(true);
    }

    @Test
    public void matchWithSeasonAndEpisodeWithYearPrefix(){
        String title = "Agent X";
        String candidate = "Agent.X.(2015).S12E12.Blah.Blah";
        boolean matches = classUnderTest.seriesTitleMatchesCandidate(title, candidate);
        assertThat(matches).isEqualTo(true);
    }

    @Test
    public void matchWithOnlyYearPrefix(){
        String title = "Agent X";
        String candidate = "Agent.X.(2015).Blah.Blah";
        boolean matches = classUnderTest.seriesTitleMatchesCandidate(title, candidate);
        assertThat(matches).isEqualTo(true);
    }


    @Test
    public void matchWithApostrophe(){
        String title = "Agent X";
        String candidate = "Agen't.X.(2015).Blah.Blah";
        boolean matches = classUnderTest.seriesTitleMatchesCandidate(title, candidate);
        assertThat(matches).isEqualTo(true);
    }

    @Test
    public void matchWithOnlyYearPrefixNoParenthesis(){
        String title = "Agent X";
        String candidate = "Agent.X.2015.Blah.Blah";
        boolean matches = classUnderTest.seriesTitleMatchesCandidate(title, candidate);
        assertThat(matches).isEqualTo(true);
    }

    @Test
    public void matchWithOnlyYearPrefixAndLowerCase(){
        String title = "agent x";
        String candidate = "Agent.X.(2015).Blah.Blah";
        boolean matches = classUnderTest.seriesTitleMatchesCandidate(title, candidate);
        assertThat(matches).isEqualTo(true);
    }

    @Test
    public void nonMatchExtraWordsWithYearPrefix(){
        String title = "Agent X";
        String candidate = "Agent.X.A.(2015).Blah.Blah";
        boolean matches = classUnderTest.seriesTitleMatchesCandidate(title, candidate);
        assertThat(matches).isEqualTo(false);
    }

    @Test
    public void nonMatchExtraWordsWitSeasonAndEpisodeWithhYearPrefix(){
        String title = "Agent X";
        String candidate = "Agent.X.A.(2015).S12.E22.Blah.Blah";
        boolean matches = classUnderTest.seriesTitleMatchesCandidate(title, candidate);
        assertThat(matches).isEqualTo(false);
    }

    @Test
    public void nonMatchExtraWordsWitSeasonAndEpisode(){
        String title = "Agent X";
        String candidate = "Agent.X.A.S12.E22.Blah.Blah";
        boolean matches = classUnderTest.seriesTitleMatchesCandidate(title, candidate);
        assertThat(matches).isEqualTo(false);
    }

    @Test
    public void nonMatchEmptyString(){
        String title = "Agent X";
        String candidate = "";
        boolean matches = classUnderTest.seriesTitleMatchesCandidate(title, candidate);
        assertThat(matches).isEqualTo(false);
    }

    @Test
    public void nonMatchDifferentTitle(){
        String title = "Agent X";
        String candidate = "Backstrom.S12E45.BLAH";
        boolean matches = classUnderTest.seriesTitleMatchesCandidate(title, candidate);
        assertThat(matches).isEqualTo(false);
    }

    @Test
    public void nonMatchCompleteSeason(){
        String title = "Agent X";
        String candidate = "Agent.X.Season.1.Complete.BLAH";
        boolean matches = classUnderTest.seriesTitleMatchesCandidate(title, candidate);
        assertThat(matches).isEqualTo(false);
    }

    @Test
    public void matchDashDelimeter(){
        String title = "Agent X";
        String candidate = "Agent-X-S12E45.BLAH.BLAH";
        boolean matches = classUnderTest.seriesTitleMatchesCandidate(title, candidate);
        assertThat(matches).isEqualTo(true);
    }

    @Test
    public void matchCommaDelimeter(){
        String title = "Agent X";
        String candidate = "Agent,X,S12E45.BLAH.BLAH";
        boolean matches = classUnderTest.seriesTitleMatchesCandidate(title, candidate);
        assertThat(matches).isEqualTo(true);
    }

    @Test
    public void matchDoubleSpaceDelimeter(){
        String title = "Agent X";
        String candidate = "Agent  X      S12E45.BLAH.BLAH";
        boolean matches = classUnderTest.seriesTitleMatchesCandidate(title, candidate);
        assertThat(matches).isEqualTo(true);
    }

    @Test
    public void matchUnderscoreDelimeter(){
        String title = "Agent X";
        String candidate = "Agent_X____         S12E45.BLAH.BLAH";
        boolean matches = classUnderTest.seriesTitleMatchesCandidate(title, candidate);
        assertThat(matches).isEqualTo(true);
    }

    @Test
    public void matchPlusSignDelimeter(){
        String title = "Agent X";
        String candidate = "Agent+X+S12E45.BLAH.BLAH";
        boolean matches = classUnderTest.seriesTitleMatchesCandidate(title, candidate);
        assertThat(matches).isEqualTo(true);
    }

    @Test
    public void matchMultipleDelimeters(){
        String title = "Agent X";
        String candidate = "Agent+X. _;,S12E45.BLAH.BLAH";
        boolean matches = classUnderTest.seriesTitleMatchesCandidate(title, candidate);
        assertThat(matches).isEqualTo(true);
    }
}