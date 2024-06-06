package com.Network.Network.Exception;


import org.springframework.stereotype.Component;

@Component
public class ServiceException extends RuntimeException {

    ExceptionDetails exceptionDetails;

    public ServiceException() {

    }

    public ServiceException(String message, ExceptionDetails exceptionDetails) {
        super(message);
        this.exceptionDetails = exceptionDetails;
    }
}
