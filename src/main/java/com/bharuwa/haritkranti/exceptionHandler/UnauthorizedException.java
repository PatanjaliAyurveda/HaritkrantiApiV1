package com.bharuwa.haritkranti.exceptionHandler;

/**
 * @author anuragdhunna
 */
public class UnauthorizedException extends RuntimeException {

    public UnauthorizedException(String message) {
        super(message);
    }

}
