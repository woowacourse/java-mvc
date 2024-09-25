package com.interface21.webmvc.servlet.mvc.tobe;

import java.net.http.HttpRequest;

import jakarta.servlet.http.HttpServletRequest;

public interface HandlerMapping {

    Object getHandler(final HttpServletRequest httpRequest);
}
