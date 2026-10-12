package com.interface21.webmvc.servlet;

import com.interface21.webmvc.servlet.mvc.HandlerAdapter;
import com.interface21.webmvc.servlet.mvc.HandlerMapping;
import com.interface21.webmvc.servlet.mvc.AnnotationHandlerAdapter;
import com.interface21.webmvc.servlet.mvc.AnnotationHandlerMapping;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DispatcherServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(DispatcherServlet.class);

    private final String basePackage;

    private List<HandlerMapping> handlerMappings;
    private List<HandlerAdapter> handlerAdapters;

    public DispatcherServlet(final String basePackage) {
        this.basePackage = Objects.requireNonNull(basePackage);
    }

    @Override
    public void init() {
        handlerMappings = List.of(
                new AnnotationHandlerMapping(basePackage)
        );

        handlerAdapters = List.of(
                new AnnotationHandlerAdapter()
        );

        for (HandlerMapping handlerMapping : handlerMappings) {
            handlerMapping.initialize();
        }
    }

    @Override
    protected void service(
            final HttpServletRequest request,
            final HttpServletResponse response
    ) throws ServletException {
        log.debug(
                "Method : {}, Request URI : {}",
                request.getMethod(),
                request.getRequestURI()
        );

        try {
            final Object handler = getHandler(request);

            if (handler == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
                return;
            }

            final HandlerAdapter adapter = getHandlerAdapter(handler);

            if (adapter == null) {
                throw new IllegalStateException(
                        "지원하는 HandlerAdapter가 없습니다: "
                                + handler.getClass().getName()
                );
            }

            final ModelAndView mav = adapter.handle(
                    handler,
                    request,
                    response
            );

            mav.getView().render(
                    mav.getModel(),
                    request,
                    response
            );

        } catch (Exception e) {
            log.error("Exception : {}", e.getMessage(), e);
            throw new ServletException("요청 처리 중 오류가 발생했습니다.", e);
        }
    }

    private Object getHandler(final HttpServletRequest request) {
        for (HandlerMapping handlerMapping : handlerMappings) {
            final Object handler = handlerMapping.getHandler(request);

            if (handler != null) {
                return handler;
            }
        }

        return null;
    }

    private HandlerAdapter getHandlerAdapter(final Object handler) {
        for (HandlerAdapter adapter : handlerAdapters) {
            if (adapter.supports(handler)) {
                return adapter;
            }
        }

        return null;
    }
}
