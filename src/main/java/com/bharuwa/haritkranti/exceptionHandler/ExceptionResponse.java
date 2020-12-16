package com.bharuwa.haritkranti.exceptionHandler;


public class ExceptionResponse {

    private String errorMessage;
    private String requestedURI;
    private String errorCode;

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(final String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public void callerURL(final String requestedURI) {
        this.requestedURI = requestedURI;
    }
}
