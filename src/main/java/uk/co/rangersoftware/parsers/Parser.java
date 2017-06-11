package uk.co.rangersoftware.parsers;

import uk.co.rangersoftware.model.MagnetLink;
import uk.co.rangersoftware.model.Series;

import java.util.List;

public interface Parser {
    List<MagnetLink> candidates(String rawHtmlData, Series series, List<MagnetLink> candidates);
}
