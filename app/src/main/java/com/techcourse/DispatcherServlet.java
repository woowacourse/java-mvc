package com.techcourse;

import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.mvc.handler.adapter.AnnotationHandlerAdapter;
import com.interface21.webmvc.servlet.mvc.handler.mapping.AnnotationHandlerMapping;
import com.interface21.webmvc.servlet.mvc.handler.adapter.HandlerAdapter;
import com.interface21.webmvc.servlet.mvc.handler.adapter.HandlerAdapterRegistry;
import com.interface21.webmvc.servlet.mvc.handler.mapping.HandlerMapping;
import com.interface21.webmvc.servlet.mvc.handler.mapping.HandlerMappingRegistry;
import com.interface21.webmvc.servlet.mvc.handler.adapter.ManualHandlerAdapter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DispatcherServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(DispatcherServlet.class);

    private final HandlerMappingRegistry handlerMappingRegistry;
    private final HandlerAdapterRegistry handlerAdapterRegistry;

    public DispatcherServlet(
            final HandlerMappingRegistry handlerMappingRegistry,
            final HandlerAdapterRegistry handlerAdapterRegistry
    ) {
        this.handlerMappingRegistry = handlerMappingRegistry;
        this.handlerAdapterRegistry = handlerAdapterRegistry;
    }

    public void addHandlerAdapter(final HandlerAdapter handlerAdapter) {
        handlerAdapterRegistry.addHandlerAdapter(handlerAdapter);
    }

    public void addHandlerMapping(final HandlerMapping handlerMapping) {
        handlerMappingRegistry.addHandlerMapping(handlerMapping);
    }

    @Override
    public void init() {
        // HandlerMapping Initialize
        final ManualHandlerMapping manualHandlerMapping = new ManualHandlerMapping();
        final AnnotationHandlerMapping annotationHandlerMapping = new AnnotationHandlerMapping();
        manualHandlerMapping.initialize();
        annotationHandlerMapping.initialize();
        addHandlerMapping(manualHandlerMapping);
        addHandlerMapping(annotationHandlerMapping);

        // HandlerAdapter Initialize
        addHandlerAdapter(new ManualHandlerAdapter());
        addHandlerAdapter(new AnnotationHandlerAdapter());
    }

    @Override
    protected void service(
            final HttpServletRequest request,
            final HttpServletResponse response
    ) throws ServletException {
        log.debug("Method : {}, Request URI : {}", request.getMethod(), request.getRequestURI());

        try {
            final var handler = handlerMappingRegistry.getHandler(request);
            final HandlerAdapter handlerAdapter = handlerAdapterRegistry.getHandlerAdapter(handler);
            final ModelAndView modelAndView = handlerAdapter.handle(request, response, handler);
            modelAndView.render(request, response);
        } catch (Throwable e) {
            log.error("Exception : {}", e.getMessage(), e);
            throw new ServletException(e.getMessage());
        }
    }
}
