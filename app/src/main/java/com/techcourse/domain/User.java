package com.techcourse.domain;

public class User {

    private long id;
    private final String account;
    private final String password;
    private final String email;

    public User(final String account, final String password, final String email) {
        this.account = account;
        this.password = password;
        this.email = email;
    }

    public void setId(final long id) {
        this.id = id;
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
