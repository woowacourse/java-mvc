package com.techcourse.domain;

public class Email {

    private final String value;

    public Email(final String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "email= " + value;
    }

    public String getValue() {
        return value;
    }
}
