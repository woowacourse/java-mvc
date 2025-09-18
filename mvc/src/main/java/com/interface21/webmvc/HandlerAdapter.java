package com.interface21.webmvc;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface HandlerAdapter {

    void doHandle(
            Object handler,
            HttpServletRequest request,
            HttpServletResponse response
    ) throws Exception;

    boolean isProcessable(Object handler);
}
