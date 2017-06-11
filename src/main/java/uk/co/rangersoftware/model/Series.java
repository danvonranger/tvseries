package uk.co.rangersoftware.model;

import java.util.ArrayList;
import java.util.List;

public class Series {
    private String title;
    private List<String> history = new ArrayList<String>();
    private List<String> downloadUrls = new ArrayList<String>();

    public List<String> getHistory() {
        return history;
    }

    public List<String> getDownloadUrls(){
        return downloadUrls;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void initializeHistory(String csv){
        history.clear();
        for(String s : csv.split(",")){
            if(s.trim().length() == 0) continue;
            if(history.contains(s)) continue;
            history.add(s);
        }
    }

    public void addToHistory(Show show){
        String asString = showAsString(show);
        if(history.contains(asString)) return;
        history.add(asString);
    }

    public boolean showHasBeenDownloaded(Show show){
        String asString = showAsWildcard(show);
        if(history.contains(asString)) return true;
        asString = showAsString(show);
        return history.contains(asString);
    }

    public void addDownloadUrl(String url){
        if(downloadUrls.contains(url)) return;
        downloadUrls.add(url);
    }

    private String showAsString(Show show){
        return String.format("%s-%s", show.getSeason(), show.getEpisode());
    }

    private String showAsWildcard(Show show){
        return String.format("%s-*", show.getSeason());
    }
}
