package com.techcourse;

import com.techcourse.support.web.handler.adaptor.HandlerAdaptors;
import com.techcourse.support.web.handler.adaptor.ManualHandlerAdaptor;
import com.techcourse.support.web.handler.adaptor.ManualHandlerMappingWrapped;
import com.techcourse.support.web.handler.mapping.HandlerMappings;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webmvc.org.springframework.web.servlet.ModelAndView;
import webmvc.org.springframework.web.servlet.mvc.HandlerAdaptor;
import webmvc.org.springframework.web.servlet.mvc.tobe.AnnotationHandlerAdaptor;
import webmvc.org.springframework.web.servlet.mvc.tobe.AnnotationHandlerMapping;
import java.util.List;

public class DispatcherServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(DispatcherServlet.class);

    private HandlerMappings handlerMapping;
    private HandlerAdaptors handlerAdaptors;

    public DispatcherServlet() {
    }

    @Override
    public void init() {
        handlerMapping = new HandlerMappings(List.of(
                new ManualHandlerMappingWrapped(),
                new AnnotationHandlerMapping("com.techcourse")));
        handlerAdaptors = new HandlerAdaptors(List.of(
                new ManualHandlerAdaptor(),
                new AnnotationHandlerAdaptor()));
        handlerMapping.initialize();
    }

    @Override
    protected void service(final HttpServletRequest request, final HttpServletResponse response) throws ServletException {
        log.debug("Method : {}, Request URI : {}", request.getMethod(), request.getRequestURI());
        try {
            final var optionalHandler = handlerMapping.getHandler(request);
            if (optionalHandler.isEmpty()) {
                log.warn("Handler Not Found");
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
                return;
            }
            final var handler = optionalHandler.get();

            final var handlerAdaptor = findHandlerAdaptor(handler);
            final var modelAndView = handlerAdaptor.execute(request, response, handler);

            move(modelAndView, request, response);
        } catch (final Throwable e) {
            log.error("Exception : {}", e.getMessage(), e);
            throw new ServletException(e.getMessage());
        }
    }

    private HandlerAdaptor findHandlerAdaptor(final Object handler) {
        final var optionalHandlerAdaptor = handlerAdaptors.findHandlerAdaptor(handler);
        if (optionalHandlerAdaptor.isPresent()) {
            return optionalHandlerAdaptor.get();
        }
        throw new IllegalArgumentException("Handler Adaptor Not Found");
    }

    private void move(final ModelAndView modelAndView, final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        final var view = modelAndView.getView();
        view.render(modelAndView.getModel(), request, response);
    }
}
