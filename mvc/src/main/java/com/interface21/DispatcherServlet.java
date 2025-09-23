package com.interface21;

import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.mvc.tobe.AnnotationHandlerAdapter;
import com.interface21.webmvc.servlet.mvc.tobe.AnnotationHandlerMapping;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerAdapter;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerMapping;
import com.interface21.webmvc.servlet.mvc.tobe.ManualHandlerAdapter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DispatcherServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(DispatcherServlet.class);

    private final List<HandlerMapping> handlerMappings = new ArrayList<>(List.of(
            new ManualHandlerMapping(), new AnnotationHandlerMapping("com.techcourse.controller")
    ));
    private final List<HandlerAdapter> handlerAdapters = new ArrayList<>(List.of(
            new AnnotationHandlerAdapter(),
            new ManualHandlerAdapter()
    ));

    public DispatcherServlet() {
    }

    @Override
    public void init() {
        initializeHandlerMapping();
    }

    @Override
    protected void service(final HttpServletRequest request, final HttpServletResponse response) throws ServletException {
        final String requestURI = request.getRequestURI();
        log.debug("Method : {}, Request URI : {}", request.getMethod(), requestURI);

        try {
            ModelAndView modelAndView = executeHandler(request, response);
            modelAndView.getView().render(modelAndView.getModel(), request, response);
        } catch (Throwable e) {
            log.error("Exception : {}", e.getMessage(), e);
            throw new ServletException(e.getMessage());
        }
    }

    private void initializeHandlerMapping() {
        handlerMappings.forEach(HandlerMapping::initialize);
    }

    private ModelAndView executeHandler(final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        Object handler = getHandler(request);
        HandlerAdapter adapter = getAdapter(handler);


        return adapter.handle(request, response, handler);
    }

    private Object getHandler(final HttpServletRequest request) {
        for (HandlerMapping handlerMapping : handlerMappings) {
            Object handler = handlerMapping.getHandler(request);
            if (handler != null) {
                return handler;
            }
        }
        throw new IllegalArgumentException("no such handler: " + request.getRequestURI());
    }

    private HandlerAdapter getAdapter(Object handler) {
        return handlerAdapters.stream()
                .filter(handlerAdapter -> handlerAdapter.supports(handler))
                .findFirst()
                .orElseThrow();
    }
}
