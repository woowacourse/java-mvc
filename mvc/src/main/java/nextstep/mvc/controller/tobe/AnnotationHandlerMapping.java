package nextstep.mvc.controller.tobe;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.servlet.http.HttpServletRequest;
import nextstep.mvc.HandlerMapping;
import nextstep.web.annotation.RequestMapping;
import nextstep.web.support.RequestMethod;

public class AnnotationHandlerMapping implements HandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final Object[] basePackage;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(final Object... basePackage) {
        this.basePackage = basePackage;
        this.handlerExecutions = new HashMap<>();
    }

    @Override
    public void initialize() {
        log.info("Initialized AnnotationHandlerMapping!");

        ControllerScanner controllerScanner = new ControllerScanner(basePackage);

        Map<Class<?>, Object> controllerMapping = controllerScanner.getControllerMapping();

        Set<Class<?>> classes = controllerMapping.keySet();
        Set<Method> methods = scanMethods(classes);

        for (Method method : methods) {
            RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
            addHandler(controllerMapping, method, requestMapping);
        }

        handlerExecutions.keySet()
            .forEach(handlerKey -> log.info("Path : {}, Controller : {}", handlerKey,
                handlerExecutions.get(handlerKey).getClass()));
    }

    private Set<Method> scanMethods(Set<Class<?>> classes) {
        Set<Method> methods = new HashSet<>();
        for (Class<?> clazz : classes) {
            scanMethodsInClass(methods, clazz);
        }
        return methods;
    }

    private void scanMethodsInClass(Set<Method> methods, Class<?> clazz) {
        for (Method method : clazz.getMethods()) {
            checkMethod(methods, method);
        }
    }

    private void checkMethod(Set<Method> methods, Method method) {
        RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
        if (requestMapping != null) {
            methods.add(method);
        }
    }

    private void addHandler(Map<Class<?>, Object> controllerMapping, Method method, RequestMapping requestMapping) {
        List<HandlerKey> handlerKeys = new ArrayList<>();
        for (RequestMethod requestMethod : requestMapping.method()) {
            handlerKeys.add(new HandlerKey(requestMapping.value(), requestMethod));
        }
        for (HandlerKey handlerKey : handlerKeys) {
            Class<?> declaringClass = method.getDeclaringClass();
            handlerExecutions.put(handlerKey, new HandlerExecution(controllerMapping.get(declaringClass), method));
        }
    }

    @Override
    public Object getHandler(final HttpServletRequest request) {
        HandlerKey key = new HandlerKey(request.getRequestURI(), RequestMethod.
            valueOf(request.getMethod()));
        return handlerExecutions.getOrDefault(key, null);
    }
}
