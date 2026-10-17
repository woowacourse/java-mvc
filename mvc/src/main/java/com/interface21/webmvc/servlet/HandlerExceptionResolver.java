package com.interface21.webmvc.servlet;

import jakarta.servlet.http.HttpServletResponse;

public interface HandlerExceptionResolver {

    /** Returns true when the exception has been handled. */
    boolean resolve(Throwable exception, HttpServletResponse response);
}
