package com.techcourse;

import com.interface21.webmvc.servlet.View;
import com.interface21.webmvc.servlet.mvc.DefaultHandlerAdapter;
import com.interface21.webmvc.servlet.mvc.HandlerAdapter;
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

    private final HandlerAdapter handlerAdapter;

    private List<HandlerMapping> handlerMappings;

    public DispatcherServlet() {
        this.handlerAdapter = new DefaultHandlerAdapter();
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
    protected void service(final HttpServletRequest request, final HttpServletResponse response) throws ServletException {
        log.debug("Method : {}, Request URI : {}", request.getMethod(), request.getRequestURI());
        try {
            final var handler = findHandler(request);
            final var modelAndView = handlerAdapter.handle(request, response, handler);
            final View view = modelAndView.getView();
            view.render(modelAndView.getModel(), request, response);
        } catch (Throwable e) {
            log.error("Exception : {}", e.getMessage(), e);
            throw new ServletException(e.getMessage());
        }
    }

    private Object findHandler(final HttpServletRequest request) {
        for (final HandlerMapping handlerMapping : handlerMappings) {
            final Object handler = handlerMapping.getHandler(request);
            if (handler == null) {
                continue;
            }
            return handler;
        }
        throw new IllegalArgumentException(
                "요청을 처리할 수 있는 컨트롤러가 존재하지 않습니다. 요청 URI : " + request.getRequestURI()
        );
    }
}
