package com.techcourse.domain;

public class User {

    private final Long id;
    private final String account;
    private final String password;
    private final String email;

    public User(final String account, final String password, final String email) {
        this(null, account, password, email);
    }

    public User(final Long id, final String account, final String password, final String email) {
        this.id = id;
        this.account = account;
        this.password = password;
        this.email = email;
    }

    public boolean checkPassword(final String password) {
        return this.password.equals(password);
    }

    public User newInstanceWithId(final Long id) {
        return new User(id, account, password, email);
    }

    public long getId() {
        return id;
    }

    public String getAccount() {
        return account;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
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
