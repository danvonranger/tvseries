package uk.co.rangersoftware.matchers;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import uk.co.rangersoftware.config.GlobalConfig;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class MagnetMatcherTest {
    private MagnetMatch classUnderTest;
    private List<String> history;

    @Mock
    private GlobalConfig config;

    @Before
    public void setUp() throws Exception {
        classUnderTest = new MagnetMatcher(config);
        history = new ArrayList<String>();
        when(config.downloadHistory()).thenReturn(history);
    }

    @Test
    public void returnsFalseWhenHistoryIsEmpty(){
        boolean actual = classUnderTest.hasBeenDownloaded("link");
        assertThat(actual).isEqualTo(false);
    }

    @Test
    public void returnsFalseWhenHistoryDoesNotContainLink(){
        history.add("fred");
        boolean actual = classUnderTest.hasBeenDownloaded("link");
        assertThat(actual).isEqualTo(false);
    }

    @Test
    public void returnsTrueWhenHistoryDoesContainLink(){
        history.add("link");
        boolean actual = classUnderTest.hasBeenDownloaded("link");
        assertThat(actual).isEqualTo(true);
    }
}