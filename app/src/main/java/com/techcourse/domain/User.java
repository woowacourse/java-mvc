package com.techcourse.domain;

public class User {

    private final long id;
    private final String account;
    private final String password;
    private final String email;

    public User(long id, String account, String password, String email) {
        validateData(account, password, email);
        this.id = id;
        this.account = account;
        this.password = password;
        this.email = email;
    }

    private void validateData(String account, String password, String email) {
        if (account.isEmpty() || password.isEmpty() || email.isEmpty()) {
            throw new UserCreationException();
        }
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
