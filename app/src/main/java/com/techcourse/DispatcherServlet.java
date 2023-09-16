package com.techcourse;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webmvc.org.springframework.web.servlet.mvc.tobe.HandlerAdapter;
import webmvc.org.springframework.web.servlet.mvc.tobe.HandlerMapping;
import webmvc.org.springframework.web.servlet.mvc.tobe.HandlerNotFoundException;
import webmvc.org.springframework.web.servlet.view.JspView;

public class DispatcherServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(DispatcherServlet.class);

    private final List<HandlerMapping> handlerMappings;
    private final List<HandlerAdapter> handlerAdapters;

    public DispatcherServlet(List<HandlerMapping> handlerMappings, List<HandlerAdapter> handlerAdapters) {
        this.handlerMappings = handlerMappings;
        this.handlerAdapters = handlerAdapters;
    }

    @Override
    public void init() {
        handlerMappings.forEach(HandlerMapping::initialize);
    }

    @Override
    protected void service(final HttpServletRequest request, final HttpServletResponse response) throws ServletException {
        log.debug("Method : {}, Request URI : {}", request.getMethod(), request.getRequestURI());
        final var handler = handlerMappings.stream()
                                           .map(mapping -> mapping.getHandler(request))
                                           .filter(Objects::nonNull)
                                           .findAny()
                                           .orElseThrow(() -> new HandlerNotFoundException("Not found handler for request URI : " + request.getRequestURI()));
        final var handlerAdapter = handlerAdapters.stream()
                                                  .filter(adapter -> adapter.supports(handler))
                                                  .findAny()
                                                  .orElseThrow(() -> new HandlerNotFoundException("Not found handler adapter for handler : " + handler));
        try {
            final var modelAndView = handlerAdapter.handle(request, response, handler);
            move(modelAndView.getViewName(), request, response);
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

        final var requestDispatcher = request.getRequestDispatcher(viewName);
        requestDispatcher.forward(request, response);
    }
}
