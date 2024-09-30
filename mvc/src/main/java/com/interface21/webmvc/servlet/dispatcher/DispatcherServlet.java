package com.interface21.webmvc.servlet.dispatcher;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.adapter.HandlerAdapterRegistry;
import com.interface21.webmvc.servlet.mapping.HandlerMappingRegistry;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerMapping;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class DispatcherServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(DispatcherServlet.class);

    private Object basePackages;
    private HandlerMappingRegistry handlerMappingRegistry;
    private HandlerAdapterRegistry handlerAdapterRegistry;

    public DispatcherServlet(Object basePackages) {
        this.basePackages = basePackages;
    }

    @Override
    public void init() {
        this.handlerMappingRegistry = new HandlerMappingRegistry(basePackages);
        this.handlerAdapterRegistry = new HandlerAdapterRegistry();
    }

    @Override
    protected void service(final HttpServletRequest request, final HttpServletResponse response) throws ServletException {
        final String requestURI = request.getRequestURI();
        log.debug("Method : {}, Request URI : {}", request.getMethod(), requestURI);

        HandlerMapping handlerMapping = handlerMappingRegistry.getHandlerMapping(request);
        Object handler = handlerMapping.getHandler(request);

        try {
            ModelAndView mav = handlerAdapterRegistry.getHandlerAdapter(handler).adapt(handler, request, response);
            mav.getView().render(mav.getModel(), request, response);
        } catch (Exception e) {
            log.error("Exception : {}", e.getMessage(), e);
            throw new ServletException(e.getMessage());
        }
    }
}
