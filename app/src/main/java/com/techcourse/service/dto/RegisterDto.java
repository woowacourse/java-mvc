package com.techcourse.service.dto;

public class RegisterDto {

    private final String account;
    private final String password;
    private final String email;

    private RegisterDto(String account, String password, String email) {
        this.account = account;
        this.password = password;
        this.email = email;
    }

    public static RegisterDto of(String account, String password, String email) {
        return new RegisterDto(account, password, email);
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
