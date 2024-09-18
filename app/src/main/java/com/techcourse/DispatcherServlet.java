package com.techcourse;

import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.View;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerAdaptor;
import com.interface21.webmvc.servlet.mvc.tobe.handleradaptor.ControllerHandlerAdaptor;
import com.interface21.webmvc.servlet.mvc.tobe.handleradaptor.HandlerAdaptors;
import com.interface21.webmvc.servlet.mvc.tobe.handleradaptor.HandlerExecutionHandlerAdaptor;
import com.interface21.webmvc.servlet.mvc.tobe.handlermapping.AnnotationHandlerMapping;
import com.interface21.webmvc.servlet.mvc.tobe.handlermapping.HandlerMappings;
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

    private HandlerMappings handlerMappings;
    private HandlerAdaptors handlerAdaptors;

    public DispatcherServlet() {
    }

    @Override
    public void init() {
        initHandlerMappings();
        initHandlerAdaptors();
    }

    private void initHandlerMappings() {
        ManualHandlerMapping manualHandlerMapping = new ManualHandlerMapping();
        manualHandlerMapping.initialize();

        String packageName = getClass().getPackageName();
        AnnotationHandlerMapping annotationHandlerMapping = new AnnotationHandlerMapping(packageName);
        annotationHandlerMapping.initialize();

        handlerMappings = new HandlerMappings(annotationHandlerMapping, manualHandlerMapping);
    }

    private void initHandlerAdaptors() {
        handlerAdaptors = new HandlerAdaptors(new HandlerExecutionHandlerAdaptor(), new ControllerHandlerAdaptor());
    }

    @Override
    protected void service(final HttpServletRequest request, final HttpServletResponse response)
            throws ServletException {
        log.debug("Method : {}, Request URI : {}", request.getMethod(), request.getRequestURI());
        try {
            Object handler = handlerMappings.findHandler(request);
            HandlerAdaptor handlerAdaptor = handlerAdaptors.findHandlerAdaptor(handler);
            ModelAndView modelAndView = handlerAdaptor.handle(request, response, handler);
            move(modelAndView, request, response);
        } catch (Throwable e) {
            log.error("Exception : {}", e.getMessage(), e);
            throw new ServletException(e.getMessage());
        }
    }

    private void move(ModelAndView modelAndView, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        Map<String, Object> model = modelAndView.getModel();
        View view = modelAndView.getView();
        view.render(model, request, response);
    }
}
