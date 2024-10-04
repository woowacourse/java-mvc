package com.interface21.webmvc.servlet.mvc;

import jakarta.servlet.http.HttpServletRequest;

public interface HandlerMapping {

    void initialize() throws Exception;

    Object getHandler(HttpServletRequest request);
}
