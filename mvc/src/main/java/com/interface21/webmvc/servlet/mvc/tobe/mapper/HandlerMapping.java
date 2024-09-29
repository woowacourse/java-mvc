package com.interface21.webmvc.servlet.mvc.tobe.mapper;

import jakarta.servlet.http.HttpServletRequest;

public interface HandlerMapping {

    Object getHandler(HttpServletRequest request);
}
