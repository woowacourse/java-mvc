package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import jakarta.servlet.http.HttpServletRequest;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class AnnotationHandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final String[] basePackageNames;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(final String... basePackageNames) {
        this.basePackageNames = basePackageNames;
        this.handlerExecutions = new HashMap<>();
    }

    public void initialize() {
        for (String basePackageName : basePackageNames) {
            final Reflections reflectionsOfBasePackage = new Reflections(basePackageName);
            final Set<Class<?>> controllerClasses = reflectionsOfBasePackage.getTypesAnnotatedWith(Controller.class);
            addAnnotatedMethodsInClassesAsHandlerExecutions(controllerClasses);
        }
        log.info("Initialized AnnotationHandlerMapping!");
    }

    public Object getHandler(final HttpServletRequest request) {
        final HandlerKey key = new HandlerKey(
                request.getRequestURI(),
                RequestMethod.valueOf(request.getMethod())
        );
        return handlerExecutions.get(key);
    }

    private void addAnnotatedMethodsInClassesAsHandlerExecutions(final Set<Class<?>> controllerClasses) {
        for (Class<?> controllerClass : controllerClasses) {
            final Object controller = createController(controllerClass);
            for (Method method : controllerClass.getDeclaredMethods()) {
                if (method.isAnnotationPresent(RequestMapping.class)) {
                    addHandlerExecutions(controller, method);
                }
            }
        }
    }

    private Object createController(final Class<?> controllerClass) {
        try {
            return controllerClass.getDeclaredConstructor().newInstance();
        } catch (ReflectiveOperationException e) {
            throw new IllegalStateException(
                    "컨트롤러 생성 실패: " + controllerClass.getName(), e
            );
        }
    }

    private void addHandlerExecutions(final Object controller, final Method method) {
        final HandlerExecution handlerExecution = new HandlerExecution(controller, method);
        final List<HandlerKey> handlerKeys = getHandlerKeys(method);
        handlerKeys.forEach(key -> handlerExecutions.put(key, handlerExecution));
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
}
