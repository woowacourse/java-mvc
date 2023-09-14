package com.techcourse;

import com.techcourse.support.HandlerAdapters;
import com.techcourse.support.HandlerExceptionResolvers;
import com.techcourse.support.HandlerMappings;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webmvc.org.springframework.web.servlet.ModelAndView;
import webmvc.org.springframework.web.servlet.View;
import webmvc.org.springframework.web.servlet.mvc.HandlerAdapter;
import webmvc.org.springframework.web.servlet.mvc.HandlerExceptionResolver;
import webmvc.org.springframework.web.servlet.mvc.HandlerMapping;

public class DispatcherServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(DispatcherServlet.class);

    private final transient HandlerMappings handlerMappings = new HandlerMappings();
    private final transient HandlerAdapters handlerAdapters = new HandlerAdapters();
    private final transient HandlerExceptionResolvers handlerExceptionResolvers = new HandlerExceptionResolvers();

    @Override
    public void init() {
        handlerMappings.initialize();
    }

    public void addHandlerMapping(HandlerMapping handlerMapping) {
        handlerMappings.addHandlerMapping(handlerMapping);
    }

    public void addHandlerAdapter(HandlerAdapter handlerAdapter) {
        handlerAdapters.addHandlerAdapter(handlerAdapter);
    }

    public void addHandlerExceptionResolvers(HandlerExceptionResolver handlerExceptionResolver) {
        handlerExceptionResolvers.addHandlerExceptionResolver(handlerExceptionResolver);
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse res) throws ServletException {
        log.debug("Method : {}, Request URI : {}", req.getMethod(), req.getRequestURI());
        try {
            Object handler = handlerMappings.getHandler(req);
            HandlerAdapter adapter = handlerAdapters.getHandlerAdapter(handler);
            ModelAndView modelAndView = adapter.handle(req, res, handler);
            View view = modelAndView.getView();
            view.render(modelAndView.getModel(), req, res);
        } catch (Exception e) {
            resolveException(req, res, e);
        }
    }

    private void resolveException(HttpServletRequest req, HttpServletResponse res, Exception ex)
        throws ServletException {
        try {
            HandlerExceptionResolver handlerExceptionResolver = handlerExceptionResolvers.getExceptionResolver(ex);
            ModelAndView modelAndView = handlerExceptionResolver.resolveException(req, res, ex);
            View view = modelAndView.getView();
            view.render(modelAndView.getModel(), req, res);
        } catch (Exception e) {
            log.error("Exception : {}", e.getMessage(), e);
            throw new ServletException(e.getMessage());
        }
    }
}
