package uk.co.rangersoftware.version;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import uk.co.rangersoftware.config.GlobalConfiguration;
import uk.co.rangersoftware.matchers.VersionMatcher;
import uk.co.rangersoftware.model.Show;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class VersionMatcherTest {
    private VersionMatcher classUnderTest;

    @Mock
    private GlobalConfiguration globalConfig;

    @Before
    public void before() throws FileNotFoundException{
        List<String> regExList = new ArrayList<String>();
        regExList.add("S(?<season>\\d{1,2})E(?<episode>\\d{1,2})");
        regExList.add("(?<season>\\d{1,2})x(?<episode>\\d{1,2})");
        regExList.add("(?<season>\\d{1,2})\\.(?<episode>\\d{1,2})");
        when(globalConfig.regularExpressionsForShows()).thenReturn(regExList);
        classUnderTest = new VersionMatcher(globalConfig);
    }

    @Test
    public void provideTitleWhereEpisodeIsGreaterThanTwoNumbers() throws Exception {
        String input = "A.Haunting.S02E17720p.Blah.Blah";
        Show show = classUnderTest.mapToShow(input);
        assertThat(show.getSeason()).isEqualTo(2);
        assertThat(show.getEpisode()).isEqualTo(17);
    }

    @Test
    public void zeroSeasonAndEpisodeAsSeasonIsTooLong() throws Exception {
        String input = "A.Haunting.S044E17720p.Blah.Blah";
        Show show = classUnderTest.mapToShow(input);
        assertThat(show.getSeason()).isEqualTo(0);
        assertThat(show.getEpisode()).isEqualTo(0);
    }

    @Test
    public void invalidAsSeasonAndEpisodeIsZero() throws Exception {
        String input = "A.Haunting.S044E17720p.Blah.Blah";
        Show show = classUnderTest.mapToShow(input);
        assertThat(show.getSeason()).isEqualTo(0);
        assertThat(show.getEpisode()).isEqualTo(0);
        assertThat(show.isValid()).isEqualTo(false);
    }

    @Test
    public void zeroSeasonAndEpisodeWhenStringDoesNotContainDetails() throws Exception {
        String input = "A Haunting Complete Series";
        Show show = classUnderTest.mapToShow(input);
        assertThat(show.getSeason()).isEqualTo(0);
        assertThat(show.getEpisode()).isEqualTo(0);
    }

    @Test
    public void zeroSeasonAndEpisodeWhenProvidingAnEmptyString() throws Exception {
        String input = "";
        Show show = classUnderTest.mapToShow(input);
        assertThat(show.getSeason()).isEqualTo(0);
        assertThat(show.getEpisode()).isEqualTo(0);
    }

    @Test
    public void extractShowPaddedNumbersStandardFormat() throws Exception {
        String input = "A.Haunting.S09E07.Mothers.Terror.HDTV.x264-W4F[ettv]";
        Show show = classUnderTest.mapToShow(input);
        assertThat(show.getSeason()).isEqualTo(9);
        assertThat(show.getEpisode()).isEqualTo(7);
    }

    @Test
    public void isValidValidInput() throws Exception {
        String input = "A.Haunting.S09E07.Mothers.Terror.HDTV.x264-W4F[ettv]";
        Show show = classUnderTest.mapToShow(input);
        assertThat(show.getSeason()).isEqualTo(9);
        assertThat(show.getEpisode()).isEqualTo(7);
        assertThat(show.isValid()).isEqualTo(true);
    }

    @Test
    public void extractShowNonPaddedNumbersStandardFormat() throws Exception {
        String input = "A.Haunting.S9E7.Mothers.Terror.HDTV.x264-W4F[ettv]";
        Show show = classUnderTest.mapToShow(input);
        assertThat(show.getSeason()).isEqualTo(9);
        assertThat(show.getEpisode()).isEqualTo(7);
    }

    @Test
    public void extractShowPaddedNumbersDotDelimeterNoPrefixes() throws Exception {
        String input = "A.Haunting.09.07.Mothers.Terror.HDTV.x264-W4F[ettv]";
        Show show = classUnderTest.mapToShow(input);
        assertThat(show.getSeason()).isEqualTo(9);
        assertThat(show.getEpisode()).isEqualTo(7);
    }

    @Test
    public void extractShowNonPaddedNumbersDotDelimeterNoPrefixes() throws Exception {
        String input = "A.Haunting.9.7.Mothers.Terror.HDTV.x264-W4F[ettv]";
        Show show = classUnderTest.mapToShow(input);
        assertThat(show.getSeason()).isEqualTo(9);
        assertThat(show.getEpisode()).isEqualTo(7);
    }

    @Test
    public void extractShowNonPaddedNumbersExDelimeterNoSorEPrefixes() throws Exception {
        String input = "A.Haunting.9x7.Mothers.Terror.HDTV.x264-W4F[ettv]";
        Show show = classUnderTest.mapToShow(input);
        assertThat(show.getSeason()).isEqualTo(9);
        assertThat(show.getEpisode()).isEqualTo(7);
    }

    @Test
    public void extractShowPaddedNumbersExDelimeterNoSorEPrefixes() throws Exception {
        String input = "A.Haunting.09x07.Mothers.Terror.HDTV.x264-W4F[ettv]";
        Show show = classUnderTest.mapToShow(input);
        assertThat(show.getSeason()).isEqualTo(9);
        assertThat(show.getEpisode()).isEqualTo(7);
    }

    @Test
    public void extractZerosWhenOnlyAYearIsSupplied() throws Exception{
        String input = "A.Haunting.2013.Copmlete.Season.1.ettv]";
        Show show = classUnderTest.mapToShow(input);
        assertThat(show.getSeason()).isEqualTo(0);
        assertThat(show.getEpisode()).isEqualTo(0);
    }
}