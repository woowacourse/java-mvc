package com.interface21.webmvc.servlet.mvc.asis;

import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerAdapter;
import com.interface21.webmvc.servlet.view.JspView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface Controller extends HandlerAdapter {
    String execute(final HttpServletRequest req, final HttpServletResponse res) throws Exception;

    @Override
    default ModelAndView handle(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String requestURI = execute(request, response);
        return new ModelAndView(new JspView(requestURI));
    }
}
