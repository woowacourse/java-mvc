package com.techcourse;

import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.mvc.HandlerAdapter;
import com.interface21.webmvc.servlet.mvc.HandlerMapping;
import com.interface21.webmvc.servlet.mvc.asis.ControllerHandlerAdapter;
import com.interface21.webmvc.servlet.mvc.tobe.AnnotationHandlerMapping;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerExecutionAdapter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class DispatcherServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(DispatcherServlet.class);

    private List<HandlerMapping> handlerMappings;
    private List<HandlerAdapter> handlerAdapters;

    public DispatcherServlet() {
    }

    @Override
    public void init() {
        initializeHandlerMappings();
        initializeHandlerAdapters();
        
        log.info("DispatcherServlet initialized with {} HandlerMappings and {} HandlerAdapters", 
            handlerMappings.size(), handlerAdapters.size());
    }

    private void initializeHandlerMappings() {
        handlerMappings = new ArrayList<>();
        
        // Manual HandlerMapping 추가
        final ManualHandlerMapping manualHandlerMapping = new ManualHandlerMapping();
        manualHandlerMapping.initialize();
        handlerMappings.add(manualHandlerMapping);
        
        // Annotation HandlerMapping 추가
        final AnnotationHandlerMapping annotationHandlerMapping = new AnnotationHandlerMapping("com.techcourse.controller");
        annotationHandlerMapping.initialize();
        handlerMappings.add(annotationHandlerMapping);
        
        log.info("Initialized {} HandlerMappings", handlerMappings.size());
    }

    private void initializeHandlerAdapters() {
        handlerAdapters = new ArrayList<>();
        
        // Controller HandlerAdapter 추가
        handlerAdapters.add(new ControllerHandlerAdapter());
        
        // HandlerExecution HandlerAdapter 추가
        handlerAdapters.add(new HandlerExecutionAdapter());
        
        log.info("Initialized {} HandlerAdapters", handlerAdapters.size());
    }

    @Override
    protected void service(final HttpServletRequest request, final HttpServletResponse response) 
            throws ServletException {
        final String requestURI = request.getRequestURI();
        final String requestMethod = request.getMethod();
        log.debug("Method : {}, Request URI : {}", requestMethod, requestURI);

        try {
            final Object handler = getHandler(request);
            if (handler == null) {
                handleNoHandlerFound(request, response);
                return;
            }

            final HandlerAdapter handlerAdapter = getHandlerAdapter(handler);
            if (handlerAdapter == null) {
                handleNoAdapterFound(handler, request, response);
                return;
            }

            final ModelAndView modelAndView = handlerAdapter.handle(request, response, handler);
            processModelAndView(modelAndView, request, response);
            
        } catch (Throwable e) {
            log.error("Exception : {}", e.getMessage(), e);
            throw new ServletException(e.getMessage());
        }
    }

    private Object getHandler(final HttpServletRequest request) {
        for (final HandlerMapping handlerMapping : handlerMappings) {
            final Object handler = handlerMapping.getHandler(request);
            if (handler != null) {
                log.debug("Found handler: {} from {}", 
                    handler.getClass().getSimpleName(), handlerMapping.getClass().getSimpleName());
                return handler;
            }
        }
        
        log.debug("No handler found for request: {} {}", request.getMethod(), request.getRequestURI());
        return null;
    }

    private HandlerAdapter getHandlerAdapter(final Object handler) {
        for (final HandlerAdapter handlerAdapter : handlerAdapters) {
            if (handlerAdapter.supports(handler)) {
                log.debug("Found adapter: {} for handler: {}", 
                    handlerAdapter.getClass().getSimpleName(), handler.getClass().getSimpleName());
                return handlerAdapter;
            }
        }
        
        log.warn("No adapter found for handler: {}", handler.getClass().getName());
        return null;
    }

    private void handleNoHandlerFound(final HttpServletRequest request, final HttpServletResponse response) 
            throws Exception {
        log.warn("No handler found for request: {} {}", request.getMethod(), request.getRequestURI());
        response.sendError(HttpServletResponse.SC_NOT_FOUND, "No handler found");
    }

    private void handleNoAdapterFound(final Object handler, final HttpServletRequest request, 
                                      final HttpServletResponse response) throws Exception {
        log.error("No adapter found for handler: {}", handler.getClass().getName());
        response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "No adapter found for handler");
    }

    private void processModelAndView(final ModelAndView modelAndView, final HttpServletRequest request, 
                                     final HttpServletResponse response) throws Exception {
        if (modelAndView == null) {
            log.debug("ModelAndView is null, nothing to render");
            return;
        }

        final var view = modelAndView.getView();
        if (view == null) {
            log.warn("View is null in ModelAndView");
            return;
        }

        log.debug("Rendering view: {}", view.getClass().getSimpleName());
        view.render(modelAndView.getModel(), request, response);
    }
}
