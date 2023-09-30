package com.techcourse.controller;

import webmvc.org.springframework.web.servlet.view.JspView;

public class JspViews {

    private JspViews() {
    }

    public static JspView toIndexPage() {
        return new JspView("/index.jsp");
    }

    public static JspView toLoginPage() {
        return new JspView("/login.jsp");
    }

    public static JspView to401Page() {
        return new JspView("/401.jsp");
    }

    public static JspView toRegisterPage() {
        return new JspView("/register.jsp");
    }
}
