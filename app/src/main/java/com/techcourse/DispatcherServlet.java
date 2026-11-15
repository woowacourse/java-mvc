package com.techcourse;

import com.interface21.webmvc.servlet.View;
import com.interface21.webmvc.servlet.mvc.AnnotationHandlerAdapter;
import com.interface21.webmvc.servlet.mvc.HandlerAdapter;
import com.interface21.webmvc.servlet.mvc.HandlerMapping;
import com.interface21.webmvc.servlet.mvc.ManualHandlerAdapter;
import com.interface21.webmvc.servlet.mvc.tobe.AnnotationHandlerMapping;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DispatcherServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(DispatcherServlet.class);

    private final List<HandlerAdapter> handlerAdapters;

    private List<HandlerMapping> handlerMappings;

    public DispatcherServlet() {
        this.handlerAdapters = new ArrayList<>();
        handlerAdapters.add(new ManualHandlerAdapter());
        handlerAdapters.add(new AnnotationHandlerAdapter());
    }

    @Override
    public void init() {
        this.handlerMappings = new ArrayList<>();

        final HandlerMapping manualHandlerMapping = new ManualHandlerMapping();
        handlerMappings.add(manualHandlerMapping);
        final HandlerMapping annotationHandlerMapping = new AnnotationHandlerMapping("com.techcourse.controller");
        handlerMappings.add(annotationHandlerMapping);

        handlerMappings.forEach(HandlerMapping::initialize);
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
            final var handlerAdapter = findHandlerAdapter(handler, request.getRequestURI());
            final var modelAndView = handlerAdapter.handle(request, response, handler);
            final View view = modelAndView.getView();
            view.render(modelAndView.getModel(), request, response);
        } catch (Exception e) {
            log.error("Exception : {}", e.getMessage(), e);
            throw new ServletException(e.getMessage());
        }
    }

    private Optional<Object> findHandler(final HttpServletRequest request) {
        for (final HandlerMapping handlerMapping : handlerMappings) {
            final Object handler = handlerMapping.getHandler(request);
            if (handler == null) {
                continue;
            }
            return Optional.of(handler);
        }
        return Optional.empty();
    }

    private HandlerAdapter findHandlerAdapter(final Object handler, final String requestURI) {
        for (HandlerAdapter handlerAdapter : handlerAdapters) {
            if (handlerAdapter.supports(handler)) {
                return handlerAdapter;
            }
        }
        throw new IllegalArgumentException(handler + " 핸들러로 " + requestURI + "요청을 처리할 수 없습니다.");
    }
}
