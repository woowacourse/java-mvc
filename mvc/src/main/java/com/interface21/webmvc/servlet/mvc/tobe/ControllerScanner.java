package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ControllerScanner {

    private static final Logger log = LoggerFactory.getLogger(ControllerScanner.class);
    private final Map<HandlerKey, HandlerExecution> handlerExecutions = new HashMap<>();

    public void instantiateControllers(final Set<Class<?>> controllerClasses) {
        for (Class<?> controllerClass : controllerClasses) {
            try {
                final Object controllerInstance = controllerClass.getDeclaredConstructor()
                        .newInstance();
                final Method[] methods = controllerClass.getMethods();

                insertMethods(controllerClass, methods, controllerInstance);
            } catch (final Exception e) {
                log.error("Error scanning controller {}", controllerClass.getName(), e);
                throw new RuntimeException(e);
            }
        }
    }

    private void insertMethods(Class<?> controllerClass, Method[] methods, Object controllerInstance) {
        for (final Method method : methods) {
            if (!method.isAnnotationPresent(RequestMapping.class)) {
                continue;
            }

            final RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
            final String requestUri = requestMapping.value();
            final RequestMethod[] requestMethods = requestMapping.method();

            for (final RequestMethod requestMethod : requestMethods) {
                final HandlerKey handlerKey = new HandlerKey(requestUri, requestMethod);
                final HandlerExecution handlerExecution = new HandlerExecution(controllerInstance, method);
                handlerExecutions.put(handlerKey, handlerExecution);
                log.info(
                        "Mapped handler: {} {} -> {}.{}",
                        requestMethod,
                        requestUri,
                        controllerClass.getSimpleName(),
                        method.getName()
                );
            }
        }
    }

    public Map<HandlerKey, HandlerExecution> getHandlerExecutions() {
        return handlerExecutions;
    }
}
