package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import jakarta.servlet.http.HttpServletRequest;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class AnnotationHandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final Object[] basePackage;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(final Object... basePackage) {
        this.basePackage = basePackage;
        this.handlerExecutions = new HashMap<>();
    }

    public void initialize() {
        final Reflections reflections = new Reflections(basePackage);
        final Set<Class<?>> controllerTypes = reflections.getTypesAnnotatedWith(Controller.class);
        for (final Class<?> controllerType : controllerTypes) {
            final Object controller = createController(controllerType);
            registerHandlers(controllerType, controller);
        }

        log.info("Initialized AnnotationHandlerMapping!");
    }

    private Object createController(final Class<?> controllerType) {
        try {
            return controllerType.getDeclaredConstructor().newInstance();
        } catch (ReflectiveOperationException e) {
            throw new IllegalStateException("컨트롤러를 생성할 수 없습니다: " + controllerType.getName(), e);
        }
    }

    private void registerHandlers(final Class<?> controllerType, final Object controller) {
        for (final Method method : controllerType.getDeclaredMethods()) {
            final RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
            if (requestMapping == null) {
                continue;
            }

            final RequestMethod[] requestMethods = requestMapping.method().length == 0
                    ? RequestMethod.values() : requestMapping.method();
            final HandlerExecution handlerExecution = new HandlerExecution(controller, method);
            for (final RequestMethod requestMethod : requestMethods) {
                handlerExecutions.put(new HandlerKey(requestMapping.value(), requestMethod), handlerExecution);
            }
        }
    }

    public HandlerExecution getHandler(final HttpServletRequest request) {
        final String url = request.getRequestURI();
        final RequestMethod requestMethod = RequestMethod.valueOf(request.getMethod());
        return handlerExecutions.get(new HandlerKey(url, requestMethod));
    }
}
