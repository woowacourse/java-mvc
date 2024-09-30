package com.interface21.webmvc.handler;

import com.interface21.webmvc.ModelAndView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface RequestHandler {

    ModelAndView handle(HttpServletRequest httpRequest, HttpServletResponse httpResponse)
            throws ReflectiveOperationException;
}
