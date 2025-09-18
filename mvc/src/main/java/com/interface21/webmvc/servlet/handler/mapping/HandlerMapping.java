package com.interface21.webmvc.servlet.handler.mapping;

import jakarta.servlet.http.HttpServletRequest;

public interface HandlerMapping {

    Object getHandler(HttpServletRequest httpServletRequest);
}
