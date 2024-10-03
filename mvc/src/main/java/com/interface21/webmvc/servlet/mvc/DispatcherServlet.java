package com.interface21.webmvc.servlet.mvc;

import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.View;
import com.interface21.webmvc.servlet.mvc.tobe.AnnotationHandlerMapping;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerExecution;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DispatcherServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(DispatcherServlet.class);

    private AnnotationHandlerMapping annotationHandlerMapping;

    public DispatcherServlet(AnnotationHandlerMapping annotationHandlerMapping) {
        this.annotationHandlerMapping = annotationHandlerMapping;
    }

    @Override
    public void init() {
        annotationHandlerMapping.initialize();
    }

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response)
            throws ServletException {
        log.debug("Method : {}, Request URI : {}", request.getMethod(), request.getRequestURI());

        HandlerExecution handlerExecution = (HandlerExecution) annotationHandlerMapping.getHandler(request);
        if (handlerExecution != null) {
            handleWithHandlerExecution(handlerExecution, request, response);
        }
    }

    private void handleWithHandlerExecution(HandlerExecution handlerExecution, HttpServletRequest request,
                                            HttpServletResponse response) throws ServletException {
        try {
            ModelAndView modelAndView = handlerExecution.handle(request, response);
            View view = modelAndView.getView();
            view.render(modelAndView.getModel(), request, response);
        } catch (Exception e) {
            log.error("Exception : {}", e.getMessage(), e);
            throw new ServletException(e.getMessage());
        }
    }
}
