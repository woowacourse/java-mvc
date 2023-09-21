package com.techcourse;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webmvc.org.springframework.web.servlet.ModelAndView;
import webmvc.org.springframework.web.servlet.mvc.handlermapping.HandlerAdapter;
import webmvc.org.springframework.web.servlet.mvc.handlermapping.HandlerMapping;
import webmvc.org.springframework.web.servlet.mvc.tobe.AnnotationHandlerMapping;
import webmvc.org.springframework.web.servlet.mvc.tobe.HandlerExecutionAdapter;
import webmvc.org.springframework.web.servlet.view.JspView;

public class DispatcherServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(DispatcherServlet.class);

    private final List<HandlerMapping> handlerMappings = new ArrayList<>();
    private final List<HandlerAdapter> handlerAdapters = new ArrayList<>();

    @Override
    public void init() {
        var manualHandlerMapping = new ManualHandlerMapping();
        var annotationHandlerMapping = new AnnotationHandlerMapping("com.techcourse.controller");
        annotationHandlerMapping.initialize();
        manualHandlerMapping.initialize();
        handlerMappings.add(manualHandlerMapping);
        handlerMappings.add(annotationHandlerMapping);

        handlerAdapters.add(new ControllerAdapter());
        handlerAdapters.add(new HandlerExecutionAdapter());
    }

    @Override
    protected void service(final HttpServletRequest request, final HttpServletResponse response)
        throws ServletException {
        final String requestURI = request.getRequestURI();
        log.debug("Method : {}, Request URI : {}", request.getMethod(), requestURI);

        try {
            final var handler = findHandler(request);
            final var handlerAdapter = findHandlerAdapter(handler);
            final var modelAndView = handlerAdapter.handle(handler, request, response);
            move(modelAndView, request, response);
        } catch (Exception e) {
            log.error("Exception : {}", e.getMessage(), e);
            throw new ServletException(e.getMessage());
        }
    }

    private Object findHandler(HttpServletRequest request) {
        for (HandlerMapping handlerMapping : handlerMappings) {
            Optional<Object> handler = handlerMapping.getHandler(request);
            if (handler.isPresent()) {
                return handler.get();
            }
        }
        throw new IllegalArgumentException("handler를 찾을 수 없습니다.");
    }

    private HandlerAdapter findHandlerAdapter(Object handler) {
        return handlerAdapters.stream()
                              .filter(handlerAdapter -> handlerAdapter.supports(handler))
                              .findFirst()
                              .orElseThrow(() -> new IllegalArgumentException("handlerAdapter를 찾을 수 없습니다."));
    }

    private void move(
        final ModelAndView modelAndView,
        final HttpServletRequest request,
        final HttpServletResponse response
    ) throws Exception {
        String viewName = modelAndView.getView().getViewName();
        if (viewName.startsWith(JspView.REDIRECT_PREFIX)) {
            response.sendRedirect(viewName.substring(JspView.REDIRECT_PREFIX.length()));
            return;
        }

        final var requestDispatcher = request.getRequestDispatcher(viewName);
        requestDispatcher.forward(request, response);
    }
}
