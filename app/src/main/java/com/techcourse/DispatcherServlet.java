package com.techcourse;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.mvc.asis.Controller;
import com.interface21.webmvc.servlet.mvc.tobe.AnnotationHandlerMapping;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerExecution;
import com.interface21.webmvc.servlet.view.JspView;

public class DispatcherServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(DispatcherServlet.class);
    private static final String BASE_PACKAGE = "com.techcourse.controller";

    private HandlerManager handlerManager;

    public DispatcherServlet() {
    }

    @Override
    public void init() {
        handlerManager = new HandlerManager(
                new ManualHandlerMapping(),
                new AnnotationHandlerMapping(BASE_PACKAGE)
        );
        handlerManager.initialize();
    }

    @Override
    protected void service(final HttpServletRequest request, final HttpServletResponse response) {
        final String requestURI = request.getRequestURI();
        log.debug("Method : {}, Request URI : {}", request.getMethod(), requestURI);

        final Object handler = handlerManager.findHandler(request);
        final ModelAndView modelAndView = processHandler(request, response, handler);
        try {
            modelAndView.render(request, response);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private ModelAndView processHandler(final HttpServletRequest request, final HttpServletResponse response,
                                        final Object handler) {
        if (handler instanceof Controller) {
            return processController(request, response, (Controller) handler);
        }
        return processHandlerExecute(request, response, (HandlerExecution) handler);
    }

    private ModelAndView processController(final HttpServletRequest request, final HttpServletResponse response,
                                           final Controller handler) {
        try {
            final String viewName = handler.execute(request, response);
            return new ModelAndView(new JspView(viewName));
        } catch (final Exception e) {
            throw new RuntimeException(e);
        }
    }

    private ModelAndView processHandlerExecute(final HttpServletRequest request, final HttpServletResponse response,
                                               final HandlerExecution handler) {
        try {
            return handler.handle(request, response);
        } catch (final Exception e) {
            throw new RuntimeException(e);
        }
    }
}
