package com.exception;

import nextstep.mvc.exeption.CustomException;

public class NotFoundException extends CustomException {

    public NotFoundException(String message) {
        super(message);
    }
}
