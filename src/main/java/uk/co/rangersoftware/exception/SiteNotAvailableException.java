package uk.co.rangersoftware.exception;

public class SiteNotAvailableException extends Exception {
    private String message;

    public SiteNotAvailableException(String message) {
        this.message = message;
    }
}
