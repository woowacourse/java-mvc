package com.techcourse;

import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.mvc.tobe.AnnotationHandlerMapping;
import com.techcourse.handleradapter.ControllerHandlerAdapter;
import com.techcourse.handleradapter.HandlerExecutionHandlerAdapter;
import com.techcourse.handlermapping.ManualHandlerMapping;
import com.techcourse.viewresolver.SimpleViewResolver;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DispatcherServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(DispatcherServlet.class);
    private static final String CONTROLLER_BASE_PACKAGE = "com.techcourse.controller";

    private final HandlerMappingRegistry handlerMappingRegistry;
    private final HandlerAdapterRegistry handlerAdapterRegistry;
    private final ViewResolver viewResolver;

    public DispatcherServlet() {
        this.handlerMappingRegistry = new HandlerMappingRegistry();
        this.handlerAdapterRegistry = new HandlerAdapterRegistry();
        this.viewResolver = new SimpleViewResolver();
    }

    @Override
    public void init() {
        addHandlerMapping(new AnnotationHandlerMapping(CONTROLLER_BASE_PACKAGE));
        addHandlerMapping(new ManualHandlerMapping());
        addHandlerAdapter(new HandlerExecutionHandlerAdapter());
        addHandlerAdapter(new ControllerHandlerAdapter());

    }

    public void addHandlerMapping(HandlerMapping handlerMapping) {
        handlerMapping.initialize();
        handlerMappingRegistry.addHandlerMapping(handlerMapping);
    }

    public void addHandlerAdapter(HandlerAdapter handlerAdapter) {
        handlerAdapterRegistry.addHandlerAdapter(handlerAdapter);
    }

    @Override
    protected void service(final HttpServletRequest request, final HttpServletResponse response)
            throws ServletException {
        log.info("Method : {}, Request URI : {}", request.getMethod(), request.getRequestURI());
        try {
            log.info("handlerMappingRegistry = {}", handlerMappingRegistry);
            Object controller = handlerMappingRegistry.getHandler(request);
            HandlerAdapter handlerAdapter = handlerAdapterRegistry.getHandlerAdapter(controller);
            ModelAndView modelAndView = handlerAdapter.handle(request, response, controller);
            viewResolver.resolveView(modelAndView, request, response);
        } catch (Throwable e) {
            log.error("Error while handling request: {}", e.getMessage(), e);
            throw new ServletException("Error while processing request", e);
        }
    }
}
