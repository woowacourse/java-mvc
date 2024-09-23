package com.interface21.webmvc.servlet.mvc.tobe;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface Handler {

    boolean canHandle(HttpServletRequest request);

    void handle(HttpServletRequest request, HttpServletResponse response) throws Exception;

    void initialize();
}
