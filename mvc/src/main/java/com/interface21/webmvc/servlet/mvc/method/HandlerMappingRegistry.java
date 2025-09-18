package com.interface21.webmvc.servlet.mvc.method;

import com.interface21.core.util.ReflectionUtils;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HandlerMappingRegistry {

    private static final Logger log = LoggerFactory.getLogger(HandlerMappingRegistry.class);

    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public HandlerMappingRegistry() {
        this.handlerExecutions = new HashMap<>();
    }

    public void registerControllers(final Map<Class<?>, Object> controllers) {
        for (final Entry<Class<?>, Object> entry : controllers.entrySet()) {
            final Class<?> controllerClass = entry.getKey();
            final Object controllerInstance = entry.getValue();

            registerControllerMethods(controllerClass, controllerInstance);
        }

        logRegisteredHandlers();
    }

    private void registerControllerMethods(final Class<?> controllerClass, final Object controllerInstance) {
        final Set<Method> requestMappingMethods = findRequestMappingMethods(controllerClass);

        for (final Method method : requestMappingMethods) {
            registerHandlerMethod(controllerInstance, method);
        }
    }

    private Set<Method> findRequestMappingMethods(final Class<?> controllerClass) {
        return ReflectionUtils.getAllMethods(controllerClass, ReflectionUtils.withAnnotation(RequestMapping.class));
    }

    private void registerHandlerMethod(final Object controllerInstance, final Method method) {
        final RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);

        final String url = requestMapping.value();
        final RequestMethod[] httpMethods = getHttpMethods(requestMapping);

        for (final RequestMethod httpMethod : httpMethods) {
            final HandlerKey handlerKey = new HandlerKey(url, httpMethod);
            final HandlerExecution handlerExecution = new HandlerExecution(controllerInstance, method);

            // 중복 매핑 검사: 이미 존재하면 기존값 리턴, 없으면 새값 추가 후 null 리턴
            final HandlerExecution existing = handlerExecutions.putIfAbsent(handlerKey, handlerExecution);
            if (existing != null) {
                throw new IllegalStateException(
                    String.format("Ambiguous mapping. Cannot map '%s' method %s to %s %s: There is already '%s' method %s mapped.",
                        controllerInstance.getClass().getSimpleName(), method.getName(),
                        httpMethod, url,
                        existing.getHandler().getClass().getSimpleName(), existing.getMethod().getName()));
            }

            log.debug("Registered handler: {} {} -> {}.{}",
                    httpMethod, url, controllerInstance.getClass().getSimpleName(), method.getName());
        }
    }

    private RequestMethod[] getHttpMethods(final RequestMapping requestMapping) {
        final RequestMethod[] methods = requestMapping.method();
        return methods.length == 0 ? RequestMethod.values() : methods;
    }

    private void logRegisteredHandlers() {
        log.info("Total handlers registered: {}", handlerExecutions.size());
        handlerExecutions.keySet().forEach(key ->
                log.debug("Handler registered: {}", key));
    }

    public HandlerExecution getHandlerExecution(final HandlerKey handlerKey) {
        return handlerExecutions.get(handlerKey);
    }

    public boolean isEmpty() {
        return handlerExecutions.isEmpty();
    }

    public int size() {
        return handlerExecutions.size();
    }
}
