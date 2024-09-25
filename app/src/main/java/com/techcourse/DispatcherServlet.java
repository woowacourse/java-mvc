package com.techcourse;

import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.View;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DispatcherServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(DispatcherServlet.class);

    private final List<HandlerMapping> registryHandlerMapping;
    private final HandlerAdaptor handlerAdaptor;

    public DispatcherServlet(List<HandlerMapping> handlerMappings) {
        this.registryHandlerMapping = handlerMappings;
        this.handlerAdaptor = new HandlerAdaptor();
    }

    @Override
    protected void service(final HttpServletRequest request, final HttpServletResponse response) throws ServletException {
        final String requestURI = request.getRequestURI();
        log.debug("Method : {}, Request URI : {}", request.getMethod(), requestURI);

        try {
            Object handler = getHandler(request);
            ModelAndView modelAndView = handlerAdaptor.adaptHandler(handler, request, response);
            move(modelAndView, request, response);
        } catch (Throwable e) {
            log.error("Exception : {}", e.getMessage(), e);
            throw new ServletException(e.getMessage());
        }
    }

    private Object getHandler(HttpServletRequest request) {
        for (HandlerMapping handlerMapping : registryHandlerMapping) {
            Object handler = handlerMapping.getHandler(request);
            if(handler != null) {
                return handler;
            }
        }
        throw new IllegalArgumentException("매핑 가능한 Handler가 존재하지 않습니다. (Request URI : %s"
                .formatted(request.getRequestURI()));
    }

    private void move(final ModelAndView modelAndView, final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        HashMap<String, ?> model = new HashMap<>();
        View view = modelAndView.getView();

        view.render(model, request, response);
    }
}
