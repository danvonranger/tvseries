package uk.co.rangersoftware.downloader;

public class DownloadResult {
    private boolean inError;
    private String errorMessage;
    private String data;

    public String rawData(){return data == null ? "" : data;}

    public void setRawData(String s){ data = s;}

    public boolean isInError() {
        return inError;
    }

    public void setInError(boolean inError) {
        this.inError = inError;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
