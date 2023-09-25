package com.techcourse.controlleradvisor;

import webmvc.org.springframework.web.servlet.exception.HandlerAdapterNotFoundException;
import webmvc.org.springframework.web.servlet.exception.HandlerNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webmvc.org.springframework.web.servlet.ExceptionHandler;
import webmvc.org.springframework.web.servlet.ExceptionManager;

@ExceptionManager
public class DispatcherServletAdvisor {

    private static final Logger log = LoggerFactory.getLogger(DispatcherServletAdvisor.class);

    @ExceptionHandler(HandlerNotFoundException.class)
    public String show404Page(final HttpServletRequest request, final HttpServletResponse response) {
        log.error("핸들러못찾았셔");
        return "/404.jsp";
    }

    @ExceptionHandler(HandlerAdapterNotFoundException.class)
    public String show500Page(final HttpServletRequest request, final HttpServletResponse response) {
        log.error("핸들러어댑떠못찾았셔");
        return "/500.jsp";
    }
}
