package com.techcourse.domain;

public class User {

    private final Account account;
    private final Password password;
    private final Email email;

    public User(final Account account, final Password password, final Email email) {
        this.account = account;
        this.password = password;
        this.email = email;
    }

    public boolean checkPassword(final Password password) {
        return this.password.equals(password);
    }

    public boolean checkAccount(final Account account) {
        return this.account.equals(account);
    }

    @Override
    public String toString() {
        return "User{" +
                account + '\'' +
                email + '\'' +
                '}';
    }
}
