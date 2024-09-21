package com.interface21.webmvc.servlet.mvc.tobe;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import jakarta.servlet.http.HttpServletRequest;

import org.reflections.Reflections;
import org.reflections.scanners.Scanners;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;

public class AnnotationHandlerMapping {

    private final Object[] basePackage;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(final Object... basePackage) {
        this.basePackage = basePackage;
        this.handlerExecutions = new HashMap<>();
    }

    public void initialize() {
        Reflections typesAnnotatedReflections = new Reflections(basePackage, Scanners.TypesAnnotated);
        Set<Class<?>> controllers = typesAnnotatedReflections.getTypesAnnotatedWith(Controller.class);
        controllers.forEach(this::mapRequestMappingToHandler);
    }

    private void mapRequestMappingToHandler(Class<?> controller) {
        List<Method> requestMappings = Arrays.stream(controller.getDeclaredMethods())
                .filter(method -> method.isAnnotationPresent(RequestMapping.class))
                .toList();

        requestMappings.forEach(method -> registerHandler(controller, method));
    }

    private void registerHandler(Class<?> controller, Method method) {
        RequestMapping annotation = method.getAnnotation(RequestMapping.class);
        List<HandlerKey> handlerKeys = generateHandlerKeys(annotation);

        handlerKeys.forEach(key -> {
            HandlerExecution execution = new HandlerExecution(controller, method);
            handlerExecutions.put(key, execution);
        });
    }

    private List<HandlerKey> generateHandlerKeys(final RequestMapping requestMapping) {
        RequestMethod[] methods = getHttpMethods(requestMapping);
        return Arrays.stream(methods)
                .map(method -> new HandlerKey(requestMapping.value(), method))
                .toList();
    }

    private RequestMethod[] getHttpMethods(final RequestMapping requestMapping) {
        RequestMethod[] methods = requestMapping.method();
        if (methods.length == 0) {
            methods = RequestMethod.values();
        }

        return methods;
    }

    public Object getHandler(final HttpServletRequest request) {
        return handlerExecutions.get(
                new HandlerKey(request.getRequestURI(), RequestMethod.valueOf(request.getMethod())));
    }
}
