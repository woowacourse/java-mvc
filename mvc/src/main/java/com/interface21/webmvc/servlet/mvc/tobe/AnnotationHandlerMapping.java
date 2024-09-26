package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.web.bind.annotation.RequestMethod;
import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.View;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AnnotationHandlerMapping implements HandlerMapping{

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final Packages basePackage;
    private final AnnotationExecutions handlerExecutions;

    public AnnotationHandlerMapping(final Object... basePackage) {
        this.basePackage = new Packages(basePackage);
        this.handlerExecutions = new AnnotationExecutions();
    }

    @Override
    public void initialize() {
        basePackage.initialize();
        List<Object> controllerInstances = basePackage.controllerInstances();
        for (Object controllerInstance : controllerInstances) {
            List<Method> requestMappingMethods = basePackage.findRequestMappingMethodsByControllerInstance(controllerInstance);
            for (Method requestMappingMethod : requestMappingMethods) {
                handlerExecutions.addExecutor(controllerInstance, requestMappingMethod);
            }
        }
        log.info("AnnotationHandlerMapping 초기화 완료");
    }

    @Override
    public void handler(HttpServletRequest request, HttpServletResponse response) throws Exception {
        HandlerExecution handler = getHandler(request);
        ModelAndView modelAndView = handler.handle(request, response);
        View view = modelAndView.getView();
        view.render(modelAndView.getModel(), request, response);
    }

    private HandlerExecution getHandler(final HttpServletRequest request) {
        return handlerExecutions.getHandler(request.getRequestURI(), RequestMethod.findByName(request.getMethod()));
    }

    @Override
    public boolean canHandle(HttpServletRequest request) {
        return handlerExecutions.hasHandler(request.getRequestURI(), RequestMethod.findByName(request.getMethod()));
    }
}
