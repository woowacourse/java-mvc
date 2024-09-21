package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.reflections.Reflections;
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

    public void initialize()
            throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        Reflections reflections = new Reflections(basePackage);
        initializeMappingInformation(reflections);
        log.info("Initialized AnnotationHandlerMapping!");
    }

    private void initializeMappingInformation(Reflections reflections)
            throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        Set<Class<?>> controllerClasses = reflections.getTypesAnnotatedWith(Controller.class);
        for (Class<?> controllerClass : controllerClasses) {
            Method[] declaredMethods = controllerClass.getDeclaredMethods();
            initializeRequestMappingMethod(controllerClass, declaredMethods);
        }
    }

    private void initializeRequestMappingMethod(Class<?> controllerClass, Method[] declaredMethods)
            throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        for (Method declaredMethod : declaredMethods) {
            RequestMapping annotation = declaredMethod.getAnnotation(RequestMapping.class);
            putMappingHandlerExecution(controllerClass, declaredMethod, annotation);
        }
    }

    private void putMappingHandlerExecution(Class<?> controllerClass, Method declaredMethod, RequestMapping annotation)
            throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        if (annotation != null) {
            List<RequestMethod> annotationMethodTypes = findSupportedRequestMethods(annotation);
            Object controllerClassInstance = controllerClass.getDeclaredConstructor().newInstance();
            for (RequestMethod annotationMethodType : annotationMethodTypes) {
                HandlerExecution handlerExecution = new HandlerExecution(controllerClassInstance, declaredMethod);
                HandlerKey handlerKey = new HandlerKey(annotation.value(), annotationMethodType);
                handlerExecutions.put(handlerKey, handlerExecution);
            }
        }
    }

    private List<RequestMethod> findSupportedRequestMethods(RequestMapping annotation) {
        RequestMethod[] methodTypes = annotation.method();
        if (methodTypes.length == 0) {
            methodTypes = RequestMethod.values();
        }
        return Arrays.stream(methodTypes).toList();
    }

    public Object getHandler(final HttpServletRequest request) {
        return handlerExecutions.get(
                new HandlerKey(request.getRequestURI(), RequestMethod.valueOf(request.getMethod())));
    }
}
