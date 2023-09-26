package com.techcourse.dto;

public class RegisterRequest {

    private String account;
    private String password;
    private String email;

    public RegisterRequest() {
    }

    public RegisterRequest(final String account, final String password, final String email) {
        this.account = account;
        this.password = password;
        this.email = email;
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
}
