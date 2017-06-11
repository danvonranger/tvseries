package uk.co.rangersoftware.model;

import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ShowTest {
    private Show classUnderTest;

    @Before
    public void before() {
        classUnderTest = new Show();
    }

    @Test
    public void isValidReturnsFalseWhenBothZeros() {
        assertThat(classUnderTest.isValid()).isEqualTo(false);
    }

    @Test
    public void isValidReturnsFalseIfSeasonIsZero() {
        classUnderTest.setEpisode(33);
        assertThat(classUnderTest.isValid()).isEqualTo(false);
    }

    @Test
    public void isValidReturnsFalseIfEpisodeIsZero() {
        classUnderTest.setSeason(33);
        assertThat(classUnderTest.isValid()).isEqualTo(false);
    }

    @Test
    public void isValidReturnsTrueIfBothGreaterThanZero() {
        classUnderTest.setSeason(33);
        classUnderTest.setEpisode(43);
        assertThat(classUnderTest.isValid()).isEqualTo(true);
    }

    @Test
    public void propertyWiredCorrectly() {
        classUnderTest.setSeason(33);
        classUnderTest.setEpisode(43);
        assertThat(classUnderTest.getSeason()).isEqualTo(33);
        assertThat(classUnderTest.getEpisode()).isEqualTo(43);
    }
}