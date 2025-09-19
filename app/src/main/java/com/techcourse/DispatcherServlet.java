package com.techcourse;

import com.interface21.webmvc.servlet.mvc.asis.Controller;
import com.interface21.webmvc.servlet.mvc.tobe.AnnotationHandlerMapping;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerExecution;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerMapping;
import com.interface21.webmvc.servlet.view.JspView;
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
    private static final Logger log = LoggerFactory.getLogger(DispatcherServlet.class);

    private List<HandlerMapping> mappings;

    public DispatcherServlet() {
    }

    @Override
    public void init() {
        mappings = new ArrayList<>();
        ManualHandlerMapping manualHandlerMapping = new ManualHandlerMapping();
        manualHandlerMapping.initialize();
        AnnotationHandlerMapping annotationHandlerMapping = new AnnotationHandlerMapping("com.techcourse.controller");
        annotationHandlerMapping.initialize();

        mappings.add(manualHandlerMapping);
        mappings.add(annotationHandlerMapping);
    }

    @Override
    protected void service(
            final HttpServletRequest request,
            final HttpServletResponse response
    ) throws ServletException {
        final String requestURI = request.getRequestURI();
        log.debug("Method : {}, Request URI : {}", request.getMethod(), requestURI);

        try {
            for (HandlerMapping handlerMapping : mappings) {
                final var handler = handlerMapping.getHandler(request);
                if (handler != null) {
                    if (handler instanceof HandlerExecution) {
                        final var modelAndView = ((HandlerExecution) handler).handle(request, response);
                        modelAndView.getView().render(modelAndView.getModel(), request, response);
                    } else if (handler instanceof Controller) {
                        final var viewName = ((Controller) handler).execute(request, response);
                        move(viewName, request, response);
                    } else {
                        response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    }
                }
            }
        } catch (Throwable e) {
            log.error("Exception : {}", e.getMessage(), e);
            throw new ServletException(e.getMessage());
        }
    }

    private void move(
            final String viewName,
            final HttpServletRequest request,
            final HttpServletResponse response
    ) throws Exception {
        final var view = new JspView(viewName);
        view.render(Map.of(), request, response);
    }
}
