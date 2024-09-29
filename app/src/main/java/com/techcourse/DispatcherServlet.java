package com.techcourse;

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
import com.interface21.webmvc.servlet.mvc.tobe.AnnotationHandlerMapping;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerAdapter;

public class DispatcherServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(DispatcherServlet.class);

    private HandlerMappingRegistry handlerMappingRegistry;
    private HandlerAdapter handlerAdapter;

    public DispatcherServlet() {
    }

    @Override
    public void init() {
        final ManualHandlerMapping manualHandlerMapping = new ManualHandlerMapping();
        manualHandlerMapping.initialize();
        final AnnotationHandlerMapping annotationHandlerMapping = new AnnotationHandlerMapping();
        try {
            annotationHandlerMapping.initialize();
        } catch (final Exception e) {
            log.warn("DispatcherServlet 초기화 실패 : {}", e.getMessage());
        }
        log.debug("HandlerMapping 초기화 끝");
        handlerMappingRegistry = new HandlerMappingRegistry(List.of(manualHandlerMapping, annotationHandlerMapping));
        handlerAdapter = new HandlerAdapter();
    }

    @Override
    protected void service(final HttpServletRequest request, final HttpServletResponse response)
            throws ServletException {
        final String requestURI = request.getRequestURI();
        log.debug("Method : {}, Request URI : {}", request.getMethod(), requestURI);
        try {
            final Object controller = handlerMappingRegistry.getHandler(request);
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
