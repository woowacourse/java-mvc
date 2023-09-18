package com.techcourse;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webmvc.org.springframework.web.servlet.ModelAndView;
import webmvc.org.springframework.web.servlet.View;
import webmvc.org.springframework.web.servlet.mvc.asis.Controller;
import webmvc.org.springframework.web.servlet.mvc.tobe.AnnotationHandlerMapping;
import webmvc.org.springframework.web.servlet.mvc.tobe.HandlerExecution;
import webmvc.org.springframework.web.servlet.view.JspView;

public class DispatcherServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(DispatcherServlet.class);

    private ManualHandlerMapping manualHandlerMapping;
    private AnnotationHandlerMapping annotationHandlerMapping;

    public DispatcherServlet() {
    }

    @Override
    public void init() {
        final String packageName = getClass().getPackageName();

        annotationHandlerMapping = new AnnotationHandlerMapping(packageName);
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
            final Controller controller = manualHandlerMapping.getHandler(requestURI);
            if (Objects.nonNull(controller)) {
                handleByControllerInterface(controller, request, response);
                return;
            }
            final HandlerExecution handler = annotationHandlerMapping.getHandler(request);
            if (Objects.nonNull(handler)) {
                handleByRequestMappingAnnotation(handler, request, response);
                return;
            }

            setNotFound(request, response);
        } catch (Exception e) {
            log.error("Exception : {}", e.getMessage(), e);
            throw new ServletException(e.getMessage());
        }
    }

    private void handleByControllerInterface(
        final Controller controller,
        final HttpServletRequest request,
        final HttpServletResponse response
    ) throws Exception {
        final String viewName = controller.execute(request, response);
        move(viewName, request, response);
    }

    private void move(final String viewName, final HttpServletRequest request,
        final HttpServletResponse response) throws IOException, ServletException {
        if (viewName.startsWith(JspView.REDIRECT_PREFIX)) {
            response.sendRedirect(viewName.substring(JspView.REDIRECT_PREFIX.length()));
            return;
        }

        final var requestDispatcher = request.getRequestDispatcher(viewName);
        requestDispatcher.forward(request, response);
    }

    private void handleByRequestMappingAnnotation(
        final HandlerExecution handlerExecution,
        final HttpServletRequest request,
        final HttpServletResponse response
    ) throws Exception {
        final ModelAndView modelAndView = handlerExecution.handle(request, response);
        final View view = modelAndView.getView();
        if (modelAndView.isViewExist()) {
            view.render(modelAndView.getModel(), request, response);
        }
    }

    private void setNotFound(final HttpServletRequest request, final HttpServletResponse response)
        throws ServletException, IOException {
        final RequestDispatcher requestDispatcher = request.getRequestDispatcher("404.jsp");
        response.setStatus(404);
        requestDispatcher.forward(request, response);
    }
}
