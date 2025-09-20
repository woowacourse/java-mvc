package com.interface21.webmvc.servlet.mvc;

import com.interface21.webmvc.servlet.mvc.adapter.HandlerAdapter;
import com.interface21.webmvc.servlet.mvc.adapter.HandlerAdapterRegistry;
import com.interface21.webmvc.servlet.mvc.adapter.HandlerExecutionAdapter;
import com.interface21.webmvc.servlet.mvc.adapter.asis.ControllerAdapter;
import com.interface21.webmvc.servlet.mvc.exception.NoHandlerFoundException;
import com.interface21.webmvc.servlet.mvc.execution.HandlerExecutor;
import com.interface21.webmvc.servlet.mvc.mapping.AnnotationHandlerMapping;
import com.interface21.webmvc.servlet.mvc.mapping.HandlerMapping;
import com.interface21.webmvc.servlet.mvc.mapping.HandlerMappingRegistry;
import com.interface21.webmvc.servlet.mvc.mapping.asis.ManualHandlerMapping;
import com.interface21.webmvc.servlet.view.ModelAndView;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DispatcherServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(DispatcherServlet.class);

    private final Object[] basePackage;
    private final Properties properties;
    private final HandlerMappingRegistry handlerMappingRegistry = new HandlerMappingRegistry();
    private final HandlerAdapterRegistry handlerAdapterRegistry = new HandlerAdapterRegistry();
    private HandlerExecutor handlerExecutor;

    public DispatcherServlet(Properties properties, Object... basePackage) {
        this.basePackage = basePackage;
        this.properties = properties;
    }

    @Override
    public void init() {
        initializeHandlerMappingRegistry();
        initializeHandlerAdapterRegistry();
        handlerExecutor = new HandlerExecutor(handlerMappingRegistry, handlerAdapterRegistry);
    }

    private void initializeHandlerMappingRegistry() {
        HandlerMapping manualHandlerMapping = new ManualHandlerMapping(properties);
        manualHandlerMapping.initialize();
        handlerMappingRegistry.register(manualHandlerMapping);

        HandlerMapping annotationHandlerMapping = new AnnotationHandlerMapping(basePackage);
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
            throws ServletException, IOException {
        try {
            log.info("요청 URI: {}, Method: {}", request.getRequestURI(), request.getMethod());
            ModelAndView modelAndView = handlerExecutor.execute(request, response);
            modelAndView.render(request, response);
        } catch (NoHandlerFoundException e) {
            log.warn(e.getMessage(), e);
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        } catch (Throwable e) {
            log.error("Exception : {}", e.getMessage(), e);
            throw new ServletException(e.getMessage(), e);
        }
    }
}
