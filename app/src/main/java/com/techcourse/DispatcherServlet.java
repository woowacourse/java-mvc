package com.techcourse;

import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.View;
import com.interface21.webmvc.servlet.mvc.asis.Controller;
import com.interface21.webmvc.servlet.mvc.tobe.AnnotationHandlerMapping;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerExecution;
import com.interface21.webmvc.servlet.view.JspView;
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

    private ManualHandlerMapping manualHandlerMapping;
    private AnnotationHandlerMapping annotationHandlerMapping;

    public DispatcherServlet() {
    }

    @Override
    public void init() {
        manualHandlerMapping = new ManualHandlerMapping();
        manualHandlerMapping.initialize();

        String packageName = getClass().getPackageName();
        annotationHandlerMapping = new AnnotationHandlerMapping(packageName);
        annotationHandlerMapping.initialize();
    }

    @Override
    protected void service(final HttpServletRequest request, final HttpServletResponse response)
            throws ServletException {
        log.debug("Method : {}, Request URI : {}", request.getMethod(), request.getRequestURI());
        try {
            Object handler = getHandler(request);
            ModelAndView modelAndView = handle(handler, request, response);
            move(modelAndView, request, response);
        } catch (Throwable e) {
            log.error("Exception : {}", e.getMessage(), e);
            throw new ServletException(e.getMessage());
        }
    }

    private Object getHandler(HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        try {
            return annotationHandlerMapping.getHandler(request);
        } catch (IllegalArgumentException e) {
            log.info("annotationHandlerMapping에 존재하지 않는 요청 : {} {}", request.getMethod(), requestURI);
        }
        return manualHandlerMapping.getHandler(requestURI);
    }

    private ModelAndView handle(Object handler, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        if (handler instanceof HandlerExecution handlerExecution) {
            return handlerExecution.handle(request, response);
        }
        if (handler instanceof Controller controller) {
            String viewName = controller.execute(request, response);
            return new ModelAndView(new JspView(viewName));
        }
        throw new IllegalArgumentException("지원하지 않는 handler 입니다.");
    }

    private void move(ModelAndView modelAndView, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        Map<String, Object> model = modelAndView.getModel();
        View view = modelAndView.getView();
        view.render(model, request, response);
    }
}
