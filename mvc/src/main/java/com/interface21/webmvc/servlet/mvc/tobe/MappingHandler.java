package com.interface21.webmvc.servlet.mvc.tobe;

import jakarta.servlet.http.HttpServletRequest;

public interface MappingHandler {

    void initialize();

    Object getHandler(HttpServletRequest path);
}
