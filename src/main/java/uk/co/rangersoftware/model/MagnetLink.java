package uk.co.rangersoftware.model;

public class MagnetLink {
    private String link;
    private String title;
    private Show show;
    private int sizeInMB;

    public MagnetLink(String link, String title, int sizeInMB) {
        this.link = link;
        this.title = title;
        this.sizeInMB = sizeInMB;
    }

    public Show getShow() {
        return show;
    }

    public void setShow(Show show) {
        this.show = show;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getSizeInMB() {
        return sizeInMB;
    }
}
