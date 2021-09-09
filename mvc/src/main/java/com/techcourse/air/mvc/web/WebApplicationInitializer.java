package com.techcourse.air.mvc.web;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;

public interface WebApplicationInitializer {
    void onStartup(ServletContext servletContext) throws ServletException;
}
