package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Set;
import org.reflections.ReflectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class AnnotationHandlerMapping implements HandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final Object[] basePackage;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(final Object... basePackage) {
        this.basePackage = basePackage;
        this.handlerExecutions = new HashMap<>();
    }

    public void initialize() {
        ControllerScanner cs = new ControllerScanner(basePackage);

        Map<Class<?>, Object> controllers = cs.getControllers();

        for (Map.Entry<Class<?>, Object> entry : controllers.entrySet()) {

            Class<?> clazz = entry.getKey();
            Object instance = entry.getValue();
            Set<Method> methods = ReflectionUtils.getAllMethods(clazz,
                    ReflectionUtils.withAnnotation(RequestMapping.class));

            for (Method method : methods) {
                RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);

                RequestMethod[] requestMethods = requestMapping.method();
                if (requestMethods.length == 0) {
                    requestMethods = RequestMethod.values();
                }

                for (RequestMethod requestMethod : requestMethods) {
                    HandlerKey handlerKey = new HandlerKey(requestMapping.value(), requestMethod);
                    handlerExecutions.put(handlerKey, new HandlerExecution(instance, method));
                }
            }
        }
    }

    @Override
    public Object getHandler(HttpServletRequest request) {
        HandlerKey handlerKey = new HandlerKey(request.getRequestURI(), RequestMethod.valueOf(request.getMethod()));
        return handlerExecutions.get(handlerKey);
    }
}
