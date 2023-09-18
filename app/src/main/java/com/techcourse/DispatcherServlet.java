package com.techcourse;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webmvc.org.springframework.web.servlet.mvc.asis.Controller;
import webmvc.org.springframework.web.servlet.mvc.tobe.AnnotationHandlerMapping;
import webmvc.org.springframework.web.servlet.mvc.tobe.HandlerExecution;
import webmvc.org.springframework.web.servlet.view.JspView;

import java.util.Optional;

public class DispatcherServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(DispatcherServlet.class);

    private AnnotationHandlerMapping annotationHandlerMapping;
    private ManualHandlerMapping manualHandlerMapping;

    public DispatcherServlet() {
    }

    @Override
    public void init() {
        annotationHandlerMapping = new AnnotationHandlerMapping("com.techcourse.controller");
        annotationHandlerMapping.initialize();
        manualHandlerMapping = new ManualHandlerMapping();
        manualHandlerMapping.initialize();
    }

    @Override
    protected void service(final HttpServletRequest request, final HttpServletResponse response)
            throws ServletException {
        final String requestURI = request.getRequestURI();
        log.debug("Method : {}, Request URI : {}", request.getMethod(), requestURI);

        try {
            Optional<HandlerExecution> handler = annotationHandlerMapping.getHandler(request);
            if (handler.isPresent()) {
                annotatedHandle(request, response, handler.get());
                return;
            }
            manualHandle(request, response, manualHandlerMapping.getHandler(requestURI));
        } catch (Exception exception) {
            log.error("Exception : {}", exception.getMessage(), exception);
            throw new ServletException(exception.getMessage());
        }
    }

    // TODO Servlet 인터페이스로 추상화, HandlerAdaptor 사용
    private void manualHandle(HttpServletRequest request, HttpServletResponse response, Controller controller) throws Exception {
        final var viewName = controller.execute(request, response);
        move(viewName, request, response);
    }

    private void move(final String viewName, final HttpServletRequest request, final HttpServletResponse response)
            throws Exception {
        if (viewName.startsWith(JspView.REDIRECT_PREFIX)) {
            response.sendRedirect(viewName.substring(JspView.REDIRECT_PREFIX.length()));
            return;
        }

        final var requestDispatcher = request.getRequestDispatcher(viewName);
        requestDispatcher.forward(request, response);
    }

    private void annotatedHandle(HttpServletRequest request, HttpServletResponse response, HandlerExecution handler) throws Exception {
        final var modelAndView = handler.handle(request, response);
        final var view = modelAndView.getView();
        view.render(modelAndView.getModel(), request, response);
    }
}
