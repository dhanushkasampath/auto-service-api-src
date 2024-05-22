package com.auto.care.autoserviceapisrc.exception;

import lombok.Getter;
import org.springframework.stereotype.Component;

import java.io.Serial;

@Component
@Getter
public class ControllerException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 2881116644451554263L;

    private String errorCode;
    private String errorMessage;

    public ControllerException(String errorCode, String errorMessage) {
        super();
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public ControllerException() {
    }

}