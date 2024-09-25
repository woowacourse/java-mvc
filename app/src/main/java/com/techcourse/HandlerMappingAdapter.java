package com.techcourse;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import com.interface21.webmvc.servlet.ModelAndView;

public interface HandlerMappingAdapter {

    ModelAndView adapt(HttpServletRequest request, HttpServletResponse response) throws Exception;
}
