package com.techcourse;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webmvc.org.springframework.web.servlet.ModelAndView;
import webmvc.org.springframework.web.servlet.View;
import webmvc.org.springframework.web.servlet.mvc.asis.Controller;
import webmvc.org.springframework.web.servlet.mvc.tobe.HandlerExecution;
import webmvc.org.springframework.web.servlet.view.JspView;

public class DispatcherServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(DispatcherServlet.class);

    private transient HandlerAdaptor handlerAdaptor;

    public DispatcherServlet() {
    }

    @Override
    public void init() {
        handlerAdaptor = new HandlerAdaptor();
        handlerAdaptor.initialize();
    }

    @Override
    protected void service(final HttpServletRequest request, final HttpServletResponse response) throws ServletException {
        final String requestURI = request.getRequestURI();
        log.debug("Method : {}, Request URI : {}", request.getMethod(), requestURI);

        final Object handler = handlerAdaptor.getHandler(request);

        if (handler instanceof Controller) {
            try {
                final String viewName = ((Controller) handler).execute(request, response);
                move(viewName, request, response);
            } catch (Exception e) {
                log.error("Exception : {}", e.getMessage(), e);
                throw new ServletException(e.getMessage());
            }
        }

        if (handler instanceof HandlerExecution) {
            try {
                final ModelAndView modelAndView = ((HandlerExecution) handler).handle(request, response);
                final View view = modelAndView.getView();
                view.render(modelAndView.getModel(), request, response);
            } catch (Throwable e) {
                log.error("Exception : {}", e.getMessage(), e);
                throw new ServletException(e.getMessage());
            }
        }
    }

    private void move(final String viewName, final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        if (viewName.startsWith(JspView.REDIRECT_PREFIX)) {
            response.sendRedirect(viewName.substring(JspView.REDIRECT_PREFIX.length()));
            return;
        }

        final var requestDispatcher = request.getRequestDispatcher(viewName);
        requestDispatcher.forward(request, response);
    }
}
