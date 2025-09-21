package com.techcourse;

import com.interface21.webmvc.servlet.mvc.tobe.AnnotationHandlerAdapter;
import com.interface21.webmvc.servlet.mvc.tobe.AnnotationHandlerMapping;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerAdapter;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerMapping;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DispatcherServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(DispatcherServlet.class);

    private List<HandlerMapping> handlerMappings;
    private List<HandlerAdapter> handlerAdapters;

    public DispatcherServlet() {
    }

    @Override
    public void init() {
        ManualHandlerMapping manualHandlerMapping = new ManualHandlerMapping();
        AnnotationHandlerMapping annotationHandlerMapping = new AnnotationHandlerMapping("com");

        manualHandlerMapping.initialize();
        annotationHandlerMapping.initialize();

        handlerMappings = List.of(manualHandlerMapping, annotationHandlerMapping);
        handlerAdapters = List.of(new ManualHandlerAdapter(), new AnnotationHandlerAdapter());
    }

    @Override
    protected void service(final HttpServletRequest request, final HttpServletResponse response) throws ServletException {
        final String requestURI = request.getRequestURI();
        log.debug("Method : {}, Request URI : {}", request.getMethod(), requestURI);

        try {
            for (final HandlerMapping handlerMapping : handlerMappings) {
                final var controller = handlerMapping.getHandler(request);
                if (controller == null) {
                    continue;
                }

                if (processHandler(request, response, controller)) {
                    return;
                }
            }
        } catch (Throwable e) {
            log.error("Exception : {}", e.getMessage(), e);
            throw new ServletException(e.getMessage());
        }

        response.setStatus(HttpServletResponse.SC_NOT_FOUND);
    }

    private boolean processHandler(HttpServletRequest request, HttpServletResponse response, Object controller)
            throws Exception {
        for (final HandlerAdapter handlerAdapter : handlerAdapters) {
            if (handlerAdapter.supports(controller)) {
                final var modelView = handlerAdapter.handle(request, response, controller);
                modelView.getView().render(modelView.getModel(), request, response);
                return true;
            }
        }
        return false;
    }
}
