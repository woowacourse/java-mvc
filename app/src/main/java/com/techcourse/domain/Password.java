package com.techcourse.domain;

import java.util.Objects;

public class Password {

    private final String value;

    public Password(final String value) {
        this.value = value;
    }

    @Override
    public boolean equals(final Object o) {
        if (!(o instanceof Password password)) {
            return false;
        }
        return Objects.equals(value, password.value);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(value);
    }
}
