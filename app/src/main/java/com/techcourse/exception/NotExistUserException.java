package com.techcourse.exception;

import webmvc.org.springframework.web.servlet.exception.JsonException;

public class NotExistUserException extends JsonException {

    public NotExistUserException(final String message) {
        super(message);
    }
}
