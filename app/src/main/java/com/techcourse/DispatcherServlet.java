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

public class DispatcherServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(DispatcherServlet.class);

    private final ManualHandlerMapping manualHandlerMapping;
    private final AnnotationHandlerMapping annotationHandlerMapping;

    public DispatcherServlet(ManualHandlerMapping manualHandlerMapping, AnnotationHandlerMapping annotationHandlerMapping) {
        this.manualHandlerMapping = manualHandlerMapping;
        this.annotationHandlerMapping = annotationHandlerMapping;
    }

    @Override
    public void init() {
        manualHandlerMapping.initialize();
        annotationHandlerMapping.initialize();
    }

    @Override
    protected void service(final HttpServletRequest request, final HttpServletResponse response) throws ServletException {
        String requestURI = request.getRequestURI();
        String requestMethod = request.getMethod();
        log.debug("Method : {}, Request URI : {}", requestMethod, requestURI);

        try {
            Controller controller = manualHandlerMapping.getHandler(requestURI);
            HandlerExecution handler = annotationHandlerMapping.getHandler(request);

            validateDuplicate(controller, handler, requestURI);
            executeHandler(controller, handler, request, response);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void validateDuplicate(Controller controller, HandlerExecution handler, String requestURI) {
        if (controller != null && handler != null) {
            log.debug("요청을 처리하는 URI가 중복되었습니다. Handler1={}, Handelr2={}, requestURI={}", controller, handler, requestURI);
            throw new RuntimeException();
        }
    }

    private void executeHandler(Controller controller, HandlerExecution handler,
                                HttpServletRequest request, HttpServletResponse response) throws Exception {
        if (controller == null && handler == null) {
            move("redirect:404.jsp", request, response);
            return;
        }

        if (controller != null) {
            controller.execute(request, response);
            return;
        }
        handler.handle(request, response);
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
