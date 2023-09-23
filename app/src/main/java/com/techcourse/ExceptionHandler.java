package com.techcourse;

import jakarta.servlet.http.HttpServletResponse;
import webmvc.org.springframework.web.servlet.ModelAndView;
import webmvc.org.springframework.web.servlet.mvc.ExceptionHandlerAdapter;
import webmvc.org.springframework.web.servlet.view.JspView;

import java.util.NoSuchElementException;

public class ExceptionHandler extends ExceptionHandlerAdapter {

    @Override
    protected ModelAndView handleException(HttpServletResponse response, Exception exception) {
        // TODO 401 예외 처리 구현 후 매핑
        if (exception instanceof NoSuchElementException) {
            return new ModelAndView(new JspView("/404.jsp"));
        }
        return new ModelAndView(new JspView("/500.jsp"));
    }
}
