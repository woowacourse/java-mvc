package com.interface21.web.servlet;

import jakarta.servlet.http.HttpServletRequest;

public interface HandlerMapping {

    Object getHandler(HttpServletRequest request) throws Exception;
}
