package com.techcourse;

import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.View;
import com.interface21.webmvc.servlet.mvc.asis.Controller;
import com.interface21.webmvc.servlet.mvc.tobe.AnnotationHandlerMapping;
import com.interface21.webmvc.servlet.mvc.tobe.ControllerScanner;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerExecution;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerMapping;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DispatcherServlet extends HttpServlet {

    private static final String DEFAULT_BASE_PACKAGE = "com.techcourse";
    private static final ControllerScanner DEFAULT_CONTROLLER_SCANNER = new ControllerScanner();
    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(DispatcherServlet.class);

    private List<HandlerMapping> handlerMappings = new ArrayList<>();

    public DispatcherServlet() {
    }

    @Override
    public void init() {
        ManualHandlerMapping manualHandlerMapping = new ManualHandlerMapping();
        manualHandlerMapping.initialize();
        handlerMappings.add(manualHandlerMapping);

        AnnotationHandlerMapping annotationHandlerMapping = new AnnotationHandlerMapping(
                DEFAULT_CONTROLLER_SCANNER,
                DEFAULT_BASE_PACKAGE
        );
        annotationHandlerMapping.initialize();
        handlerMappings.add(annotationHandlerMapping);
    }

    @Override
    protected void service(final HttpServletRequest request, final HttpServletResponse response)
            throws ServletException {
        final String requestURI = request.getRequestURI();
        log.debug("Method : {}, Request URI : {}", request.getMethod(), requestURI);

        try {
            ModelAndView modelAndView = handle(request, response);

            View view = modelAndView.getView();
            view.render(modelAndView.getModel(), request, response);
        } catch (Throwable e) {
            log.error("Exception : {}", e.getMessage(), e);
            throw new ServletException(e.getMessage());
        }
    }

    private ModelAndView handle(final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        for (HandlerMapping handlerMapping : handlerMappings) {
            Object handler = handlerMapping.getHandler(request);

            if (handler instanceof Controller) {
                return ((Controller) handler).execute(request, response);
            } else if (handler instanceof HandlerExecution) {
                return ((HandlerExecution) handler).handle(request, response);
            }
        }

        throw new ServletException("Unsupported handler type");
    }
}
