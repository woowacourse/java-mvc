package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
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

    public void initialize() {
        handlerExecutions.clear();

        final Reflections reflections = new Reflections(basePackage);
        final Set<Class<?>> controllerClasses = reflections.getTypesAnnotatedWith(Controller.class);

        for (Class<?> controllerClass : controllerClasses) {
            registerController(controllerClass);
        }

        log.info("Initialized AnnotationHandlerMapping!");
    }

    public Object getHandler(final HttpServletRequest request) {
        final HandlerKey handlerKey = new HandlerKey(
                request.getRequestURI(),
                RequestMethod.valueOf(request.getMethod())
        );
        return handlerExecutions.get(handlerKey);
    }

    private void registerController(final Class<?> controllerClass) {
        final Object controller = createController(controllerClass);
        final RequestMapping classRequestMapping =
                controllerClass.getAnnotation(RequestMapping.class);

        for (Method method : controllerClass.getDeclaredMethods()) {
            registerMethod(controller, classRequestMapping, method);
        }
    }

    private void registerMethod(
            final Object controller,
            final RequestMapping classRequestMapping,
            final Method method
    ) {
        if (!Modifier.isPublic(method.getModifiers())) {
            return;
        }

        final RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
        if (requestMapping == null) {
            return;
        }

        final String path = resolvePath(classRequestMapping, requestMapping);
        final HandlerExecution handlerExecution = new HandlerExecution(controller, method);

        for (RequestMethod requestMethod : getRequestMethods(requestMapping)) {
            registerHandler(path, requestMethod, handlerExecution, method);
        }
    }

    private void registerHandler(
            final String path,
            final RequestMethod requestMethod,
            final HandlerExecution handlerExecution,
            final Method method
    ) {
        final HandlerKey handlerKey = new HandlerKey(path, requestMethod);
        handlerExecutions.put(handlerKey, handlerExecution);
        log.info("핸들러 매핑 등록 - HTTP 메서드: {}, 경로: {}, 메서드: {}",
                requestMethod, path, method);
    }

    private RequestMethod[] getRequestMethods(final RequestMapping requestMapping) {
        final RequestMethod[] requestMethods = requestMapping.method();

        if (requestMethods.length > 0) {
            return requestMethods;
        }

        return RequestMethod.values();
    }

    private String resolvePath(
            final RequestMapping classRequestMapping,
            final RequestMapping methodRequestMapping
    ) {
        final String classPath = getClassPath(classRequestMapping);
        final String methodPath = methodRequestMapping.value();

        if (classPath.isEmpty()) {
            return normalizePath(methodPath);
        }
        if (methodPath.isEmpty()) {
            return normalizePath(classPath);
        }
        return combinePaths(classPath, methodPath);
    }

    private String getClassPath(final RequestMapping classRequestMapping) {
        if (classRequestMapping == null) {
            return "";
        }

        return classRequestMapping.value();
    }

    private String normalizePath(final String path) {
        final String trimmedPath = trimSlashes(path);
        if (trimmedPath.isEmpty()) {
            return "/";
        }

        return "/" + trimmedPath;
    }

    private String trimSlashes(final String path) {
        return path.replaceAll("^/+|/+$", "");
    }

    private String combinePaths(final String classPath, final String methodPath) {
        final String normalizedClassPath = normalizePath(classPath);
        final String normalizedMethodPath = normalizePath(methodPath);

        if (normalizedClassPath.equals("/")) {
            return normalizedMethodPath;
        }

        return normalizedClassPath + normalizedMethodPath;
    }

    private Object createController(final Class<?> controllerClass) {
        try {
            return controllerClass.getDeclaredConstructor().newInstance();
        } catch (ReflectiveOperationException e) {
            throw new IllegalStateException(
                    "컨트롤러를 생성할 수 없습니다: " + controllerClass.getName(),
                    e
            );
        }
    }

}
