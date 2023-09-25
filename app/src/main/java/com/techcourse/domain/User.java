package com.techcourse.domain;

import java.util.Objects;

public class User {

    private final long id;
    private final String account;
    private final String password;
    private final String email;

    public User(final long id, final String account, final String password, final String email) {
        this.id = id;
        this.account = account;
        this.password = password;
        this.email = email;
    }

    public boolean checkPassword(final String password) {
        return this.password.equals(password);
    }

    public String getAccount() {
        return account;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof User)) {
            return false;
        }
        final User user = (User) o;
        return id == user.id && Objects.equals(account, user.account) && Objects.equals(password,
                user.password) && Objects.equals(email, user.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, account, password, email);
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", account='" + account + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
