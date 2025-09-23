package com.techcourse.domain;

import java.util.Objects;

public class Account {
    private final String value;

    public Account(final String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "account= " + value;
    }

    @Override
    public boolean equals(final Object o) {
        if (!(o instanceof Account account)) {
            return false;
        }
        return Objects.equals(value, account.value);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(value);
    }
}
