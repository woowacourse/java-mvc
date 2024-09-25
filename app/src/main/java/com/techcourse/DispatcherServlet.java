package com.techcourse;

import com.interface21.web.servlet.HandlerAdapter;
import com.interface21.web.servlet.HandlerMapping;
import com.interface21.web.servlet.mvc.ControllerHandlerAdapter;
import com.interface21.web.servlet.mvc.HandlerExecutionAdapter;
import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.mvc.tobe.AnnotationHandlerMapping;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DispatcherServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(DispatcherServlet.class);
    private static final String CONTROLLER_PACKAGE = "com.techcourse.controller";

    private List<HandlerMapping> handlerMappings;
    private List<HandlerAdapter> handlerAdapters;

    public DispatcherServlet() {
    }

    @Override
    public void init() {
        ManualHandlerMapping manualHandlerMapping = new ManualHandlerMapping();
        AnnotationHandlerMapping annotationHandlerMapping = new AnnotationHandlerMapping(CONTROLLER_PACKAGE);

        manualHandlerMapping.initialize();
        annotationHandlerMapping.initialize();

        handlerMappings = new ArrayList<>();
        handlerMappings.add(manualHandlerMapping);
        handlerMappings.add(annotationHandlerMapping);

        handlerAdapters = new ArrayList<>();

        handlerAdapters.add(new ControllerHandlerAdapter());
        handlerAdapters.add(new HandlerExecutionAdapter());
    }

    @Override
    protected void service(final HttpServletRequest request, final HttpServletResponse response)
            throws ServletException {
        log.debug("Method : {}, Request URI : {}", request.getMethod(), request.getRequestURI());

        try {
            for (HandlerMapping handlerMapping : handlerMappings) {
                Object handler = handlerMapping.getHandler(request);
                if (handler != null) {
                    for (HandlerAdapter handlerAdapter : handlerAdapters) {
                        if (handlerAdapter.supports(handler)) {
                            ModelAndView modelAndView = handlerAdapter.handle(request, response, handler);
                            modelAndView.getView().render(modelAndView.getModel(), request, response);
                            return;
                        }
                    }
                    throw new ServletException("No adapter for handler [" + handler
                            + "]: The DispatcherServlet configuration needs to include a HandlerAdapter that supports this handler");
                }
            }

            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        } catch (Throwable e) {
            log.error("Exception : {}", e.getMessage(), e);
            throw new ServletException(e.getMessage());
        }
    }
}
