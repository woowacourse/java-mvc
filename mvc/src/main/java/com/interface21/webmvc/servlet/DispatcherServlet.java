package com.interface21.webmvc.servlet;

import com.interface21.webmvc.servlet.mvc.adapter.HandlerAdapter;
import com.interface21.webmvc.servlet.mvc.asis.ControllerHandlerAdapter;
import com.interface21.webmvc.servlet.mvc.asis.ManualHandlerMapping;
import com.interface21.webmvc.servlet.mvc.mapping.HandlerMapping;
import com.interface21.webmvc.servlet.mvc.tobe.AnnotationHandlerAdapter;
import com.interface21.webmvc.servlet.mvc.tobe.AnnotationHandlerMapping;
import com.interface21.webmvc.servlet.view.View;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DispatcherServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(DispatcherServlet.class);

    private final List<HandlerMapping> handlerMappings = new ArrayList<>();
    private final List<HandlerAdapter> handlerAdapters = new ArrayList<>();

    @Override
    public void init() {
        final var annotationHandlerMapping = new AnnotationHandlerMapping("com.techcourse");
        annotationHandlerMapping.initialize();

        final var manualHandlerMapping = new ManualHandlerMapping();
        manualHandlerMapping.initialize();

        handlerMappings.add(annotationHandlerMapping);
        handlerMappings.add(manualHandlerMapping);

        handlerAdapters.add(new AnnotationHandlerAdapter());
        handlerAdapters.add(new ControllerHandlerAdapter());
    }

    @Override
    protected void service(final HttpServletRequest req, final HttpServletResponse res) throws ServletException {
        log.debug("Method : {}, Request URI : {}", req.getMethod(), req.getRequestURI());

        try {
            final Object handler = getHandler(req);
            if (handler == null) {
                res.sendError(HttpServletResponse.SC_NOT_FOUND);
                return;
            }

            final HandlerAdapter adapter = getHandlerAdapter(handler);
            final ModelAndView modelAndView = adapter.handle(req, res, handler);
            render(modelAndView, req, res);
        } catch (Exception e) {
            log.error("Exception : {}", e.getMessage(), e);
            throw new ServletException(e.getMessage());
        }
    }

    private Object getHandler(final HttpServletRequest req) {
        return handlerMappings.stream()
                .map(handlerMapping -> handlerMapping.getHandler(req))
                .filter(Objects::nonNull)
                .findFirst()
                .orElse(null);
    }

    private HandlerAdapter getHandlerAdapter(final Object handler) {
        return handlerAdapters.stream()
                .filter(adapter -> adapter.supports(handler))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("No adapter for handler " + handler));
    }

    private void render(final ModelAndView modelAndView, final HttpServletRequest req, final HttpServletResponse res) throws Exception {
        final View view = modelAndView.getView();
        view.render(modelAndView.getModel(), req, res);
    }
}
