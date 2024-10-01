package com.interface21.webmvc.adapter;

import com.interface21.webmvc.ModelAndView;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface HandlerAdapter {

    ModelAndView handle(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, ReflectiveOperationException;
}
