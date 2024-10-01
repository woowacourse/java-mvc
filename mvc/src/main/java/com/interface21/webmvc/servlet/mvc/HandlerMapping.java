package com.interface21.webmvc.servlet.mvc;

import jakarta.servlet.http.HttpServletRequest;

@FunctionalInterface
public interface HandlerMapping {

    Object getHandler(HttpServletRequest request);
}
