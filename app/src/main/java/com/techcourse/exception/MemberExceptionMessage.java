package com.techcourse.exception;

public enum MemberExceptionMessage {
    NO_ACCOUNT("존재하지 않는 account 입니다.");

    private final String message;

    MemberExceptionMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
