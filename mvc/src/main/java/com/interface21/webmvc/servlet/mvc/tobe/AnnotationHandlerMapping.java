package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import jakarta.servlet.http.HttpServletRequest;
import org.reflections.ReflectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class AnnotationHandlerMapping implements HandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final Object[] basePackage;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(final Object... basePackage) {
        this.basePackage = basePackage;
        this.handlerExecutions = new HashMap<>();
    }

    public void initialize() {
        ControllerScanner scanner = new ControllerScanner(basePackage);
        Map<Class<?>, Object> controllers = scanner.getControllers();

        for (Method method : getRequestMappingMethods(controllers.keySet())) {
            RequestMapping mapping = method.getAnnotation(RequestMapping.class);
            addHandlerExecutions(controllers, method, mapping);
        }
        Set<Method> requestMappingMethods = getRequestMappingMethods(controllers.keySet());

        log.info("Initialized AnnotationHandlerMapping!");
    }

    @Override
    public Object getHandler(final HttpServletRequest request) {
        String contextPath = request.getContextPath();
        String requestURI = request.getRequestURI();
        String path = requestURI.substring(contextPath == null ? 0 : contextPath.length());
        HandlerKey handlerKey = new HandlerKey(path, RequestMethod.valueOf(request.getMethod()));

        return handlerExecutions.get(handlerKey);
    }

    private void addHandlerExecutions(Map<Class<?>, Object> controllers, Method method, RequestMapping requestMapping) {

        List<HandlerKey> handlerKeys = mapHandlerKeys(requestMapping.value(), requestMapping.method());
        for (Map.Entry<Class<?>, Object> entry : controllers.entrySet()) {
            if (!method.getDeclaringClass().isAssignableFrom(entry.getKey())) {
                continue;
            }
            HandlerExecution handlerExecution = new HandlerExecution(entry.getValue(), method);

            for (HandlerKey handlerKey : handlerKeys) {
                if (handlerExecutions.put(handlerKey, handlerExecution) != null) {
                    throw new IllegalStateException("Duplicate handler key: " + handlerKey);
                }
            }
        }
    }

    private Set<Method> getRequestMappingMethods(Set<Class<?>> classes) {
        Set<Method> methods = new HashSet<>();
        classes.forEach(clazz ->
                methods.addAll(ReflectionUtils.getAllMethods(
                        clazz, ReflectionUtils.withAnnotation(RequestMapping.class)))
        );
        return methods;
    }

    private List<HandlerKey> mapHandlerKeys(String url, RequestMethod[] methods) {
        List<HandlerKey> handlerKeys = new ArrayList<>();
        RequestMethod[] targets = (methods.length == 0) ? RequestMethod.values() : methods;

        for (RequestMethod target : targets) {
            handlerKeys.add(new HandlerKey(url, target));
        }
        return handlerKeys;
    }
}
