package com.techcourse;

import java.util.Map;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.mvc.handler.mapping.AnnotationHandlerMapping;
import com.interface21.webmvc.servlet.mvc.handler.adapter.HandlerAdapter;
import com.interface21.webmvc.servlet.mvc.handler.adapter.HandlerAdapterRegistry;
import com.interface21.webmvc.servlet.mvc.handler.adapter.HandlerExecutionAdapter;
import com.interface21.webmvc.servlet.mvc.handler.mapping.HandlerMapping;
import com.interface21.webmvc.servlet.mvc.handler.mapping.HandlerMappingRegistry;

public class DispatcherServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(DispatcherServlet.class);

    private final HandlerMappingRegistry handlerMappingRegistry = new HandlerMappingRegistry();
    private final HandlerAdapterRegistry handlerAdapterRegistry = new HandlerAdapterRegistry();

    public DispatcherServlet() {
    }

    @Override
    public void init() {
        addHandlerMapping(new AnnotationHandlerMapping());
        addHandlerAdapter(new HandlerExecutionAdapter());
    }

    private void addHandlerMapping(HandlerMapping handlerMapping) {
        handlerMappingRegistry.addHandlerMapping(handlerMapping);
    }

    private void addHandlerAdapter(HandlerAdapter handlerAdapter) {
        handlerAdapterRegistry.addHandlerAdapter(handlerAdapter);
    }

    @Override
    protected void service(final HttpServletRequest request, final HttpServletResponse response) throws ServletException {
        log.debug("Method : {}, Request URI : {}", request.getMethod(), request.getRequestURI());

        try {
            Object handler = handlerMappingRegistry.getHandler(request);
            HandlerAdapter handlerAdapter = handlerAdapterRegistry.getHandlerAdapter(handler);
            ModelAndView modelAndView = handlerAdapter.handle(request, response, handler);
            modelAndView.getView().render(modelAndView.getModel(), request, response);
        } catch (Throwable e) {
            log.error("Exception : {}", e.getMessage(), e);
            throw new ServletException(e.getMessage());
        }
    }
}
