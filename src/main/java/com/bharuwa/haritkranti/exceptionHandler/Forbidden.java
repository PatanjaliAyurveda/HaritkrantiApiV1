package com.bharuwa.haritkranti.exceptionHandler;

/**
 * @author anuragdhunna
 */
public class Forbidden extends RuntimeException {

    public Forbidden(String message) {
        super(message);
    }
}
