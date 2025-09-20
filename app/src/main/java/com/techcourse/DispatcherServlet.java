package com.techcourse;

import com.interface21.web.bind.annotation.RequestMethod;
import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.View;
import com.interface21.webmvc.servlet.mvc.asis.Controller;
import com.interface21.webmvc.servlet.mvc.tobe.AnnotationHandlerMapping;
import com.interface21.webmvc.servlet.mvc.tobe.ComponentScanner;
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
    private static final String BASE_PACKAGE = "com.techcourse.controller";
    private static final List<HandlerMapping> handlerMappings = new ArrayList<>();

    public DispatcherServlet() {
    }

    @Override
    public void init() {
        ManualHandlerMapping manualHandlerMapping = new ManualHandlerMapping();
        manualHandlerMapping.initialize();
        handlerMappings.add(manualHandlerMapping);

        AnnotationHandlerMapping annotationHandlerMapping = new AnnotationHandlerMapping(new ComponentScanner(),
                BASE_PACKAGE);
        annotationHandlerMapping.initialize();
        handlerMappings.add(annotationHandlerMapping);
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
        for (HandlerMapping handlerMapping : handlerMappings) {
            String path = request.getServletPath();
            RequestMethod requestMethod = RequestMethod.valueOf(request.getMethod());
            Object handler = handlerMapping.getHandler(path, requestMethod);

            if (handler == null) {
                continue;
            }

            try {
                ModelAndView modelAndView = executeHandler(request, response, handler);
                move(modelAndView, request, response);
                return;
            } catch (Exception e) {
                log.error("Exception : {}", e.getMessage(), e);
                throw new ServletException(e.getMessage());
            }
        }
        response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        try {
            new JspView("/404.jsp").render(Map.of(), request, response);
        } catch (Exception e) {
            throw new ServletException(e.getMessage(), e);
        }
    }

    private ModelAndView executeHandler(final HttpServletRequest request, final HttpServletResponse response,
                                        final Object handler) throws Exception {
        if (handler instanceof Controller controller) {
            return controller.execute(request, response);
        } else if (handler instanceof HandlerExecution execution) {
            return execution.handle(request, response);
        } else {
            throw new RuntimeException();
        }
    }

    private void move(final ModelAndView modelAndView, final HttpServletRequest request,
                      final HttpServletResponse response) throws Exception {
        View view = modelAndView.getView();
        view.render(modelAndView.getModel(), request, response);
    }
}
