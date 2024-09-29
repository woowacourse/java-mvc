package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AnnotationHandlerMapping implements HandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);
    public static final int EMPTY = 0;

    private final Object[] basePackage;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(final Object... basePackage) {
        this.basePackage = basePackage;
        this.handlerExecutions = new HashMap<>();
        initialize();
    }

    private void initialize() {
        ControllerScanner controllerScanner = new ControllerScanner(basePackage);
        Map<Class<?>, Object> controllers = controllerScanner.getControllers();
        initializeHandlerExecutions(controllers);
        log.info("Initialized AnnotationHandlerMapping!");
    }

    private void initializeHandlerExecutions(Map<Class<?>, Object> controllers) {
        for (Map.Entry<Class<?>, Object> entry : controllers.entrySet()) {
            List<Method> requestMappingMethods = getRequestMappingMethods(entry.getKey());
            Object controller = entry.getValue();

            setHandlerExecutions(requestMappingMethods, controller);
        }
    }

    public List<Method> getRequestMappingMethods(Class<?> controllerClass) {
        Method[] methods = controllerClass.getDeclaredMethods();
        return Arrays.stream(methods)
                .filter(method -> method.isAnnotationPresent(RequestMapping.class))
                .toList();
    }

    private void setHandlerExecutions(List<Method> requestMappingMethods, Object controller) {
        for (Method requestMappingMethod : requestMappingMethods) {
            RequestMapping annotation = requestMappingMethod.getAnnotation(RequestMapping.class);
            setHandlerExecutionsByMethod(controller, requestMappingMethod, annotation);
        }
    }

    private void setHandlerExecutionsByMethod(Object controller, Method method, RequestMapping annotation) {
        String url = annotation.value();
        RequestMethod[] requestMethods = annotation.method();
        if (requestMethods.length == EMPTY) {
            requestMethods = RequestMethod.values();
        }

        HandlerExecution handlerExecution = new HandlerExecution(controller, method);
        for (RequestMethod requestMethod : requestMethods) {
            HandlerKey handlerKey = new HandlerKey(url, requestMethod);
            handlerExecutions.put(handlerKey, handlerExecution);
        }
    }

    @Override
    public Object getHandler(final HttpServletRequest request) {
        HandlerKey handlerKey = new HandlerKey(request.getRequestURI(), RequestMethod.valueOf(request.getMethod()));
        return handlerExecutions.get(handlerKey);
    }
}
