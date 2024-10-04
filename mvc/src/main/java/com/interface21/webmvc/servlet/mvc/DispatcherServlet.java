package com.interface21.webmvc.servlet.mvc;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.View;

public class DispatcherServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(DispatcherServlet.class);

    private final String basePackage;
    private HandlerMappingRegistry handlerMappingRegistry;
    private HandlerAdapterRegistry handlerAdapterRegistry;

    public DispatcherServlet(final String basePackage) {
        this.basePackage = basePackage;
    }

    @Override
    public void init() {
        handlerMappingRegistry = new HandlerMappingRegistry();
        try {
            handlerMappingRegistry.addHandlerMapping(new AnnotationHandlerMapping(basePackage));
        } catch (Exception e) {
            throw new RuntimeException("HandlerMapping 초기화 실패 : {}", e);
        }
        handlerAdapterRegistry = new HandlerAdapterRegistry(List.of(new AnnotationHandlerAdapter()));
    }

    @Override
    protected void service(final HttpServletRequest request, final HttpServletResponse response)
            throws ServletException {
        final String requestURI = request.getRequestURI();
        log.debug("Method : {}, Request URI : {}", request.getMethod(), requestURI);
        try {
            final Object controller = handlerMappingRegistry.getHandler(request);
            final HandlerAdapter handlerAdapter = handlerAdapterRegistry.getHandlerAdapter(controller);
            final ModelAndView modelAndView = handlerAdapter.handle(controller, request, response);
            render(modelAndView, request, response);
        } catch (Throwable e) {
            log.error("service Exception : {}", e.getMessage(), e);
            throw new ServletException(e.getMessage());
        }
    }

    private void render(final ModelAndView modelAndView, final HttpServletRequest request,
                        final HttpServletResponse response) throws Exception {
        final View view = modelAndView.getView();
        final Map<String, ?> model = new HashMap<>();
        view.render(model, request, response);
    }
}
