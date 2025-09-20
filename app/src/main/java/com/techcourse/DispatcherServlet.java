package com.techcourse;

import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.View;
import com.interface21.webmvc.servlet.mvc.tobe.AnnotationHandlerAdapter;
import com.interface21.webmvc.servlet.mvc.tobe.AnnotationHandlerMapping;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerAdapter;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerMapping;
import com.interface21.webmvc.servlet.mvc.tobe.ManualHandlerAdapter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DispatcherServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final String basePackage = "interface21";
    private static final Logger log = LoggerFactory.getLogger(DispatcherServlet.class);

    private final List<HandlerMapping> handlerMapping;
    private final List<HandlerAdapter> handlerAdapter;

    public DispatcherServlet() {
        this.handlerAdapter = List.of(new AnnotationHandlerAdapter(), new ManualHandlerAdapter());
        this.handlerMapping = List.of(new AnnotationHandlerMapping(basePackage), new ManualHandlerMapping());
    }

    @Override
    public void init() {
        for (HandlerMapping handler : handlerMapping) {
            handler.initialize();
        }
    }

    @Override
    protected void service(final HttpServletRequest request, final HttpServletResponse response)
            throws ServletException {
        final String requestURI = request.getRequestURI();
        log.debug("Method : {}, Request URI : {}", request.getMethod(), requestURI);

        try {
            final Object handler = getHandler(request);
            final ModelAndView modelAndView = getModelAndView(request, response, handler);
            render(modelAndView, request, response);
        } catch (Throwable e) {
            log.error("Exception : {}", e.getMessage(), e);
            throw new ServletException(e.getMessage());
        }
    }

    private Object getHandler(final HttpServletRequest request) {
        for (HandlerMapping mapping : handlerMapping) {
            final Object handler = mapping.getHandler(request);
            if (handler != null) {
                return handler;
            }
        }
        throw new IllegalArgumentException("핸들러를 찾을 수 없습니다.");
    }

    private ModelAndView getModelAndView(final HttpServletRequest request, final HttpServletResponse response,
                                         final Object handler)
            throws Exception {
        for (HandlerAdapter adapter : handlerAdapter) {
            if (adapter.supports(handler)) {
                return adapter.handle(request, response, handler);
            }
        }
        throw new IllegalArgumentException("핸들러 어뎁터를 찾을 수 없습니다.");
    }

    private void render(final ModelAndView modelAndView, final HttpServletRequest request,
                        final HttpServletResponse response)
            throws Exception {
        final View view = modelAndView.getView();
        view.render(modelAndView.getModel(), request, response);
    }
}
