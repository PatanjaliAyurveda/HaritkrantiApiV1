package com.bharuwa.haritkranti.utils;

import javax.ws.rs.WebApplicationException;

/**
 * @author anuragdhunna
 */
public class RestErrorException extends WebApplicationException {
    private Object errorResponse;

    public RestErrorException() {
    }

    public RestErrorException(Object errorResponse) {
        this.errorResponse = errorResponse;
    }

    public Object getErrorResponse() {
        return errorResponse;
    }

    public void setErrorResponse(Object errorResponse) {
        this.errorResponse = errorResponse;
    }
}
