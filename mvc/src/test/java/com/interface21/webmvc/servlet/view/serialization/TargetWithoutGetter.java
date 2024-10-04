package com.interface21.webmvc.servlet.view.serialization;

public class TargetWithoutGetter {

    private final long id;
    private final String account;
    private final String password;
    private final String email;

    public TargetWithoutGetter(long id, String account, String password, String email) {
        this.id = id;
        this.account = account;
        this.password = password;
        this.email = email;
    }
}
