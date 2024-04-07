package com.auto.care.autoserviceapisrc.exception;

import org.springframework.http.HttpStatus;

public class AutoServiceException extends Exception {

    private static final long serialVersionUID = 2881116644451554263L;

    private final HttpStatus httpStatusCode;

    public AutoServiceException(HttpStatus httpStatusCode, String message) {
        super(message);
        this.httpStatusCode = httpStatusCode;
    }

    public HttpStatus getHttpStatusCode() {
        return httpStatusCode;
    }

}
