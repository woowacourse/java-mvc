package com.techcourse;

import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.mvc.HandlerAdapter;
import com.interface21.webmvc.servlet.mvc.HandlerAdapterRegistry;
import com.interface21.webmvc.servlet.mvc.HandlerExecutor;
import com.interface21.webmvc.servlet.mvc.HandlerMapping;
import com.interface21.webmvc.servlet.mvc.HandlerMappingRegistry;
import com.interface21.webmvc.servlet.mvc.asis.ControllerAdapter;
import com.interface21.webmvc.servlet.mvc.tobe.AnnotationHandlerMapping;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerExecutionAdapter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DispatcherServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(DispatcherServlet.class);

    private final HandlerMappingRegistry handlerMappingRegistry = new HandlerMappingRegistry();
    private final HandlerAdapterRegistry handlerAdapterRegistry = new HandlerAdapterRegistry();
    private HandlerExecutor handlerExecutor;

    public DispatcherServlet() {
    }

    @Override
    public void init() {
        initializeHandlerMappingRegistry();
        initializeHandlerAdapterRegistry();
        handlerExecutor = new HandlerExecutor(handlerMappingRegistry, handlerAdapterRegistry);
    }

    private void initializeHandlerMappingRegistry() {
        HandlerMapping manualHandlerMapping = new ManualHandlerMapping();
        manualHandlerMapping.initialize();
        handlerMappingRegistry.register(manualHandlerMapping);

        HandlerMapping annotationHandlerMapping = new AnnotationHandlerMapping("com.techcourse.controller");
        annotationHandlerMapping.initialize();
        handlerMappingRegistry.register(annotationHandlerMapping);
    }

    private void initializeHandlerAdapterRegistry() {
        HandlerAdapter controllerAdapter = new ControllerAdapter();
        handlerAdapterRegistry.register(controllerAdapter);

        HandlerAdapter handlerExecutionAdapter = new HandlerExecutionAdapter();
        handlerAdapterRegistry.register(handlerExecutionAdapter);
    }

    @Override
    protected void service(final HttpServletRequest request, final HttpServletResponse response)
            throws ServletException {
        try {
            log.info("요청 URI: {}, Method: {}", request.getRequestURI(), request.getMethod());
            ModelAndView modelAndView = handlerExecutor.execute(request, response);
            modelAndView.render(request, response);
        } catch (Throwable e) {
            log.error("Exception : {}", e.getMessage(), e);
            throw new ServletException(e.getMessage());
        }
    }
}
