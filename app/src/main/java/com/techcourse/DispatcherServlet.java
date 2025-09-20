package com.techcourse;

import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.View;
import com.interface21.webmvc.servlet.mvc.asis.Controller;
import com.interface21.webmvc.servlet.mvc.tobe.AnnotationHandlerMapping;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerExecution;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DispatcherServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(DispatcherServlet.class);

    private HandlerMappingRegistry handlerMappingRegistry;

    public DispatcherServlet() {
        handlerMappingRegistry = new HandlerMappingRegistry();
    }

    @Override
    public void init() {
        try {
            ManualHandlerMapping manualHandlerMapping = new ManualHandlerMapping();
            AnnotationHandlerMapping annotationHandlerMapping = new AnnotationHandlerMapping(
                    "com.techcourse.annotationController");
            manualHandlerMapping.initialize();
            annotationHandlerMapping.initialize();
            handlerMappingRegistry.addHandlerMapping(manualHandlerMapping);
            handlerMappingRegistry.addHandlerMapping(annotationHandlerMapping);
        } catch (Exception e) {
            log.warn(e.getMessage());
            System.exit(1);
        }
    }

    @Override
    protected void service(final HttpServletRequest request, final HttpServletResponse response)
            throws ServletException {
        final String requestURI = request.getRequestURI();
        log.debug("Method : {}, Request URI : {}", request.getMethod(), requestURI);
        try {
            Object handler = handlerMappingRegistry.getHandler(request);
            ModelAndView modelAndView = handle(handler, request, response);
            render(modelAndView, request, response);
        } catch (Throwable e) {
            log.error("Exception : {}", e.getMessage(), e);
            throw new ServletException(e.getMessage());
        }
    }

    private void render(ModelAndView modelAndView, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        View view = modelAndView.getView();
        Map<String, Object> model = modelAndView.getModel();
        view.render(model, request, response);
    }

    private ModelAndView handle(Object handler, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        if (handler instanceof Controller) {
            return ((Controller) handler).execute(request, response);
        } else if (handler instanceof HandlerExecution) {
            return ((HandlerExecution) handler).handle(request, response);
        } else {
            throw new RuntimeException("잘못된 핸들러입니다.");
        }
    }
}
