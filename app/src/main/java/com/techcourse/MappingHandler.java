package com.techcourse;

import jakarta.servlet.http.HttpServletRequest;

public interface MappingHandler {

    void initialize();

    Object getHandler(HttpServletRequest path);
}
