package com.interface21.webmvc.servlet.mvc.tobe;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface ViewResolver {

    void resolveView(Object modelAndView, HttpServletRequest request, HttpServletResponse response) throws Exception;
}
