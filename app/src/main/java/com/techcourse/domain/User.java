package com.techcourse.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;


@Getter
@RequiredArgsConstructor
public class User {

    private final long id;
    private final String account;
    private final String password;
    private final String email;

    public boolean checkPassword(final String password) {
        return this.password.equals(password);
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", account='" + account + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
