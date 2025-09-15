package com.techcourse;

import com.interface21.webmvc.servlet.AnnotationHandlerAdapter;
import com.interface21.webmvc.servlet.HandlerAdapter;
import com.interface21.webmvc.servlet.HandlerMapping;
import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.SimpleControllerHandlerAdapter;
import com.interface21.webmvc.servlet.View;
import com.interface21.webmvc.servlet.mvc.tobe.AnnotationHandlerMapping;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class DispatcherServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(DispatcherServlet.class);

    private List<HandlerMapping> handlerMappings;
    private List<HandlerAdapter> handlerAdapters;

    public DispatcherServlet() {
    }

    @Override
    public void init() {
        final ManualHandlerMapping manualHandlerMapping = new ManualHandlerMapping();
        manualHandlerMapping.initialize();
        final AnnotationHandlerMapping annotationHandlerMapping = new AnnotationHandlerMapping("com.techcourse");
        annotationHandlerMapping.initialize();
        this.handlerMappings = List.of(manualHandlerMapping, annotationHandlerMapping);
        this.handlerAdapters = List.of(new SimpleControllerHandlerAdapter(), new AnnotationHandlerAdapter());
    }

    @Override
    protected void service(
            final HttpServletRequest request,
            final HttpServletResponse response
    ) throws ServletException {
        log.debug("Method : {}, Request URI : {}", request.getMethod(), request.getRequestURI());
        try {
            final Object handler = getHandler(request);
            if (handler == null) {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                return;
            }
            final HandlerAdapter handlerAdapter = getHandlerAdapter(handler);
            if (handlerAdapter == null) {
                throw new ServletException("요청을 처리할 수 있는 어댑터를 찾을 수 없습니다. handler=" + handler.getClass().getName());
            }
            final ModelAndView modelAndView = handlerAdapter.handle(request, response, handler);
            move(modelAndView, request, response);
        } catch (final Throwable exception) {
            log.error("요청 처리 중 예외 발생", exception);
            throw new ServletException(exception.getMessage());
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

    private void move(
            final ModelAndView modelAndView,
            final HttpServletRequest request,
            final HttpServletResponse response
    ) throws Exception {
        final View view = modelAndView.getView();
        view.render(modelAndView.getModel(), request, response);
    }
}
