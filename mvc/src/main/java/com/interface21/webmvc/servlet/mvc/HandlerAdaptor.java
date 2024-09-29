package com.interface21.webmvc.servlet.mvc;

import com.interface21.webmvc.servlet.ModelAndView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface HandlerAdaptor {

    boolean canExecute(Object handler);

    ModelAndView execute(Object handler, final HttpServletRequest request, HttpServletResponse response) throws Exception;
}
