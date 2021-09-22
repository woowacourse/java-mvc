package com.techcourse.controller;

public enum Page {
    INDEX_JSP("/index.jsp"),
    REDIRECT_INDEX_JSP("redirect:/index.jsp"),
    LOGIN_JSP("/login.jsp"),
    REGISTER_JSP("/register.jsp"),
    REDIRECT_401_JSP("redirect:/401.jsp");

    private final String path;

    Page(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }
}
