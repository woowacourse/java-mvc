package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import com.interface21.webmvc.servlet.mvc.handler.HandlerMapping;
import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

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
        log.info("Initialized AnnotationHandlerMapping!");

        final ControllerScanner controllerScanner = createControllerScanner(basePackage);
        try {
            final Map<Class<?>, Object> controllers = controllerScanner.getControllers();
            final Set<Method> requestMappingMethods = getRequestMappingMethods(controllers.keySet());
            for (Method method : requestMappingMethods) {
                final Object controllerInstance = controllers.get(method.getDeclaringClass());
                addHandlerExecutions(controllerInstance, method);
            }
        } catch (Exception e) {
            log.warn("initialize exception", e);
            throw new RuntimeException(e);
        }
    }

    public Object getHandler(final HttpServletRequest request) {
        final String requestURI = request.getRequestURI();
        final RequestMethod method = RequestMethod.valueOf(request.getMethod());

        final HandlerKey handlerKey = new HandlerKey(requestURI, method);

        return handlerExecutions.get(handlerKey);
    }

    private ControllerScanner createControllerScanner(final Object[] basePackage) {
        final Reflections reflections = new Reflections(basePackage);
        return new ControllerScanner(reflections);
    }

    private Set<Method> getRequestMappingMethods(final Set<Class<?>> controllers) {
        final Set<Method> methods = new HashSet<>();
        for (Class<?> controller : controllers) {
            for (Method method : controller.getDeclaredMethods()) {
                if (method.isAnnotationPresent(RequestMapping.class)) {
                    methods.add(method);
                }
            }
        }
        return methods;
    }

    private void addHandlerExecutions(
            final Object controllerInstance,
            final Method method
    ) {
        // @RequestMapping에서 uri, requestMethod 추출
        final RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
        final String uri = requestMapping.value();
        final RequestMethod[] requestMethods = getRequestMethods(requestMapping);
        final List<HandlerKey> handlerKeys = mapHandlerKeys(uri, requestMethods);

        // requestMethod 마다 HandlerKey, HandlerExecution 생성 후 등록
        for (HandlerKey handlerKey : handlerKeys) {
            final HandlerExecution handlerExecution = new HandlerExecution(controllerInstance, method);

            if (handlerExecutions.containsKey(handlerKey)) {
                throw new IllegalStateException("Duplicate mapping found: " + handlerKey);
            }
            handlerExecutions.put(handlerKey, handlerExecution);
            log.info("mapped: {}", handlerKey.toString());
        }
    }

    private RequestMethod[] getRequestMethods(final RequestMapping requestMapping) {
        // requestMethod는 여러 개 가능하나,
        RequestMethod[] requestMethods = requestMapping.method();

        if (requestMethods.length == 0) {
            // 메서드에 requestMethod가 없다면 모든 HTTP method를 지원한다.
            requestMethods = RequestMethod.values();
        }
        return requestMethods;
    }

    private List<HandlerKey> mapHandlerKeys(final String uri, final RequestMethod[] requestMethods) {
        final List<HandlerKey> handlerKeys = new ArrayList<>();
        for (RequestMethod requestMethod : requestMethods) {
            handlerKeys.add(new HandlerKey(uri, requestMethod));
        }
        return handlerKeys;
    }
}
