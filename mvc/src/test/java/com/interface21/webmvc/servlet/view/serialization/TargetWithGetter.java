package com.interface21.webmvc.servlet.view.serialization;

public class TargetWithGetter {

    private final long id;
    private final String account;
    private final String password;
    private final String email;

    public TargetWithGetter(long id, String account, String password, String email) {
        this.id = id;
        this.account = account;
        this.password = password;
        this.email = email;
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
}
