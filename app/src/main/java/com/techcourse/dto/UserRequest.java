package com.techcourse.dto;

public class UserRequest {

    private String account;

    public UserRequest() {
    }

    public UserRequest(final String account) {
        this.account = account;
    }

    public String getAccount() {
        return account;
    }
}
