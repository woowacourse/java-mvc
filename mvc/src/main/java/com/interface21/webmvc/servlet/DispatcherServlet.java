package com.interface21.webmvc.servlet;

import com.interface21.webmvc.handleradapter.AnnotationHandlerAdapter;
import com.interface21.webmvc.handleradapter.HandlerAdapter;
import com.interface21.webmvc.handleradapter.HandlerAdapterRegistry;
import com.interface21.webmvc.handlermapping.ControllerScanner;
import com.interface21.webmvc.handlermapping.HandlerMappingRegistry;
import com.interface21.webmvc.handlermapping.annotation.AnnotationHandlerMapping;
import com.interface21.webmvc.handlermapping.annotation.HandlerMapping;
import com.interface21.webmvc.view.ModelAndView;
import com.interface21.webmvc.view.View;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.interface21.webmvc.view.JspView;

import java.util.Map;

public class DispatcherServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(DispatcherServlet.class);

    private HandlerMappingRegistry handlerMappings;
    private HandlerAdapterRegistry handlerAdapters;

    public DispatcherServlet() {
        this.handlerMappings = new HandlerMappingRegistry();
        this.handlerAdapters = new HandlerAdapterRegistry();
    }

    @Override
    public void init() {
        if (handlerMappings == null) {
            this.handlerMappings = new HandlerMappingRegistry();
        }
        if (handlerAdapters == null) {
            this.handlerAdapters = new HandlerAdapterRegistry();
        }
    }

    public void addHandlerMapping(final HandlerMapping handlerMapping) {
        handlerMappings.addMapping(handlerMapping);
    }

    public void addHandlerAdapter(final HandlerAdapter handlerAdapter) {
        handlerAdapters.addAdapter(handlerAdapter);
    }

    @Override
    protected void service(final HttpServletRequest request, final HttpServletResponse response) throws ServletException {
        final String requestURI = request.getRequestURI();
        log.debug("Method : {}, Request URI : {}", request.getMethod(), requestURI);

        try {
            ModelAndView modelAndView = handle(request, response);
            move(modelAndView, request, response);
        } catch (Throwable e) {
            log.error("Exception : {}", e.getMessage());
        }
    }

    private ModelAndView handle(HttpServletRequest request, HttpServletResponse response) {
        try {
            Object handler = handlerMappings.getHandler(request);
            HandlerAdapter handlerAdapter = handlerAdapters.getHandlerAdapter(handler);
            return handlerAdapter.handle(handler, request, response);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void move(final ModelAndView modelAndView, final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        View view = modelAndView.getView();

        Map<String, Object> model = modelAndView.getModel();
        view.render(model, request, response);
    }
}
