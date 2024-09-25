package com.interface21.webmvc.servlet.mvc.tobe;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import jakarta.servlet.http.HttpServletRequest;

import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import com.interface21.webmvc.servlet.mvc.tobe.handler.HandlerExecution;
import com.interface21.webmvc.servlet.mvc.tobe.handler.HandlerExecutions;
import com.interface21.webmvc.servlet.mvc.tobe.handler.HandlerKey;

public class AnnotationHandlerMapping {

    private static final String ROOT_PACKAGE_NAME = "com";
    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final Object[] basePackages;
    private final HandlerExecutions handlerExecutions;

    public AnnotationHandlerMapping(final Object... basePackages) {
        this.basePackages = setBasePackages(basePackages);
        this.handlerExecutions = new HandlerExecutions();
    }

    private Object[] setBasePackages(final Object... basePackages) {
        if (basePackages.length == 0) {
            return new Object[]{ROOT_PACKAGE_NAME};
        }

        validateBasePackages(basePackages);
        return basePackages;
    }

    private void validateBasePackages(final Object[] basePackages) {
        Arrays.stream(basePackages)
                .map(String::valueOf)
                .forEach(this::validateBasePackageIsNullOrBlank);
    }

    private void validateBasePackageIsNullOrBlank(final Object basePackage) {
        if (basePackage == null || String.valueOf(basePackage).isBlank()) {
            throw new IllegalArgumentException("패키지 명은 null 혹은 공백일 수 없습니다. - " + basePackage);
        }
    }

    public void initialize() {
        log.info("Initialized AnnotationHandlerMapping!");
        Arrays.stream(basePackages).forEach(this::registerHandlers);
    }

    private void registerHandlers(final Object basePackage) {
        parseControllerClasses(basePackage).forEach(this::registerHandlers);
    }

    private Set<Class<?>> parseControllerClasses(final Object basePackage) {
        final String basePackageValue = String.valueOf(basePackage);
        Reflections reflections = new Reflections(basePackageValue);
        return reflections.getTypesAnnotatedWith(Controller.class);
    }

    private void registerHandlers(final Class<?> clazz) {
        parseHandlers(clazz).forEach(method -> registerHandler(clazz, method));
    }

    private Set<Method> parseHandlers(final Class<?> clazz) {
        return Arrays.stream(clazz.getDeclaredMethods())
                .filter(method -> method.isAnnotationPresent(RequestMapping.class))
                .collect(Collectors.toSet());
    }

    private void registerHandler(final Class<?> clazz, final Method method) {
        final RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
        final HandlerExecution handlerExecution = new HandlerExecution(clazz, method);
        final List<HandlerKey> handlerKeys = generateHandlerKeys(requestMapping);

        handlerKeys.forEach(handlerKey -> handlerExecutions.add(handlerKey, handlerExecution));
    }

    private List<HandlerKey> generateHandlerKeys(final RequestMapping requestMapping) {
        final String requestUrl = requestMapping.value();
        final List<RequestMethod> requestMethods = parseRequestMethods(requestMapping);
        return requestMethods.stream()
                .map(requestMethod -> new HandlerKey(requestUrl, requestMethod))
                .toList();
    }

    private List<RequestMethod> parseRequestMethods(final RequestMapping requestMapping) {
        final RequestMethod[] requestMethods = requestMapping.method();
        if (requestMethods.length == 0) {
            return Arrays.stream(RequestMethod.values()).toList();
        }

        return Arrays.asList(requestMethods);
    }

    public Optional<HandlerExecution> findHandler(final HttpServletRequest request) {
        final String requestUri = request.getRequestURI();
        final String requestMethod = request.getMethod().toUpperCase();
        final HandlerKey handlerKey = new HandlerKey(requestUri, RequestMethod.valueOf(requestMethod));
        return handlerExecutions.findHandlerExecution(handlerKey);
    }
}
