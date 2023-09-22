package com.techcourse;

import com.techcourse.exception.NoSuchHandlerFoundException;
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

    private HandlerAdaptors handlerAdaptors;

    public DispatcherServlet() {
    }

    @Override
    public void init() {
        handlerAdaptors = HandlerAdaptors.getInstance();
    }

    @Override
    protected void service(final HttpServletRequest request, final HttpServletResponse response)
            throws ServletException {
        final String requestURI = request.getRequestURI();
        log.debug("Method : {}, Request URI : {}", request.getMethod(), requestURI);

        try {
            final var viewName = getViewName(request, response);
            move(viewName, request, response);
        } catch (Throwable e) {
            log.error("Exception : {}", e.getMessage(), e);
            throw new ServletException(e.getMessage());
        }
    }

    private String getViewName(final HttpServletRequest request, final HttpServletResponse response)
            throws Exception {
        HandlerAdaptor handlerAdaptor = handlerAdaptors.getHandler(request);
        Object handler = handlerAdaptor.getHandlerMapping();
        if (handler instanceof AnnotationHandlerMapping) {
            AnnotationHandlerMapping annotationHandlerMapping = (AnnotationHandlerMapping) handler;
            HandlerExecution handlerExecution = (HandlerExecution) annotationHandlerMapping.getHandler(request);
            return handlerExecution.handle(request, response).getViewName();
        }
        if (handler instanceof ManualHandlerMapping) {
            ManualHandlerMapping manualHandlerMapping = (ManualHandlerMapping) handler;
            Controller controller = manualHandlerMapping.getHandler(request.getRequestURI());
            return controller.execute(request, response);
        }
        log.error("Not Found");
        throw new NoSuchHandlerFoundException();
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
}
