package uk.co.rangersoftware.model;

import org.apache.commons.lang.StringUtils;

public class Show {
    private int season = 0;
    private int episode = 0;

    public int getSeason() {
        return season;
    }

    public void setSeason(int season) {
        this.season = season;
    }

    public int getEpisode() {
        return episode;
    }

    public void setEpisode(int episode) {
        this.episode = episode;
    }

    @Override
    public String toString(){
        return String.format("S%sE%s",
                StringUtils.leftPad("" + getSeason(), 2, "0"),
                StringUtils.leftPad("" + getEpisode(), 2, "0"));
    }

    public boolean isValid(){
        return season > 0 && episode > 0;
    }
}
