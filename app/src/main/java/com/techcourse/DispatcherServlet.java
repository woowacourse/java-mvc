package com.techcourse;

import com.techcourse.support.mvc.adapter.HandlerAdapters;
import com.techcourse.support.mvc.handler.HandlerMappers;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webmvc.org.springframework.web.servlet.ModelAndView;
import webmvc.org.springframework.web.servlet.mvc.tobe.adapter.HandlerAdapter;
import webmvc.org.springframework.web.servlet.mvc.tobe.handler.HandlerMapper;
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
        handlerMappers.init();
    }

    protected void addHandlerMapper(final HandlerMapper handlerMapper) {
        handlerMappers.addHandlerMapper(handlerMapper);
    }

    protected void addHandlerAdapter(final HandlerAdapter handlerAdapter) {
        handlerAdapters.addHandlerAdapter(handlerAdapter);
    }

    @Override
    protected void service(final HttpServletRequest request, final HttpServletResponse response)
            throws ServletException {
        log.debug("Method : {}, Request URI : {}", request.getMethod(), request.getRequestURI());
        try {
            Object handler = handlerMappers.getHandler(request);
            HandlerAdapter handlerAdapter = handlerAdapters.getHandlerAdapter(handler);
            ModelAndView modelAndView = handlerAdapter.handle(request, response, handler);

            JspView view = (JspView) modelAndView.getView();
            move(view.getViewName(), request, response);
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
