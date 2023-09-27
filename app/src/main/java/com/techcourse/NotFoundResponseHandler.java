package com.techcourse;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Objects;
import webmvc.org.springframework.web.servlet.ModelAndView;
import webmvc.org.springframework.web.servlet.mvc.tobe.exceptionhandlermapping.ExceptionHandler;
import webmvc.org.springframework.web.servlet.view.JspView;

public class NotFoundResponseHandler implements ExceptionHandler {

    private static final int NOT_FOUND_STATUS_CODE = 404;

    private String viewName;

    @Override
    public void initialize() {
        this.viewName = "404.jsp";
    }

    @Override
    public boolean isHandleable(final HttpServletRequest request, final HttpServletResponse response) {
        return response.getStatus() == NOT_FOUND_STATUS_CODE;
    }

    @Override
    public ModelAndView handle(final HttpServletRequest request) {
        if (Objects.nonNull(viewName)) {
            return new ModelAndView(new JspView(viewName));
        }
        throw new UnsupportedOperationException();
    }
}
