package com.interface21.webmvc.servlet;

import com.interface21.webmvc.servlet.mvc.handler.HandlerAdapter;
import com.interface21.webmvc.servlet.mvc.handler.HandlerMapping;
import com.interface21.webmvc.servlet.mvc.handler.annotation.AnnotationHandlerMapping;
import com.interface21.webmvc.servlet.mvc.handler.annotation.HandlerExecutionHandlerAdapter;
import com.interface21.webmvc.servlet.mvc.handler.controller.ControllerHandlerAdapter;
import com.interface21.webmvc.servlet.mvc.handler.controller.ManualHandlerMapping;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DispatcherServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final List<HandlerMapping> handlerMappings = new ArrayList<>();
    private static final List<HandlerAdapter> handlerAdapters = new ArrayList<>();
    private static final Logger log = LoggerFactory.getLogger(DispatcherServlet.class);

    static {
        handlerMappings.add(new AnnotationHandlerMapping("com.techcourse.controller"));
        handlerMappings.add(new ManualHandlerMapping());

        handlerAdapters.add(new HandlerExecutionHandlerAdapter());
        handlerAdapters.add(new ControllerHandlerAdapter());
    }

    public DispatcherServlet() {
    }

    @Override
    public void init() {
        handlerMappings.forEach(HandlerMapping::initialize);
    }

    @Override
    protected void service(final HttpServletRequest request, final HttpServletResponse response)
            throws ServletException {
        final String requestURI = request.getRequestURI();
        log.debug("Method : {}, Request URI : {}", request.getMethod(), requestURI);

        try {
            final HandlerMapping handlerMapping = getHandlerMapping(request);
            final Object handler = handlerMapping.getHandler(request);
            final HandlerAdapter handlerAdapter = getHandlerAdapter(handler);
            final ModelAndView modelAndView = handlerAdapter.handle(request, response, handler);

            render(modelAndView, request, response);
        } catch (Throwable e) {
            log.error("Exception : {}", e.getMessage(), e);
            throw new ServletException(e.getMessage());
        }
    }

    private HandlerMapping getHandlerMapping(HttpServletRequest request) {
        return handlerMappings.stream()
                .filter(handlerMapping -> handlerMapping.getHandler(request) != null)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No Handler Found. "
                        + "requestURI: " + request.getRequestURI() + " method: " + request.getMethod()));
    }

    private HandlerAdapter getHandlerAdapter(Object handler) {
        return handlerAdapters.stream()
                .filter(handlerAdapter -> handlerAdapter.supports(handler))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No HandlerAdapter Found. handler: " + handler));

    }

    private void render(final ModelAndView mv, final HttpServletRequest request, final HttpServletResponse response)
            throws Exception {
        Map<String, ?> model = mv.getModel();
        View view = mv.getView();

        view.render(model, request, response);
    }
}
