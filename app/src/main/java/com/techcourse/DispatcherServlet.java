package com.techcourse;

import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.View;
import com.interface21.webmvc.servlet.mvc.ControllerHandlerAdapter;
import com.interface21.webmvc.servlet.mvc.HandlerAdapter;
import com.interface21.webmvc.servlet.mvc.HandlerExecutionHandlerAdapter;
import com.interface21.webmvc.servlet.mvc.HandlerMapping;
import com.interface21.webmvc.servlet.mvc.tobe.AnnotationHandlerMapping;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class DispatcherServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(DispatcherServlet.class);

    private List<HandlerMapping> handlerMappings;
    private List<HandlerAdapter> handlerAdapters;

    @Override
    public void init() {
        initHandlerMappings();
        initHandlerAdapters();
    }

    private void initHandlerMappings() {
        handlerMappings = new ArrayList<>();

        final ManualHandlerMapping manualHandlerMapping = new ManualHandlerMapping();
        manualHandlerMapping.initialize();
        handlerMappings.add(manualHandlerMapping);

        final AnnotationHandlerMapping annotationHandlerMapping = new AnnotationHandlerMapping("com.techcourse.controller");
        annotationHandlerMapping.initialize();
        handlerMappings.add(annotationHandlerMapping);
    }

    private void initHandlerAdapters() {
        handlerAdapters = List.of(
                new ControllerHandlerAdapter(),
                new HandlerExecutionHandlerAdapter()
        );
    }

    @Override
    protected void service(final HttpServletRequest request, final HttpServletResponse response)
            throws ServletException {
        log.debug("Method : {}, Request URI : {}", request.getMethod(), request.getRequestURI());

        try {
            final Object handler = getHandler(request);
            if (handler == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
                return;
            }

            final HandlerAdapter handlerAdapter = getHandlerAdapter(handler);
            if (handlerAdapter == null) {
                throw new ServletException("No adapter for handler [" + handler + "]");
            }

            final ModelAndView modelAndView = handlerAdapter.handle(request, response, handler);
            render(modelAndView, request, response);
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

    private HandlerAdapter getHandlerAdapter(final Object handler) {
        for (final HandlerAdapter handlerAdapter : handlerAdapters) {
            if (handlerAdapter.supports(handler)) {
                return handlerAdapter;
            }
        }
        return null;
    }

    private void render(final ModelAndView modelAndView,
                        final HttpServletRequest request,
                        final HttpServletResponse response) throws Exception {
        if (modelAndView == null) {
            return;
        }

        final View view = modelAndView.getView();
        if (view == null) {
            throw new IllegalStateException("ModelAndView does not contain a view");
        }

        view.render(modelAndView.getModel(), request, response);
    }
}
