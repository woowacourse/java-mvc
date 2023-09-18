package com.techcourse.servlet;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import webmvc.org.springframework.web.servlet.mvc.asis.Controller;
import webmvc.org.springframework.web.servlet.mvc.handler.HandlerAdapter;

public class ControllerHandlerAdapter implements HandlerAdapter {

    private final Controller controller;

    public ControllerHandlerAdapter(final Object handler) {
        this.controller = (Controller) handler;
    }

    @Override
    public String execute(final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        return controller.execute(request, response);
    }
}
