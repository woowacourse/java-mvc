package com.techcourse.controller;

import context.org.springframework.stereotype.Controller;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.Objects;
import web.org.springframework.web.bind.annotation.RequestMapping;
import web.org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class ForwardController {

    private final String path;

    public ForwardController(final String path) {
        this.path = Objects.requireNonNull(path);
    }

    public ForwardController() {
        this("/index.jsp");
    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String execute(final HttpServletRequest request, final HttpServletResponse response) {
        return path;
    }
}
