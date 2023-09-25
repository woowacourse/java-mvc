package com.techcourse.domain;

public class User {

    private final Long id;
    private final String account;
    private final String password;
    private final String email;

    public User(final String account, final String password, final String email) {
        this(null, account, password, email);
    }

    public User(final Long id, final User user) {
        this(id, user.account, user.password, user.email);
    }

    public User(final Long id, final String account, final String password, final String email) {
        this.id = id;
        this.account = account;
        this.password = password;
        this.email = email;
    }

    public boolean checkPassword(String password) {
        return this.password.equals(password);
    }

    public String getAccount() {
        return account;
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
