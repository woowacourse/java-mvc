package com.interface21.webmvc.servlet.mvc.tobe;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface HandlerMapping {

    void initialize();

    boolean hasHandler(final HttpServletRequest request);

    void handle(final HttpServletRequest request, final HttpServletResponse response);
}
