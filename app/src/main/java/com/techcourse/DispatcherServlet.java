package com.techcourse;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webmvc.org.springframework.web.servlet.ModelAndView;
import webmvc.org.springframework.web.servlet.View;
import webmvc.org.springframework.web.servlet.mvc.HandlerAdapter;
import webmvc.org.springframework.web.servlet.mvc.HandlerAdapterRegistry;
import webmvc.org.springframework.web.servlet.mvc.HandlerMappingRegistry;
import webmvc.org.springframework.web.servlet.mvc.asis.ManualHandlerAdapter;
import webmvc.org.springframework.web.servlet.mvc.tobe.AnnotationHandlerAdapter;
import webmvc.org.springframework.web.servlet.mvc.tobe.AnnotationHandlerMapping;
import webmvc.org.springframework.web.servlet.view.JspView;

import java.util.Optional;

public class DispatcherServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(DispatcherServlet.class);

    private final HandlerMappingRegistry handlerMappings;
    private final HandlerAdapterRegistry handlerAdapters;

    public DispatcherServlet() {
        handlerMappings = new HandlerMappingRegistry();
        handlerAdapters = new HandlerAdapterRegistry();
    }

    @Override
    public void init() {
        handlerMappings.addHandlerMapping(new AnnotationHandlerMapping("com.techcourse"));
        handlerMappings.addHandlerMapping(new ManualHandlerMapping());

        handlerAdapters.addHandlerAdapter(new AnnotationHandlerAdapter());
        handlerAdapters.addHandlerAdapter(new ManualHandlerAdapter());
    }

    @Override
    protected void service(final HttpServletRequest request, final HttpServletResponse response) throws ServletException {
        final String requestURI = request.getRequestURI();
        log.debug("Method : {}, Request URI : {}", request.getMethod(), requestURI);

        try {
            final Optional<Object> handler = handlerMappings.getHandler(request);
            if (handler.isEmpty()) {
                // handler is null when no handler is found
            }

            final HandlerAdapter handlerAdapter = handlerAdapters.getHandlerAdapter(handler);
            final Object result = handlerAdapter.handle(request, response, handler);
            processResult(result, request, response);
        } catch (Throwable e) {
            log.error("Exception : {}", e.getMessage(), e);
            throw new ServletException(e.getMessage());
        }
    }

    private void processResult(final Object result, final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        if (result instanceof ModelAndView) {
            final View view = ((ModelAndView) result).getView();
            if (view instanceof JspView) {
                move(((JspView) view).getViewName(), request, response);
            }
            // TODO: jsonView
        }
        if (result instanceof String) {
            move((String) result, request, response);
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
