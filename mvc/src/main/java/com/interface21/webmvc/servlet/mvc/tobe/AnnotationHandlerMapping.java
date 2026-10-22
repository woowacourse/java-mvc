package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import com.interface21.webmvc.servlet.mvc.HandlerMapping;
import jakarta.servlet.http.HttpServletRequest;
import org.reflections.ReflectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Stream;

public class AnnotationHandlerMapping implements HandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final String[] basePackageNames;
    private final ControllerScanner controllerScanner;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(final String basePackageName, final String... additionalBasePackageNames) {
        this.basePackageNames = Stream.concat(
                Stream.of(basePackageName),
                Arrays.stream(additionalBasePackageNames)
        ).toArray(String[]::new);
        this.controllerScanner = new ControllerScanner();
        this.handlerExecutions = new HashMap<>();
    }

    @Override
    public void initialize() {
        for (String basePackageName : basePackageNames) {
            registerHandlerExecutions(basePackageName);
        }
        log.info("Initialized AnnotationHandlerMapping!");
    }

    @Override
    public Object getHandler(final HttpServletRequest request) {
        final HandlerKey key = new HandlerKey(
                request.getRequestURI(),
                RequestMethod.valueOf(request.getMethod())
        );
        return handlerExecutions.get(key);
    }

    private void registerHandlerExecutions(final String basePackageName) {
        final Map<Class<?>, Object> controllerInstances = controllerScanner.createControllerInstances(basePackageName);
        addAnnotatedMethodsInClassesAsHandlerExecutions(controllerInstances);
    }

    private void addAnnotatedMethodsInClassesAsHandlerExecutions(final Map<Class<?>, Object> controllerInstances) {
        for (Entry<Class<?>, Object> controllerEntry : controllerInstances.entrySet()) {
            final Object controllerInstance = controllerEntry.getValue();
            final Set<Method> annotatedMethods = ReflectionUtils.getAllMethods(
                    controllerEntry.getKey(), ReflectionUtils.withAnnotation(RequestMapping.class)
            );
            annotatedMethods.forEach(annotatedMethod -> addHandlerExecutions(controllerInstance, annotatedMethod));
        }
    }

    private void addHandlerExecutions(final Object controller, final Method method) {
        final HandlerExecution handlerExecution = new HandlerExecution(controller, method);
        getHandlerKeys(method).forEach(key -> registerHandlerExecution(key, handlerExecution));
    }

    private List<HandlerKey> getHandlerKeys(final Method method) {
        final RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
        final String url = requestMapping.value();
        final RequestMethod[] requestMethods = requestMapping.method();
        if (requestMethods.length == 0) {
            return Arrays.stream(RequestMethod.values())
                    .map(requestMethod -> new HandlerKey(url, requestMethod))
                    .toList();
        }
        return Arrays.stream(requestMethods)
                .map(requestMethod -> new HandlerKey(url, requestMethod))
                .toList();
    }

    private void registerHandlerExecution(
            final HandlerKey key,
            final HandlerExecution handlerExecution
    ) {
        final HandlerExecution existing = handlerExecutions.get(key);

        if (existing != null) {
            throw new IllegalStateException(
                    "중복된 핸들러 매핑: " + key
                            + "\n기존 메서드: " + existing
                            + "\n등록하려는 메서드: " + handlerExecution
            );
        }

        handlerExecutions.put(key, handlerExecution);
    }
}
