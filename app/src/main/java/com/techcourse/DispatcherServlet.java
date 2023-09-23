package com.techcourse;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import web.org.springframework.web.exception.InvalidHandlerTypeException;
import webmvc.org.springframework.web.servlet.ModelAndView;
import webmvc.org.springframework.web.servlet.mvc.asis.Controller;
import webmvc.org.springframework.web.servlet.mvc.tobe.AnnotationHandlerMapping;
import webmvc.org.springframework.web.servlet.mvc.tobe.HandlerExecution;
import webmvc.org.springframework.web.servlet.mvc.tobe.HandlerMapping;

import java.util.ArrayList;
import java.util.List;

public class DispatcherServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(DispatcherServlet.class);

    private HandlerMappingRegistry handlerMappingRegistry;

    public DispatcherServlet() {
    }

    @Override
    public void init() {
        List<HandlerMapping> handlerMappings = new ArrayList<>();
        handlerMappings.add(new ManualHandlerMapping());
        handlerMappings.add(new AnnotationHandlerMapping());
        handlerMappings.forEach(HandlerMapping::initialize);
        this.handlerMappingRegistry = new HandlerMappingRegistry(handlerMappings);
    }

    @Override
    protected void service(final HttpServletRequest request, final HttpServletResponse response) throws ServletException {
        String requestURI = request.getRequestURI();
        log.debug("Method : {}, Request URI : {}", request.getMethod(), requestURI);

        HandlerMapping handlerMapping = handlerMappingRegistry.getHandlerMapping(request);
        Object handler = handlerMapping.getHandler(request);
        try {
            ModelAndView mav;
            if (handler instanceof Controller) {
                mav = ((Controller)handler).execute(request, response);
            } else if (handler instanceof HandlerExecution) {
                mav = ((HandlerExecution)handler).handle(request, response);
            } else {
                throw new InvalidHandlerTypeException("존재하지 않는 Handler 타입입니다.");
            }
            mav.render(request, response);
        } catch (Exception e) {
            log.error("Exception : {}", e.getMessage(), e);
            throw new ServletException(e.getMessage());
        }
    }
}
