package com.interface21.webmvc.servlet.mvc.tobe;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface HandlerMapping {

    void initialize();

    void handler(HttpServletRequest request, HttpServletResponse response) throws Exception;

    boolean canHandle(HttpServletRequest request);
}
