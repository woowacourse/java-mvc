package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Set;
import org.reflections.ReflectionUtils;
import org.reflections.util.ReflectionUtilsPredicates;
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

    public void initialize() {
        log.info("Initialized AnnotationHandlerMapping!");

        final ControllerScanner scanner = ControllerScanner.from(basePackage);

        Map<Class<?>, Object> controllers = scanner.getControllers();
        for (Object controller : controllers.values()) {
            Set<Method> methods = getRequestMappingMethods(controller);
            methods.forEach(method -> addHandlerExecutions(controller, method));
        }
    }

    @Override
    public Object getHandler(final HttpServletRequest request) {
        String requestUrl = request.getRequestURI();
        RequestMethod requestMethod = RequestMethod.from(request.getMethod());

        HandlerKey handlerKey = new HandlerKey(requestUrl, requestMethod);
        return handlerExecutions.getOrDefault(handlerKey, null);
    }

    private void addHandlerExecutions(Object controller, Method method) {
        final RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);

        RequestMethod[] requestMethods = getRequestMethods(requestMapping);
        for (RequestMethod requestMethod : requestMethods) {
            HandlerKey handlerKey = new HandlerKey(requestMapping.value(), requestMethod);

            HandlerExecution handlerExecution = getHandlerExecution(controller, method, handlerKey);
            handlerExecutions.put(handlerKey, handlerExecution);
        }
    }

    private HandlerExecution getHandlerExecution(Object controller, Method method, HandlerKey handlerKey) {
        if (handlerExecutions.containsKey(handlerKey)) {
            throw new IllegalArgumentException("중복된 요청 매핑입니다: " + handlerKey);
        }
        return new HandlerExecution(controller, method);
    }

    private Set<Method> getRequestMappingMethods(Object controller) {
        return ReflectionUtils.getAllMethods(controller.getClass(),
                ReflectionUtilsPredicates.withAnnotation(RequestMapping.class));
    }

    private RequestMethod[] getRequestMethods(RequestMapping annotation) {
        RequestMethod[] methods = annotation.method();
        if (methods.length == 0) {
            return RequestMethod.values();
        }
        return methods;
    }
}
