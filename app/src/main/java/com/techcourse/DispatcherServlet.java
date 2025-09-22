package com.techcourse;

import com.interface21.web.bind.annotation.RequestMethod;
import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.mvc.tobe.AnnotationHandlerAdapterExecution;
import com.interface21.webmvc.servlet.mvc.tobe.AnnotationHandlerMapping;
import com.interface21.webmvc.servlet.mvc.tobe.ComponentScanner;
import com.interface21.webmvc.servlet.mvc.tobe.ControllerHandlerAdapter;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerAdapter;
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
    private static final String BASE_PACKAGE = "com.techcourse.controller";

    private final HandlerRegistry handlerRegistry = new HandlerRegistry();

    @Override
    public void init() {
        ManualHandlerMapping manualHandlerMapping = new ManualHandlerMapping();
        manualHandlerMapping.initialize();
        handlerRegistry.registerMapping(manualHandlerMapping);

        AnnotationHandlerMapping annotationHandlerMapping = new AnnotationHandlerMapping(
                new ComponentScanner(BASE_PACKAGE));
        annotationHandlerMapping.initialize();
        handlerRegistry.registerMapping(annotationHandlerMapping);

        handlerRegistry.registerAdapter(new ControllerHandlerAdapter());
        handlerRegistry.registerAdapter(new AnnotationHandlerAdapterExecution());
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

        Optional<Object> handler = handlerRegistry.getHandler(path, requestMethod);
        if (handler.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            try {
                new JspView("/404.jsp").render(Map.of(), request, response);
            } catch (
                    Exception e) {
                throw new ServletException(e.getMessage(), e);
            }
            return;
        }

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
