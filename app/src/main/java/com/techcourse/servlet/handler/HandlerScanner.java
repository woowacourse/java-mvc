package com.techcourse.servlet.handler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webmvc.org.springframework.web.servlet.mvc.tobe.AnnotationHandlerMapping;
import webmvc.org.springframework.web.servlet.mvc.tobe.HandlerMapping;

import java.util.ArrayList;
import java.util.List;

public class HandlerScanner {

    private static final Logger log = LoggerFactory.getLogger(HandlerScanner.class);

    private final List<HandlerMapping> handlerMappings = new ArrayList<>();

    public HandlerScanner(final Object... basePackages) {
        handlerMappings.add(new AnnotationHandlerMapping(basePackages));
    }

    public Object getHandler(final HttpServletRequest request) throws ServletException {
        for (final HandlerMapping handlerMapping : handlerMappings) {
            final Object handler = handlerMapping.getHandler(request);
            if (handler != null) {
                return handler;
            }
        }

        log.error("해당 요청을 지원하는 핸들러가 없습니다. request 정보 = {}", createLogMessage(request));

        throw new ServletException("처리할 수 없는 요청입니다.");
    }

    private String createLogMessage(final HttpServletRequest request) {
        return request.getMethod() + " " + request.getRequestURI();
    }
}
