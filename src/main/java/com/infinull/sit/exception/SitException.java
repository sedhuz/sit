package com.infinull.sit.exception;

public class SitException extends IllegalArgumentException {

    private final int statusCode;  // Custom status code

    public SitException() {
        super();
        this.statusCode = 1;
    }

    public SitException(int statusCode, String message) {
        super(message);
        this.statusCode = statusCode;
    }
    
    public int getStatusCode() {
        return statusCode;
    }
}
