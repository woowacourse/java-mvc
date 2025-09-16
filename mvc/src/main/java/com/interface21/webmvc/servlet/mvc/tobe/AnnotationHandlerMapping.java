package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AnnotationHandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final Object[] basePackage; // package 마다 동명 controller 존재 가능성
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(final Object... basePackage) {
        this.basePackage = basePackage;
        this.handlerExecutions = new HashMap<>();
    }

    public void initialize()
            throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        log.info("Initialized AnnotationHandlerMapping!");
        Reflections reflections = new Reflections(basePackage);
        for (Class<?> controllerClass : reflections.getTypesAnnotatedWith(Controller.class)) {
            RequestMapping controllerRequestMapping = controllerClass.getAnnotation(RequestMapping.class);
            String basePath = "";
            if (controllerRequestMapping != null) {
                basePath = controllerRequestMapping.value();
            }

            Method[] methods = controllerClass.getMethods();
            for (Method method : methods) {
                RequestMapping methodRequestMapping = method.getAnnotation(RequestMapping.class);

                if (methodRequestMapping == null) {
                    return ;
                }
                String methodPath = methodRequestMapping.value();
                String fullPath = basePath + methodPath;

                RequestMethod[] requestMethods = methodRequestMapping.method();
                // TODO 2025. 9. 16. 21:07: requestMethods[0] tmp 디버깅 용
                for (RequestMethod requestMethod : requestMethods) {
                    handlerExecutions.put(new HandlerKey(fullPath, requestMethod),
                            new HandlerExecution(controllerClass.getConstructor().newInstance(), method));
                }
            }
        }
    }

    public HandlerExecution getHandler(final HttpServletRequest request) {
        HandlerKey handlerKey = new HandlerKey(request.getRequestURI(), RequestMethod.findByName(request.getMethod()));
        return handlerExecutions.get(handlerKey);
    }
}
