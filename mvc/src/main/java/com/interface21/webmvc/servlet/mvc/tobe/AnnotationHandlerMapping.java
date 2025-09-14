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

public final class AnnotationHandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final Object[] basePackage;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(final Object... basePackage) {
        this.basePackage = basePackage;
        this.handlerExecutions = new HashMap<>();
    }

    public void initialize() {
        final Reflections reflections = new Reflections(basePackage);
        final Set<Class<?>> controllerClasses = reflections.getTypesAnnotatedWith(Controller.class);
        for (final Class<?> controllerClass : controllerClasses) {
            try {
                final Object controllerInstance = controllerClass.getDeclaredConstructor().newInstance();
                final Method[] methods = controllerClass.getMethods();
                for (final Method method : methods) {
                    if (method.isAnnotationPresent(RequestMapping.class)) {
                        final RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
                        final String path = requestMapping.value();
                        final RequestMethod[] requestMethods = requestMapping.method();
                        if (requestMethods.length == 0) {
                            for (RequestMethod requestMethod : RequestMethod.values()) {
                                final HandlerKey handlerKey = new HandlerKey(path, requestMethod);
                                final HandlerExecution handlerExecution = new HandlerExecution(controllerInstance, method);
                                handlerExecutions.put(handlerKey, handlerExecution);
                            }
                            continue;
                        }
                        for (final RequestMethod requestMethod : requestMethods) {
                            final HandlerKey handlerKey = new HandlerKey(path, requestMethod);
                            final HandlerExecution handlerExecution = new HandlerExecution(controllerInstance, method);
                            handlerExecutions.put(handlerKey, handlerExecution);
                        }
                    }
                }
            } catch (final Exception e) {
                log.error("컨트롤러 인스턴스 생성 실패: {}", controllerClass.getName(), e);
            }
        }
        log.info("AnnotationHandlerMapping 초기화 완료!");
    }

    public Object getHandler(final HttpServletRequest request) {
        final String requestURI = request.getRequestURI();
        final RequestMethod requestMethod = RequestMethod.valueOf(request.getMethod());
        final HandlerKey handlerKey = new HandlerKey(requestURI, requestMethod);
        log.info("HandlerKey: {}", handlerKey);
        return handlerExecutions.get(handlerKey);
    }
}
