package com.interface21.webmvc.servlet;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface View {
    void render(HttpServletRequest request, HttpServletResponse response) throws Exception;

    String getViewName();
}
