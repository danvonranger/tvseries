package uk.co.rangersoftware.matchers;

import uk.co.rangersoftware.model.Show;

import java.io.FileNotFoundException;

public interface VersionMatch {
    Show mapToShow(String input) throws FileNotFoundException;
}
