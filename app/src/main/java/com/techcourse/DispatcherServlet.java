package com.techcourse;

import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.mvc.asis.Controller;
import com.interface21.webmvc.servlet.mvc.tobe.AnnotationHandlerMapping;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerExecution;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerMapping;
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

    private List<HandlerMapping> handlerMappings;

    public DispatcherServlet() {
    }

    @Override
    public void init() {
        handlerMappings = new ArrayList<>();
        AnnotationHandlerMapping annotationHandlerMapping = new AnnotationHandlerMapping();
        annotationHandlerMapping.initialize();
        ManualHandlerMapping manualHandlerMapping = new ManualHandlerMapping();
        manualHandlerMapping.initialize();
        handlerMappings.add(annotationHandlerMapping);
        handlerMappings.add(manualHandlerMapping);
    }

    @Override
    protected void service(final HttpServletRequest httpServletRequest, final HttpServletResponse httpServletResponse) throws ServletException {
        final String requestURI = httpServletRequest.getRequestURI();
        log.debug("Method : {}, Request URI : {}", httpServletRequest.getMethod(), requestURI);

        try {
            Object handler = getHandler(httpServletRequest);
            ModelAndView modelAndView;
            if (handler instanceof Controller) {
                modelAndView = ((Controller) handler).execute(httpServletRequest, httpServletResponse);
            } else if (handler instanceof HandlerExecution) {
                modelAndView = ((HandlerExecution) handler).handle(httpServletRequest, httpServletResponse);
            } else {
                throw new ServletException("지원하지 않는 응답 방식입니다.");
            }
            modelAndView.render(httpServletRequest, httpServletResponse);
        } catch (Throwable e) {
            log.error("Exception : {}", e.getMessage(), e);
            throw new ServletException(e.getMessage());
        }
    }

    private Object getHandler(HttpServletRequest httpServletRequest) throws ServletException {
        for (HandlerMapping handlerMapping : this.handlerMappings) {
            Object handler = handlerMapping.getHandler(httpServletRequest);
            if (handler != null) {
                return handler;
            }
        }
        throw new ServletException("제공하지 않는 URI입니다.");
    }
}
