package com.interface21.webmvc.servlet.mvc.servlet;

import com.interface21.webmvc.servlet.mvc.asis.ManualHandlerMapping;
import com.interface21.webmvc.servlet.view.ModelAndView;
import com.interface21.webmvc.servlet.mvc.adapter.ControllerAdapter;
import com.interface21.webmvc.servlet.mvc.adapter.HandlerAdapter;
import com.interface21.webmvc.servlet.mvc.adapter.HandlerExecutionAdapter;
import com.interface21.webmvc.servlet.mvc.mapping.AnnotationHandlerMapping;
import com.interface21.webmvc.servlet.mvc.adapter.HandlerAdapterRegistry;
import com.interface21.webmvc.servlet.mvc.mapping.HandlerMapping;
import com.interface21.webmvc.servlet.mvc.mapping.HandlerMappingRegistry;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DispatcherServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(DispatcherServlet.class);

    private final Object[] basePackage;
    private final HandlerMappingRegistry handlerMappingRegistry = new HandlerMappingRegistry();
    private final HandlerAdapterRegistry handlerAdapterRegistry = new HandlerAdapterRegistry();

    public DispatcherServlet(Object... basePackage) {
        this.basePackage = basePackage;

    }

    @Override
    public void init() {
        initializeHandlerMappingRegistry();
        initializeHandlerAdapterRegistry();
    }

    private void initializeHandlerMappingRegistry() {
        final List<HandlerMapping> mappings = List.of(
                new ManualHandlerMapping(),
                new AnnotationHandlerMapping(basePackage)
        );

        mappings.forEach(mapping -> {
            mapping.initialize();
            handlerMappingRegistry.registerMapping(mapping);
        });
    }

    private void initializeHandlerAdapterRegistry() {
        final List<HandlerAdapter> adapters = List.of(
                new ControllerAdapter(),
                new HandlerExecutionAdapter()
        );

        adapters.forEach(adapter -> {
            handlerAdapterRegistry.registerAdapter(adapter);
        });
    }

    @Override
    protected void service(final HttpServletRequest request, final HttpServletResponse response) throws ServletException {
        final String requestURI = request.getRequestURI();
        log.info("Method : {}, Request URI : {}", request.getMethod(), requestURI);

        try {
            final Object handler = handlerMappingRegistry.getHandler(request);
            final HandlerAdapter adapter = handlerAdapterRegistry.getHandlerAdapter(handler);
            final ModelAndView modelAndView = adapter.handle(request, response, handler);

            modelAndView.render(request, response);
        } catch (Throwable e) {
            log.error("Exception : {}", e.getMessage(), e);
            throw new ServletException(e.getMessage());
        }
    }
}
