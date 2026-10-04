package com.techcourse;

import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.mvc.asis.Controller;
import com.interface21.webmvc.servlet.mvc.tobe.AnnotationHandlerMapping;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerAdapter;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerExecution;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerMapping;
import com.interface21.webmvc.servlet.view.JspView;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class DispatcherServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(DispatcherServlet.class);

    private List<HandlerMapping> handlerMappings;
    private List<HandlerAdapter> handlerAdapters;

    @Override
    public void init() {
        final ManualHandlerMapping manualHandlerMapping = new ManualHandlerMapping();
        manualHandlerMapping.initialize();

        final AnnotationHandlerMapping annotationHandlerMapping =
                new AnnotationHandlerMapping("com.techcourse.controller");
        annotationHandlerMapping.initialize();

        handlerMappings = List.of(manualHandlerMapping, annotationHandlerMapping);
        handlerAdapters = List.of(new LegacyControllerHandlerAdapter(), new HandlerExecutionHandlerAdapter());
    }

    @Override
    protected void service(final HttpServletRequest request, final HttpServletResponse response) throws ServletException {
        final String requestURI = request.getRequestURI();
        log.debug("Method : {}, Request URI : {}", request.getMethod(), requestURI);

        try {
            final Object handler = getHandler(request);
            if (handler == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
                return;
            }

            final HandlerAdapter handlerAdapter = handlerAdapters.stream()
                    .filter(adapter -> adapter.supports(handler))
                    .findFirst()
                    .orElseThrow(() -> new IllegalStateException(
                            "No HandlerAdapter found for " + handler.getClass().getName()));

            final ModelAndView modelAndView = handlerAdapter.handle(handler, request, response);
            modelAndView.getView().render(modelAndView.getModel(), request, response);
        } catch (Throwable e) {
            log.error("Exception : {}", e.getMessage(), e);
            throw new ServletException(e.getMessage(), e);
        }
    }

    private Object getHandler(final HttpServletRequest request) {
        for (final HandlerMapping handlerMapping : handlerMappings) {
            final Object handler = handlerMapping.getHandler(request);
            if (handler != null) {
                return handler;
            }
        }
        return null;
    }

    private static class LegacyControllerHandlerAdapter implements HandlerAdapter {

        @Override
        public boolean supports(final Object handler) {
            return handler instanceof Controller;
        }

        @Override
        public ModelAndView handle(final Object handler, final HttpServletRequest request, final HttpServletResponse response) throws Exception {
            final String viewName = ((Controller) handler).execute(request, response);
            return new ModelAndView(new JspView(viewName));
        }
    }

    private static class HandlerExecutionHandlerAdapter implements HandlerAdapter {

        @Override
        public boolean supports(final Object handler) {
            return handler instanceof HandlerExecution;
        }

        @Override
        public ModelAndView handle(final Object handler, final HttpServletRequest request, final HttpServletResponse response) throws Exception {
            return ((HandlerExecution) handler).handle(request, response);
        }
    }
}
