package com.interface21.web.bind.annotation;

public class NotSupportMethodException extends RuntimeException {
    public NotSupportMethodException(final String message) {
        super(message);
    }
}
