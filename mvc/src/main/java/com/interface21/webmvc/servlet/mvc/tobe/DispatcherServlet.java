package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.webmvc.servlet.ModelAndView;
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
    private static final String APPLICATION_BASE_PACKAGE = "com.techcourse";

    private final HandlerMappingRegistry handlerMappingRegistry = new HandlerMappingRegistry();

    public DispatcherServlet() {
    }

    @Override
    public void init() {
        handlerMappingRegistry.register(new AnnotationHandlerMapping(APPLICATION_BASE_PACKAGE));
    }

    @Override
    protected void service(final HttpServletRequest request, final HttpServletResponse response) throws ServletException {
        final String requestURI = request.getRequestURI();
        log.debug("Method : {}, Request URI : {}", request.getMethod(), requestURI);

        try {
            HandlerAdapter handler = handlerMappingRegistry.getHandler(request);
            ModelAndView modelAndView = handler.handle(request, response);
            Map<String, Object> model = modelAndView.getModel();
            modelAndView.getView().render(model, request, response);
        } catch (Throwable e) {
            log.error("Exception : {}", e.getMessage(), e);
            throw new ServletException(e.getMessage());
        }
    }
}
