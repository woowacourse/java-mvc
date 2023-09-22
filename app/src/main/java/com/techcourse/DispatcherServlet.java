package com.techcourse;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webmvc.org.springframework.web.servlet.mvc.asis.Controller;
import webmvc.org.springframework.web.servlet.mvc.tobe.AnnotationHandlerMapping;
import webmvc.org.springframework.web.servlet.mvc.tobe.HandlerExecution;
import webmvc.org.springframework.web.servlet.mvc.tobe.HandlerMapping;
import webmvc.org.springframework.web.servlet.view.JspView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class DispatcherServlet extends HttpServlet {

    public static final String BASE_PACKAGE = "com.techcourse.controller";
    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(DispatcherServlet.class);


    private final List<HandlerMapping> handlerMappings = new ArrayList<>();

    public DispatcherServlet() {
    }

    @Override
    public void init() {
        handlerMappings.add(new ManualHandlerMapping());
        handlerMappings.add(new AnnotationHandlerMapping(BASE_PACKAGE));
        handlerMappings.forEach(HandlerMapping::initialize);
    }

    @Override
    protected void service(final HttpServletRequest request, final HttpServletResponse response) throws ServletException {
        final String requestURI = request.getRequestURI();
        log.info("Method : {}, Request URI : {}", request.getMethod(), requestURI);

        try {
            final Optional<Object> optionalHandler = handlerMappings.stream()
                    .map(handlerMapping -> handlerMapping.getHandler(request))
                    .filter(Objects::nonNull)
                    .findFirst();

            optionalHandler.ifPresentOrElse(handler -> handle(request, response, handler), () -> {
                throw new IllegalArgumentException("Not found handler for request URI : " + requestURI);
            });
        } catch (Throwable e) {
            log.error("Exception : {}", e.getMessage(), e);
            throw new ServletException(e.getMessage());
        }
    }

    private void handle(final HttpServletRequest request, final HttpServletResponse response, final Object handler) {
        if (handler instanceof Controller) {
            handleController(request, response, (Controller) handler);
            return;
        }
        if (handler instanceof HandlerExecution) {
            handleException(request, response, (HandlerExecution) handler);
            return;
        }
        throw new IllegalArgumentException("Not supported handler type");
    }

    private void handleException(final HttpServletRequest request, final HttpServletResponse response, final HandlerExecution handler) {
        try {
            final var modelAndView = handler.handle(request, response);
            move(modelAndView.getViewName(), request, response);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void handleController(final HttpServletRequest request, final HttpServletResponse response, final Controller handler) {
        try {
            final String viewName = handler.execute(request, response);
            move(viewName, request, response);
        } catch (Exception e) {
            throw new RuntimeException(e);
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
