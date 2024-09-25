package com.interface21.webmvc.servlet.mvc.tobe;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import jakarta.servlet.http.HttpServletRequest;

import org.reflections.Reflections;
import org.reflections.scanners.Scanners;
import org.reflections.util.ConfigurationBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;

public class AnnotationHandlerMapping implements HandlerMapping {

    private static final String ALL_CLASS_PATH = "";
    private static final int NO_BASE_PACKAGE = 0;
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
        Set<Class<?>> controllerClasses = findControllerClasses();
        for (Class<?> controllerClass : controllerClasses) {
            Object controller = getInstance(controllerClass);
            Set<Method> methods = findRequestMappingMethods(controllerClass);
            methods.forEach(method -> addHandlerExecution(controller, method));
        }
    }

    private Set<Class<?>> findControllerClasses() {
        Reflections reflections = getReflections();

        return reflections.getTypesAnnotatedWith(Controller.class);
    }

    private Reflections getReflections() {
        if (basePackage.length == NO_BASE_PACKAGE) {
            ConfigurationBuilder builder = new ConfigurationBuilder();
            builder.forPackages(ALL_CLASS_PATH)
                    .addScanners(Scanners.TypesAnnotated);

            return new Reflections(builder);
        }
        return new Reflections(basePackage, Scanners.TypesAnnotated);
    }

    private Object getInstance(Class<?> clazz) {
        try {
            return clazz.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new IllegalStateException("Class의 인스턴스를 생성하는데 실패했습니다. Class = " + clazz.getCanonicalName());
        }
    }

    private Set<Method> findRequestMappingMethods(Class<?> controllerClass) {
        return Arrays.stream(controllerClass.getDeclaredMethods())
                .filter(method -> method.isAnnotationPresent(RequestMapping.class))
                .collect(Collectors.toSet());
    }

    private void addHandlerExecution(Object controller, Method method) {
        RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
        String url = getUrl(requestMapping, method.getName());
        for (RequestMethod requestMethod : getRequestMethods(requestMapping)) {
            handlerExecutions.put(new HandlerKey(url, requestMethod), new HandlerExecution(controller, method));
        }
    }

    private String getUrl(RequestMapping requestMapping, String methodName) {
        String url = requestMapping.value();
        if (url.isBlank()) {
            throw new IllegalStateException("@RequestMapping의 value값이 지정되어 있지 않습니다. methodName = " + methodName);
        }
        return url;
    }

    private Set<RequestMethod> getRequestMethods(RequestMapping requestMapping) {
        Set<RequestMethod> methods = Arrays.stream(requestMapping.method())
                .collect(Collectors.toSet());

        if (methods.isEmpty()) {
            Set<RequestMethod> allRequestMethods = Arrays.stream(RequestMethod.values())
                    .collect(Collectors.toSet());
            return allRequestMethods;
        }
        return methods;
    }

    @Override
    public Object getHandler(HttpServletRequest request) {
        HandlerKey handlerKey = new HandlerKey(request.getRequestURI(), RequestMethod.valueOf(request.getMethod()));
        if (!handlerExecutions.containsKey(handlerKey)) {
            throw new IllegalArgumentException("HttpServletRequest에 대응하는 handlerKey가 등록되어 있지 않습니다. " + handlerKey);
        }
        return handlerExecutions.get(handlerKey);
    }
}
