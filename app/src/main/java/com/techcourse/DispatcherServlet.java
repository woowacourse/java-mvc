package com.techcourse;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webmvc.org.springframework.web.servlet.ModelAndView;
import webmvc.org.springframework.web.servlet.View;
import webmvc.org.springframework.web.servlet.mvc.HandlerAdapter;
import webmvc.org.springframework.web.servlet.mvc.HandlerExceptionResolver;
import webmvc.org.springframework.web.servlet.mvc.HandlerMapping;
import webmvc.org.springframework.web.servlet.mvc.exception.HandlerNotFoundException;

public class DispatcherServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(DispatcherServlet.class);

    private final List<HandlerMapping> handlerMappings = new ArrayList<>();
    private final List<HandlerAdapter> handlerAdapters = new ArrayList<>();
    private final List<HandlerExceptionResolver> handlerExceptionResolvers = new ArrayList<>();

    public DispatcherServlet() {
    }

    @Override
    public void init() {
        handlerMappings.forEach(HandlerMapping::initialize);
    }

    public void addHandlerMapping(HandlerMapping handlerMapping) {
        handlerMappings.add(handlerMapping);
    }

    public void addHandlerAdapter(HandlerAdapter handlerAdapter) {
        handlerAdapters.add(handlerAdapter);
    }

    public void addHandlerExceptionResolvers(HandlerExceptionResolver handlerExceptionResolver) {
        handlerExceptionResolvers.add(handlerExceptionResolver);
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse res) throws ServletException {
        log.debug("Method : {}, Request URI : {}", req.getMethod(), req.getRequestURI());
        try {
            Object handler = getHandler(req);
            HandlerAdapter adapter = getHandlerAdapter(handler);
            ModelAndView modelAndView = adapter.handle(req, res, handler);
            View view = modelAndView.getView();
            view.render(modelAndView.getModel(), req, res);
        } catch (Exception e) {
            resolveException(req, res, e);
        }
    }

    private Object getHandler(HttpServletRequest request) {
        return handlerMappings.stream()
            .map(handlerMapping -> handlerMapping.getHandler(request))
            .filter(Objects::nonNull)
            .findAny()
            .orElseThrow(() -> {
                String uri = request.getRequestURI();
                String method = request.getMethod();
                throw new HandlerNotFoundException("handler not found! uri: " + uri + ", method: " + method);
            });
    }

    private HandlerAdapter getHandlerAdapter(Object handler) throws ServletException {
        return handlerAdapters.stream()
            .filter(adapter -> adapter.supports(handler))
            .findAny()
            .orElseThrow(() -> new ServletException("handler adapter not found! handler: " + handler));
    }

    private void resolveException(HttpServletRequest req, HttpServletResponse res, Exception ex)
        throws ServletException {
        for (HandlerExceptionResolver handlerExceptionResolver : handlerExceptionResolvers) {
            try {
                ModelAndView modelAndView = handlerExceptionResolver.resolveException(req, res, ex);
                View view = modelAndView.getView();
                view.render(modelAndView.getModel(), req, res);
            } catch (Exception e) {
                log.error("Exception : {}", e.getMessage(), e);
                throw new ServletException(e.getMessage());
            }
        }
    }
}
