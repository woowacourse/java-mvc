package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.context.stereotype.Controller;
import com.interface21.core.util.ReflectionUtils;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import jakarta.servlet.http.HttpServletRequest;
import org.reflections.Reflections;
import org.reflections.scanners.Scanners;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.*;

public class AnnotationHandlerMapping implements HandlerMapping{

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final Object[] basePackage;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(final Object... basePackage) {
        this.basePackage = basePackage;
        handlerExecutions = new HashMap<>();
    }

    public void initialize() {
        for (Object bp : basePackage) {
            Reflections reflections = new Reflections(bp, Scanners.TypesAnnotated);
            Set<Class<?>> controllerTypes = reflections.getTypesAnnotatedWith(Controller.class);

            for (Class<?> controllerType : controllerTypes) {
                Object controller = getControllerInstance(controllerType);

                List<Method> methods = ReflectionUtils.getAllMethods(controllerType, RequestMapping.class);

                methods.forEach(
                        method -> {
                            if (method.isAnnotationPresent(RequestMapping.class)) {
                                RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
                                HandlerExecution handlerExecution = new HandlerExecution(controller, method);

                                for (RequestMethod requestMethod : requestMapping.method()) {
                                    handlerExecutions.put(
                                            new HandlerKey(requestMapping.value(), requestMethod),
                                            handlerExecution
                                    );
                                }
                            }
                        }
                );
            }
        }

        log.info("Initialized AnnotationHandlerMapping!");
    }

    @Override
    public HandlerExecution getHandler(final HttpServletRequest request) {
        String requestURI = request.getRequestURI();

        log.debug("Request Mapping Uri : {}", requestURI);

        RequestMethod method = RequestMethod.valueOf(request.getMethod());
        HandlerKey handlerKey = new HandlerKey(requestURI, method);
        return handlerExecutions.get(handlerKey);
    }

    private Object getControllerInstance(Class<?> controllerType) {
        try {
            return controllerType.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }
}
