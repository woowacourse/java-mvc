package com.interface21.webmvc.servlet;

import jakarta.servlet.http.HttpServletRequest;

public interface HandlerMapping {

    /**
     * Return the handler for the request, or null if no mapping matches.
     */
    Object getHandler(HttpServletRequest request);
}
