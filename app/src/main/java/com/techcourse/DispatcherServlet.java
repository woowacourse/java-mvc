package com.techcourse;

import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.mvc.asis.Controller;
import com.interface21.webmvc.servlet.mvc.tobe.AnnotationHandlerMapping;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerMappingRegister;
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

    private final HandlerMappingRegister handlerMappingRegister = new HandlerMappingRegister();
    private ManualHandlerMapping manualHandlerMapping;

    public DispatcherServlet() {
    }

    @Override
    public void init() {
        ManualHandlerMapping manualHandlerMapping = new ManualHandlerMapping();
        manualHandlerMapping.initialize();

        AnnotationHandlerMapping annotationHandlerMapping = new AnnotationHandlerMapping("com.techcourse.controller");
        annotationHandlerMapping.initialize();

        handlerMappingRegister.addHandlerMapping(manualHandlerMapping);
        handlerMappingRegister.addHandlerMapping(annotationHandlerMapping);
    }

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        log.debug("Method : {}, Request URI : {}", request.getMethod(), request.getRequestURI());

        try {
            Object handler = handlerMappingRegister.getHandler(request);
            String viewName = extractViewName(request, response);
            renderView(viewName, request, response);
        } catch (Throwable e) {
            log.error("Exception : {}", e.getMessage(), e);
            throw new ServletException(e.getMessage());
        }
    }

    private String extractViewName(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Controller controller = (Controller) manualHandlerMapping.getHandler(request);
        return controller.execute(request, response);
    }

    private void renderView(String viewName, HttpServletRequest request, HttpServletResponse response) throws Exception {
        JspView jspView = new JspView(viewName);
        ModelAndView modelAndView = new ModelAndView(jspView);

        jspView.render(modelAndView.getModel(), request, response);
    }
}
