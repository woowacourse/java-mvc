package com.interface21.webmvc.servlet;

import com.interface21.webmvc.servlet.mvc.tobe.AnnotationHandlerMapping;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerExecutionAdapter;
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

    private final String basePackage;
    private List<HandlerMapping> handlerMappings;
    private List<HandlerAdapter> handlerAdapters;

    public DispatcherServlet(final String basePackage) {
        this.basePackage = basePackage;
    }

    @Override
    public void init() {
        final AnnotationHandlerMapping annotationHandlerMapping =
                new AnnotationHandlerMapping(basePackage);
        annotationHandlerMapping.initialize();

        handlerMappings = List.of(annotationHandlerMapping);
        handlerAdapters = List.of(new HandlerExecutionAdapter());
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
            final HandlerAdapter handlerAdapter = getHandlerAdapter(handler);
            final ModelAndView modelAndView = handlerAdapter.handle(request, response, handler);
            modelAndView.getView().render(modelAndView.getModel(), request, response);
        } catch (Exception e) {
            log.error("Exception : {}", e.getMessage(), e);
            throw new ServletException(e);
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
        throw new IllegalStateException("핸들러를 실행할 어댑터가 없습니다: " + handler.getClass().getName());
    }
}
