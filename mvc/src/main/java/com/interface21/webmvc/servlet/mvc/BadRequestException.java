package com.interface21.webmvc.servlet.mvc;

public class BadRequestException extends RuntimeException {

    public BadRequestException(String message) {
        super(message);
    }
}
