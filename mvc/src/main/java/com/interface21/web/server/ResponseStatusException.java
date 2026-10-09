package com.interface21.web.server;

public class ResponseStatusException extends RuntimeException {

    private final int status;

    public ResponseStatusException(final int status, final String message) {
        super(message);
        this.status = status;
    }

    public int getStatus() {
        return status;
    }
}
