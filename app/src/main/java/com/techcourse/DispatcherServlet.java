package com.techcourse;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webmvc.org.springframework.web.servlet.ModelAndView;
import webmvc.org.springframework.web.servlet.View;
import webmvc.org.springframework.web.servlet.mvc.tobe.HandlerAdapter;
import webmvc.org.springframework.web.servlet.mvc.tobe.HandlerExecution;
import webmvc.org.springframework.web.servlet.view.JspView;

import java.io.IOException;

public class DispatcherServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(DispatcherServlet.class);

    private HandlerMappings handlerMappings;
    private HandlerAdapters handlerAdapters;

    public DispatcherServlet() {
    }

    @Override
    public void init() {
        this.handlerMappings = new HandlerMappings();
        this.handlerAdapters = new HandlerAdapters();

        handlerMappings.init();
        handlerAdapters.init();
    }


    @Override
    protected void service(final HttpServletRequest request, final HttpServletResponse response) {
        log.debug("Method : {}, Request URI : {}", request.getMethod(), request.getRequestURI());
        try {
            final HandlerExecution mappedHandler = handlerMappings.getHandler(request);
            final HandlerAdapter handlerAdapter = handlerAdapters.getHandlerAdapter(mappedHandler.getMethod());

            final ModelAndView modelAndView = handlerAdapter.handle(request, response, mappedHandler);
            final View view = modelAndView.getView();
            move(view.getName(), request, response);
        } catch (Exception e) {
            log.error("exception occurred : {}", e.getMessage(), e);
        } catch (Error e) {
            log.error("error occurred : {}", e.getMessage(), e);
        }
    }

    private void move(final String viewName, final HttpServletRequest request,
                      final HttpServletResponse response) {
        try {
            if (viewName.startsWith(JspView.REDIRECT_PREFIX)) {
                response.sendRedirect(viewName.substring(JspView.REDIRECT_PREFIX.length()));
                return;
            }
            final var requestDispatcher = request.getRequestDispatcher(viewName);
            requestDispatcher.forward(request, response);
        } catch (ServletException | IOException e) {
            throw new RenderingException(e);
        }
    }
}
