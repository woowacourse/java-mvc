package com.interface21.webmvc.servlet.view.serialization;

public class TargetWithPartialGetter {

    private final long id;
    private final String account;
    private final String password;
    private final String email;

    public TargetWithPartialGetter(long id, String account, String password, String email) {
        this.id = id;
        this.account = account;
        this.password = password;
        this.email = email;
    }

    public long getId() {
        return id;
    }
}
