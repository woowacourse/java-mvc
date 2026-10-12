package com.techcourse;

import com.interface21.webmvc.servlet.HandlerAdapterRegistry;
import com.interface21.webmvc.servlet.HandlerMappingRegistry;
import com.interface21.webmvc.servlet.mvc.asis.SimpleControllerHandlerAdapter;
import com.interface21.webmvc.servlet.mvc.tobe.AnnotationHandlerMapping;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerExecutionHandlerAdapter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class DispatcherServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(DispatcherServlet.class);

    private HandlerMappingRegistry handlerMappings;
    private HandlerAdapterRegistry handlerAdapters;

    public DispatcherServlet() {
    }

    @Override
    public void init() {
        final var manualHandlerMapping = new ManualHandlerMapping();
        manualHandlerMapping.initialize();
        final var annotationHandlerMapping = new AnnotationHandlerMapping("com.techcourse.controller");
        annotationHandlerMapping.initialize();

        handlerMappings = new HandlerMappingRegistry(List.of(annotationHandlerMapping, manualHandlerMapping));
        handlerAdapters = new HandlerAdapterRegistry(
                List.of(new SimpleControllerHandlerAdapter(), new HandlerExecutionHandlerAdapter()));
    }

    @Override
    protected void service(final HttpServletRequest request, final HttpServletResponse response) throws ServletException {
        final String requestURI = request.getRequestURI();
        log.debug("Method : {}, Request URI : {}", request.getMethod(), requestURI);

        try {
            final var handler = handlerMappings.getHandler(request);
            if (handler == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
                return;
            }

            final var handlerAdapter = handlerAdapters.getHandlerAdapter(handler);
            final var modelAndView = handlerAdapter.handle(request, response, handler);
            modelAndView.getView().render(modelAndView.getModel(), request, response);
        } catch (Exception e) {
            log.error("Exception : {}", e.getMessage(), e);
            throw new ServletException(e.getMessage(), e);
        }
    }
}
