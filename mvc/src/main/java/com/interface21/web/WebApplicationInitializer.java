package com.interface21.web;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import java.lang.reflect.InvocationTargetException;

public interface WebApplicationInitializer {
    void onStartup(ServletContext servletContext)
            throws ServletException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException;
}
