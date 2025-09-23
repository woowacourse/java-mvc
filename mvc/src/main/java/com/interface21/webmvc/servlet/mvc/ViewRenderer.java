package com.interface21.webmvc.servlet.mvc;

import com.interface21.webmvc.servlet.ModelAndView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface ViewRenderer {

    void render(ModelAndView modelAndView, HttpServletRequest request, HttpServletResponse response) throws Exception;
}
