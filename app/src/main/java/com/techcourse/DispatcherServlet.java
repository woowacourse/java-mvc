package com.techcourse;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webmvc.org.springframework.web.servlet.ModelAndView;
import webmvc.org.springframework.web.servlet.mvc.tobe.handler.adapter.AnnotationHandlerAdapter;
import webmvc.org.springframework.web.servlet.mvc.tobe.handler.adapter.HandlerAdapter;
import webmvc.org.springframework.web.servlet.mvc.tobe.handler.adapter.HandlerAdapters;
import webmvc.org.springframework.web.servlet.mvc.tobe.handler.adapter.ManualHandlerAdapter;
import webmvc.org.springframework.web.servlet.mvc.tobe.handler.mapper.AnnotationHandlerMapping;
import webmvc.org.springframework.web.servlet.mvc.tobe.handler.mapper.HandlerMappers;
import webmvc.org.springframework.web.servlet.view.JspView;

public class DispatcherServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(DispatcherServlet.class);

    private final HandlerMappers handlerMappers;
    private final HandlerAdapters handlerAdapters;

    public DispatcherServlet() {
        handlerMappers = new HandlerMappers();
        handlerAdapters = new HandlerAdapters();
    }

    @Override
    public void init() {
        setupManualHandler();
        setupAnnotationHandler();
        handlerMappers.init();
    }

    private void setupManualHandler() {
        handlerMappers.addHandlerMapping(new ManualHandlerMapping());
        handlerAdapters.addHandlerAdapter(new ManualHandlerAdapter());
    }

    private void setupAnnotationHandler() {
        handlerMappers.addHandlerMapping(new AnnotationHandlerMapping("com.techcourse"));
        handlerAdapters.addHandlerAdapter(new AnnotationHandlerAdapter());
    }

    @Override
    protected void service(final HttpServletRequest request, final HttpServletResponse response) throws ServletException {
        String requestURI = request.getRequestURI();
        log.debug("Method : {}, Request URI : {}", request.getMethod(), requestURI);

        try {
            Object handler = handlerMappers.getHandler(request);
            HandlerAdapter adapter = handlerAdapters.getHandlerAdapter(handler);
            Object viewName = adapter.execute(request, response, handler);

            if (viewName instanceof ModelAndView) {
                JspView view = (JspView) ((ModelAndView) viewName).getView();
                viewName = view.getViewName();
            }

            move((String) viewName, request, response);
        } catch (Throwable e) {
            log.error("Exception : {}", e.getMessage(), e);
            throw new ServletException(e.getMessage());
        }
    }

    private void move(final String viewName, final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        if (viewName.startsWith(JspView.REDIRECT_PREFIX)) {
            response.sendRedirect(viewName.substring(JspView.REDIRECT_PREFIX.length()));
            return;
        }

        RequestDispatcher requestDispatcher = request.getRequestDispatcher(viewName);
        requestDispatcher.forward(request, response);
    }
}
