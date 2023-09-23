package com.techcourse;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webmvc.org.springframework.web.servlet.ModelAndView;
import webmvc.org.springframework.web.servlet.mvc.tobe.AnnotationHandlerMapping;
import webmvc.org.springframework.web.servlet.mvc.tobe.HandlerExecution;
import webmvc.org.springframework.web.servlet.view.JspView;

import java.io.IOException;

public class DispatcherServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(DispatcherServlet.class);

    private ManualHandlerMapping manualHandlerMapping;
    private AnnotationHandlerMapping annotationHandlerMapping;

    public DispatcherServlet() {
    }

    @Override
    public void init() {
        manualHandlerMapping = new ManualHandlerMapping();
        manualHandlerMapping.initialize();
        annotationHandlerMapping = new AnnotationHandlerMapping(getAllSubPackageName(Application.class));
        annotationHandlerMapping.initialize();
    }

    private String getAllSubPackageName(Class<?> mainClass) {
        return mainClass.getPackageName() + ".*";
    }

    @Override
    protected void service(
            final HttpServletRequest request,
            final HttpServletResponse response
    ) throws ServletException {
        final String requestURI = request.getRequestURI();
        log.debug("Method : {}, Request URI : {}", request.getMethod(), requestURI);

        if (annotationHandlerMapping.containsHandler(request)) {
            final HandlerExecution handler = (HandlerExecution) annotationHandlerMapping.getHandler(request);
            handleByAnnotation(request, response, handler);

            return;
        }

        handleByManual(request, response, requestURI);
    }

    private void handleByAnnotation(
            final HttpServletRequest request,
            final HttpServletResponse response,
            final HandlerExecution handler
    ) throws ServletException {
        try {
            final ModelAndView modelAndView = handler.handle(request, response);
            modelAndView.getView().render(modelAndView.getModel(), request, response);
        } catch (Exception e) {
            log.error("Exception : {}", e.getMessage(), e);
            throw new ServletException(e.getMessage());
        }
    }

    private void handleByManual(
            final HttpServletRequest request,
            final HttpServletResponse response,
            final String requestURI
    ) throws ServletException {
        try {
            final var controller = manualHandlerMapping.getHandler(requestURI);
            final var viewName = controller.execute(request, response);
            move(viewName, request, response);
        } catch (Exception e) {
            log.error("Exception : {}", e.getMessage(), e);
            throw new ServletException(e.getMessage());
        }
    }

    private void move(
            final String viewName,
            final HttpServletRequest request,
            final HttpServletResponse response
    ) throws IOException, ServletException {
        if (viewName.startsWith(JspView.REDIRECT_PREFIX)) {
            response.sendRedirect(viewName.substring(JspView.REDIRECT_PREFIX.length()));
            return;
        }

        final var requestDispatcher = request.getRequestDispatcher(viewName);
        requestDispatcher.forward(request, response);
    }
}
