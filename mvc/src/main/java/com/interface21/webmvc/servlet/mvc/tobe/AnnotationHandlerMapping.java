package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.reflections.ReflectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AnnotationHandlerMapping implements HandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final Object[] basePackage;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(final Object... basePackage) {
        this.basePackage = basePackage;
        this.handlerExecutions = new HashMap<>();
    }

    public void initialize() {
        log.info("Initialized AnnotationHandlerMapping!");

        Map<Class<?>, Object> controllers = new ControllerScanner(basePackage).getControllers();
        controllers.forEach((controller, controllerInstance) ->
                ReflectionUtils.getAllMethods(controller, ReflectionUtils.withAnnotation(RequestMapping.class))
                        .forEach(method -> addHandlerExecutions(controllerInstance, method))
        );
    }

    private void addHandlerExecutions(final Object controllerInstance, final Method method) {
        RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
        String mappingUrl = requestMapping.value();
        List<RequestMethod> requestMethods = getRequestMethods(requestMapping);

        requestMethods.forEach(requestMethod -> {
            HandlerKey handlerKey = new HandlerKey(mappingUrl, requestMethod);
            HandlerExecution handlerExecution = new HandlerExecution(controllerInstance, method);
            handlerExecutions.put(handlerKey, handlerExecution);
        });
    }

    private List<RequestMethod> getRequestMethods(final RequestMapping requestMapping) {
        List<RequestMethod> requestMethods = List.of(requestMapping.method());
        if (requestMethods.isEmpty()) {
            requestMethods = List.of(RequestMethod.values());
        }
        return requestMethods;
    }

    public HandlerExecution getHandler(final HttpServletRequest request) {
        HandlerKey handlerKey = new HandlerKey(request.getRequestURI(), RequestMethod.valueOf(request.getMethod()));
        return handlerExecutions.get(handlerKey);
    }
}
