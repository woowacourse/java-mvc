package com.interface21.webmvc.servlet.mvc.tobe.handler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import com.interface21.webmvc.servlet.ModelAndView;

import static com.interface21.webmvc.servlet.view.JspView.NOT_FOUND_VIEW;

public class ErrorController {

    public ModelAndView notFoundErrorHandle(final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        return new ModelAndView(NOT_FOUND_VIEW);
    }
}
