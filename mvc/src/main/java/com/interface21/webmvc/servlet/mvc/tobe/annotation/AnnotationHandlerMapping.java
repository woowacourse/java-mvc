package com.interface21.webmvc.servlet.mvc.tobe.annotation;

import com.interface21.core.ControllerScanner;
import com.interface21.core.util.ReflectionUtils;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import com.interface21.webmvc.servlet.HandlerMapping;
import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
        final ControllerScanner controllerScanner = new ControllerScanner(basePackage);
        final Map<Class<?>, Object> controllers = controllerScanner.getControllers();

        getRequestMappingMethods(controllers.keySet())
                .forEach(method ->
                        addHandlerExecutions(controllers, method, method.getAnnotation(RequestMapping.class))
                );

        log.info("Initialized AnnotationHandlerMapping!");
    }

    private void addHandlerExecutions(final Map<Class<?>, Object> controllers,
                                      final Method method,
                                      final RequestMapping requestMapping) {
        final HandlerExecution handlerExecution =
                new HandlerExecution(controllers.get(method.getDeclaringClass()), method);

        mapHandlerKeys(requestMapping.value(), requestMapping.method())
                .forEach(handlerKey -> {
                    if (handlerExecutions.putIfAbsent(handlerKey, handlerExecution) != null) {
                        throw new IllegalStateException("이미 등록된 핸들러입니다: " + handlerKey);
                    }
                });
    }

    private Set<Method> getRequestMappingMethods(final Set<Class<?>> controllerClasses) {
        return controllerClasses.stream()
                .flatMap(controllerClass -> ReflectionUtils.getAllMethods(controllerClass,
                        ReflectionUtils.withAnnotation(RequestMapping.class)).stream())
                .collect(Collectors.toSet());
    }

    private List<HandlerKey> mapHandlerKeys(final String url, final RequestMethod[] requestMethods) {
        final RequestMethod[] targetMethods = requestMethods.length == 0 ? RequestMethod.values() : requestMethods;

        return Arrays.stream(targetMethods)
                .map(requestMethod -> new HandlerKey(url, requestMethod))
                .toList();
    }

    @Override
    public Object getHandler(final HttpServletRequest request) {
        return handlerExecutions.get(new HandlerKey(request.getRequestURI(),
                RequestMethod.valueOf(request.getMethod())));
    }
}
