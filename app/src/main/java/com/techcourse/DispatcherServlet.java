package com.techcourse;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webmvc.org.springframework.web.servlet.ModelAndView;
import webmvc.org.springframework.web.servlet.mvc.asis.Controller;
import webmvc.org.springframework.web.servlet.mvc.tobe.HandlerExecutorComposite;
import webmvc.org.springframework.web.servlet.mvc.tobe.HandlerMappingComposite;
import webmvc.org.springframework.web.servlet.view.JspView;

public class DispatcherServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(DispatcherServlet.class);

    private final HandlerMappingComposite handlerMappingComposite;
    private final HandlerExecutorComposite handlerExecutorComposite;

    public DispatcherServlet() {
        this.handlerMappingComposite = new HandlerMappingComposite(new ManualHandlerMapping());
        this.handlerExecutorComposite = new HandlerExecutorComposite(new ControllerInterfaceHandlerExecutor());
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
            final var viewName = modelAndView.getView().viewName();
            move(viewName, request, response);
        } catch (Throwable e) {
            log.error("Exception : {}", e.getMessage(), e);
            throw new ServletException(e.getMessage());
        }
    }

    private void move(final String viewName, final HttpServletRequest request, final HttpServletResponse response)
            throws Exception {
        if (viewName.startsWith(JspView.REDIRECT_PREFIX)) {
            response.sendRedirect(viewName.substring(JspView.REDIRECT_PREFIX.length()));
            return;
        }

        final var requestDispatcher = request.getRequestDispatcher(viewName);
        requestDispatcher.forward(request, response);
    }
}
