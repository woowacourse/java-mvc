package com.techcourse.controller.dto;

import com.techcourse.domain.User;

public class UserDto {
    private final long id;
    private final String account;
    private final String email;

    private UserDto(long id, String account, String email) {
        this.id = id;
        this.account = account;
        this.email = email;
    }

    public static UserDto from(User user) {
        return new UserDto(user.getId(),
                           user.getAccount(),
                           user.getEmail());
    }

    public long getId() {
        return id;
    }

    public String getAccount() {
        return account;
    }

    public String getEmail() {
        return email;
    }
}
