package com.interface21.webmvc.servlet.mvc.tobe.handlerMapping;

import jakarta.servlet.http.HttpServletRequest;

public interface HandlerMapping {

    boolean isSupported(HttpServletRequest request);

    Object getHandler(HttpServletRequest request);
}
