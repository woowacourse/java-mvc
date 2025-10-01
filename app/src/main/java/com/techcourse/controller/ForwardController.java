package com.techcourse.controller;

import static com.interface21.web.bind.annotation.RequestMethod.GET;

import java.util.Objects;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Controller
public class ForwardController {

    private static final String DEFAULT_PATH = "/index.jsp";

    private final String path;

    public ForwardController() {
        this.path = DEFAULT_PATH;
    }

    public ForwardController(final String path) {
        this.path = Objects.requireNonNull(path);
    }

    @RequestMapping(value = "/", method = GET)
    public String execute(final HttpServletRequest request, final HttpServletResponse response) {
        return path;
    }
}
