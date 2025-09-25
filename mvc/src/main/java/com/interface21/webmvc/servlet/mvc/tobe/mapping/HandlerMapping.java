package com.interface21.webmvc.servlet.mvc.tobe.mapping;

import jakarta.servlet.http.HttpServletRequest;

public interface HandlerMapping {

    Object getHandler(HttpServletRequest request);
}
