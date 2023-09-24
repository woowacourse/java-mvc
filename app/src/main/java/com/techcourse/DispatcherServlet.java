package com.techcourse;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webmvc.org.springframework.web.servlet.ModelAndView;
import webmvc.org.springframework.web.servlet.mvc.tobe.AnnotationHandlerMapping;
import webmvc.org.springframework.web.servlet.mvc.tobe.HandlerAdapter;
import webmvc.org.springframework.web.servlet.mvc.tobe.HandlerExecutionHandlerAdapter;
import webmvc.org.springframework.web.servlet.mvc.tobe.HandlerMapping;

import java.util.List;

public class DispatcherServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(DispatcherServlet.class);

    private HandlerMappingRegistry handlerMappingRegistry;
    private HandlerAdapterRegistry handlerAdapterRegistry;

    public DispatcherServlet() {
    }

    @Override
    public void init() {
        List<HandlerMapping> handlerMappings = List.of(new ManualHandlerMapping(), new AnnotationHandlerMapping());
        handlerMappings.forEach(HandlerMapping::initialize);
        this.handlerMappingRegistry = new HandlerMappingRegistry(handlerMappings);

        List<HandlerAdapter> handlerAdapters = List.of(new ControllerHandlerAdapter(), new HandlerExecutionHandlerAdapter());
        this.handlerAdapterRegistry = new HandlerAdapterRegistry(handlerAdapters);
    }

    @Override
    protected void service(final HttpServletRequest request, final HttpServletResponse response) throws ServletException {
        String requestURI = request.getRequestURI();
        log.debug("Method : {}, Request URI : {}", request.getMethod(), requestURI);

        HandlerMapping handlerMapping = handlerMappingRegistry.getHandlerMapping(request);
        Object handler = handlerMapping.getHandler(request);
        try {
            HandlerAdapter handlerAdapter = handlerAdapterRegistry.getHandlerAdapter(handler);
            ModelAndView mav = handlerAdapter.handle(handler, request, response);
            mav.render(request, response);
        } catch (Exception e) {
            log.error("Exception : {}", e.getMessage(), e);
            throw new ServletException(e.getMessage());
        }
    }
}
