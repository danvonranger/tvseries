package uk.co.rangersoftware.model;

public class DownloadSize {
    private boolean sizeIsValid = false;
    private int sizeInMB = 0;
    private String failureMessage;

    public boolean isValid() {
        return sizeIsValid;
    }

    public void setSizeIsValid(boolean sizeIsValid) {
        this.sizeIsValid = sizeIsValid;
    }

    public String getFailureMessage() {
        return failureMessage;
    }

    public void setFailureMessage(String failureMessage) {
        this.failureMessage = failureMessage;
    }

    public int getSizeInMB() {
        return sizeInMB;
    }

    public void setSizeInMB(int sizeInMB) {
        this.sizeInMB = sizeInMB;
    }
}
