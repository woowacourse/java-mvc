package com.interface21.webmvc.servlet.mvc.tobe.annotation;

import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import com.interface21.webmvc.servlet.mvc.tobe.mapping.HandlerMapping;
import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.reflections.Reflections;
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

    public void initialize() {
        final Reflections reflections = new Reflections(basePackage);

        final ControllerScanner controllerScanner = new ControllerScanner(reflections);
        final Map<Class<?>, Object> controllers = controllerScanner.getControllers();

        addHandlerExecutions(controllers);

        log.info("Initialized AnnotationHandlerMapping!");
    }

    private void addHandlerExecutions(Map<Class<?>, Object> controllers) {
        for (Class<?> clazz : controllers.keySet()) {
            final Set<Method> requestMappingMethods = getRequestMappingMethods(clazz);

            final Object controller = controllers.get(clazz);

            for (Method requestMappingMethod : requestMappingMethods) {
                final HandlerExecution handlerExecution = new HandlerExecution(controller, requestMappingMethod);
                final RequestMapping requestMapping = requestMappingMethod.getAnnotation(RequestMapping.class);

                final List<HandlerKey> handlerKeys = mapHandlerKeys(
                        requestMapping.value(),
                        resolveRequestMethods(requestMapping));

                putHandlerExecutions(handlerKeys, handlerExecution);
            }
        }
    }

    private Set<Method> getRequestMappingMethods(Class<?> clazz) {
        final Set<Method> annotations = new HashSet<>();
        for (Method method : clazz.getMethods()) {
            if (method.isAnnotationPresent(RequestMapping.class)) {
                annotations.add(method);
            }
        }
        return annotations;
    }

    private List<HandlerKey> mapHandlerKeys(String uri, RequestMethod[] requestMethods) {
        return Arrays.stream(requestMethods)
                .map(requestMethod -> new HandlerKey(uri, requestMethod))
                .toList();
    }

    private void putHandlerExecutions(List<HandlerKey> handlerKeys, HandlerExecution handlerExecution) {
        for (HandlerKey handlerKey : handlerKeys) {
            if (handlerExecutions.get(handlerKey) != null) {
                throw new IllegalStateException("중복된 핸들러가 등록되었습니다: " + handlerKey);
            }

            handlerExecutions.put(handlerKey, handlerExecution);
            log.info("HandlerKey : {}, HandlerExecution : {}", handlerKey, handlerExecution);
        }
    }

    private RequestMethod[] resolveRequestMethods(RequestMapping requestMapping) {
        final RequestMethod[] methods = requestMapping.method();
        if (methods.length == 0) {
            return RequestMethod.values();
        }
        return methods;
    }

    @Override
    public Object getHandler(final HttpServletRequest request) {
        final String requestURI = request.getRequestURI();
        final RequestMethod httpMethod = RequestMethod.valueOf(request.getMethod());
        log.debug("getHandler 호출, requestURI = {}, httpMethod = {}", requestURI, httpMethod.name());

        return handlerExecutions.get(new HandlerKey(requestURI, httpMethod));
    }
}
