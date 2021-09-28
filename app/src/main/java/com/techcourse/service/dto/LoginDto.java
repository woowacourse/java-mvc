package com.techcourse.service.dto;

public class LoginDto {

    private final String account;
    private final String password;

    public static LoginDto of(String account, String password) {
        return new LoginDto(account, password);
    }

    private LoginDto(String account, String password) {
        this.account = account;
        this.password = password;
    }

    public String getAccount() {
        return account;
    }

    public String getPassword() {
        return password;
    }
}
