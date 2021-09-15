package com.techcourse.service.dto;

import jakarta.servlet.http.HttpServletRequest;

public class RegisterDto {

    private final String account;
    private final String password;
    private final String email;

    public static RegisterDto of(HttpServletRequest request) {
        return new RegisterDto(request.getParameter("account"),
                               request.getParameter("password"),
                               request.getParameter("email"));
    }

    public static RegisterDto of(String account, String password, String email) {
        return new RegisterDto(account, password, email);
    }

    private RegisterDto(String account, String password, String email) {
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
