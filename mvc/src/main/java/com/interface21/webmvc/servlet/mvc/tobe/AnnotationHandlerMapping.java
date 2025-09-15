package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.reflections.Reflections;
import org.reflections.scanners.Scanners;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AnnotationHandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final Object[] basePackage;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(final Object... basePackage) {
        this.basePackage = basePackage;
        this.handlerExecutions = new HashMap<>();
    }

    public void initialize() {
        log.info("Initialized AnnotationHandlerMapping!");
        try {
            Reflections reflections = new Reflections(basePackage);
            Set<Class<?>> controllers = reflections.getTypesAnnotatedWith(Controller.class);
            for (Class<?> controller : controllers) {
                Object controllerInstance = controller.getConstructor().newInstance();
                Method[] controllerMethods = controller.getMethods();
                for (Method controllerMethod : controllerMethods) {
                    if (controllerMethod.isAnnotationPresent(RequestMapping.class)) {
                        RequestMapping requestMappingAnnotation = controllerMethod.getAnnotation(
                            RequestMapping.class);
                        RequestMethod[] httpMethods = requestMappingAnnotation.method();
                        String path = requestMappingAnnotation.value();
                        for (RequestMethod httpMethod : httpMethods) {
                            HandlerKey handlerKey = new HandlerKey(path, httpMethod);
                            HandlerExecution handlerExecution = new HandlerExecution(controllerInstance, controllerMethod);
                            this.handlerExecutions.put(handlerKey, handlerExecution);
                            System.out.println(path + " " + httpMethod);
                        }
                    }
                }
            }
        } catch (Exception exception) {
            log.error("알 수 없는 오류가 발생했습니다.");
        }
    }

    public HandlerExecution getHandler(final HttpServletRequest request) {
        HandlerKey handlerKey = new HandlerKey(request.getRequestURI(), RequestMethod.from(request.getMethod()));
        return this.handlerExecutions.get(handlerKey);
    }
}
