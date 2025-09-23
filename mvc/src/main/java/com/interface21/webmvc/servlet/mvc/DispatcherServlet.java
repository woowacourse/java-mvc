package com.interface21.webmvc.servlet.mvc;

import com.interface21.web.bind.annotation.RequestMethod;
import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.view.JspView;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DispatcherServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(DispatcherServlet.class);
    private static final String BASE_PACKAGE = "com.techcourse";

    private final HandlerRegistry handlerRegistry = new HandlerRegistry();

    @Override
    public void init() {
        AnnotationHandlerMapping annotationHandlerMapping = new AnnotationHandlerMapping(
                new ComponentScanner(BASE_PACKAGE));
        annotationHandlerMapping.initialize();
        handlerRegistry.registerMapping(annotationHandlerMapping);

        handlerRegistry.registerAdapter(new AnnotationHandlerAdapter());
    }

    @Override
    protected void service(final HttpServletRequest request, final HttpServletResponse response)
            throws ServletException {
        final String requestURI = request.getRequestURI();
        log.debug("Method : {}, Request URI : {}", request.getMethod(), requestURI);

        adapt(request, response);
    }

    private void adapt(final HttpServletRequest request, final HttpServletResponse response)
            throws ServletException {
        String path = request.getServletPath();
        RequestMethod requestMethod = RequestMethod.valueOf(request.getMethod());

        Optional<Object> optionalHandler = handlerRegistry.getHandler(path, requestMethod);
        if (optionalHandler.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            try {
                new JspView("/404.jsp").render(Map.of(), request, response);
            } catch (
                    Exception e) {
                throw new ServletException(e.getMessage(), e);
            }
            return;
        }
        Object handler = optionalHandler.get();

        try {
            ModelAndView modelAndView = executeHandler(request, response, handler);
            modelAndView.render(request, response);
        } catch (Exception e) {
            log.error("Exception : {}", e.getMessage(), e);
            throw new ServletException(e.getMessage());
        }
    }

    private ModelAndView executeHandler(final HttpServletRequest request, final HttpServletResponse response,
                                        final Object handler) throws Exception {
        Optional<HandlerAdapter> adapter = handlerRegistry.getAdapter(handler);
        if (adapter.isPresent()) {
            return adapter.get().handle(request, response, handler);
        }
        throw new ServletException("No HandlerAdapter for handler: " + handler.getClass().getName());
    }
}
