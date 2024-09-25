package com.interface21.webmvc.servlet.mvc.asis;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.Objects;

public class ForwardController implements Controller {

    @Override
    public String execute(final HttpServletRequest request, final HttpServletResponse response) {
        return "/index.jsp";
    }
}
