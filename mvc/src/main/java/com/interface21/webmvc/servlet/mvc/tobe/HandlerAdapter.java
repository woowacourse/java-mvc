package com.interface21.webmvc.servlet.mvc.tobe;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface HandlerAdapter {

    boolean canHandle(final Object handler);

    void handle(final Object handler, final HttpServletRequest request, final HttpServletResponse response);
}
