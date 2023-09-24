package com.techcourse.controller;

final class JspConstants {

    public static final String REDIRECT_ROOT = "redirect:/";
    public static final String INDEX_JSP = "/index.jsp";
    public static final String REDIRECT_INDEX_JSP = "redirect:/index.jsp";
    public static final String LOGIN_JSP = "/login.jsp";
    public static final String REGISTER_JSP = "/register.jsp";
    public static final String REDIRECT_401_JSP = "redirect:/401.jsp";

    private JspConstants() {
        throw new IllegalStateException("Utility class");
    }
}
