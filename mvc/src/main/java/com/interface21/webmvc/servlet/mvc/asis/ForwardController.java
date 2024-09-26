package com.interface21.webmvc.servlet.mvc.asis;

import java.util.Objects;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class ForwardController implements Controller {

    private final String path;

    public ForwardController(String path) {
        this.path = Objects.requireNonNull(path);
    }

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        return path;
    }
}
