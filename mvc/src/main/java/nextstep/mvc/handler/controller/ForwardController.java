package nextstep.mvc.handler.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.Objects;

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
