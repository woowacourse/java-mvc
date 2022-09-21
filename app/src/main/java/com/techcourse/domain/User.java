package com.techcourse.domain;

import java.util.Objects;

public class User {

    private final long id;
    private final String account;
    private final String password;
    private final String email;

    public User(long id, String account, String password, String email) {
        this.id = id;
        this.account = account;
        this.password = password;
        this.email = email;

        validate();
    }

    private void validate() {
        if (Objects.isNull(this.password) || doesNotHaveText()) {
            throw new IllegalArgumentException(String.format("password를 확인해주세요 : %s", this.password));
        }
    }

    private boolean doesNotHaveText() {
        return password.strip().length() == 0;
    }

    public User(String account, String password, String email) {
        this(0L, account, password, email);
    }

    public boolean checkPassword(String password) {
        return this.password.equals(password);
    }

    public String getAccount() {
        return account;
    }

    public User assignId(long id) {
        return new User(id, this.account, this.password, this.email);
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
