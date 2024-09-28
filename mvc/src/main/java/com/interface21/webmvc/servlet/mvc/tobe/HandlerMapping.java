package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.webmvc.servlet.ModelAndView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface HandlerMapping {

    void initialize();

    ModelAndView execute(HttpServletRequest request, HttpServletResponse response); // TODO: 책임 다시 고려

    Object getHandler(HttpServletRequest request);

    boolean containsRequest(HttpServletRequest request);
}
