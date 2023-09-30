package com.techcourse;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webmvc.org.springframework.web.servlet.ModelAndView;
import webmvc.org.springframework.web.servlet.View;
import webmvc.org.springframework.web.servlet.mvc.tobe.HandlerExecutorComposite;
import webmvc.org.springframework.web.servlet.mvc.tobe.HandlerMappingComposite;

public class DispatcherServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(DispatcherServlet.class);

    private final HandlerMappingComposite handlerMappingComposite;
    private final HandlerExecutorComposite handlerExecutorComposite;

    public DispatcherServlet() {
        this.handlerMappingComposite = new HandlerMappingComposite();
        this.handlerExecutorComposite = new HandlerExecutorComposite();
    }

    @Override
    public void init() {
        handlerMappingComposite.initialize();
    }

    @Override
    protected void service(final HttpServletRequest request, final HttpServletResponse response)
            throws ServletException {
        final String requestURI = request.getRequestURI();
        log.debug("Method : {}, Request URI : {}", request.getMethod(), requestURI);

        try {
            final var handler = handlerMappingComposite.getHandler(request);
            ModelAndView modelAndView = handlerExecutorComposite.handle(request, response, handler);
            View view = modelAndView.getView();
            view.render(modelAndView.getModel(), request, response);
        } catch (Throwable e) {
            log.error("Exception : {}", e.getMessage(), e);
            throw new ServletException(e.getMessage());
        }
    }
}
