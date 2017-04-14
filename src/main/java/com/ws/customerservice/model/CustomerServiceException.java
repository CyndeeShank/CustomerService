package com.ws.customerservice.model;


/**
 * Created by cshank on 1/6/2016.
 */
public class CustomerServiceException extends Exception {
    public CustomerServiceException() {}
    public CustomerServiceException(String message) {
        super(message);
    }
    public CustomerServiceException(Throwable cause) {
        super(cause);
    }
    public CustomerServiceException(String message, Throwable cause) {
        super(message, cause);
    }
    public CustomerServiceException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
