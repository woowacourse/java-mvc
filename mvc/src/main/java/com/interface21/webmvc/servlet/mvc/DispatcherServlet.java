package com.interface21.webmvc.servlet.mvc;

import com.interface21.web.servlet.HandlerAdaptor;
import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.View;
import com.interface21.webmvc.servlet.mvc.tobe.AnnotationHandlerMapping;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerAdaptorRegistry;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerExecutionAdaptor;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerMappingRegistry;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DispatcherServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(DispatcherServlet.class);

    private HandlerAdaptorRegistry handlerAdaptorRegistry;
    private HandlerMappingRegistry handlerMappingRegistry;

    public DispatcherServlet() {
    }

    @Override
    public void init() {
        handlerAdaptorRegistry = new HandlerAdaptorRegistry();
        handlerMappingRegistry = new HandlerMappingRegistry();

        addHandlerAdaptors();
        addHandlerMappings();
    }

    private void addHandlerAdaptors() {
        handlerAdaptorRegistry.addHandlerAdaptors(new HandlerExecutionAdaptor());
    }

    private void addHandlerMappings() {
        handlerMappingRegistry.addHandlerMappings(new AnnotationHandlerMapping());
    }

    @Override
    protected void service(final HttpServletRequest request, final HttpServletResponse response) throws ServletException {
        try {
            Object handler = handlerMappingRegistry.getHandler(request);
            HandlerAdaptor handlerAdaptor = handlerAdaptorRegistry.getHandlerAdaptors(handler);

            ModelAndView modelAndView = handlerAdaptor.handle(request, response, handler);
            render(modelAndView, request, response);
        } catch (Throwable e) {
            log.error("Exception : {}", e.getMessage(), e);
            throw new ServletException(e.getMessage());
        }
    }

    private void render(ModelAndView modelAndView, HttpServletRequest request, HttpServletResponse response) throws Exception {
        View view = modelAndView.getView();
        view.render(modelAndView.getModel(), request, response);
    }
}
