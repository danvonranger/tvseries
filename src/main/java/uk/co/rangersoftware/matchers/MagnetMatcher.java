package uk.co.rangersoftware.matchers;

import uk.co.rangersoftware.config.GlobalConfig;

import java.util.List;

public class MagnetMatcher implements MagnetMatch {
    private GlobalConfig config;
    private List<String> history = null;

    public MagnetMatcher(GlobalConfig config) {
        this.config = config;
    }

    public boolean hasBeenDownloaded(String link) {
        loadHistory();
        return history.contains(link);
    }

    private void loadHistory(){
        if(history != null) return;
        history = config.downloadHistory();
    }
}
