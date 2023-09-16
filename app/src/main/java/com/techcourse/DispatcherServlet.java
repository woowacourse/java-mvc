package com.techcourse;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webmvc.org.springframework.web.servlet.ModelAndView;
import webmvc.org.springframework.web.servlet.View;
import webmvc.org.springframework.web.servlet.mvc.tobe.AnnotationHandlerMapping;
import webmvc.org.springframework.web.servlet.mvc.tobe.HandlerExecution;
import webmvc.org.springframework.web.servlet.view.JspView;

import java.util.Map;

public class DispatcherServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(DispatcherServlet.class);

    private AnnotationHandlerMapping annotationHandlerMapping;
    private ManualHandlerMapping manualHandlerMapping;

    public DispatcherServlet() {
    }

    @Override
    public void init() {
        manualHandlerMapping = new ManualHandlerMapping();
        manualHandlerMapping.initialize();
        annotationHandlerMapping = new AnnotationHandlerMapping("com.techcourse");
        annotationHandlerMapping.initialize();
    }

    @Override
    protected void service(final HttpServletRequest request, final HttpServletResponse response) throws ServletException {
        final String requestURI = request.getRequestURI();
        log.debug("Method : {}, Request URI : {}", request.getMethod(), requestURI);

        if (tryAnnotationHandlerMapping(request, response)){
            return;
        }

        tryManualHandlerMapping(request, response);
    }

    private boolean tryAnnotationHandlerMapping(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        try {
            final var handlerExecution = (HandlerExecution) annotationHandlerMapping.getHandler(request);
            if (handlerExecution != null) {
                final ModelAndView modelAndView = handlerExecution.handle(request, response);
                render(modelAndView, request, response);
                return true;
            }
        } catch (Exception e) {
            log.error("Exception : {}", e.getMessage(), e);
            throw new ServletException(e.getMessage());
        }
        return false;
    }

    private void render(final ModelAndView modelAndView, final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        final View view = modelAndView.getView();
        final Map<String, Object> model = modelAndView.getModel();
        view.render(model, request, response);
    }

    private boolean tryManualHandlerMapping(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        try {
            final var controller = manualHandlerMapping.getHandler(request.getRequestURI());
            if (controller != null){
                final var viewName = controller.execute(request, response);
                move(viewName, request, response);
                return true;
            }
            return false;
        } catch (Throwable e) {
            log.error("Exception : {}", e.getMessage(), e);
            throw new ServletException(e.getMessage());
        }
    }

    private void move(final String viewName, final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        if (viewName.startsWith(JspView.REDIRECT_PREFIX)) {
            response.sendRedirect(viewName.substring(JspView.REDIRECT_PREFIX.length()));
            return;
        }

        final var requestDispatcher = request.getRequestDispatcher(viewName);
        requestDispatcher.forward(request, response);
    }
}
