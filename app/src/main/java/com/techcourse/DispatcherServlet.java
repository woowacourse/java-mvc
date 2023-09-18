package com.techcourse;

import com.techcourse.support.mvc.HandlerAdapters;
import com.techcourse.support.mvc.HandlerMappings;
import com.techcourse.support.mvc.ManualHandlerAdapter;
import com.techcourse.support.mvc.ManualHandlerMapping;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webmvc.org.springframework.web.servlet.ModelAndView;
import webmvc.org.springframework.web.servlet.mvc.tobe.AnnotationHandlerAdapter;
import webmvc.org.springframework.web.servlet.mvc.tobe.AnnotationHandlerMapping;
import webmvc.org.springframework.web.servlet.mvc.tobe.HandlerAdapter;

public class DispatcherServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(DispatcherServlet.class);

    private final HandlerMappings handlerMappings = new HandlerMappings();
    private final HandlerAdapters handlerAdapters = new HandlerAdapters();

    @Override
    public void init() {
        handlerMappings.addHandlerMapping(new ManualHandlerMapping());
        handlerMappings.addHandlerMapping(new AnnotationHandlerMapping("com.techcourse.controller"));
        handlerMappings.initialize();
        handlerAdapters.addHandlerAdapter(new ManualHandlerAdapter());
        handlerAdapters.addHandlerAdapter(new AnnotationHandlerAdapter());
    }

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        log.debug("Method : {}, Request URI : {}", request.getMethod(), request.getRequestURI());

        try {
            Object handler = handlerMappings.getHandler(request);
            HandlerAdapter handlerAdapter = handlerAdapters.getHandlerAdapter(handler);
            ModelAndView modelAndView = handlerAdapter.handle(handler, request, response);
            modelAndView.render(request, response);
        } catch (Throwable e) {
            log.error("Exception : {}", e.getMessage(), e);
            throw new ServletException(e.getMessage());
        }
    }
}
