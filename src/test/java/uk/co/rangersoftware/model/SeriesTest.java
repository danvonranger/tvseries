package uk.co.rangersoftware.model;

import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class SeriesTest {
    private Series classUnderTest;

    @Before
    public void before(){
        classUnderTest = new Series();
    }

    @Test
    public void shouldMatchOnAddingShowWithoutHistory(){
        Show show = new Show();
        show.setSeason(10);
        show.setEpisode(11);
        classUnderTest.addToHistory(show);
        assertThat(classUnderTest.showHasBeenDownloaded(show)).isEqualTo(true);
    }

    @Test
    public void shouldMatchOnAddingShowWithHistory(){
        Show show = new Show();
        show.setSeason(10);
        show.setEpisode(11);
        String history = "10-11";
        classUnderTest.initializeHistory(history);
        assertThat(classUnderTest.showHasBeenDownloaded(show)).isEqualTo(true);
    }

    @Test
    public void shouldMatchOnAddingShowWithHistoryCcntainingWildcard(){
        Show show = new Show();
        show.setSeason(10);
        show.setEpisode(11);
        String history = "10-*";
        classUnderTest.initializeHistory(history);
        assertThat(classUnderTest.showHasBeenDownloaded(show)).isEqualTo(true);
    }

    @Test
    public void shouldNotMatchOnAddingShowWithMissingHistory(){
        Show show = new Show();
        show.setSeason(10);
        show.setEpisode(11);
        String history = "10-10";
        classUnderTest.initializeHistory(history);
        assertThat(classUnderTest.showHasBeenDownloaded(show)).isEqualTo(false);
    }
}