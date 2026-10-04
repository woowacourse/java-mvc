package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
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
            final Object controller = createController(controllerClass);

            for (Method method : controllerClass.getDeclaredMethods()) {
                final RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
                if (requestMapping == null) {
                    continue;
                }

                final RequestMethod[] requestMethods = requestMapping.method();
                if (requestMethods.length == 0) {
                    registerAllRequestMethods(controller, method, requestMapping.value());
                    continue;
                }

                final HandlerExecution handlerExecution =
                        new HandlerExecution(controller, method);
                for (RequestMethod requestMethod : requestMethods) {
                    registerHandler(requestMapping.value(), requestMethod, handlerExecution, method);
                }
            }
        }

        log.info("Initialized AnnotationHandlerMapping!");
    }

    private void registerAllRequestMethods(
            final Object controller,
            final Method method,
            final String path
    ) {
        final HandlerExecution handlerExecution = new HandlerExecution(controller, method);
        for (RequestMethod requestMethod : RequestMethod.values()) {
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
        log.info("Mapped {} {} to {}", requestMethod, path, method);
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

    public Object getHandler(final HttpServletRequest request) {
        final HandlerKey handlerKey = new HandlerKey(
                request.getRequestURI(),
                RequestMethod.valueOf(request.getMethod())
        );
        return handlerExecutions.get(handlerKey);
    }
}
