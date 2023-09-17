package com.techcourse;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webmvc.org.springframework.web.servlet.ModelAndView;
import webmvc.org.springframework.web.servlet.mvc.tobe.AnnotationHandlerAdaptor;
import webmvc.org.springframework.web.servlet.mvc.tobe.AnnotationHandlerMapping;
import webmvc.org.springframework.web.servlet.mvc.tobe.HandlerAdaptor;
import webmvc.org.springframework.web.servlet.mvc.tobe.HandlerAdaptorFinder;
import webmvc.org.springframework.web.servlet.mvc.tobe.HandlerMapping;
import webmvc.org.springframework.web.servlet.mvc.tobe.HandlerMappings;
import webmvc.org.springframework.web.servlet.view.JspView;

import java.util.List;

public class DispatcherServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(DispatcherServlet.class);

    private HandlerMapping handlerMapping;
    private HandlerAdaptorFinder handlerAdaptorFinder;

    public DispatcherServlet() {
    }

    @Override
    public void init() {
        initHandlerMapping();
        initHandlerAdaptorFinder();
    }

    private void initHandlerMapping() {
        handlerMapping = new HandlerMappings(
            List.of(new AnnotationHandlerMapping(Application.class.getPackageName() + ".*"),
                new ManualHandlerMapping()));
        handlerMapping.initialize();
    }

    private void initHandlerAdaptorFinder() {
        handlerAdaptorFinder = new HandlerAdaptorFinder(List.of(new AnnotationHandlerAdaptor(), new ManualHandlerAdaptor()));
    }

    @Override
    protected void service(final HttpServletRequest request, final HttpServletResponse response) throws ServletException {
        final String requestURI = request.getRequestURI();
        log.debug("Method : {}, Request URI : {}", request.getMethod(), requestURI);

        try {
            final Object handler = handlerMapping.getHandler(request);
            final HandlerAdaptor handlerAdaptor = handlerAdaptorFinder.find(handler);
            final ModelAndView modelAndView = handlerAdaptor.handle(request, response, handler);
            move(modelAndView, request, response);
        } catch (Throwable e) {
            log.error("Exception : {}", e.getMessage(), e);
            throw new ServletException(e.getMessage());
        }
    }

    private void move(final ModelAndView modelAndView, final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        final String viewName = modelAndView.getView().getName();
        if (viewName.startsWith(JspView.REDIRECT_PREFIX)) {
            response.sendRedirect(viewName.substring(JspView.REDIRECT_PREFIX.length()));
            return;
        }

        final var requestDispatcher = request.getRequestDispatcher(viewName);
        requestDispatcher.forward(request, response);
    }
}
