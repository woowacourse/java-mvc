package com.interface21.web.servlet;

import jakarta.servlet.http.HttpServletRequest;

import java.util.Optional;

public interface HandlerMapping {

    Object getHandler(HttpServletRequest request);
}
