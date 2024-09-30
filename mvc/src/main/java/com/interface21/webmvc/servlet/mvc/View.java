package com.interface21.webmvc.servlet.mvc;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

public interface View {

    void render(Map<String, ?> model, HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException;
}
