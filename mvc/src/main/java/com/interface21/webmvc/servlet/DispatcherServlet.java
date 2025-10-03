package com.interface21.webmvc.servlet;

import com.interface21.webmvc.servlet.mvc.adaptor.HandlerAdaptor;
import com.interface21.webmvc.servlet.mvc.mapping.HandlerMapping;
import com.interface21.webmvc.servlet.mvc.registry.HandlerAdaptorRegistry;
import com.interface21.webmvc.servlet.mvc.registry.HandlerMappingRegistry;
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

    private final HandlerMappingRegistry handlerMappingRegistry;
    private final HandlerAdaptorRegistry handlerAdaptorRegistry;

    public DispatcherServlet(final List<HandlerMapping> handlerMappings, final List<HandlerAdaptor> handlerAdaptors) {
        this.handlerMappingRegistry = new HandlerMappingRegistry(handlerMappings);
        this.handlerAdaptorRegistry = new HandlerAdaptorRegistry(handlerAdaptors);
    }

    @Override
    public void init() {
        handlerMappingRegistry.initialize();
    }

    @Override
    protected void service(final HttpServletRequest request, final HttpServletResponse response)
            throws ServletException {
        final String requestURI = request.getRequestURI();
        log.debug("Method : {}, Request URI : {}", request.getMethod(), requestURI);
        try {
            render(request, response);
        } catch (Exception e) {
            log.error("Exception : {}", e.getMessage(), e);
            throw new ServletException(e.getMessage());
        }
    }

    private void render(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Object handler = handlerMappingRegistry.getHandler(request);
        HandlerAdaptor handlerAdaptor = handlerAdaptorRegistry.getHandlerAdaptor(handler);

        ModelAndView modelAndView = handlerAdaptor.handle(request, response, handler);
        modelAndView.render(request, response);
    }
}
