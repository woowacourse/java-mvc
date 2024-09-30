package com.interface21.webmvc.servlet;

import java.io.IOException;
import java.util.Map;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface View {

    void render(Map<String, ?> model, HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException;
}
