package com.techcourse.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.mvc.controller.Controller;

import java.util.Objects;

public class ForwardController implements Controller {

    private final String path;

    public ForwardController(final String path) {
        this.path = Objects.requireNonNull(path);
    }

    @Override
    public String execute(final HttpServletRequest request, final HttpServletResponse response) {
        return path;
    }
}
