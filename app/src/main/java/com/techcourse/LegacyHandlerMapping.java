package com.techcourse;

import jakarta.servlet.http.HttpServletRequest;
import webmvc.org.springframework.web.servlet.mvc.HandlerMapping;

public abstract class LegacyHandlerMapping implements HandlerMapping {

    @Override
    public Object getHandler(HttpServletRequest httpServletRequest) {
        return getHandler(httpServletRequest.getRequestURI());
    }

    public abstract Object getHandler(String requestURI);
}
