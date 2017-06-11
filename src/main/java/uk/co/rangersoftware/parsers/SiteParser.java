package uk.co.rangersoftware.parsers;

public interface SiteParser {
    Parser siteParser(SiteParserFactory.SiteType type) throws Exception;
}
