package com.techcourse.exception;

import webmvc.org.springframework.web.servlet.ModelAndView;
import webmvc.org.springframework.web.servlet.mvc.tobe.exception.HandlerNotFoundException;
import webmvc.org.springframework.web.servlet.mvc.tobe.handler.ExceptionHandler;
import webmvc.org.springframework.web.servlet.view.JspView;

public class NotFoundExceptionHandler implements ExceptionHandler {

    @Override
    public boolean support(Throwable ex) {
        return ex instanceof HandlerNotFoundException;
    }

    @Override
    public ModelAndView handle() {
        return new ModelAndView(new JspView("/404.jsp"));
    }
}
