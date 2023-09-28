package com.techcourse.exception;

import webmvc.org.springframework.web.servlet.mvc.exception.NotFoundException;

public class UserNotFoundException extends NotFoundException {
    public UserNotFoundException(final String message) {
        super(message);
    }
}
