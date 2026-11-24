package com.techcourse;

import com.interface21.webmvc.servlet.View;
import com.interface21.webmvc.servlet.mvc.AnnotationHandlerAdapter;
import com.interface21.webmvc.servlet.mvc.HandlerAdapter;
import com.interface21.webmvc.servlet.mvc.HandlerMapping;
import com.interface21.webmvc.servlet.mvc.tobe.AnnotationHandlerMapping;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Optional;

public class DispatcherServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(DispatcherServlet.class);

    private final HandlerAdapter handlerAdapter;
    private final HandlerMapping handlerMapping;

    public DispatcherServlet() {
        this.handlerAdapter = new AnnotationHandlerAdapter();
        this.handlerMapping = new AnnotationHandlerMapping("com.techcourse.controller");
    }

    @Override
    public void init() {
        handlerMapping.initialize();
    }

    @Override
    protected void service(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException {
        log.debug("Method : {}, Request URI : {}", request.getMethod(), request.getRequestURI());
        final Optional<Object> handlerResult = findHandler(request);
        if (handlerResult.isEmpty()) {
            response.sendError(404);
            return;
        }
        try {
            final var handler = handlerResult.get();
            final var modelAndView = handlerAdapter.handle(request, response, handler);
            final View view = modelAndView.getView();
            view.render(modelAndView.getModel(), request, response);
        } catch (Exception e) {
            log.error("Exception : {}", e.getMessage(), e);
            throw new ServletException(e.getMessage());
        }
    }

    private Optional<Object> findHandler(final HttpServletRequest request) {
        final Object handler = handlerMapping.getHandler(request);
        if (handler == null) {
            return Optional.empty();
        }
        return Optional.of(handler);
    }
}
