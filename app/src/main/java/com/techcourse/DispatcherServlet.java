package com.techcourse;

import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.mvc.exception.NonExistenceHandlerException;
import com.interface21.webmvc.servlet.mvc.tobe.AnnotationHandlerMapping;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerExecution;
import com.interface21.webmvc.servlet.view.JspView;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DispatcherServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(DispatcherServlet.class);
    private static final String INTERNAL_SERVER_ERROR_JSP = "/500.jsp";
    private static final String PAGE_NOT_FOUND_JSP = "/404.jsp";

    private final HandlerMappingRegistry handlerMappingRegistry;

    public DispatcherServlet() {
        this.handlerMappingRegistry = new HandlerMappingRegistry();
    }

    @Override
    public void init() {
        AnnotationHandlerMapping annotationHandlerMapping = new AnnotationHandlerMapping("com.techcourse.controller");
        annotationHandlerMapping.initialize();
        handlerMappingRegistry.addHandlerMapping(annotationHandlerMapping);
    }

    @Override
    protected void service(
            final HttpServletRequest request,
            final HttpServletResponse response
    ) throws ServletException {
        log.info("Received request: {} {}", request.getMethod(), request.getRequestURI());

        try {
            HandlerExecution handler = getHandler(request);
            ModelAndView mav = invokeHandler(handler, request, response);
            renderView(mav, request, response);
        } catch (NonExistenceHandlerException e) {
            log.error("Unhandled exception: {}", e.getMessage(), e);
            renderView(new ModelAndView(new JspView(PAGE_NOT_FOUND_JSP)), request, response);
            throw new ServletException("Error occurred while handling the request.", e);
        } catch (Exception e) {
            log.error("Unhandled exception: {}", e.getMessage(), e);
            renderView(new ModelAndView(new JspView(INTERNAL_SERVER_ERROR_JSP)), request, response);
            throw new ServletException("Error occurred while handling the request.", e);
        }
    }

    private HandlerExecution getHandler(HttpServletRequest request) {
        log.debug("Attempting to retrieve handler for request URI: {}", request.getRequestURI());
        return handlerMappingRegistry.getHandler(request);
    }

    private ModelAndView invokeHandler(Object handler, HttpServletRequest request, HttpServletResponse response) {
        if (!(handler instanceof HandlerExecution)) {
            renderView(new ModelAndView(new JspView(INTERNAL_SERVER_ERROR_JSP)), request, response);
            throw new ClassCastException("Handler is not an instance of HandlerExecution.");
        }
        log.debug("Invoking handler: {}", handler.getClass().getName());
        return ((HandlerExecution) handler).handle(request, response);
    }

    private void renderView(ModelAndView mav, HttpServletRequest request, HttpServletResponse response) {
        log.debug("Rendering view: {}", mav.getView().getClass().getName());
        try {
            mav.getView().render(mav.getModel(), request, response);
        } catch (Exception e) {
            renderView(new ModelAndView(new JspView(INTERNAL_SERVER_ERROR_JSP)), request, response);
            throw new IllegalArgumentException();
        }
    }
}
