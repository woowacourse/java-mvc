package com.interface21.webmvc.servlet.mvc.tobe;

import jakarta.servlet.http.HttpServletRequest;

import com.interface21.webmvc.servlet.View;

public interface HandlerMapping {
    View handle(HttpServletRequest request);
}
