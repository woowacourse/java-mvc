package com.techcourse;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface ViewResolver {

    void resolveView(Object modelAndView, HttpServletRequest request, HttpServletResponse response) throws Exception;
}
