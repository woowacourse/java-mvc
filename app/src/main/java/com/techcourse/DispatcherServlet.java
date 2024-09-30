package com.techcourse;

import com.interface21.webmvc.servlet.HandlerAdaptor;
import com.interface21.webmvc.servlet.HandlerMapping;
import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.View;
import com.interface21.webmvc.servlet.mvc.adater.AnnotationHandlerAdapter;
import com.interface21.webmvc.servlet.mvc.mapping.AnnotationHandlerMapping;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DispatcherServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(DispatcherServlet.class);

    private List<HandlerMapping> handlerMappings;
    private List<HandlerAdaptor> handlerAdaptors;

    public DispatcherServlet() {
        initHandlerMapping();
        initHandlerAdaptor();
    }

    private void initHandlerMapping() {
        handlerMappings = List.of(
                new ManualHandlerMapping(),
                new AnnotationHandlerMapping("com.techcourse.controller")
        );

        for (HandlerMapping handlerMapping : handlerMappings) {
            log.info("initialize HandlerMapping :: %s".formatted(handlerMapping.getClass().getName()));
            handlerMapping.initialize();
        }
    }

    private void initHandlerAdaptor() {
        this.handlerAdaptors = List.of(
                new ManualHandlerAdaptor(),
                new AnnotationHandlerAdapter()
        );
    }

    @Override
    protected void service(final HttpServletRequest request, final HttpServletResponse response) throws ServletException {
        final String requestURI = request.getRequestURI();
        log.debug("Method : {}, Request URI : {}", request.getMethod(), requestURI);

        try {
            Object handler = getHandler(request);
            HandlerAdaptor handlerAdaptor = getAdaptor(handler);

            ModelAndView modelAndView = handlerAdaptor.handle(request, response, handler);
            move(modelAndView, request, response);
        } catch (Throwable e) {
            log.error("Exception : {}", e.getMessage(), e);
            throw new ServletException(e.getMessage());
        }
    }

    private HandlerAdaptor getAdaptor(Object handler) {
        for (HandlerAdaptor handlerAdaptor : handlerAdaptors) {
            if(handlerAdaptor.supports(handler)) {
                return handlerAdaptor;
            }
        }
        throw new IllegalArgumentException("매핑 가능한 HandlerAdaptor가 존재하지 않습니다. (Handler type: %s)"
                .formatted(handler.getClass().getName()));
    }

    private Object getHandler(HttpServletRequest request) {
        for (HandlerMapping handlerMapping : handlerMappings) {
            Object handler = handlerMapping.getHandler(request);
            if(handler != null) {
                return handler;
            }
        }
        throw new IllegalArgumentException("매핑 가능한 Handler가 존재하지 않습니다. (Request URI : %s"
                .formatted(request.getRequestURI()));
    }

    private void move(final ModelAndView modelAndView, final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        Map<String, Object> model = modelAndView.getModel();
        View view = modelAndView.getView();

        view.render(model, request, response);
    }
}
