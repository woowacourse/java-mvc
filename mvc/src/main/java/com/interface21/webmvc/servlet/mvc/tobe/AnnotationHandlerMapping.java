package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AnnotationHandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final Object[] basePackage;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions = new HashMap<>();

    public AnnotationHandlerMapping(final Object... basePackage) {
        this.basePackage = basePackage;
    }

    public void initialize() {
        final ControllerScanner controllerScanner = new ControllerScanner(basePackage);
        final Map<Class<?>, Object> controllers = controllerScanner.getControllers();
        final Set<Method> requestMappingMethods = getRequestMappingMethods(controllers.keySet());

        for (Method method : requestMappingMethods) {
            final RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
            addHandlerExecutions(controllers, method, requestMapping);
        }
    }

    public Object getHandler(HttpServletRequest request) {
        String requestUri = request.getRequestURI();
        RequestMethod requestMethod = RequestMethod.of(request.getMethod());
        return handlerExecutions.get(new HandlerKey(requestUri, requestMethod));
    }

    private void addHandlerExecutions(Map<Class<?>, Object> controllers, Method method, RequestMapping requestMapping) {
        List<HandlerKey> handlerKeys = mapHandlerKeys(requestMapping.value(), requestMapping.method());
        Object controller = controllers.get(method.getDeclaringClass());
        HandlerExecution handlerExecution = new HandlerExecution(controller, method);

        for (HandlerKey handlerKey : handlerKeys) {
            log.info("Mapped : {}, Handler : {}", handlerKey, handlerExecution);
            handlerExecutions.put(handlerKey, handlerExecution);
        }
    }

    private Set<Method> getRequestMappingMethods(Set<Class<?>> controllers) {
        Set<Method> requestMappingMethods = new HashSet<>();
        for (Class<?> controller : controllers) {
            Method[] methods = controller.getDeclaredMethods();
            for (Method method : methods) {
                if (method.isAnnotationPresent(RequestMapping.class)) {
                    requestMappingMethods.add(method);
                }
            }
        }
        return requestMappingMethods;
    }

    private List<HandlerKey> mapHandlerKeys(String url, RequestMethod[] requestMethods) {
        List<HandlerKey> handlerKeys = new ArrayList<>();
        if (requestMethods == null || requestMethods.length == 0) {
            for (RequestMethod method : RequestMethod.values()) {
                handlerKeys.add(new HandlerKey(url, method));
            }
            return handlerKeys;
        }

        for (RequestMethod method : requestMethods) {
            handlerKeys.add(new HandlerKey(url, method));
        }
        return handlerKeys;
    }
}
