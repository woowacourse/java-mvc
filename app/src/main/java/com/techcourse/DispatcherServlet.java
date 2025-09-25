package com.techcourse;

import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.View;
import com.interface21.webmvc.servlet.mvc.adapter.HandlerAdapter;
import com.interface21.webmvc.servlet.mvc.mapping.HandlerMapping;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DispatcherServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(DispatcherServlet.class);

    private final HandlerMappingRegistry handlerMappingRegistry;
    private final HandlerAdaptorRegistry handlerAdaptorRegistry;

    public DispatcherServlet(HandlerMappingRegistry handlerMappingRegistry,
                             HandlerAdaptorRegistry handlerAdaptorRegistry) {
        this.handlerMappingRegistry = handlerMappingRegistry;
        this.handlerAdaptorRegistry = handlerAdaptorRegistry;
    }

    @Override
    public void init() {
    }

    @Override
    protected void service(final HttpServletRequest request, final HttpServletResponse response) throws ServletException {
        final String requestURI = request.getRequestURI();
        log.debug("Method : {}, Request URI : {}", request.getMethod(), requestURI);

        try {
            HandlerMapping handlerMapping = handlerMappingRegistry.getHandlerMapping(request)
                    .orElseThrow(IllegalArgumentException::new);

            Object handler = handlerMapping.getHandler(request);

            HandlerAdapter handlerAdapter = handlerAdaptorRegistry.getHandlerAdapter(handler)
                    .orElseThrow(IllegalArgumentException::new);

            ModelAndView modelAndView = handlerAdapter.handle(request, response, handler);

            Map<String, Object> model = modelAndView.getModel();
            View view = modelAndView.getView();

            view.render(model, request, response);

        } catch (Throwable e) {
            log.error("Exception : {}", e.getMessage(), e);
            throw new ServletException(e.getMessage());
        }
    }
}
