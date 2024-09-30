package com.interface21.webmvc.servlet.mvc.handlerAdapter;

import com.interface21.webmvc.servlet.ModelAndView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface HandlerAdapter {

    boolean isSupported(Object object);

    ModelAndView handle(Object object, HttpServletRequest request, HttpServletResponse response);
}
