package com.interface21.webmvc.servlet.mvc;

import jakarta.servlet.http.HttpServletRequest;

public interface HandlerMapping {

    void initialize();

    /**
     * return Handler which can execute http request
     * when there is no handler, then return null
     */
    Object getHandler(HttpServletRequest request);
}
