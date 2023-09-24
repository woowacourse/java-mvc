package com.techcourse.exception;

import context.org.springframework.stereotype.ControllerAdvice;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webmvc.org.springframework.web.servlet.ModelAndView;
import webmvc.org.springframework.web.servlet.mvc.ExceptionHandlerAdapter;
import webmvc.org.springframework.web.servlet.view.JspView;

import java.util.NoSuchElementException;

@ControllerAdvice
public class ExceptionHandler extends ExceptionHandlerAdapter {

    private static final Logger log = LoggerFactory.getLogger(ExceptionHandler.class);

    @Override
    protected ModelAndView handleException(HttpServletResponse response, Exception exception) {
        log.error("Handle Exception : {}", exception.getClass().getName());
        if (exception instanceof UnauthorizedException) {
            return new ModelAndView(new JspView("redirect:/401.jsp"));
        }
        if (exception instanceof NoSuchElementException) {
            return new ModelAndView(new JspView("redirect:/404.jsp"));
        }
        return new ModelAndView(new JspView("redirect:/500.jsp"));
    }
}
